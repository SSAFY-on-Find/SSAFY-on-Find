package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.Builder;

@Builder
public record ApplicantDto(
	Long id,
	String name,
	NotificationDomainType type,
	String track,
	String isMajor,
	String position
) {
}
