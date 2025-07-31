package com.sonfind.chelsea.dto.student;

import lombok.Builder;

@Builder
public record StudentResponse(
	Long studentId,
	String name,
	String major
) {
}
