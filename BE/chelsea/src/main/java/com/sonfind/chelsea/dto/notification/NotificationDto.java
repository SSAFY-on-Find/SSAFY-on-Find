package com.sonfind.chelsea.dto.notification;

import org.bson.types.ObjectId;

import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.NotificationType;

import lombok.Builder;

@Builder
public record NotificationDto<T>(
	ObjectId id,
	String event,
	NotificationType type,
	NotificationStatus status,
	String time,
	T data
) {
}
