package com.sonfind.chelsea.service;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.teams.Recruitment;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.dashboard.MemberSummary;
import com.sonfind.chelsea.dto.dashboard.TeamInfoUpdateDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.*;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.BusinessException;
import com.sonfind.chelsea.global.error.ErrorCode;
import com.sonfind.chelsea.repository.*;
import com.sonfind.chelsea.types.MemberChageAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

	private final TeamRepository teamRepository;
	private final SubCodeRepository subCodeRepository;
	private final StudentRepository studentRepository;
	private final StudentInfoRepository studentInfoRepository;
	private final TeamFavoriteRepository teamFavoriteRepository;
	private final RecruitmentRepository recruitmentRepository;

	private final StudentService studentService;
	private final FavoriteService favoriteService;

	//이벤트 발행기
	private final ApplicationEventPublisher eventPublisher;
	private final DashBoardCommandService dashBoardCommandService;

	//팀 생성
	@Transactional
	public Long createTeam(Long studentId, CreateTeamRequestDto request) {

		//팀에 속해 있는 교육생은 팀 생성 못함
		Student student = studentService.findByStudentId(studentId);
		if (student.getTeamId() != null) {
			throw AppException.teamAlreadyJoined();
		}

		//subCode로 track
		//없는 트랙이면 badrequest
		SubCode track = getSubCodeByValue(request.track());
		if (track == null) {
			throw AppException.trackNotFound();
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
		// student.setTeamId(team.getTeamId());
		// studentRepository.save(student);
		addStudentToTeam(team.getTeamId(), studentId);

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

		//db에서 팀 규칙 조회
		List<TeamRuleResponseDto> teamRules = getTeamRulesForCreatePage();

		return new TeamCreatePageDto(tracks, positions, teamRules);
	}

	//db에서 팀 규칙 조회
	private List<TeamRuleResponseDto> getTeamRulesForCreatePage() {
		//DB에서 useYn True인 규칙 조회
		List<SubCode> rules = subCodeRepository.findByMainCodeAndUseYnTrue("RULE");

		return rules.stream()
				.map(rule -> TeamRuleResponseDto.builder()
						.ruleCode(rule.getSubCode())
						.ruleName(rule.getSubCodeName())
						.ruleDescription(rule.getSubCodeDescription())
						.isOk(false)
						.requiredStatus(null)
						.build())
				.sorted((r1, r2) -> r1.ruleCode().compareTo(r2.ruleCode()))
				.collect(Collectors.toList());

	}

	// 규칙 코드별 필요 조건 문자열 반환 메서드
	// private String getRequiredStatusByRuleCode(String ruleCode) {
	// 	return switch (ruleCode) {
	// 		case "RULE001" -> "6명";
	// 		case "RULE002" -> "전공자 2명 이상";
	// 		case "RULE003" -> "비전공자 2명 이상";
	// 		default -> {
	// 			yield "조건 확인 필요";
	// 		}
	// 	};
	// }

	//팀 수정
	@Transactional
	public void updateTeam(Long teamId, Long studentId, UpdateTeamRequestDto request) {

		//팀에 속해 있지 않은 교육생은 수정 불가
		Student Student = studentService.findByStudentId(studentId);
		if (!teamId.equals(Student.getTeamId())) {
			throw AppException.teamPermissionDenied();
		}

		//존재하는 팀인지 확인
		Team team = teamRepository.findTeamByTeamId(teamId)
				.orElseThrow(AppException::teamNotFound);

		// 이벤트 발행용 변경사항 추적
		boolean hasChanges = false;
		String updatedTrack = null;
		String updateDescription = null;
		List<String> updatePositions = null;

		//팀 설명 수정
		if (request.description() != null) {
			team.updateDescription(request.description());
			updateDescription = request.description();
			hasChanges = true;
		}

		//희망 트랙 수정
		if (request.track() != null) {
			SubCode newTrack = getSubCodeByValue(request.track());
			if (newTrack == null) {
				throw AppException.trackNotFound();
			}
			team.updateTrack(newTrack);
			updatedTrack = newTrack.getSubCodeName();
			hasChanges = true;
		}

		//모집 포지션 수정
		if (request.positions() != null) {
			List<Recruitment> recruitments = toRecruitments(request.positions(), team);
			team.updatePositions(recruitments);
			updatePositions = request.positions();
			hasChanges = true;
		}

		if (hasChanges) {
			TeamInfoUpdateDto updateDto = TeamInfoUpdateDto.builder()
					.teamId(teamId)
					.track(updatedTrack != null ? updatedTrack : team.getTrack().getSubCodeName())
					.description(updateDescription != null ? updateDescription : team.getDescription())
					.afterNeedByPosition(updatePositions != null ? updatePositions :
							team.getRecruitments().stream()
									.map(r -> r.getPosition().getSubCodeName())
									.collect(Collectors.toList()))
					.build();

			//이벤트 발행
			eventPublisher.publishEvent(updateDto);
		}
	}

	//팀에 학생 추가
	@Transactional
	public void addStudentToTeam(Long teamId, Long studentId) {
		//들어가고 싶은 팀
		Team targetTeam = teamRepository.findTeamByTeamId(teamId)
				.orElseThrow(AppException::teamNotFound);

		if (targetTeam.isDeleted()) {
			throw AppException.teamDeletedAccess();
		}

		//학생
		Student student = studentService.findByStudentId(studentId);
		Long currentTeamId = student.getTeamId();

		//들어가고 싶은 팀 정원
		List<Student> targetTeamMembers = studentRepository.findAllByTeamId(teamId);

		//이미 해당 팀이라면
		if (teamId.equals(currentTeamId)) {
			throw AppException.teamAlreadyJoined();
		}

		//정원 초과 확인
		if (targetTeamMembers.size() >= 6) {
			throw AppException.teamFull();
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
			targetTeam.incrementMajorCount();
		} else {
			targetTeam.incrementNonMajorCount();
		}
		teamRepository.save(targetTeam);

		//멤버 정보 생성 및 이벤트 발행(팀 목록에서의 갱신 및 대시보드)
		MemberSummary memberSummary = createMemberSummary(student);
		teamMemberChangeEventPublisher(teamId, MemberChageAction.JOINED, memberSummary);

		if (currentTeamId != null) {
			log.info("교육생 {}이 팀 {}에서 팀 {}으로 이동했습니다.", studentId, currentTeamId, teamId);
		} else {
			log.info("교육생 {}이 팀 {}에 합류했습니다.", studentId, teamId);
		}
	}

	//두 팀 합치기
	//sourceTeamId 뿌셔질 팀
	//targetTeamId 유지되는 팀
	@Transactional
	public void mergeTeams(Long sourceTeamId, Long targetTeamId) {
		//두 팀의 존재에 관하여
		Team sourceTeam = teamRepository.findTeamByTeamId(sourceTeamId)
				.orElseThrow(AppException::teamNotFound);

		Team targetTeam = teamRepository.findTeamByTeamId(targetTeamId)
				.orElseThrow(AppException::teamNotFound);

		if (sourceTeam.isDeleted() || targetTeam.isDeleted()) {
			throw new BusinessException(ErrorCode.BAD_REQUEST);
		}
		//같은 팀인지 확인
		if (sourceTeamId.equals(targetTeamId)) {
			throw new BusinessException(ErrorCode.BAD_REQUEST);
		}

		//각 팀의 학생 조회
		//뿌셔지는 팀 학생
		List<Student> sourceMembers = studentRepository.findAllByTeamId(sourceTeamId);
		//유지되는 팀 학생
		List<Student> targetMembers = studentRepository.findAllByTeamId(targetTeamId);

		//합칠 때 팀 규칙 정원 확인
		if (sourceMembers.size() + targetMembers.size() > 6) {
			throw new BusinessException(ErrorCode.CONFLICT);
		}

		//소스 팀 멤버를 타켓 팀으로 이동
		for (Student member : sourceMembers) {
			//멤버 정보 미리 생성
			MemberSummary memberSummary = createMemberSummary(member);

			member.setTeamId(targetTeamId);
			studentRepository.save(member);

			//전공/비전공 업데이트
			if (Boolean.TRUE.equals(member.getMajorYn())) {
				targetTeam.incrementMajorCount();
			} else {
				targetTeam.incrementNonMajorCount();
			}

			// 각 멤버별 합류 이벤트 발행
			teamMemberChangeEventPublisher(targetTeamId, MemberChageAction.JOINED, memberSummary);
		}

		//소스 팀 삭제
		sourceTeam.softDelete();
		teamRepository.save(sourceTeam);
		teamRepository.save(targetTeam);

		log.info("팀 {}과 팀 {}이 합쳐졌습니다.", sourceTeamId, targetTeamId);
	}

	//팀에서 교육생 제거
	@Transactional
	public void removeStudentFromTeam(Long teamId, Long studentId, boolean skipValidation) {
		Team team = teamRepository.findTeamByTeamId(teamId)
				.orElseThrow(AppException::teamNotFound);

		Student student = studentService.findByStudentId(studentId);

		if (!skipValidation && !teamId.equals(student.getTeamId())) {
			throw AppException.teamNotInTeam();
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
			throw AppException.teamNotInTeam();
		}

		Long teamId = student.getTeamId();

		List<Student> membersBeforeLeave = studentRepository.findAllByTeamId(teamId);
		boolean willBeEmptyTeam = membersBeforeLeave.size() <= 1;

		//멤버 정보 나가기 전에 생성
		MemberSummary memberSummary = createMemberSummary(student);

		//팀에서 나가버렷~~
		removeStudentFromTeam(teamId, studentId, true);

		//팀 나가기 이벤트 발행(팀 목록에서의 갱신 및 대시보드)
		teamMemberChangeEventPublisher(teamId, MemberChageAction.LEFT, memberSummary);

		return LeaveTeamResponseDto.builder()
				.message(willBeEmptyTeam ? "팀에서 나갔습니다. 팀이 삭제되었습니다." : "팀에서 나갔습니다.")
				.teamDeleted(willBeEmptyTeam)
				.build();
	}

	//타 팀 상세조회
	@Transactional(readOnly = true)
	public TeamResponseDto getTeamDetail(Long teamId, Long studentId) {
		//존재하는 팀인지 확인
		Team team = teamRepository.findTeamByTeamId(teamId)
				.orElseThrow(AppException::teamNotFound);

		List<Student> teamMembers = studentRepository.findAllByTeamId(teamId);

		//팀원 정보 변환
		List<TeamMemberResponseDto> members = teamMembers.stream()
				.map(this::convertToTeamMemberResponse)
				.collect(Collectors.toList());

		int majorCount = (int) teamMembers.stream()
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
				.teamCount((long) teamMembers.size())
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
		Student student = studentService.findByStudentId(studentId);
		if (student.getTeamId() == null) {
			throw AppException.teamNotInTeam();
		}

		TeamResponseDto teamInfo = getTeamDetail(student.getTeamId(), studentId);

		List<Student> teamMembers = studentRepository.findAllByTeamId(student.getTeamId());

		int majorCount = (int) teamMembers.stream()
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
	//기존 75번 쿼리
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

	//쿼리 최적화 1: DTO 프로젝션 사용 (repo의 3,4번 쿼리)
	@Transactional(readOnly = true)
	public List<TeamListResponseDto> getAllTeamsOptimized(Long currentStudentId) {
		// 1. 팀 기본 정보 조회
		List<TeamWithMembersDto> teams = teamRepository.findAllTeamSummaries();
		List<Long> teamIds = teams.stream().map(TeamWithMembersDto::teamId).toList();

		// 2. 팀별 멤버 정보 한 번에 조회
		List<TeamMemberDto> members = teamRepository.findTeamMembersByTeamIds(teamIds);
		Map<Long, List<TeamMemberDto>> membersByTeam = members.stream()
				.collect(Collectors.groupingBy(TeamMemberDto::teamId));

		// 3. 팀별 모집공고 정보 한 번에 조회 (추가됨!)
		List<TeamRecruitmentDto> recruitments = teamRepository.findRecruitmentsByTeamIds(teamIds);
		Map<Long, List<TeamRecruitmentDto>> recruitmentsByTeam = recruitments.stream()
				.collect(Collectors.groupingBy(TeamRecruitmentDto::teamId));

		// 4. 즐겨찾기 정보 한 번에 조회
		Map<Long, Boolean> favoritesByTeam = getFavoritesByTeams(currentStudentId, teamIds);

		// 5. DTO 조합
		return teams.stream()
				.map(team -> TeamListResponseDto.builder()
						.teamId(team.teamId())
						.teamName(team.name())
						.description(team.description())
						.track(new SubCodeResponseDto(team.trackCode(), team.trackName()))
						.recruitments(convertToRecruitmentDtos(recruitmentsByTeam.get(team.teamId()))) // 추가됨!
						.members(convertToMemberDtos(membersByTeam.get(team.teamId())))
						.isRecruitingComplete(isRecruitingComplete(team, membersByTeam.get(team.teamId())))
						.isFavorite(favoritesByTeam.getOrDefault(team.teamId(), false))
						.build())
				.sorted(
						Comparator.comparing(TeamListResponseDto::isRecruitingComplete)
								.thenComparing(dto -> parseTeamNumber(dto.teamName()))
				)
				.toList();
	}

	//최적화된 방식 2: fetch join
	@Transactional(readOnly = true)
	public List<TeamListResponseDto> getAllTeamsFetchJoin(Long currentStudentId) {
		// 1. Fetch Join으로 팀과 모집공고 정보 조회
		List<Team> teams = teamRepository.findAllTeamsWithRecruitments();
		List<Long> teamIds = teams.stream().map(Team::getTeamId).toList();

		// 2. 멤버 정보는 별도 조회 (StudentInfo가 복잡하므로)
		List<TeamMemberDto> members = teamRepository.findTeamMembersByTeamIds(teamIds);
		Map<Long, List<TeamMemberDto>> membersByTeam = members.stream()
				.collect(Collectors.groupingBy(TeamMemberDto::teamId));

		// 3. 즐겨찾기 정보 조회
		Map<Long, Boolean> favoritesByTeam = getFavoritesByTeams(currentStudentId, teamIds);

		return teams.stream()
				.map(team -> convertToDto(team, membersByTeam.get(team.getTeamId()),
						favoritesByTeam.getOrDefault(team.getTeamId(), false)))
				.sorted(
						Comparator.comparing(TeamListResponseDto::isRecruitingComplete)
								.thenComparing(dto -> parseTeamNumber(dto.teamName()))
				)
				.toList();
	}

	//fetch join용 메서드
	// 3. convertToDto 메서드 수정 (Team 엔티티용)
	private TeamListResponseDto convertToDto(Team team, List<TeamMemberDto> members, boolean isFavorite) {
		return TeamListResponseDto.builder()
				.teamId(team.getTeamId())
				.teamName(team.getName())
				.description(team.getDescription())
				.track(new SubCodeResponseDto(team.getTrack().getSubCode(), team.getTrack().getSubCodeName()))
				.recruitments(team.getRecruitments().stream()
						.map(r -> new RecruitmentDto(r.getPosition().getSubCode(), r.getPosition().getSubCodeName()))
						.toList())
				.members(convertToMemberDtos(members))
				.isRecruitingComplete(isRecruitingComplete(team, members)) // Team 버전 사용
				.isFavorite(isFavorite)
				.build();
	}

	//밑에 4개는 쿼리 최적화를 위해 만든 메서드
	private Map<Long, Boolean> getFavoritesByTeams(Long studentId, List<Long> teamIds) {
		if (studentId == null)
			return Map.of();

		try {
			return teamFavoriteRepository.findFavoritesByStudentAndTeams(studentId, teamIds)
					.stream()
					.collect(Collectors.toMap(
							tf -> tf.getTeam().getTeamId(),
							tf -> tf.getIsFavorite() != null && tf.getIsFavorite()
					));
		} catch (Exception e) {
			return Map.of();
		}
	}

	private boolean isRecruitingComplete(TeamWithMembersDto team, List<TeamMemberDto> members) {
		if (members == null)
			return false;
		int currentMemberCount = members.size();
		int targetMemberCount = team.majorCount() + team.nonMajorCount();
		return currentMemberCount >= targetMemberCount;
	}

	// Team 엔티티용 오버로딩 메서드 추가
	private boolean isRecruitingComplete(Team team, List<TeamMemberDto> members) {
		if (members == null)
			return false;
		int currentMemberCount = members.size();
		// 기존 로직처럼 6명 기준으로 하거나, 팀의 목표 인원수로 할 수 있음
		return currentMemberCount >= 6; // 또는 team.getMajorCount() + team.getNonMajorCount()
	}

	private List<RecruitmentDto> convertToRecruitmentDtos(List<TeamRecruitmentDto> recruitments) {
		if (recruitments == null)
			return List.of();
		return recruitments.stream()
				.map(r -> new RecruitmentDto(r.positionCode(), r.positionName()))
				.toList();
	}

	private List<TeamMemberResponseDto> convertToMemberDtos(List<TeamMemberDto> members) {
		if (members == null)
			return List.of();

		return members.stream()
				.map(m -> TeamMemberResponseDto.builder()
						.studentId(m.studentId())
						.name(m.name())
						.major(m.majorYn() != null && m.majorYn() ? "전공" : "비전공")
						.profileImageUrl(m.profileImageUrl())
						.position(new SubCodeResponseDto(m.positionCode(), m.positionName()))
						.build())
				.toList();
	}

	//쿼리 최적화 2: Fetch Join 사용(repo의 1,2번 쿼리)

	/**
	 * 대시보드 용 팀 추천 함수
	 *
	 */
	public List<TeamListResponseDto> getRecommendTeamList(Long studentId) {

		if (teamRepository.findAll().isEmpty()) {
			return List.of(TeamListResponseDto.builder().teamId(0L).build());
		}

		Student student = studentService.findByStudentId(studentId);
		Long teamId = student.getTeamId();
		//내가 팀에 소속되어 있지 않은 경우
		List<TeamListResponseDto> result = null;
		SubCode trackCode = null;
		List<SubCode> positionCodes = null;
		int teamMemberCount = 0;
		List<Long> teamMember = null;

		if (teamId == null) {
			StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(studentId)
					.orElseThrow(AppException::studentInfoNotFound);
			trackCode = studentInfo.getTrackCode();
			positionCodes = List.of(studentInfo.getPositionCode());
			teamMemberCount = 1;
			teamMember = List.of(studentId);
		} else {    //내가 팀에 소속된 경우
			Team team = teamRepository.findTeamByTeamId(teamId).orElseThrow(AppException::teamNotFound);
			teamMemberCount = team.getMajorCount() + team.getNonMajorCount();
			teamMember = studentRepository.findAllByTeamId(teamId)
					.stream()
					.map(Student::getTeamId)
					.collect(Collectors.toList());
			trackCode = team.getTrack();
			positionCodes = recruitmentRepository.findPositionByTeamId(teamId);
		}

		List<Long> candidateTeamId = teamRepository.findCandidateTeams(trackCode, positionCodes,
				teamMemberCount);
		List<Team> teamInfo = teamRepository.findRecommend(teamMember, candidateTeamId, teamId, teamMemberCount);
		result = teamInfo.stream()
				.map(team -> convertToTeamListResponse(team, studentId))
				.collect(Collectors.toList());

		return result;
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
					.map(studentInfo -> studentInfo.getProfile().getProfileImageUrl())
					.orElse(null);
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
	private TeamMemberResponseDto convertToTeamMemberResponse(Student student) {
		try {
			StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(student.getStudentId())
					.orElse(null);

			String major = Boolean.TRUE.equals(student.getMajorYn()) ? "전공" : "비전공";
			String profileImageUrl = (studentInfo != null && studentInfo.getProfile() != null) ?
					studentInfo.getProfile().getProfileImageUrl() : "";

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
		} catch (Exception e) {
			return TeamMemberResponseDto.builder()
					.studentId(student.getStudentId())
					.name(student.getName())
					.major(Boolean.TRUE.equals(student.getMajorYn()) ? "전공" : "비전공")
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
						throw AppException.positionNotFound();
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
	 *
	 * @param teamId
	 * @return TeamSimpleResponseDto
	 * @throws ResponseStatusException
	 */
	public TeamSimpleResponseDto findSimpleTeamInfoByTeamId(Long teamId) {
		Optional<Team> optionalTeam = teamRepository.findById(teamId);

		if (optionalTeam.isEmpty()) {
			throw AppException.teamNotFound();
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

	@Transactional(readOnly = true)
	public Map<Long, TeamMini> findTeamMiniMap(Collection<Long> ids) {
		if (ids == null || ids.isEmpty()) return Map.of();
		return teamRepository.findMiniByTeamIdIn(ids).stream()
				.collect(Collectors.toMap(TeamMini::getTeamId, Function.identity()));
	}

	private MemberSummary createMemberSummary(Student student) {
		try {
			StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(student.getStudentId())
					.orElse(null);

			String majorType = Boolean.TRUE.equals(student.getMajorYn()) ? "전공" : "비전공";
			String progileImageUrl = (studentInfo != null && studentInfo.getProfile() != null) ?
					studentInfo.getProfile().getProfileImageUrl() : "";
			String position = (studentInfo != null && studentInfo.getPositionCode() != null) ?
					studentInfo.getPositionCode().getSubCodeName() : "";

			return MemberSummary.builder()
					.id(student.getStudentId())
					.name(student.getName())
					.profileImageUrl(progileImageUrl)
					.majorType(majorType)
					.position(position)
					.build();
		} catch (Exception e) {
			return MemberSummary.builder()
					.id(student.getStudentId())
					.name(student.getName())
					.profileImageUrl("")
					.majorType(Boolean.TRUE.equals(student.getMajorYn()) ? "전공" : "비전공")
					.position("")
					.build();
		}

	}

	private void teamMemberChangeEventPublisher(Long teamId, MemberChageAction action, MemberSummary memberSummary) {
		// 팀 빌딩 진행률 업데이트(대시보드 갱신)
		dashBoardCommandService.publishTeamBuildingProgressEvent();
		// 팀원 변경 이벤트 발행
		dashBoardCommandService.publishTeamMemberChangedEvent(teamId, action, memberSummary);
	}

}
