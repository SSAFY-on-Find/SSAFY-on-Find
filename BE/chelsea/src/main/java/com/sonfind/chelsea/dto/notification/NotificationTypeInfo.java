package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationType;

import lombok.Builder;

@Builder
public record NotificationTypeInfo(
	NotificationType type,
	NotificationDomainType pubType,
	NotificationDomainType subType
) {
}
