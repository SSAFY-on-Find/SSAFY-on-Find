package com.sonfind.chelsea.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record NotificationIdentity(
	Long id,
	String name,
	NotificationDomainType type,
	String track
) {
}
