package com.sonfind.chelsea.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

	private final StudentRepository studentRepository;

	public Student findByStudentId(long studentId) {

		Optional<Student> authOptional = studentRepository.findByStudentId(studentId);
		Student student = null;

		if (authOptional.isPresent()) {
			student = authOptional.get();
		}

		return student;
	}

}
