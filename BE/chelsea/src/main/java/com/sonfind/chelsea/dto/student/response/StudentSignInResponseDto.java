package com.sonfind.chelsea.dto.student.response;

import lombok.Builder;

@Builder
public record StudentSignInResponseDto(
	Long studentId,
	String name,
	String major,
	String className,
	Boolean isCreatedStudentInfo
) {
}
