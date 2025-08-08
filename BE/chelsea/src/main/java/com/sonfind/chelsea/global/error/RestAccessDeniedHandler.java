package com.sonfind.chelsea.global.error;

import java.io.IOException;

import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex) throws IOException {
		ProblemDetail p = ProblemDetail.forStatus(ErrorCode.ACCESS_DENIED.status);
		p.setTitle(ErrorCode.ACCESS_DENIED.name()); // .code → .name()
		p.setDetail(ErrorCode.ACCESS_DENIED.message);
		p.setProperty("path", req.getRequestURI());
		res.setStatus(ErrorCode.ACCESS_DENIED.status.value());
		res.setContentType("application/problem+json");
		res.getWriter().write("""
			{"type":"about:blank","title":"ACCESS_DENIED","status":403,"detail":"접근 권한이 없습니다."}
			""");
	}
}