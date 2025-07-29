package com.sonfind.chelsea.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.student.Students;
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

}
