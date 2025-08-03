package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationResponseDto(
        List<NotificationStatusResponseDto> notificationStatusList,
        int unReadCount
) {
}
