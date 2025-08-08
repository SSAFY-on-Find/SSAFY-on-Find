package com.sonfind.chelsea.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM_NOT_FOUND", "팀을 찾을 수 없습니다."),
	STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDENT_NOT_FOUND", "교육생을 찾을 수 없습니다."),
	VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "요청 값이 올바르지 않습니다."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다."),
	AUTH_REQUIRED(HttpStatus.FORBIDDEN, "AUTH_REQUIRED", "인증이 필요합니다."),
	CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "충돌이 발생했습니다."),
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.");

	public final HttpStatus status;
	public final String code;
	public final String message;

	ErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
