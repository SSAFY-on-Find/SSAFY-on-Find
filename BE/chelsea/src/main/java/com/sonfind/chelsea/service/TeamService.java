package com.sonfind.chelsea.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.teams.Recruitment;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.CreateTeamRequestDto;
import com.sonfind.chelsea.dto.teams.LeaveTeamResponseDto;
import com.sonfind.chelsea.dto.teams.MyTeamResponseDto;
import com.sonfind.chelsea.dto.teams.RecruitmentDto;
import com.sonfind.chelsea.dto.teams.TeamCreatePageDto;
import com.sonfind.chelsea.dto.teams.TeamListResponseDto;
import com.sonfind.chelsea.dto.teams.TeamMemberResponseDto;
import com.sonfind.chelsea.dto.teams.TeamResponseDto;
import com.sonfind.chelsea.dto.teams.TeamRuleResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.dto.teams.UpdateTeamRequestDto;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.repository.StudentFavoriteRepository;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.StudentRepository;
import com.sonfind.chelsea.repository.SubCodeRepository;
import com.sonfind.chelsea.repository.TeamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

	private final TeamRepository teamRepository;
	private final SubCodeRepository subCodeRepository;
	private final StudentRepository studentRepository;
	private final StudentInfoRepository studentInfoRepository;

	private final SubCodeService subCodeService;
	private final StudentService studentService;
	private final FavoriteService favoriteService;
	private final StudentFavoriteRepository studentFavoriteRepository;

	//팀 생성
	@Transactional
	public Long createTeam(Long studentId, CreateTeamRequestDto request) {

		//팀에 속해 있는 교육생은 팀 생성 못함
		Student student = studentService.findByStudentId(studentId);
		if (student.getTeamId() != null) {
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

		//최초 생성자 전공?비전공?
		int initialMajorCount = Boolean.TRUE.equals(student.getMajorYn()) ? 1 : 0;
		int initialNonMajorCount = Boolean.FALSE.equals(student.getMajorYn()) ? 1 : 0;

		//팀 명 없이 일단 저장
		Team noTeamName = Team.builder()
			.name("")
			.description(request.description())
			.track(track)
			.majorCount(initialMajorCount)
			.nonMajorCount(initialNonMajorCount)
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
		student.setTeamId(team.getTeamId());
		studentRepository.save(student);

		return team.getTeamId();
	}

	//팀 생성 페이지
	@Transactional(readOnly = true)
	public TeamCreatePageDto getTeamCreatePage() {
		List<SubCodeResponseDto> tracks = subCodeRepository
			.findByMainCodeAndUseYnTrue("TRK")
			.stream()
			.map(sc -> new SubCodeResponseDto(sc.getSubCode(), sc.getSubCodeName()))
			.collect(Collectors.toList());

		List<SubCodeResponseDto> positions = subCodeRepository
			.findByMainCodeAndUseYnTrue("POS")
			.stream()
			.map(sc -> new SubCodeResponseDto(sc.getSubCode(), sc.getSubCodeName()))
			.collect(Collectors.toList());

		return new TeamCreatePageDto(tracks, positions);
	}

	//팀 수정
	@Transactional
	public void updateTeam(Long teamId, Long studentId, UpdateTeamRequestDto request) {

		//팀에 속해 있지 않은 교육생은 수정 불가
		Student Student = studentService.findByStudentId(studentId);
		if (!teamId.equals(Student.getTeamId())) {
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

	@Transactional
	public void addStudentToTeam(Long teamId, Long studentId) {
		//들어가고 싶은 팀
		Team targetTeam = teamRepository.findTeamByTeamId(teamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."));
		if (targetTeam.isDeleted()) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST, "삭제된 팀입니다.");
		}

		//학생
		Student student = studentService.findByStudentId(studentId);
		Long currentTeamId = student.getTeamId();

		//들어가고 싶은 팀 정원
		List<Student> targetTeamMembers = studentRepository.findAllByTeamId(teamId);

		//이미 해당 팀이라면
		if (teamId.equals(currentTeamId)) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST, "이미 해당 팀의 교육생 입니다.");
		}

		//정원 초과 확인
		if (targetTeamMembers.size() >= 6) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST, "팀 정원이 초과됐습니다.");
		}

		//기존 팀이 있다면 삭제
		if (currentTeamId != null) {
			removeStudentFromTeam(currentTeamId, studentId);
		}

		//새 팀에 추가
		student.setTeamId(teamId);
		studentRepository.save(student);

		//전공/비전공 업데이트
		//팀 전공/비전공 수정
		if (Boolean.TRUE.equals(student.getMajorYn())) {
			targetTeam.decrementMajorCount();
		} else {
			targetTeam.decrementNonMajorCount();
		}
		teamRepository.save(targetTeam);

		if (currentTeamId != null) {
			log.info("교육생 {}이 팀 {}에서 팀 {}으로 이동했습니다.", studentId, currentTeamId, teamId);
		} else {
			log.info("교육생 {}이 팀 {}에 합류했습니다.", studentId, teamId);
		}
	}

	//팀에서 교육생 제거
	@Transactional
	public void removeStudentFromTeam(Long teamId, Long studentId, boolean skilValidation) {
		Team team = teamRepository.findTeamByTeamId(teamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."));

		Student student = studentService.findByStudentId(studentId);

		if (!skilValidation && !teamId.equals(student.getTeamId())) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST, "해당 팀에 속하지 않은 교육생입니다.");
		}

		//팀에서 학생 제거
		student.setTeamId(null);
		studentRepository.save(student);

		//팀 전공/비전공 수정
		if (Boolean.TRUE.equals(student.getMajorYn())) {
			team.decrementMajorCount();
		} else {
			team.decrementNonMajorCount();
		}

		List<Student> remainingMembers = studentRepository.findAllByTeamId(teamId);

		if (remainingMembers.isEmpty()) {
			team.softDelete();
			log.info("팀 {}이 빈 팀이 되어 삭제되었습니다.", teamId);
		}

		teamRepository.save(team);
		log.info("교육생 {}이 팀 {}에서 제거되었습니다.", studentId, teamId);
	}

	@Transactional
	public void removeStudentFromTeam(Long teamId, Long studentId) {
		removeStudentFromTeam(teamId, studentId, false);
	}

	//팀 나가기
	@Transactional
	public LeaveTeamResponseDto leaveTeam(Long studentId) {
		Student student = studentService.findByStudentId(studentId);
		if (student.getTeamId() == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "팀에 속해 있지 않습니다.");
		}

		Long teamId = student.getTeamId();

		List<Student> membersBeforeLeave = studentRepository.findAllByTeamId(teamId);
		boolean willBeEmptyTeam = membersBeforeLeave.size() <= 1;

		removeStudentFromTeam(teamId, studentId, true);

		return LeaveTeamResponseDto.builder()
			.message(willBeEmptyTeam ? "팀에서 나갔습니다. 팀이 삭제되었습니다." : "팀에서 나갔습니다.")
			.teamDeleted(willBeEmptyTeam)
			.build();
	}

	//두 팀 합치기
	//sourceTeamId 뿌셔질 팀
	//targetTeamId 유지되는 팀
	@Transactional
	public void mergeTeams(Long sourceTeamId, Long targetTeamId) {
		//두 팀의 존재에 관하여
		Team sourceTeam = teamRepository.findTeamByTeamId(sourceTeamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "합쳐질 팀이 존재하지 않습니다."));

		Team targetTeam = teamRepository.findTeamByTeamId(targetTeamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "합칠 팀이 존재하지 않습니다."));

		if (sourceTeam.isDeleted() || targetTeam.isDeleted()) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST, "삭제된 팀입니다.");
		}
		//같은 팀인지 확인
		if (sourceTeamId.equals(targetTeamId)) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST, "같은 팀끼리는 합칠 수 없습니다.");
		}

		//각 팀의 학생 조회
		//뿌셔지는 팀 학생
		List<Student> sourceMembers = studentRepository.findAllByTeamId(sourceTeamId);
		//유지되는 팀 학생
		List<Student> targetMembers = studentRepository.findAllByTeamId(targetTeamId);

		//합칠 때 팀 규칙 정원 확인
		if (sourceMembers.size() + targetMembers.size() > 6) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				String.format("팀 규칙 정원이 초과됨니다. 현재 %d명, 최대 인원은 6명입니다.",
					targetMembers.size() + sourceMembers.size()));
		}

		//소스 팀 멤버를 타켓 팀으로 이동
		for (Student member : sourceMembers) {
			member.setTeamId(targetTeamId);
			studentRepository.save(member);

			//전공/비전공 업데이트
			if (Boolean.TRUE.equals(member.getMajorYn())) {
				targetTeam.incrementMajorCount();
			} else {
				targetTeam.incrementNonMajorCount();
			}
		}

		//소스 팀 삭제
		sourceTeam.softDelete();
		teamRepository.save(sourceTeam);
		teamRepository.save(targetTeam);

		log.info("팀 {}과 팀 {}이 합쳐졌습니다.", sourceTeamId, targetTeamId);
	}

	//타 팀 상세조회
	@Transactional(readOnly = true)
	public TeamResponseDto getTeamDetail(Long teamId, Long studentId) {
		//존재하는 팀인지 확인
		Team team = teamRepository.findTeamByTeamId(teamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."));

		List<Student> teamMembers = studentRepository.findAllByTeamId(teamId);

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

		boolean isFavorite = favoriteService.checkTeamFavoriteStatus(studentId, teamId);

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
		Student Student = studentService.findByStudentId(studentId);
		if (Student.getTeamId() == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "팀에 속해 있지 않습니다.");
		}

		TeamResponseDto teamInfo = getTeamDetail(Student.getTeamId(), studentId);

		List<Student> teamMembers = studentRepository.findAllByTeamId(Student.getTeamId());

		int majorCount = (int)teamMembers.stream()
			.mapToLong(member -> Boolean.TRUE.equals(member.getMajorYn()) ? 1L : 0L)
			.sum();
		int nonMajorCount = teamMembers.size() - majorCount;
		int teamCount = teamMembers.size();

		List<TeamRuleResponseDto> ruleStatuses = isOkTeamRules(teamCount, majorCount, nonMajorCount);

		return MyTeamResponseDto.builder()
			.teamInfo(teamInfo)
			.majorCount(majorCount)
			.nonMajorCount(nonMajorCount)
			.ruleStatuses(ruleStatuses)
			.build();
	}

	//팀 전체 목록 조회
	@Transactional(readOnly = true)
	public List<TeamListResponseDto> getAllTeams(Long studentId) {
		List<Team> teams = teamRepository.findByIsDeletedIsFalseOrderByTeamIdAsc();

		return teams.stream()
			.map(team -> convertToTeamListResponse(team, studentId))
			.sorted(
				Comparator.comparing(TeamListResponseDto::isRecruitingComplete)
					.thenComparing(dto -> parseTeamNumber(dto.teamName()))
			)
			.collect(Collectors.toList());
	}

	//team2teamlistresponseDto
	private TeamListResponseDto convertToTeamListResponse(Team team, Long studentId) {
		List<Student> teamMembers = studentRepository.findAllByTeamId(team.getTeamId());

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

		boolean isFavorite = favoriteService.checkTeamFavoriteStatus(studentId, team.getTeamId());

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
	private String getStudentProfileImage(Student student) {
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

	//팀 빌딩 규칙 검증
	private List<TeamRuleResponseDto> isOkTeamRules(int teamSize, int majorCount, int nonMajorCount) {
		List<TeamRuleResponseDto> teamRules = List.of(
			teamSizeRule(teamSize),
			teamMajorRule(majorCount, nonMajorCount)
		);
		return teamRules;
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
	private TeamMemberResponseDto convertToTeamMemberResponse(Student Student) {
		try {
			StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(Student.getStudentId())
				.orElse(null);

			String major = Boolean.TRUE.equals(Student.getMajorYn()) ? "전공" : "비전공";
			String profileImageUrl = (studentInfo != null) ? studentInfo.getProfileImageUrl() : "";

			SubCodeResponseDto position = null;
			if (studentInfo != null && studentInfo.getPositionCode() != null) {
				position = new SubCodeResponseDto(
					studentInfo.getPositionCode().getSubCode(),
					studentInfo.getPositionCode().getSubCodeName()
				);
			}

			return TeamMemberResponseDto.builder()
				.studentId(Student.getStudentId())
				.name(Student.getName())
				.major(major)
				.profileImageUrl(profileImageUrl)
				.position(position)
				.build();
		} catch (Exception e) {
			return TeamMemberResponseDto.builder()
				.studentId(Student.getStudentId())
				.name(Student.getName())
				.major(Boolean.TRUE.equals(Student.getMajorYn()) ? "전공" : "비전공")
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

		Team team = optionalTeam.get();

		return TeamSimpleResponseDto.builder()
			.teamId(team.getTeamId())
			.name(team.getName())
			.track(team.getTrack().getSubCodeName())
			.majorCount(team.getMajorCount())
			.nonMajorCount(team.getNonMajorCount())
			.build();
	}

	public TeamSimpleResponseDto createTeamSimpleResponseDto(Long teamId, String teamName, String trackCodeName,
		int majorCount, int nonMajorCout) {
		return new TeamSimpleResponseDto(teamId, teamName, trackCodeName, majorCount, nonMajorCout);
	}

}
