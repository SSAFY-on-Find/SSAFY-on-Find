package com.sonfind.chelsea.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.teams.Recruitment;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.CreateTeamRequestDto;
import com.sonfind.chelsea.dto.teams.MyTeamResponseDto;
import com.sonfind.chelsea.dto.teams.RecruitmentDto;
import com.sonfind.chelsea.dto.teams.TeamListResponseDto;
import com.sonfind.chelsea.dto.teams.TeamMemberResponseDto;
import com.sonfind.chelsea.dto.teams.TeamResponseDto;
import com.sonfind.chelsea.dto.teams.TeamRuleResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.dto.teams.UpdateTeamRequestDto;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.StudentRepository;
import com.sonfind.chelsea.repository.SubCodeRepository;
import com.sonfind.chelsea.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;
	private final SubCodeRepository subCodeRepository;
	private final StudentRepository studentRepository;
	private final StudentInfoRepository studentInfoRepository;

	private final SubCodeService subCodeService;
	private final StudentService studentService;
	private final FavoriteService favoriteService;

	//팀 생성
	@Transactional
	public Long createTeam(Long studentId, CreateTeamRequestDto request) {

		//팀에 속해 있는 교육생은 팀 생성 못함
		Students students = studentService.findByStudentId(studentId);
		if (students.getTeamId() != null) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "이미 팀에 속해있습니다");
		}

		//subCode로 track
		//없는 트랙이면 badrequest
		SubCode track = getSubCodeByValue(request.track());
		if (track == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "없는 트랙입니다");
		}

		//팀 명 없이 일단 저장
		Team noTeamName = Team.builder()
			.name("")
			.description(request.description())
			.track(track)
			.build();

		Team team = teamRepository.save(noTeamName);

		//자동 팀명 생성(e.g. 팀 001)
		String autoName = String.format("팀 %03d", team.getTeamId());
		team.updateName(autoName);

		// positions를 recruitments로 변환
		if (request.positions() != null && !request.positions().isEmpty()) {
			List<Recruitment> recruitments = toRecruitments(request.positions(), team);
			team.updatePositions(recruitments);
		}

		//학생 teamId 저장
		students.setTeamId(team.getTeamId());
		studentRepository.save(students);

		return team.getTeamId();
	}

	//팀 수정
	@Transactional
	public void updateTeam(Long teamId, Long studentId, UpdateTeamRequestDto request) {

		//팀에 속해 있지 않은 교육생은 수정 불가
		Students students = studentService.findByStudentId(studentId);
		if (!teamId.equals(students.getTeamId())) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "팀에 속해 있지 않은 교육생은 수정할 수 없습니다."
			);
		}

		//존재하는 팀인지 확인
		Team team = teamRepository.findTeamByTeamId(teamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."));

		//팀 설명 수정
		if (request.description() != null) {
			team.updateDescription(request.description());
		}

		//희망 트랙 수정
		if (request.track() != null) {
			SubCode track = getSubCodeByValue(request.track());
			if (track == null) {
				throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "없는 트랙입니다.");
			}
			team.updateTrack(track);
		}

		//모집 포지션 수정
		if (request.positions() != null) {
			List<Recruitment> recruitments = toRecruitments(request.positions(), team);
			team.updatePositions(recruitments);
		}
	}

	//타 팀 상세조회
	@Transactional(readOnly = true)
	public TeamResponseDto getTeamDetail(Long teamId, Long studentId) {
		//존재하는 팀인지 확인
		Team team = teamRepository.findTeamByTeamId(teamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."));

		List<Students> teamMembers = studentRepository.findAllByTeamId(teamId);

		//팀원 정보 변환
		List<TeamMemberResponseDto> members = teamMembers.stream()
			.map(this::convertToTeamMemberResponse)
			.collect(Collectors.toList());

		int majorCount = (int)teamMembers.stream()
			.mapToLong(member -> Boolean.TRUE.equals(member.getMajorYn()) ? 1L : 0L)
			.sum();
		int nonMajorCount = teamMembers.size() - majorCount;
		int teamCount = teamMembers.size();

		//모집 포지션
		List<RecruitmentDto> positions = team.getRecruitments().stream()
			.map(recruitment -> new RecruitmentDto(
				recruitment.getPosition().getSubCode(),
				recruitment.getPosition().getSubCodeName()
			))
			.collect(Collectors.toList());

		boolean isFavorite = favoriteService.checkFavoriteStatus(studentId, teamId);

		return TeamResponseDto.builder()
			.teamName(team.getName())
			.teamDescription(team.getDescription())
			.track(new SubCodeResponseDto(
				team.getTrack().getSubCode(),
				team.getTrack().getSubCodeName()
			))
			.teamCount((long)teamMembers.size())
			.majorCount(majorCount)
			.nonMajorCount(nonMajorCount)
			.positions(positions)
			.members(members)
			.isFavorite(isFavorite)
			.build();
	}

	//내 팀 상세조회
	@Transactional(readOnly = true)
	public MyTeamResponseDto getMyTeamDetail(Long studentId) {
		Students students = studentService.findByStudentId(studentId);
		if (students.getTeamId() == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "팀에 속해 있지 않습니다.");
		}

		TeamResponseDto teamInfo = getTeamDetail(students.getTeamId(), studentId);

		List<Students> teamMembers = studentRepository.findAllByTeamId(students.getTeamId());

		int majorCount = (int)teamMembers.stream()
			.mapToLong(member -> Boolean.TRUE.equals(member.getMajorYn()) ? 1L : 0L)
			.sum();
		int nonMajorCount = teamMembers.size() - majorCount;
		int teamCount = teamMembers.size();

		List<TeamRuleResponseDto> ruleStatuses = activeTeamRulesSelectively(teamCount, majorCount, nonMajorCount);

		return MyTeamResponseDto.builder()
			.teamInfo(teamInfo)
			.majorCount(majorCount)
			.nonMajorCount(nonMajorCount)
			.ruleStatuses(ruleStatuses)
			.build();
	}

	//팀 전체 목록 조회
	// TeamService의 최적화된 getAllTeams 메서드 (최종 버전)
	// TeamService의 최적화된 getAllTeams 메서드 (최종 버전 - 수정됨)
	// TeamService - 기존 TeamMemberResponseDto 사용하는 방식

	@Transactional(readOnly = true)
	public List<TeamListResponseDto> getAllTeams(Long studentId) {
		// 1. 팀 데이터와 연관 데이터를 한 번에 조회
		List<Team> teams = teamRepository.findAllTeamsWithTrackAndRecruitments();

		if (teams.isEmpty()) {
			return new ArrayList<>();
		}

		// 2. 팀 ID 리스트 추출
		List<Long> teamIds = teams.stream()
			.map(Team::getTeamId)
			.collect(Collectors.toList());

		// 3. 모든 팀의 멤버 기본 정보 조회
		List<Students> allStudents = studentRepository.findAllByTeamIdIn(teamIds);

		// 4. 멤버들의 필수 정보만 조회 (프로필 이미지, 포지션)
		List<Long> studentIds = allStudents.stream()
			.map(Students::getStudentId)
			.collect(Collectors.toList());

		// 필요한 정보만 조회
		final Map<Long, StudentInfo> studentInfoMap;
		if (!studentIds.isEmpty()) {
			studentInfoMap = studentInfoRepository
				.findBasicInfoByStudentIds(studentIds)
				.stream()
				.collect(Collectors.toMap(
					info -> info.getStudent().getStudentId(),
					info -> info
				));
		} else {
			studentInfoMap = new HashMap<>();
		}

		// 5. 팀별로 멤버 그룹핑
		Map<Long, List<Students>> membersByTeam = allStudents.stream()
			.collect(Collectors.groupingBy(Students::getTeamId));

		// 6. 즐겨찾기 정보를 배치로 조회
		Map<Long, Boolean> favoriteStatus = favoriteService.checkFavoriteStatusBatch(studentId, teamIds);

		// 7. DTO 변환
		return teams.stream()
			.map(team -> convertToTeamListResponseOptimized(
				team,
				membersByTeam.getOrDefault(team.getTeamId(), new ArrayList<>()),
				studentInfoMap,
				favoriteStatus.getOrDefault(team.getTeamId(), false)
			))
			.sorted(
				Comparator.comparing(TeamListResponseDto::isRecruitingComplete)
					.thenComparing(dto -> parseTeamNumber(dto.teamName()))
			)
			.collect(Collectors.toList());
	}

	// 기존 TeamMemberResponseDto 사용하는 변환 메서드
	private TeamListResponseDto convertToTeamListResponseOptimized(
		Team team,
		List<Students> teamMembers,
		Map<Long, StudentInfo> studentInfoMap,
		boolean isFavorite
	) {
		// 기존 TeamMemberResponseDto로 변환
		List<TeamMemberResponseDto> members = teamMembers.stream()
			.map(student -> convertToTeamMemberResponseOptimized(student, studentInfoMap))
			.collect(Collectors.toList());

		// 모집 포지션 (이미 fetch join으로 조회된 데이터 사용)
		List<RecruitmentDto> recruitments = team.getRecruitments().stream()
			.map(recruitment -> new RecruitmentDto(
				recruitment.getPosition().getSubCode(),
				recruitment.getPosition().getSubCodeName()
			))
			.collect(Collectors.toList());

		// 트랙 정보 (이미 fetch join으로 조회된 데이터 사용)
		SubCodeResponseDto track = new SubCodeResponseDto(
			team.getTrack().getSubCode(),
			team.getTrack().getSubCodeName()
		);

		return TeamListResponseDto.builder()
			.teamId(team.getTeamId())
			.teamName(team.getName())
			.description(team.getDescription())
			.track(track)
			.recruitments(recruitments)
			.members(members)
			.isRecruitingComplete(teamMembers.size() >= 6)
			.isFavorite(isFavorite)
			.build();
	}

	// 기존 TeamMemberResponseDto로 변환
	private TeamMemberResponseDto convertToTeamMemberResponseOptimized(
		Students student,
		Map<Long, StudentInfo> studentInfoMap
	) {
		String major = Boolean.TRUE.equals(student.getMajorYn()) ? "전공" : "비전공";

		// StudentInfo에서 필요한 정보만 추출
		StudentInfo studentInfo = studentInfoMap.get(student.getStudentId());
		String profileImageUrl = (studentInfo != null) ? studentInfo.getProfileImageUrl() : "";

		SubCodeResponseDto position = null;
		if (studentInfo != null && studentInfo.getPositionCode() != null) {
			position = new SubCodeResponseDto(
				studentInfo.getPositionCode().getSubCode(),
				studentInfo.getPositionCode().getSubCodeName()
			);
		}

		return TeamMemberResponseDto.builder()
			.studentId(student.getStudentId())
			.name(student.getName())
			.major(major)
			.profileImageUrl(profileImageUrl)
			.position(position)
			.build();
	}

	//team2teamlistresponseDto
	private TeamListResponseDto convertToTeamListResponse(Team team, Long studentId) {
		List<Students> teamMembers = studentRepository.findAllByTeamId(team.getTeamId());

		List<TeamMemberResponseDto> members = teamMembers.stream()
			.map(this::convertToTeamMemberResponse)
			.collect(Collectors.toList());

		List<RecruitmentDto> recruitments = team.getRecruitments().stream()
			.map(recruitment -> new RecruitmentDto(
				recruitment.getPosition().getSubCode(),
				recruitment.getPosition().getSubCodeName()
			))
			.collect(Collectors.toList());

		SubCodeResponseDto track = new SubCodeResponseDto(
			team.getTrack().getSubCode(),
			team.getTrack().getSubCodeName()
		);

		boolean isFavorite = favoriteService.checkFavoriteStatus(studentId, team.getTeamId());

		return TeamListResponseDto.builder()
			.teamId(team.getTeamId())
			.teamName(team.getName())
			.description(team.getDescription())
			.track(track)
			.recruitments(recruitments)
			.members(members)
			.isRecruitingComplete(teamMembers.size() >= 6)
			.isFavorite(isFavorite)
			.build();
	}

	//교육생 프사 조회
	private String getStudentProfileImage(Students student) {
		try {
			return studentInfoRepository.findByStudent_StudentId(student.getStudentId())
				.map(StudentInfo::getProfileImageUrl)
				.orElse("");
		} catch (Exception e) {
			return "";
		}
	}

	//팀 이름 숫자만 파싱
	private int parseTeamNumber(String teamName) {
		try {
			String digits = teamName.replaceAll("\\D+", "");
			return Integer.parseInt(digits);
		} catch (Exception e) {
			return Integer.MAX_VALUE;
		}
	}

	//팀 빌딩 규칙
	//팀 규칙은 팀 테이블에 저장하지 않기로 함. 팀 규칙은 모든 팀에게 동일하게 적용이 되는데 중복 데이터를 저장해야하는 이유를 모르겠음.
	//팀 테이블에 규칙 저장하는거 만드니까 팀 생성, 수정할 매마다 계속 저장해야 함.
	//그래서 subcode의 useYn으로 팀 규칙의 사용여부를 조정할 수 있는 메소드를 만들었음.
	private List<TeamRuleResponseDto> activeTeamRulesSelectively(int teamSize, int majorCount, int nonMajorCount) {
		List<TeamRuleResponseDto> results = new ArrayList<>();

		//사용하고 있는 규칙 조회
		List<SubCode> activeRules = subCodeRepository.findByMainCodeAndUseYnTrue("RULE");
		List<String> activeRuleCodes = activeRules.stream()
			.map(SubCode::getSubCode)
			.collect(Collectors.toList());

		//RULE001 6인 1팀
		if (activeRuleCodes.contains("RULE001")) {
			results.add(teamSizeRule(teamSize));
		}

		if (activeRuleCodes.contains("RULE002") || activeRuleCodes.contains("RULE003")) {
			results.add(teamMajorRule(majorCount, nonMajorCount));
		}

		return results;
	}

	//6인 1팀
	private TeamRuleResponseDto teamSizeRule(int teamSize) {
		boolean isOk = (teamSize == 6);

		return TeamRuleResponseDto.builder()
			.ruleCode("RULE001")
			.ruleName("SIZE_LIMIT")
			.ruleDescription("6인 1팀 원칙")
			.isOk(isOk)
			.requiredStatus("6명")
			.build();
	}

	//전공 비전공 각각 2인 이상
	private TeamRuleResponseDto teamMajorRule(int majorCount, int nonMajorCount) {
		boolean isMajorOk = (majorCount >= 2);
		boolean isNonMajorOk = (nonMajorCount >= 2);
		boolean isOk = (isMajorOk && isNonMajorOk);

		String requiredStatus = "전공자 2명 이상, 비전공자 2명 이상";

		return TeamRuleResponseDto.builder()
			.ruleCode("RULE002&003")
			.ruleName("전공 비전공 최소 인원 수")
			.ruleDescription("전공자 2인 이상, 비전공자 2인 이상")
			.isOk(isOk)
			.requiredStatus(requiredStatus)
			.build();
	}

	private SubCode getSubCodeByValue(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		// 1) 먼저 PK(subCode) 로 시도
		SubCode byCode = subCodeRepository.findBySubCode(value);
		if (byCode != null) {
			return byCode;
		}
		// 2) 없으면 한글명(subCodeName) 으로 조회
		return subCodeRepository.findBySubCodeName(value);
	}

	//팀원 정보 변환
	private TeamMemberResponseDto convertToTeamMemberResponse(Students students) {
		try {
			StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(students.getStudentId())
				.orElse(null);

			String major = Boolean.TRUE.equals(students.getMajorYn()) ? "전공" : "비전공";
			String profileImageUrl = (studentInfo != null) ? studentInfo.getProfileImageUrl() : "";

			SubCodeResponseDto position = null;
			if (studentInfo != null && studentInfo.getPositionCode() != null) {
				position = new SubCodeResponseDto(
					studentInfo.getPositionCode().getSubCode(),
					studentInfo.getPositionCode().getSubCodeName()
				);
			}

			return TeamMemberResponseDto.builder()
				.studentId(students.getStudentId())
				.name(students.getName())
				.major(major)
				.profileImageUrl(profileImageUrl)
				.position(position)
				.build();
		} catch (Exception e) {
			return TeamMemberResponseDto.builder()
				.studentId(students.getStudentId())
				.name(students.getName())
				.major(Boolean.TRUE.equals(students.getMajorYn()) ? "전공" : "비전공")
				.profileImageUrl("")
				.position(null)
				.build();
		}

	}

	//String positioncodes를 recruitment 리스트로 변환
	private List<Recruitment> toRecruitments(List<String> positionNames, Team team) {
		return positionNames.stream()
			.map(name -> {
				SubCode pos = getSubCodeByValue(name);
				if (pos == null) {
					throw new ResponseStatusException(
						HttpStatus.NOT_FOUND, "없는 포지션입니다: " + name);
				}
				return Recruitment.builder()
					.position(pos)
					.team(team)
					.build();
			})
			.collect(Collectors.toList());
	}

	/**
	 * 팀 ID로 팀의 간단한 정보를 조회합니다.(팀 초대, 및 합치기에 사용)
	 * 존재하지 않는 팀이면 404 에러 발생
	 * @param teamId
	 * @return TeamSimpleResponseDto
	 * @throws ResponseStatusException
	 */
	public TeamSimpleResponseDto findSimpleTeamInfoByTeamId(Long teamId) {
		Optional<Team> optionalTeam = teamRepository.findById(teamId);

		if (optionalTeam.isEmpty()) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다.");
		}

		Team taem = optionalTeam.get();

		return TeamSimpleResponseDto.builder()
			.teamId(taem.getTeamId())
			.name(taem.getName())
			.track(taem.getTrack().getSubCodeName())
			.majorCount(taem.getMajorCount())
			.nonMajorCount(taem.getNonMajorCount())
			.build();
	}

	public TeamSimpleResponseDto createTeamSimpleResponseDto(Long teamId, String teamName, String trackCodeName,
		int majorCount, int nonMajorCout) {
		return new TeamSimpleResponseDto(teamId, teamName, trackCodeName, majorCount, nonMajorCout);
	}

}
