package com.sonfind.chelsea.dto.notification;

import java.util.List;

import lombok.Builder;

@Builder
public record NotificationAndNotificationStatusForMyNotifResponseDto(
	List<NotificationStatusResponseDto> notificationStatusList,
	int unReadCount
) {
}
