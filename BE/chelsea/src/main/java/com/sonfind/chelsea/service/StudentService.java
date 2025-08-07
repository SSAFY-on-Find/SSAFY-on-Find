package com.sonfind.chelsea.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.dto.student.request.StudentSignInRequestDto;
import com.sonfind.chelsea.dto.student.response.StudentListQueryDto;
import com.sonfind.chelsea.dto.student.response.StudentListResponseDto;
import com.sonfind.chelsea.dto.student.response.StudentResponseDto;
import com.sonfind.chelsea.dto.student.response.StudentSignInResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

	private final StudentRepository studentRepository;
	private final StudentInfoRepository studentInfoRepository;

	/**
	 * 로그인 처리 service
	 * */
	public StudentSignInResponseDto signIn(StudentSignInRequestDto request) {

		Long studentId = Long.parseLong(request.studentId());

		Student student = studentRepository.findByStudentId(studentId)
			.orElseThrow(EntityNotFoundException::new);

		String className = student.getClassCode().getSubCodeName();

		boolean isCreatedStudentInfo = studentInfoRepository.existsByStudent_StudentId(studentId);

		return StudentSignInResponseDto.builder()
			.studentId(student.getStudentId())
			.name(student.getName())
			.major(getIsMajor(student.getMajorYn()))
			.className(className)
			.teamId(student.getTeamId())
			.isCreatedStudentInfo(isCreatedStudentInfo)
			.build();
	}

	/**
	 * 전체 교육생 목록 조회
	 * */
	@Transactional(readOnly = true)
	public List<StudentListResponseDto> getStudentList(Long studentId) {

		List<StudentListQueryDto> queryResult = studentRepository.findStudentList(studentId);

		return queryResult.stream().map(dto -> new StudentListResponseDto(
			new StudentResponseDto(dto.studentId(), dto.name(), dto.major() ? "전공" : "비전공"),
			new SubCodeResponseDto(dto.positionCode(), dto.positionCodeName()),
			new SubCodeResponseDto(dto.trackCode(), dto.trackCodeName()),
			new SubCodeResponseDto(dto.goalCode(), dto.goalCodeName()),
			dto.profileImageUrl(),
			dto.isFavorite() != null,
			dto.teamName()
		)).collect(Collectors.toList());
	}

	public Student findByStudentId(long studentId) {

		Optional<Student> authOptional = studentRepository.findByStudentId(studentId);
		Student student = null;

		if (authOptional.isPresent()) {
			student = authOptional.get();
		}

		return student;
	}

	public StudentResponseDto findByStudentIdForSse(long studentId) {
		Optional<Student> authOptional = studentRepository.findByStudentId(studentId);

		if (!authOptional.isPresent()) {
			throw new IllegalArgumentException("존재하지 않는 학생입니다.");
		}
		Student student = authOptional.get();

		return StudentResponseDto.builder()
			.studentId(student.getStudentId())
			.name(student.getName())
			.major(getIsMajor(student.getMajorYn()))
			.build();
	}

	public StudentResponseDto createStudentResponse(Long studentId, String name, Boolean isMajor) {
		return new StudentResponseDto(
			studentId, name, getIsMajor(isMajor)
		);
	}

	private static String getIsMajor(Boolean major) {
		return major ? "전공" : "비전공";
	}

	public List<Student> findAllByTeamId(Long teamId) {
		return studentRepository.findAllByTeamId(teamId);
	}
}
