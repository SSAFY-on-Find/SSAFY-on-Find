package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.dto.student.request.StudentSignInRequestDto;
import com.sonfind.chelsea.dto.student.response.StudentListResponseDto;
import com.sonfind.chelsea.dto.student.response.StudentSignInResponseDto;
import com.sonfind.chelsea.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
@Tag(name = "students", description = "교육생과 관련된 API")
public class StudentController {

	private final StudentService studentService;

	@Operation(summary = "교육생 로그인", description = "교육생 학번 입력 시 로그인 성공")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공",
			content = @Content(schema = @Schema(implementation = Students.class))),
		@ApiResponse(responseCode = "404", description = "학번 조회 실패",
			content = @Content(
				schema = @Schema(
					type = "object",
					example = "{\"status\": \"FAIL\", \"message\": \"학번 조회 실패\"}"
				)))
	})
	@PostMapping("/sign-in")
	public ResponseEntity<Map<String, Object>> signup(@RequestBody @Valid StudentSignInRequestDto request,
		HttpServletRequest httpServletRequest) {

		StudentSignInResponseDto student = studentService.signIn(request);

		HttpSession oldSession = httpServletRequest.getSession(false);
		if (oldSession != null) {
			oldSession.invalidate();
		}
		HttpSession newSession = httpServletRequest.getSession(true);
		newSession.setAttribute("loginUser", student.studentId());
		Map<String, Object> body = new HashMap<>();

		body.put("status", "SUCCESS");
		body.put("data", student);

		return ResponseEntity.ok().body(body);
	}

	@Operation(summary = "교육생 목록 조회", description = "전체 교육생 목록 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = StudentListResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "작성한 자기소개서가 없음",
			content = @Content(
				schema = @Schema(
					type = "object",
					example = "{\"status\": \"FAIL\", \"message\": \"작성한 자기소개서가 없습니다.\"}"
				)))
	})
	@GetMapping
	public ResponseEntity<Map<String, Object>> getStudentList() {

		List<StudentListResponseDto> data = studentService.getStudentList();

		Map<String, Object> body = new HashMap<String, Object>();

		body.put("status", "SUCCESS");
		body.put("data", data);

		return ResponseEntity.ok().body(body);
	}
}
