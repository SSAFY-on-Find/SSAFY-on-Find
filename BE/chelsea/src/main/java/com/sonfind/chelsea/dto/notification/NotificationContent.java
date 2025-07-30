package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

@Builder
public record NotificationContent(
	String notificationTitle,
	String notificationMessage
) {
}
