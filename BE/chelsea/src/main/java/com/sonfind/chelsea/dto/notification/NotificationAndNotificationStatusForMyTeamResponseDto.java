package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

@Builder
public record NotificationAndNotificationStatusForMyTeamResponseDto(
		String notificationId,
		String profileImageUrl,
		String name,
		String isMajor,
		String track,
		Integer majorCount,
		Integer nonMajorCount
) {
}
