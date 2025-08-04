package com.sonfind.chelsea.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sonfind.chelsea.dto.studentInfo.StudentInfoCreateRequestDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoResponseDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoUpdateRequestDto;
import com.sonfind.chelsea.service.StudentInfoService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class MeController {

	private final StudentInfoService studentInfoService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> createStudentInfo(
		@Parameter(content = @Content(mediaType = "application/json"))
		@Valid @RequestPart StudentInfoCreateRequestDto requestDto,

		@RequestPart(value = "profile", required = false) MultipartFile profile,
		@RequestPart(value = "portfolio", required = false) MultipartFile portfolio,
		@CookieValue("sessionId") Long studentId
	) throws IOException {

		studentInfoService.createStudentInfo(studentId, requestDto, profile, portfolio);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");

		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getStudentInfo(
		@CookieValue("sessionId") Long studentId
	) {

		StudentInfoResponseDto studentInfo = studentInfoService.getStudentInfo(studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", studentInfo);

		return ResponseEntity.ok().body(body);
	}

	@PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> updateStudentInfo(
		@Parameter(content = @Content(mediaType = "application/json"))
		@Valid @RequestPart StudentInfoUpdateRequestDto requestDto,

		@RequestPart(value = "profile", required = false) MultipartFile profile,
		@RequestPart(value = "portfolio", required = false) MultipartFile portfolio,
		@CookieValue("sessionId") Long studentId
	) throws IOException {

		studentInfoService.updateStudentInfo(studentId, requestDto, profile, portfolio);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");

		return ResponseEntity.ok().body(body);
	}
}
