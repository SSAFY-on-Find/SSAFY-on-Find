package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationStatus;
import lombok.Builder;

@Builder
public record NotificationAndNotificationStatusForMyTeamResponseDto(
		String notificationId,
		NotificationStatus status,
		String profileImageUrl,
		Long id,
		String type,
		String name,
		String isMajor,
		String position,
		Integer majorCount,
		Integer nonMajorCount
) {
}
