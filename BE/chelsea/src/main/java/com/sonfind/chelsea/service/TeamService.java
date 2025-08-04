package com.sonfind.chelsea.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.teams.Recruitment;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.subcode.SubCodeResponse;
import com.sonfind.chelsea.dto.teams.CreateTeamRequest;
import com.sonfind.chelsea.dto.teams.MyTeamResponse;
import com.sonfind.chelsea.dto.teams.RecruitmentDto;
import com.sonfind.chelsea.dto.teams.TeamMemberResponse;
import com.sonfind.chelsea.dto.teams.TeamResponse;
import com.sonfind.chelsea.dto.teams.TeamRuleResponse;
import com.sonfind.chelsea.dto.teams.UpdateTeamRequest;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.repository.StudentRepository;
import com.sonfind.chelsea.repository.SubCodeRepository;
import com.sonfind.chelsea.repository.TeamRepository;
import com.sonfind.chelsea.repository.studentInfo.StudentInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;
	private final SubCodeRepository subCodeRepository;
	private final StudentRepository studentRepository;
	private final StudentInfoRepository studentInfoRepository;
	private final StudentService studentService;

	//팀 생성
	@Transactional
	public Long createTeam(Long studentId, CreateTeamRequest request) {

		//팀에 속해 있는 교육생은 팀 생성 못함
		Students students = studentService.findByStudentId(studentId);
		if (students.getTeamId() != null) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "이미 팀에 속해있습니다");
		}

		//subCode로 track
		//없는 트랙이면 badrequest
		SubCode track = subCodeRepository.findById(request.getTrackCode())
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "없는 트랙입니다."));

		//팀 명 없이 일단 저장
		Team noTeamName = Team.builder()
			.name("")
			.description(request.getDescription())
			.track(track)
			.build();

		Team team = teamRepository.save(noTeamName);

		//자동 팀명 생성(e.g. 팀 001)
		String autoName = String.format("팀 %03d", team.getTeamId());
		team.updateName(autoName);

		// positions를 recruitments로 변환
		if (request.getPositions() != null && !request.getPositions().isEmpty()) {
			List<Recruitment> recruitments = toRecruitments(request.getPositions(), team);
			team.updatePositions(recruitments);
		}

		//학생 teamId 저장
		students.setTeamId(team.getTeamId());
		studentRepository.save(students);

		return team.getTeamId();
	}

	//팀 수정
	@Transactional
	public void updateTeam(Long teamId, Long studentId, UpdateTeamRequest request) {

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
		if (request.getDescription() != null) {
			team.updateDescription(request.getDescription());
		}

		//희망 트랙 수정
		if (request.getTrackCode() != null) {
			SubCode track = subCodeRepository.findById(request.getTrackCode())
				.orElseThrow(() -> new ResponseStatusException(
					HttpStatus.NOT_FOUND, "없는 트랙입니다"));
			team.updateTrack(track);
		}

		//모집 포지션 수정
		if (request.getPositions() != null) {
			List<Recruitment> recruitments = toRecruitments(request.getPositions(), team);
			team.updatePositions(recruitments);
		}
	}

	//타 팀 상세조회
	@Transactional
	public TeamResponse getTeamDetail(Long teamId) {
		//존재하는 팀인지 확인
		Team team = teamRepository.findTeamByTeamId(teamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."));

		List<Students> teamMembers = studentRepository.findAllByTeamId(teamId);

		//팀원 정보 변환
		List<TeamMemberResponse> members = teamMembers.stream()
			.map(this::convertToTeamMemberResponse)
			.collect(Collectors.toList());

		//모집 포지션
		List<RecruitmentDto> positions = team.getRecruitments().stream()
			.map(recruitment -> new RecruitmentDto(
				recruitment.getPosition().getSubCode(),
				recruitment.getPosition().getSubCodeName()
			))
			.collect(Collectors.toList());

		return TeamResponse.builder()
			.teamName(team.getName())
			.teamDescription(team.getDescription())
			.teamTrack(new SubCodeResponse(
				team.getTrack().getSubCode(),
				team.getTrack().getSubCodeName()
			))
			.teamCount((long)teamMembers.size())
			.positions(positions)
			.members(members)
			.build();
	}

	//내 팀 상세조회
	@Transactional
	public MyTeamResponse getMyTeamDetail(Long studentId) {
		Students students = studentService.findByStudentId(studentId);
		if (students.getTeamId() == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "팀에 속해 있지 않습니다.");
		}

		TeamResponse teamInfo = getTeamDetail(students.getTeamId());

		List<Students> teamMembers = studentRepository.findAllByTeamId(students.getTeamId());

		int majorCount = (int)teamMembers.stream()
			.mapToLong(member -> Boolean.TRUE.equals(member.getMajorYn()) ? 1L : 0L)
			.sum();
		int nonMajorCount = teamMembers.size() - majorCount;
		int teamCount = teamMembers.size();

		List<TeamRuleResponse> ruleStatuses = isOkTeamRules(teamCount, majorCount, nonMajorCount);

		return MyTeamResponse.builder()
			.teamInfo(teamInfo)
			.majorCount(majorCount)
			.nonMajorCount(nonMajorCount)
			.ruleStatuses(ruleStatuses)
			.build();
	}

	//팀 빌딩 규칙
	//6인 1팀
	private TeamRuleResponse teamSizeRule(int teamSize) {
		boolean isOk = (teamSize == 6);

		return TeamRuleResponse.builder()
			.ruleCode("RULE001")
			.ruleName("SIZE_LIMIT")
			.ruleDescription("6인 1팀 원칙")
			.isOk(isOk)
			.requiredStatus("6명")
			.build();
	}

	//전공 비전공 각각 2인 이상
	private TeamRuleResponse teamMajorRule(int majorCount, int nonMajorCount) {
		boolean isMajorOk = (majorCount >= 2);
		boolean isNonMajorOk = (nonMajorCount >= 2);
		boolean isOk = (isMajorOk && isNonMajorOk);

		String requiredStatus = "전공자 2명 이상, 비전공자 2명 이상";

		return TeamRuleResponse.builder()
			.ruleCode("RULE002&003")
			.ruleName("전공 비전공 최소 인원 수")
			.ruleDescription("전공자 2인 이상, 비전공자 2인 이상")
			.isOk(isOk)
			.requiredStatus(requiredStatus)
			.build();
	}

	//팀 빌딩 규칙 검증
	private List<TeamRuleResponse> isOkTeamRules(int teamSize, int majorCount, int nonMajorCount) {
		List<TeamRuleResponse> teamRules = List.of(
			teamSizeRule(teamSize),
			teamMajorRule(majorCount, nonMajorCount)
		);
		return teamRules;
	}

	private SubCode getSubCodeByValue(String subCode) {
		if (subCode == null || subCode.isBlank()) {
			return null;
		}
		return subCodeRepository.findBySubCode(subCode);
	}

	//팀원 정보 변환
	private TeamMemberResponse convertToTeamMemberResponse(Students students) {
		try {
			StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(students.getStudentId())
				.orElse(null);

			String major = Boolean.TRUE.equals(students.getMajorYn()) ? "전공" : "비전공";
			String profileImageUrl = (studentInfo != null) ? studentInfo.getProfileImageUrl() : "";

			SubCodeResponse position = null;
			if (studentInfo != null && studentInfo.getPositionCode() != null) {
				position = new SubCodeResponse(
					studentInfo.getPositionCode().getSubCode(),
					studentInfo.getPositionCode().getSubCodeName()
				);
			}

			return TeamMemberResponse.builder()
				.studentId(students.getStudentId())
				.name(students.getName())
				.major(major)
				.profileImageUrl(profileImageUrl)
				.position(position)
				.build();
		} catch (Exception e) {
			return TeamMemberResponse.builder()
				.studentId(students.getStudentId())
				.name(students.getName())
				.major(Boolean.TRUE.equals(students.getMajorYn()) ? "전공" : "비전공")
				.profileImageUrl("")
				.position(null)
				.build();
		}

	}

	//String positioncodes를 recruitment 리스트로 변환
	private List<Recruitment> toRecruitments(List<String> positionCodes, Team team) {
		return positionCodes.stream()
			.map(code -> {
				SubCode position = subCodeRepository.findById(code)
					.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"없는 포지션입니다"));
				return Recruitment.builder()
					.position(position)
					.team(team)
					.build();
			})
			.collect(Collectors.toList());
	}

}
