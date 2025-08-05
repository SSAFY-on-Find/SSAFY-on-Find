package com.sonfind.chelsea.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.dto.student.StudentResponseDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoCreateRequestDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoForNotificationResponseDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoGetQueryDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoGetResponseDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoUpdateRequestDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.SubCodeRepository;
import com.sonfind.chelsea.util.StringListConverter;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentInfoService {

	private final SubCodeRepository subCodeRepository;
	private final StudentInfoRepository studentInfoRepository;

	private final StudentService studentService;
	private final SubCodeService subCodeService;
	private final TeamService teamService;
	private final FileService fileService;

	/**
	 * 자기소개 생성 함수
	 * */
	@Transactional
	public void createStudentInfo(Long studentId, StudentInfoCreateRequestDto requestDto, MultipartFile profile,
		MultipartFile portfolio) throws IOException {

		String profileImageUrl = fileService.saveProfileImage(profile);
		UploadedFile uploadPortfolio = fileService.savePortfolio(portfolio);
		StudentInfo studentInfo = saveStudentInfo(studentId, requestDto, profileImageUrl, uploadPortfolio);

		studentInfoRepository.save(studentInfo);

	}

	/**
	 * 자기소개 조회 함수
	 * */
	@Transactional(readOnly = true)
	public StudentInfoGetResponseDto getStudentInfo(Long studentId) {

		boolean check = studentInfoRepository.existsByStudent_StudentId(studentId);

		//이후에 error 처리
		if (!check) {
			return null;
		}

		StudentInfoGetQueryDto studentInfo = studentInfoRepository.findStudentInfoByStudentId(studentId);

		List<String> techStackCodes = StringListConverter.stringToList(studentInfo.techStackString());
		List<SubCodeResponseDto> techStackResponse = subCodeRepository.findAllBySubCodeIn(techStackCodes).stream()
			.map(sc -> new SubCodeResponseDto(sc.getSubCode(), sc.getSubCodeName()))
			.collect(Collectors.toList());

		List<String> strength = StringListConverter.stringToList(studentInfo.strengthString());

		StudentResponseDto studentResponse = studentService.createStudentResponse(studentInfo.studentId(),
			studentInfo.name(),
			studentInfo.isMajor());

		SubCodeResponseDto positionCode = subCodeService.createSubCodeResponse(studentInfo.positionCode(),
			studentInfo.positionCodeName());
		SubCodeResponseDto trackCode = subCodeService.createSubCodeResponse(studentInfo.trackCode(),
			studentInfo.trackCodeName());
		SubCodeResponseDto goalCode = subCodeService.createSubCodeResponse(studentInfo.goalCode(),
			studentInfo.goalCodeName());
		SubCodeResponseDto mbtiCode = subCodeService.createSubCodeResponse(studentInfo.mbtiCode(),
			studentInfo.mbtiCodeName());

		TeamSimpleResponseDto teamResponse = teamService.createTeamSimpleResponseDto(studentInfo.teamId(),
			studentInfo.teamName(), studentInfo.teamTrackCodeName(), studentInfo.majorCount(),
			studentInfo.nonMajorCount());

		return StudentInfoGetResponseDto.builder()
			.student(studentResponse)
			.position(positionCode)
			.track(trackCode)
			.goal(goalCode)
			.mbti(mbtiCode)
			.techStack(techStackResponse)
			.strength(strength)
			.description(studentInfo.description())
			.profileImageUrl(studentInfo.profileImageUrl())
			.portfolio(studentInfo.portfolio())
			.teamInfo(teamResponse)
			.build();
	}

	/**
	 * 자기소개 수정 함수
	 * */
	@Transactional
	public void updateStudentInfo(Long studentId, StudentInfoUpdateRequestDto requestDto, MultipartFile profile,
		MultipartFile portfolio) throws IOException {

		StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(studentId)
			.orElseThrow(() -> new EntityNotFoundException("해당 학생의 자기소개를 찾을 수 없습니다."));

		//새로운 프로필 이미지 입력 시 변경
		if (profile != null && !profile.isEmpty()) {
			fileService.deleteProfileImage(studentInfo.getProfileImageUrl());
			String newProfileImage = fileService.saveProfileImage(profile);
			studentInfo.updateProfileImage(newProfileImage);

		}
		//새로운 포트폴리오 입력 시 변경
		if (portfolio != null && !portfolio.isEmpty()) {
			fileService.deletePortfolioFile(studentInfo.getPortfolio());
			UploadedFile newPortfolio = fileService.savePortfolio(portfolio);
			studentInfo.updatePortfolio(newPortfolio);
		}

		List<String> requiredCodes = List.of(requestDto.track(), requestDto.position(), requestDto.goal(),
			requestDto.mbti());
		List<SubCode> subCodes = subCodeRepository.findAllBySubCodeIn(requiredCodes);
		studentInfo.update(requestDto, subCodes);

		studentInfoRepository.save(studentInfo);
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

	private StudentInfo saveStudentInfo(Long studentId, StudentInfoCreateRequestDto requestDto, String profileImageUrl,
		UploadedFile portfolio) {

		Students students = studentService.findByStudentId(studentId);

		String techStackString = StringListConverter.listToString(requestDto.techStack());
		String strengthString = StringListConverter.listToString(requestDto.strength());

		SubCode trackCode = getSubCodeByValue(requestDto.track());
		SubCode positionCode = getSubCodeByValue(requestDto.position());
		SubCode goalCode = getSubCodeByValue(requestDto.goal());
		SubCode mbtiCode = getSubCodeByValue(requestDto.mbti());

		return StudentInfo.builder()
			.student(students)
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

}
