package com.sonfind.chelsea.global.error;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiErrorResponse(
	String status,
	ErrorData data
) {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record ErrorData(
		String message,
		String path,
		Map<String, String> errors
	) {
	}

	// 기본 에러 응답 생성
	public static ApiErrorResponse of(String message, String path) {
		return new ApiErrorResponse(
			"ERROR",
			new ErrorData(
				message,
				path,
				null // errors는 null
			)
		);
	}

	// Validation 에러 응답 생성 (필드 에러 포함)
	public static ApiErrorResponse ofValidation(String message, String path, Map<String, String> fieldErrors) {
		return new ApiErrorResponse(
			"ERROR",
			new ErrorData(
				message,
				path,
				fieldErrors // validation errors 포함
			)
		);
	}
}
