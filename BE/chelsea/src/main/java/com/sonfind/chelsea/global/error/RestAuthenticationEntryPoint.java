package com.sonfind.chelsea.global.error;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws
		IOException {
		ApiErrorResponse errorResponse = ApiErrorResponse.of("인증이 필요합니다.", req.getRequestURI());

		res.setStatus(401);
		res.setContentType("application/json; charset=UTF-8");
		res.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}