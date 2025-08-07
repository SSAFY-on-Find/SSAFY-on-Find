package com.sonfind.chelsea.dto.studentInfo.response;

import lombok.Builder;

@Builder
public record StudentInfoForNotificationResponseDto(
	Long studentId,
	String position,
	String track,
	String profileImageUrl
) {
}
