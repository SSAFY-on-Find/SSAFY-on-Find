package com.sonfind.chelsea.dto.notification;

import org.bson.types.ObjectId;

import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.RecipientRole;

import lombok.Builder;

@Builder
public record NotificationStatusResponseDto(
	ObjectId statusId,
	ObjectId notificationId,
	Long targetId,
	NotificationDomainType targetType,
	RecipientRole role,
	NotificationStatus status,
	Boolean isRead,
	String updatedAt,

	Long publisherId,
	NotificationDomainType publisherType,
	String pubNotificationTitle,
	String pubNotificationMessage,
	Long subscriberId,
	NotificationDomainType subscriberType,
	String subNotificationTitle,
	String subNotificationMessage

) {
}
