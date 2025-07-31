package com.sonfind.chelsea.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sonfind.chelsea.domain.mates.StudentInfo;
import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.domain.teams.Recruitment;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.teams.CreateTeamRequest;
import com.sonfind.chelsea.dto.teams.MemberDto;
import com.sonfind.chelsea.dto.teams.RecruitmentDto;
import com.sonfind.chelsea.dto.teams.TeamDetailResponse;
import com.sonfind.chelsea.dto.teams.UpdateTeamRequest;
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
	private final StudentService studentService;

	//팀 생성
	@Transactional
	public Long createTeam(Long studentId, CreateTeamRequest request) {

		//팀에 속해 있는 교육생은 팀 생성 못함
		Students student = studentService.findByStudentId(studentId);
		if (student.getTeamId() != null) {
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
		student.setTeamId(team.getTeamId());
		studentRepository.save(student);

		return team.getTeamId();
	}

	//팀 수정
	@Transactional
	public void updateTeam(Long teamId, Long studentId, UpdateTeamRequest request) {

		//팀에 속해 있지 않은 교육생은 수정 불가
		Students student = studentService.findByStudentId(studentId);
		if (!teamId.equals(student.getTeamId())) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "팀에 속해 있지 않은 교육생은 수정할 수 없습니다."
			);
		}

		//존재하는 팀인지 확인
		Team team = teamRepository.findById(teamId)
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
	public TeamDetailResponse getTeamDetail(Long teamId) {
		//존재하는 팀인지 확인
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."));

		//Recruitments 정보
		List<RecruitmentDto> recruitments = team.getRecruitments().stream()
			.map(r -> new Recruitment(r.getPosition().getSubCodeName()))
			.collect(Collectors.toList());

		//팀원(member) 정보
		List<MemberDto> members = studentInfoRepository.findAllByStudent_TeamId(teamId).stream()
			.map(this::mapToMemberDto)
			.collect(Collectors.toList());

		return new TeamDetailResponse(
			team.getTeamId(),
			team.getName(),
			team.getDescription(),
			team.getTrack().getSubCode(),
			team.getTrack().getSubCodeName(),
			recruitments,
			members.size(),
			members
		);
	}

	//내 팀 상세조회
	// @Transactional
	// public MyTeamDetailResponse getMyTeamDetailResponse(Long teamId, Long studentId) {
	//
	// }

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

	private MemberDto mapToMemberDto(StudentInfo info) {
		return new MemberDto(
			info.getStudent().getStudentId(),
			info.getStudent().getName(),
			info.getStudent().isMajorYn(),
			info.getProfileImageUrl(), //프로필 이미지는 왜 getStudent 안하고 바로 가져오는지?
			info.getPosition().getSubCodeName()
		);
	}
}
