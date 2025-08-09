package com.sonfind.chelsea.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.dashboard.RatioResponseDto;
import com.sonfind.chelsea.dto.dashboard.TrackAggregationResult;
import com.sonfind.chelsea.dto.dashboard.TrackPositionMajorRatioResponseDto;
import com.sonfind.chelsea.dto.student.response.StudentResponseDto;
import com.sonfind.chelsea.dto.studentInfo.request.StudentInfoCreateRequestDto;
import com.sonfind.chelsea.dto.studentInfo.request.StudentInfoUpdateRequestDto;
import com.sonfind.chelsea.dto.studentInfo.response.StudentInfoForNotificationResponseDto;
import com.sonfind.chelsea.dto.studentInfo.response.StudentInfoGetDetailResponseDto;
import com.sonfind.chelsea.dto.studentInfo.response.StudentInfoGetSummaryResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.repository.StudentFavoriteRepository;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.SubCodeRepository;
import com.sonfind.chelsea.repository.TeamRepository;
import com.sonfind.chelsea.util.StringListConverter;

import jakarta.persistence.EntityNotFoundException;
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

	/**
	 * 자기소개 생성 함수
	 * */
	@Transactional
	public void createStudentInfo(Long studentId, StudentInfoCreateRequestDto requestDto, MultipartFile profile,
		MultipartFile portfolio) {

		if (studentInfoRepository.existsByStudent_StudentId(studentId)) {
			throw new IllegalStateException("이미 자기소개서를 작성했습니다.");
		}

		try {
			String profileImageUrl = fileService.saveProfileImage(profile);
			UploadedFile uploadPortfolio = fileService.savePortfolio(portfolio);
			StudentInfo studentInfo = saveStudentInfo(studentId, requestDto, profileImageUrl, uploadPortfolio);

			studentInfoRepository.save(studentInfo);
		} catch (IOException e) {
			throw new IllegalStateException("파일 저장 처리 중 오류가 발생했습니다." + e);
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

		StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(studentId).orElseThrow(null);
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
			.profileImageUrl(studentInfo.getProfileImageUrl())
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
			.orElseThrow(() -> new EntityNotFoundException("해당 학생의 자기소개를 찾을 수 없습니다."));

		try {
			//새로운 프로필 이미지 입력 시 변경
			if (profile != null && !profile.isEmpty()) {
				String newProfileImage = fileService.saveProfileImage(profile);
				String oldProfileImage = studentInfo.getProfileImageUrl();
				studentInfo.updateProfileImage(newProfileImage);
				fileService.deleteProfileImage(oldProfileImage);
			}
			//새로운 포트폴리오 입력 시 변경
			if (portfolio != null && !portfolio.isEmpty()) {
				UploadedFile newPortfolio = fileService.savePortfolio(portfolio);
				UploadedFile oldPortfolio = studentInfo.getPortfolio();
				studentInfo.updatePortfolio(newPortfolio);
				fileService.deletePortfolioFile(oldPortfolio);
			}

			List<String> requiredCodes = List.of(requestDto.track(), requestDto.position(), requestDto.goal(),
				requestDto.mbti());
			List<SubCode> subCodes = subCodeRepository.findAllBySubCodeIn(requiredCodes);
			studentInfo.update(requestDto, subCodes);
		} catch (IOException e) {
			throw new IllegalStateException("파일 수정 처리 중 오류 발생했습니다.", e);
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
			.orElse(null);

		if (findStuInfo == null) {
			throw new IllegalArgumentException("해당 학생의 정보가 없습니다. studentId: " + studentId);
		}

		return StudentInfoForNotificationResponseDto.builder()
			.studentId(studentId)
			.position(getSubCodeByValue(findStuInfo.getPositionCode().getSubCode()).getSubCodeName())
			.track(getSubCodeByValue(findStuInfo.getTrackCode().getSubCode()).getSubCodeName())
			.profileImageUrl(findStuInfo.getProfileImageUrl())
			.build();
	}

	/**
	 * 대시보드 용 희망 트랙별 포지션 비율 조회
	 * */
	@Transactional(readOnly = true)
	public List<TrackPositionMajorRatioResponseDto> getTrackPositionRatio() {

		List<Object[]> rawData = studentInfoRepository.getTrackPositionMajorRatio();
		Map<String, TrackAggregationResult> aggregationMap = new HashMap<>();

		//중간 집계 : 각 트랙별로 전공과 포지션 별로 학생 수 조합
		for (Object[] row : rawData) {
			String track = (String)row[0];
			String position = (String)row[1];
			String major = (String)row[2];
			Long longCount = (Long)row[3];
			int count = longCount.intValue();

			TrackAggregationResult helper = aggregationMap.computeIfAbsent(track, k -> new TrackAggregationResult());

			helper.totalCount += count;
			helper.majorRecord.merge(major, count, Integer::sum);
			helper.positionRecord.merge(position, count, Integer::sum);
		}
		// 최종 response 만들기
		List<TrackPositionMajorRatioResponseDto> result = new ArrayList<>();
		for (Map.Entry<String, TrackAggregationResult> entry : aggregationMap.entrySet()) {
			String track = entry.getKey();
			TrackAggregationResult helper = entry.getValue();

			//majorRecord 맵을 List<RatioResponseDto>로 변환
			List<RatioResponseDto> majorType = helper.majorRecord.entrySet().stream()
				.map(e -> RatioResponseDto.builder().name(e.getKey()).count(e.getValue()).build()).toList();

			List<RatioResponseDto> positionType = helper.positionRecord.entrySet().stream()
				.map(e -> RatioResponseDto.builder().name(e.getKey()).count(e.getValue()).build()).toList();

			result.add(TrackPositionMajorRatioResponseDto.builder()
				.track(track).totalCount(helper.totalCount)
				.majorType(majorType).positionType(positionType).build());
		}

		return result;
	}

	public StudentInfoGetDetailResponseDto getOtherDetailStudentInfo(Long loginStudentId, Long targetStudentId) {
		StudentInfoGetDetailResponseDto.StudentInfoGetDetailResponseDtoBuilder builder = buildDetailResponseDto(
			targetStudentId);

		boolean isFavorite = favoriteRepository.existsByStudentStudentIdAndTargetStudentStudentId(loginStudentId,
			targetStudentId);

		return builder.isFavorite(isFavorite).build();

	}

	private StudentInfo saveStudentInfo(Long studentId, StudentInfoCreateRequestDto requestDto, String profileImageUrl,
		UploadedFile portfolio) {

		Student Student = studentService.findByStudentId(studentId);

		String techStackString = StringListConverter.listToString(requestDto.techStack());
		String strengthString = StringListConverter.listToString(requestDto.strength());

		SubCode trackCode = getSubCodeByValue(requestDto.track());
		SubCode positionCode = getSubCodeByValue(requestDto.position());
		SubCode goalCode = getSubCodeByValue(requestDto.goal());
		SubCode mbtiCode = getSubCodeByValue(requestDto.mbti());

		return StudentInfo.builder()
			.student(Student)
			.techStack(techStackString)
			.strength(strengthString)
			.description(requestDto.description())
			.trackCode(trackCode)
			.positionCode(positionCode)
			.goalCode(goalCode)
			.mbtiCode(mbtiCode)
			.profileImageUrl(profileImageUrl)
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
			.orElseThrow(() -> new EntityNotFoundException("해당 학생의 자기소개를 찾을 수 없습니다."));

		Student student = studentInfo.getStudent();
		SubCode positionCode = studentInfo.getPositionCode();
		SubCode trackCode = studentInfo.getTrackCode();
		SubCode mbtiCode = studentInfo.getMbtiCode();
		SubCode goalCode = studentInfo.getGoalCode();
		Team team = teamRepository.findTeamByTeamId(student.getTeamId()).orElse(null);
		TeamSimpleResponseDto teamResponse = null;
		SubCodeResponseDto mbtiCodeResponse = null;

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

		if (mbtiCode != null) {
			mbtiCodeResponse = SubCodeResponseDto.builder().subcode(mbtiCode.getSubCode()).subcodeName(
				mbtiCode.getSubCodeName()).build();
		}

		return StudentInfoGetDetailResponseDto.builder()
			.student(StudentResponseDto.builder().studentId(studentId).name(student.getName())
				.major(student.getMajorYn() ? "전공" : "비전공").build())
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
			.profileImageUrl(studentInfo.getProfileImageUrl())
			.portfolio(studentInfo.getPortfolio())
			.teamInfo(teamResponse);
	}

}
