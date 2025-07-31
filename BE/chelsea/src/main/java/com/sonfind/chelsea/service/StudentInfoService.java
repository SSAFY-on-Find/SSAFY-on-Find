package com.sonfind.chelsea.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoCreateRequestDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoForNotificationResponseDto;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.SubCodeRepository;
import com.sonfind.chelsea.util.StringListConverter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentInfoService {

	private final SubCodeRepository subCodeRepository;
	private final StudentInfoRepository studentInfoRepository;
	private final StudentService studentService;
	private final FileService fileService;

	public void createStudentInfo(Long studentId, StudentInfoCreateRequestDto requestDto, MultipartFile profile,
		MultipartFile portfolio) throws IOException {

		String profileImageUrl = saveProfileImage(profile);
		UploadedFile uploadPortfolio = savePortfolio(portfolio);
		StudentInfo studentInfo = saveStudentInfo(studentId, requestDto, profileImageUrl, uploadPortfolio);
		studentInfoRepository.save(studentInfo);
	}

	/**
	 * 학생 ID로 학생 정보를 조회하는 메소드(SSE용)
	 * @param studentId
	 * @return StudentInfoForNotificationResponseDto(학생 ID, 포지션, 트랙, 프로필 이미지 URL)
	 * @throws IllegalArgumentException 해당 학생의 정보가 없을 경우
	 */
	public StudentInfoForNotificationResponseDto findByStudentId(Long studentId) {
		StudentInfo findStuInfo = studentInfoRepository.findByStudentId(studentId);

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

		Students student = studentService.findByStudentId(studentId);

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
			.profileImageUrl(profileImageUrl)
			.portfolio(portfolio)
			.build();
	}

	private String saveProfileImage(MultipartFile profile) throws IOException {

		String imageSaveUrl = "";

		if (profile == null || profile.isEmpty()) {
			//없으면 기본 이미지 처리
		} else {
			imageSaveUrl = fileService.uploadFile(profile, "profiles");
		}
		return imageSaveUrl;
	}

	private UploadedFile savePortfolio(MultipartFile portfolio) throws IOException {

		if (portfolio == null || portfolio.isEmpty()) {
			return null;
		}

		String portfolioOriImageName = portfolio.getOriginalFilename();
		String savedFilename = fileService.uploadFile(portfolio, "portfolios");

		return UploadedFile.builder()
			.originalFileName(portfolioOriImageName)
			.savedFileName(savedFilename)
			.build();
	}

	private SubCode getSubCodeByValue(String subCode) {
		if (subCode == null || subCode.isBlank()) {
			return null;
		}

		return subCodeRepository.findBySubCode(subCode);
	}
}
