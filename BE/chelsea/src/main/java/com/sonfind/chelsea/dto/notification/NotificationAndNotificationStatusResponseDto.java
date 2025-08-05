package com.sonfind.chelsea.dto.notification;

import java.util.List;

import lombok.Builder;

@Builder
public record NotificationAndNotificationStatusResponseDto(
	List<NotificationStatusResponseDto> notificationStatusList,
	int unReadCount
) {
}
