package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.Builder;

@Builder
public record NotificationMsgDto(
	Long id,
	String name,
	NotificationDomainType type,
	String track,
	String notificationTitle,
	String notificationMessage,
	Long targetId
) implements CommonField {
}
