package com.sonfind.chelsea.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.student.StudentForNotificationResponseDto;
import com.sonfind.chelsea.dto.student.StudentListQueryDto;
import com.sonfind.chelsea.dto.student.StudentListResponseDto;
import com.sonfind.chelsea.dto.student.StudentResponse;
import com.sonfind.chelsea.dto.subcode.SubCodeResponse;
import com.sonfind.chelsea.repository.StudentRepository;
import com.sonfind.chelsea.repository.studentInfo.StudentInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

	private final StudentRepository studentRepository;
	private final StudentInfoRepository studentInfoRepository;

	public Students findByStudentId(long studentId) {

		Optional<Students> authOptional = studentRepository.findByStudentId(studentId);
		Students student = null;

		if (authOptional.isPresent()) {
			student = authOptional.get();
		}

		return student;
	}

	public StudentForNotificationResponseDto findByStudentIdForSse(long studentId) {
		Optional<Students> authOptional = studentRepository.findByStudentId(studentId);

		if (!authOptional.isPresent()) {
			throw new IllegalArgumentException("존재하지 않는 학생입니다.");
		}
		Students student = authOptional.get();

		return StudentForNotificationResponseDto.builder()
			.studentId(student.getStudentId())
			.name(student.getName())
			.isMajor(getIsMajor(student))
			.build();
	}

	//교육생 목록 조회
	@Transactional(readOnly = true)
	public List<StudentListResponseDto> getStudentList(Long studentId) {

		List<StudentListQueryDto> queryResult = studentRepository.findStudentList();

		return queryResult.stream().map(dto -> new StudentListResponseDto(
			new StudentResponse(dto.studentId(), dto.name(), dto.major() ? "전공" : "비전공"),
			new SubCodeResponse(dto.positionCode(), dto.positionCodeName()),
			new SubCodeResponse(dto.trackCode(), dto.trackCodeName()),
			new SubCodeResponse(dto.goalCode(), dto.goalCodeName()),
			dto.profileImageUrl(),
			false,
			dto.teamName()
		)).collect(Collectors.toList());
	}

	private static String getIsMajor(Students student) {
		return student.getMajorYn() ? "전공" : "비전공";
	}

	public List<Students> findAllByTeamId(Long teamId) {
		return studentRepository.findAllByTeamId(teamId);
	}
}
