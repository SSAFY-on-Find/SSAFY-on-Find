package com.sonfind.chelsea.global.error;

import java.io.IOException;

import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws
		IOException {
		ProblemDetail p = ProblemDetail.forStatus(ErrorCode.AUTH_REQUIRED.status);
		p.setTitle(ErrorCode.AUTH_REQUIRED.name()); // .code → .name()
		p.setDetail(ErrorCode.AUTH_REQUIRED.message);
		p.setProperty("path", req.getRequestURI());
		res.setStatus(ErrorCode.AUTH_REQUIRED.status.value());
		res.setContentType("application/problem+json");
		res.getWriter().write("""
			{"type":"about:blank", "title":"AUTH_REQUIRED", "status":401, "detail":"인증이 필요합니다."}
			""");
	}
}