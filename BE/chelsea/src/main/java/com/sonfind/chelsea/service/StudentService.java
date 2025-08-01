package com.sonfind.chelsea.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.student.StudentResponseDto;
import com.sonfind.chelsea.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

	private final StudentRepository studentRepository;

	public Students findByStudentId(long studentId) {

		Optional<Students> authOptional = studentRepository.findByStudentId(studentId);
		Students student = null;

		if (authOptional.isPresent()) {
			student = authOptional.get();
		}

		return student;
	}

	public StudentResponseDto findByStudentIdForSse(long studentId) {
		Optional<Students> authOptional = studentRepository.findByStudentId(studentId);

		if (!authOptional.isPresent()) {
			throw new IllegalArgumentException("존재하지 않는 학생입니다.");
		}
		Students student = authOptional.get();

		return StudentResponseDto.builder()
			.studentId(student.getStudentId())
			.name(student.getName())
			.major(getIsMajor(student))
			.build();
	}

	private static String getIsMajor(Students student) {
		return student.getMajorYn() ? "전공" : "비전공";
	}

	public List<Students> findAllByTeamId(Long teamId) {
		return studentRepository.findAllByTeamId(teamId);
	}
}
