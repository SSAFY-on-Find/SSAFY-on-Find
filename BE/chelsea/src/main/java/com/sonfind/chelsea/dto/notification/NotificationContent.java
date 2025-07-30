package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.Builder;

@Builder
public record BaseNotificationData(
	Long id,
	String name,
	NotificationDomainType type,
	String track,
	String notificationTitle,
	String notificationMessage
) {
}
