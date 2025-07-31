package com.sonfind.chelsea.dto.student;

import lombok.Builder;

@Builder
public record StudentForNotificationResponseDto(
	Long studentId,
	String name,
	String isMajor
) {
}
