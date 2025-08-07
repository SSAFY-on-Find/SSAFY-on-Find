package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.Builder;

@Builder
public record ParticipantDto(
	Long id,
	String name,
	NotificationDomainType type,
	String track
) implements CommonField {
}
