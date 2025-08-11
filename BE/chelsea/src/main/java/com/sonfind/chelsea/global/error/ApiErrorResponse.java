package com.sonfind.chelsea.global.error;

import java.util.List;
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
		Map<String, String> errors,
		List<String> messages
	) {
	}

	// 기본 에러 응답 생성
	public static ApiErrorResponse of(String message, String path) {
		return new ApiErrorResponse(
			"ERROR",
			new ErrorData(
				message,
				path,
				null,
				null
			)
		);
	}

	// 여러 에러 메시지를 담는 응답 생성
	public static ApiErrorResponse ofMultiple(List<String> messages, String path) {
		String mainMessage = messages.isEmpty()
			? "오류가 발생했습니다."
			: messages.get(0);

		return new ApiErrorResponse(
			"ERROR",
			new ErrorData(
				mainMessage,
				path,
				null,
				messages.size() > 1 ? messages : null  // 2개 이상일 때만 배열 포함
			)
		);
	}

	// Validation 에러 응답 생성 (필드 에러 + 전체 메시지 배열)
	public static ApiErrorResponse ofValidation(String message, String path, Map<String, String> fieldErrors) {
		List<String> allMessages = fieldErrors.isEmpty()
			? List.of(message)
			: fieldErrors.values().stream().distinct().toList();

		return new ApiErrorResponse(
			"ERROR",
			new ErrorData(
				message,
				path,
				fieldErrors,
				allMessages.size() > 1 ? allMessages : null  // 2개 이상일 때만 배열 포함
			)
		);
	}

	// 필드 에러 개수에 따른 동적 메시지 생성
	public static ApiErrorResponse ofValidationSmart(String path, Map<String, String> fieldErrors) {
		if (fieldErrors.isEmpty()) {
			return of("요청 값이 올바르지 않습니다.", path);
		}

		List<String> allMessages = fieldErrors.values().stream().distinct().toList();
		String mainMessage = allMessages.size() == 1
			? allMessages.get(0)
			: allMessages.size() + "개의 입력 오류가 있습니다.";

		return new ApiErrorResponse(
			"ERROR",
			new ErrorData(
				mainMessage,
				path,
				fieldErrors,
				allMessages.size() > 1 ? allMessages : null
			)
		);
	}
}
