package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.dto.studentInfo.StudentInfoGetResponseDto;
import com.sonfind.chelsea.service.StudentInfoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/studentInfos")
public class StudentInfoController {

	private final StudentInfoService studentInfoService;

	@GetMapping("/{studentId}")
	public ResponseEntity<Map<String, Object>> getStudentInfo(
		@PathVariable Long studentId) {

		StudentInfoGetResponseDto studentInfo = studentInfoService.getStudentInfo(studentId);
		
		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", studentInfo);

		return ResponseEntity.ok().body(body);
	}

}
