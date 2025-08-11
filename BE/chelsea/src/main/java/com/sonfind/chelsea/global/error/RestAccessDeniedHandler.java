package com.sonfind.chelsea.global.error;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex) throws IOException {
		ApiErrorResponse errorResponse = ApiErrorResponse.of("접근 권한이 없습니다.", req.getRequestURI());

		res.setStatus(403);
		res.setContentType("application/json; charset=UTF-8");
		res.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}