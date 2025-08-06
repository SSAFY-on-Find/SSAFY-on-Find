package com.sonfind.chelsea.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.sonfind.chelsea.dto.studentInfo.StudentInfoCreateRequestDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoGetResponseDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoUpdateRequestDto;
import com.sonfind.chelsea.global.commonSwagger.ApiCreateOperation;
import com.sonfind.chelsea.global.commonSwagger.ApiGetOperation;
import com.sonfind.chelsea.service.StudentInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
@Tag(name = "me", description = "사용자 자기소개 관련 api")
public class MeController {

	private final StudentInfoService studentInfoService;

	@ApiCreateOperation(summary = "본인 자기소개 등록")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> createStudentInfo(
		@Parameter(content = @Content(mediaType = "application/json"))
		@Valid @RequestPart StudentInfoCreateRequestDto requestDto,

		@RequestPart(value = "profile", required = false) MultipartFile profile,
		@RequestPart(value = "portfolio", required = false) MultipartFile portfolio,
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId
	) throws IOException {

		studentInfoService.createStudentInfo(studentId, requestDto, profile, portfolio);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");

		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@Operation(summary = "본인 자기소개 조회", description = "본인 id에 해당하는 자기소개 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = StudentInfoGetResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "작성한 자기소개서가 없음",
			content = @Content)
	})
	@GetMapping
	public ResponseEntity<Map<String, Object>> getStudentInfo(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId
	) {

		StudentInfoGetResponseDto studentInfo = studentInfoService.getStudentInfo(studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", studentInfo);

		return ResponseEntity.ok().body(body);
	}

	@ApiGetOperation(summary = "본인 자기소개 수정")
	@PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> updateStudentInfo(
		@Parameter(content = @Content(mediaType = "application/json"), description = "자기소개 등록 정보 (JSON 형식)")
		@Valid @RequestPart StudentInfoUpdateRequestDto requestDto,

		@RequestPart(value = "profile", required = false) MultipartFile profile,
		@RequestPart(value = "portfolio", required = false) MultipartFile portfolio,
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId
	) throws IOException {

		studentInfoService.updateStudentInfo(studentId, requestDto, profile, portfolio);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");

		return ResponseEntity.ok().body(body);
	}
}
