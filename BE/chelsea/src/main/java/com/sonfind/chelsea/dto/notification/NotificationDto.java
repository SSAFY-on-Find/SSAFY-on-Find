package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationType;
import lombok.Builder;
import org.bson.types.ObjectId;

@Builder
public record NotificationDto<T>(
        ObjectId id,
        String event,
        NotificationType type,
        String time,
        T data
) {
}
