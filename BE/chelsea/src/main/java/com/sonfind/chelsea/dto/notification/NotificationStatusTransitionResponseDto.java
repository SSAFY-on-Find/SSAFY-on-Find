package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import lombok.Builder;

import java.util.Date;

@Builder
public record NotificationStatusTransitionResponseDto(
		NotificationDocument notification,
		Date now
) {
}
