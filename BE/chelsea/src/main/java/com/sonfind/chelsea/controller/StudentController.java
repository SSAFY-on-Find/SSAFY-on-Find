package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.student.StudentListResponseDto;
import com.sonfind.chelsea.global.commonSwagger.ApiGetOperation;
import com.sonfind.chelsea.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
@Tag(name = "students", description = "교육생과 관련된 API")
public class StudentController {

	private final StudentService studentService;

	@ApiGetOperation(summary = "교육생 로그인 ", description = "교육생 학번 입력 시 로그인 성공/반환 값을 학생 정보를 주는 걸로 이후에 바꿀 예정")
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

	@Operation(summary = "교육생 목록 조회", description = "전체 교육생 목록 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = StudentListResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "작성한 자기소개서가 없음",
			content = @Content)
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
