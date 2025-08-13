package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.EventTargetType;
import com.sonfind.chelsea.types.NotificationStatus;
import lombok.Builder;

@Builder
public record NotificationDto<T>(
		String id,
		String event,
		EventTargetType type,
		NotificationStatus status,
		String time,
		T data
) {
}
