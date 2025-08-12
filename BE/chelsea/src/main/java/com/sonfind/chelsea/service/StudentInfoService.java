package com.sonfind.chelsea.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.studentInfo.Portfolio;
import com.sonfind.chelsea.domain.studentInfo.Profile;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.dashboard.PositionChangeRequestDto;
import com.sonfind.chelsea.dto.dashboard.PositionMajorRatioResponseDto;
import com.sonfind.chelsea.dto.dashboard.RatioResponseDto;
import com.sonfind.chelsea.dto.student.response.StudentResponseDto;
import com.sonfind.chelsea.dto.studentInfo.request.StudentInfoCreateRequestDto;
import com.sonfind.chelsea.dto.studentInfo.request.StudentInfoUpdateRequestDto;
import com.sonfind.chelsea.dto.studentInfo.response.StudentInfoForNotificationResponseDto;
import com.sonfind.chelsea.dto.studentInfo.response.StudentInfoGetDetailResponseDto;
import com.sonfind.chelsea.dto.studentInfo.response.StudentInfoGetSummaryResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.repository.StudentFavoriteRepository;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.SubCodeRepository;
import com.sonfind.chelsea.repository.TeamRepository;
import com.sonfind.chelsea.util.StringListConverter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentInfoService {

	private final SubCodeRepository subCodeRepository;
	private final StudentInfoRepository studentInfoRepository;
	private final TeamRepository teamRepository;
	private final StudentFavoriteRepository favoriteRepository;

	private final StudentService studentService;
	private final FileService fileService;
	private final DashBoardCommandService dashBoardCommandService;

	/**
	 * 자기소개 생성 함수
	 * */
	@Transactional
	public void createStudentInfo(Long studentId, StudentInfoCreateRequestDto requestDto, MultipartFile profile,
		MultipartFile portfolio) {

		if (studentInfoRepository.existsByStudent_StudentId(studentId)) {
			throw AppException.studentInfoAlreadyExists();
		}

		try {
			Profile uploadProfile = fileService.saveProfileImage(profile);
			Portfolio uploadPortfolio = fileService.savePortfolio(portfolio);
			StudentInfo studentInfo = saveStudentInfo(studentId, requestDto, uploadProfile, uploadPortfolio);

			studentInfoRepository.save(studentInfo);
			dashBoardCommandService.publishStudentInfoUpdateEvent(
				getEventDto(null, studentInfo.getPositionCode(), studentInfo.getStudent()));

		} catch (IOException e) {
			throw AppException.fileUploadError();
		}

	}

	/**
	 * 자기소개 상세 조회 함수
	 * */
	@Transactional(readOnly = true)
	public StudentInfoGetDetailResponseDto getMeDetailStudentInfo(Long studentId) {
		StudentInfoGetDetailResponseDto.StudentInfoGetDetailResponseDtoBuilder builder = buildDetailResponseDto(
			studentId);

		return builder.isFavorite(false).build();
	}

	/**
	 * 자기소개 간단 조회 함수
	 * */
	@Transactional(readOnly = true)
	public StudentInfoGetSummaryResponseDto getSummaryStudentInfo(Long studentId) {

		StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(studentId)
			.orElseThrow(AppException::studentInfoNotFound);
		Student student = studentInfo.getStudent();
		SubCode positionCode = studentInfo.getPositionCode();
		SubCode trackCode = studentInfo.getTrackCode();

		Team team = teamRepository.findTeamByTeamId(student.getTeamId()).orElse(null);
		TeamSimpleResponseDto teamResponse = null;

		if (team != null) {
			teamResponse = TeamSimpleResponseDto.builder()
				.teamId(team.getTeamId())
				.name(team.getName())
				.track(team.getTrack().getSubCodeName())
				.majorCount(team.getMajorCount())
				.nonMajorCount(team.getNonMajorCount())
				.build();
		}

		return StudentInfoGetSummaryResponseDto.builder()
			.student(StudentResponseDto.builder().studentId(studentId).name(student.getName())
				.major(student.getMajorYn() ? "전공" : "비전공").build())
			.position(SubCodeResponseDto.builder().subcode(positionCode.getSubCode())
				.subcodeName(positionCode.getSubCodeName()).build())
			.track(SubCodeResponseDto.builder().subcode(trackCode.getSubCode())
				.subcodeName(trackCode.getSubCodeName()).build())
			.profileImageUrl(getProfileImageUrl(studentInfo.getProfile()))
			.team(teamResponse)
			.build();
	}

	/**
	 * 자기소개 수정 함수
	 * */
	@Transactional
	public void updateStudentInfo(Long studentId, StudentInfoUpdateRequestDto requestDto, MultipartFile profile,
		MultipartFile portfolio) {

		StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(studentId)
			.orElseThrow(AppException::studentInfoNotFound);

		try {
			//새로운 프로필 이미지 입력 시 변경
			if (profile != null && !profile.isEmpty()) {
				Profile newProfileImage = fileService.saveProfileImage(profile);
				Profile oldProfileImage = studentInfo.getProfile();
				studentInfo.updateProfileImage(newProfileImage);
				fileService.deleteProfileImage(oldProfileImage);
			}
			//새로운 포트폴리오 입력 시 변경
			if (portfolio != null && !portfolio.isEmpty()) {
				Portfolio newPortfolio = fileService.savePortfolio(portfolio);
				Portfolio oldPortfolio = studentInfo.getPortfolio();
				studentInfo.updatePortfolio(newPortfolio);
				fileService.deletePortfolioFile(oldPortfolio);
			}

			List<String> requiredCodes = List.of(requestDto.track(), requestDto.position(), requestDto.goal(),
				requestDto.mbti());
			List<SubCode> subCodes = subCodeRepository.findAllBySubCodeIn(requiredCodes);

			SubCode prePosition = studentInfo.getPositionCode();
			studentInfo.update(requestDto, subCodes);
			dashBoardCommandService.publishStudentInfoUpdateEvent(
				getEventDto(prePosition, studentInfo.getPositionCode(), studentInfo.getStudent()));

		} catch (IOException e) {
			throw AppException.fileUploadError();
		}
	}

	/**
	 * 학생 ID로 학생 정보를 조회하는 메소드(SSE용)
	 * @param studentId
	 * @return StudentInfoForNotificationResponseDto(학생 ID, 포지션, 트랙, 프로필 이미지 URL)
	 * @throws IllegalArgumentException 해당 학생의 정보가 없을 경우
	 */
	@Transactional(readOnly = true)
	public StudentInfoForNotificationResponseDto findByStudentId(Long studentId) {
		StudentInfo findStuInfo = studentInfoRepository.findByStudent_StudentId(studentId)
			.orElseThrow(AppException::studentInfoNotFound);

		return StudentInfoForNotificationResponseDto.builder()
			.studentId(studentId)
			.position(getSubCodeByValue(findStuInfo.getPositionCode().getSubCode()).getSubCodeName())
			.track(getSubCodeByValue(findStuInfo.getTrackCode().getSubCode()).getSubCodeName())
			.profileImageUrl(getProfileImageUrl(findStuInfo.getProfile()))
			.build();
	}

	/**
	 * 대시보드 용 희망 트랙별 포지션 비율 조회
	 * */
	@Transactional(readOnly = true)
	public Map<String, PositionMajorRatioResponseDto> getPositionRatio() {

		List<Object[]> rawData = studentInfoRepository.getPositionMajorRatio();
		Map<String, PositionMajorRatioResponseDto> result = new LinkedHashMap<>();

		Map<String, List<Object[]>> midResult = rawData.stream()
			.collect(Collectors.groupingBy(row -> (String)row[0], LinkedHashMap::new, Collectors.toList()));

		midResult.forEach((positionName, rows) -> {
			List<RatioResponseDto> teamTypeRatios = rows.stream()
				.map(row -> {
					String teamType = (String)row[1];
					int count = ((Number)row[2]).intValue();
					return new RatioResponseDto(teamType, count);
				})
				.toList();

			int totalCount = teamTypeRatios.stream().mapToInt(RatioResponseDto::count).sum();

			PositionMajorRatioResponseDto dto = PositionMajorRatioResponseDto.builder()
				.totalCount(totalCount)
				.teamType(teamTypeRatios)
				.build();

			result.put(positionName, dto);
		});

		return result;
	}

	public StudentInfoGetDetailResponseDto getOtherDetailStudentInfo(Long loginStudentId, Long targetStudentId) {
		StudentInfoGetDetailResponseDto.StudentInfoGetDetailResponseDtoBuilder builder = buildDetailResponseDto(
			targetStudentId);

		boolean isFavorite = favoriteRepository.existsByStudentStudentIdAndTargetStudentStudentId(loginStudentId,
			targetStudentId);

		return builder.isFavorite(isFavorite).build();

	}

	private StudentInfo saveStudentInfo(Long studentId, StudentInfoCreateRequestDto requestDto, Profile profile,
		Portfolio portfolio) {

		Student student = studentService.findByStudentId(studentId);

		String techStackString = StringListConverter.listToString(requestDto.techStack());
		String strengthString = StringListConverter.listToString(requestDto.strength());

		SubCode trackCode = getSubCodeByValue(requestDto.track());
		SubCode positionCode = getSubCodeByValue(requestDto.position());
		SubCode goalCode = getSubCodeByValue(requestDto.goal());
		SubCode mbtiCode = getSubCodeByValue(requestDto.mbti());

		return StudentInfo.builder()
			.student(student)
			.techStack(techStackString)
			.strength(strengthString)
			.description(requestDto.description())
			.trackCode(trackCode)
			.positionCode(positionCode)
			.goalCode(goalCode)
			.mbtiCode(mbtiCode)
			.profile(profile)
			.portfolio(portfolio)
			.build();
	}

	private SubCode getSubCodeByValue(String subCode) {

		if (subCode == null || subCode.isBlank()) {
			return null;
		}

		return subCodeRepository.findBySubCode(subCode);
	}

	/**
	 * 자기소개 상세 조회 공통 함수
	 * */
	private StudentInfoGetDetailResponseDto.StudentInfoGetDetailResponseDtoBuilder buildDetailResponseDto(
		Long studentId) {

		StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(studentId)
			.orElseThrow(AppException::studentInfoNotFound);

		Student student = Optional.ofNullable(studentInfo.getStudent()).orElseThrow(AppException::studentNotFound);

		SubCode positionCode = studentInfo.getPositionCode();
		SubCode trackCode = studentInfo.getTrackCode();
		SubCode goalCode = studentInfo.getGoalCode();

		SubCodeResponseDto mbtiCodeResponse = Optional.ofNullable(studentInfo.getMbtiCode())
			.map(sc -> new SubCodeResponseDto(sc.getSubCode(), sc.getSubCodeName()))
			.orElse(null);

		Team team = teamRepository.findTeamByTeamId(student.getTeamId()).orElse(null);
		TeamSimpleResponseDto teamResponse = null;

		//기술 스택처리
		List<String> techStackCodes = StringListConverter.stringToList(studentInfo.getTechStack());
		List<SubCodeResponseDto> techStackResponse = subCodeRepository.findAllBySubCodeIn(techStackCodes).stream()
			.map(sc -> new SubCodeResponseDto(sc.getSubCode(), sc.getSubCodeName()))
			.collect(Collectors.toList());
		//강점 처리
		List<String> strength = StringListConverter.stringToList(studentInfo.getStrength());

		if (team != null) {
			teamResponse = TeamSimpleResponseDto.builder()
				.teamId(team.getTeamId())
				.name(team.getName())
				.track(team.getTrack().getSubCodeName())
				.majorCount(team.getMajorCount())
				.nonMajorCount(team.getNonMajorCount())
				.build();
		}

		return StudentInfoGetDetailResponseDto.builder()
			.student(StudentResponseDto.builder().studentId(studentId).name(student.getName())
				.major(Boolean.TRUE.equals(student.getMajorYn()) ? "전공" : "비전공").build())
			.position(SubCodeResponseDto.builder().subcode(positionCode.getSubCode()).subcodeName(
				positionCode.getSubCodeName()).build())
			.track(SubCodeResponseDto.builder().subcode(trackCode.getSubCode()).subcodeName(
				trackCode.getSubCodeName()).build())
			.goal(SubCodeResponseDto.builder().subcode(goalCode.getSubCode()).subcodeName(
				goalCode.getSubCodeName()).build())
			.mbti(mbtiCodeResponse)
			.techStack(techStackResponse)
			.strength(strength)
			.description(studentInfo.getDescription())
			.profileImageUrl(getProfileImageUrl(studentInfo.getProfile()))
			.portfolio(studentInfo.getPortfolio())
			.teamInfo(teamResponse);
	}

	private String getProfileImageUrl(Profile profile) {
		if (profile != null) {
			return profile.getProfileImageUrl();
		}
		return "";
	}

	private PositionChangeRequestDto getEventDto(SubCode prePosition, SubCode curPosition, Student student) {
		return PositionChangeRequestDto.builder()
			.prePosition(Optional.ofNullable(prePosition).map(SubCode::getSubCodeName).orElse(""))
			.curPosition(curPosition.getSubCodeName())
			.isTeam(student.getTeamId() != null)
			.build();
	}

}
