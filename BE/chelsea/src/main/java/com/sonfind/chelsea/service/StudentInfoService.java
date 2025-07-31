package com.sonfind.chelsea.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoCreateRequestDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponse;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.repository.SubCodeRepository;
import com.sonfind.chelsea.repository.studentInfo.StudentInfoRepository;
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

		String profileImageUrl = fileService.saveProfileImage(profile);
		UploadedFile uploadPortfolio = fileService.savePortfolio(portfolio);
		StudentInfo studentInfo = saveStudentInfo(studentId, requestDto, profileImageUrl, uploadPortfolio);

		studentInfoRepository.save(studentInfo);

	}

	public StudentInfoResponseDto getStudentInfo(Long studentId) {

		StudentInfo studentInfo = studentInfoRepository.findByStudent_StudentId(studentId)
			.orElse(null);
		StudentInfoResponseDto responseDto = studentInfoRepository.findStudentInfoResponseDtoById(studentId)
			.orElse(null);

		List<String> techStackCodes = StringListConverter.stringToList(studentInfo.getTechStack());

		List<SubCodeResponse> techStackResponse = subCodeRepository.findAllBySubCodeIn(techStackCodes).stream()
			.map(sc -> new SubCodeResponse(sc.getSubCode(), sc.getSubCodeName()))
			.collect(Collectors.toList());

		List<String> strength = StringListConverter.stringToList(studentInfo.getStrength());

		return StudentInfoResponseDto.builder()
			.student(responseDto.student())
			.position(responseDto.position())
			.track(responseDto.track())
			.goal(responseDto.goal())
			.mbti(responseDto.mbti())
			.techStack(techStackResponse)
			.strength(strength)
			.description(studentInfo.getDescription())
			.profileImageUrl(studentInfo.getProfileImageUrl())
			.portfolio(studentInfo.getPortfolio())
			.teamInfo(responseDto.teamInfo())
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

	private SubCode getSubCodeByValue(String subCode) {
		
		if (subCode == null || subCode.isBlank()) {
			return null;
		}

		return subCodeRepository.findBySubCode(subCode);
	}

}
