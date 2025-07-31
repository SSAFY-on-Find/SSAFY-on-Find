package com.sonfind.chelsea.dto.student;

import lombok.Builder;

@Builder
public record StudentUnionForNotificationResponseDto(
	Long studentId,
	String name,
	String position,
	String track,
	String profileImageUrl,
	String isMajor
) {
}
