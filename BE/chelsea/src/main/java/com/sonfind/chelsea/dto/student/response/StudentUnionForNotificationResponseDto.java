package com.sonfind.chelsea.dto.student.response;

import com.sonfind.chelsea.dto.notification.ContextInfo;
import lombok.Builder;

@Builder
public record StudentUnionForNotificationResponseDto(
		Long studentId,
		String name,
		String position,
		String track,
		String profileImageUrl,
		String isMajor
) implements ContextInfo {
}
