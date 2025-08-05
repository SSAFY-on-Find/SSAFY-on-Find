package com.sonfind.chelsea.dto.student.response;

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
