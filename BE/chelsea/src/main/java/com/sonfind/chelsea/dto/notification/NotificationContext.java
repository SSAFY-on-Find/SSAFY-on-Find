package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

@Builder
public record NotificationContext(
		ContextInfo publisher,
		ContextInfo subscriber
) {
}
