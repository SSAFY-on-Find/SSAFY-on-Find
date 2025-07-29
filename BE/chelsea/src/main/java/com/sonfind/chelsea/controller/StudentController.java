package com.sonfind.chelsea.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.service.StudentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

	private static StudentService studentService;

	@PostMapping("/sing-in")
	public ResponseEntity<Object> singup(@RequestBody Long studentId, HttpSession session) {

		Students student = studentService.findByStudentId(studentId);
		if (student != null) {
			session.setAttribute("loginUser", studentId);

			return ResponseEntity.ok()
				.header("Set-Cookie", "sessionId=" + studentId)
				.build();
		}

		//학번 조회가 안되는 경우 예외 처리
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
