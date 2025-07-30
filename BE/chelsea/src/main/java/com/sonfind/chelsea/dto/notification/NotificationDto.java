package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationType;
import lombok.Builder;

@Builder
public record NotificationDto<T>(
        long id,
        String event,
        NotificationType type,
        String time,
        T data
) {
}
