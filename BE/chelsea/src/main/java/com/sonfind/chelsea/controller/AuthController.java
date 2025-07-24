package com.sonfind.chelsea.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.domain.auth.Auth;
import com.sonfind.chelsea.service.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private static AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/sing-in")
	public ResponseEntity<Object> singup(@RequestBody Long mateId, HttpSession session) {

		Auth auth = authService.findByMateId(mateId);
		if (auth != null) {
			session.setAttribute("loginUser", mateId);

			return ResponseEntity.ok()
				.header("Set-Cookie", "sessionId=" + mateId)
				.body(mateId);
		}

		//학번 조회가 안되는 경우 예외 처리
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
	}
}
