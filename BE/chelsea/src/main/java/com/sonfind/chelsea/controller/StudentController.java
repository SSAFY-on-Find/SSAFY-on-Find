package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.student.StudentListResponseDto;
import com.sonfind.chelsea.service.StudentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

	private final StudentService studentService;

	@PostMapping("/sign-in")
	public ResponseEntity<Object> signup(@RequestBody Long studentId, HttpSession session) {

		Students student = studentService.findByStudentId(studentId);
		if (student != null) {
			session.setAttribute("loginUser", studentId);

			return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE,
					"sessionId=" + studentId + "; Path=/; HttpOnly")
				.build();
		}

		//학번 조회가 안되는 경우 예외 처리
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getStudentList(@CookieValue("sessionId") Long studentId) {

		List<StudentListResponseDto> data = studentService.getStudentList(studentId);

		Map<String, Object> body = new HashMap<String, Object>();

		body.put("status", "SUCCESS");
		body.put("data", data);

		return ResponseEntity.ok().body(body);
	}
}
