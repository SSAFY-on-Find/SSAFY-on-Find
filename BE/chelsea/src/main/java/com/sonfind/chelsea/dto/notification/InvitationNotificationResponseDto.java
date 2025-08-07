package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.Builder;

@Builder
public record InvitationNotificationResponseDto(
	Long pubId,
	NotificationDomainType pubType,
	Long subId,
	NotificationDomainType subType,
	String updatedAt
) implements HasResponse {
}
