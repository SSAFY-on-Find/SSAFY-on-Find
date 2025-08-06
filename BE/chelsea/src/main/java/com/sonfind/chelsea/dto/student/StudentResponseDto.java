package com.sonfind.chelsea.dto.student;

import lombok.Builder;

@Builder
public record StudentResponseDto(
	Long studentId,
	String name,
	String major
) {
}
