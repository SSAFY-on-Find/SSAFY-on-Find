package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationType;

import lombok.Builder;

@Builder
public record NotificationResponseDto(
	NotificationType type,
	long publisherId,
	NotificationDomainType publisherType,
	String pubNotificationTitle,
	String pubNotificationMessage,
	long subscriberId,
	NotificationDomainType subscriberType,
	String subNotificationTitle,
	String subNotificationMessage
) {
}
