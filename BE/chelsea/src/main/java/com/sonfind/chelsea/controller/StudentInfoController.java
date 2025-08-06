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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/studentInfos")
public class StudentInfoController {

	private final StudentInfoService studentInfoService;

	@Operation(summary = "특정 교육생 자기소개 조회", description = "특정 교육생 studentId에 해당하는 자기소개 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = StudentInfoGetResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "작성한 자기소개서가 없음",
			content = @Content(
				schema = @Schema(
					type = "object",
					example = "{\"status\": \"FAIL\", \"message\": \"작성한 자기소개서가 없습니다.\"}"
				)))
	})
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
