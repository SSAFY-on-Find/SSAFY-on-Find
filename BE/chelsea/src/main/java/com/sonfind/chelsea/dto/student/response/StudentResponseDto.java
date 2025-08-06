package com.sonfind.chelsea.dto.student.response;

import lombok.Builder;

@Builder
public record StudentResponseDto(
	Long studentId,
	String name,
	String major
) {
}
