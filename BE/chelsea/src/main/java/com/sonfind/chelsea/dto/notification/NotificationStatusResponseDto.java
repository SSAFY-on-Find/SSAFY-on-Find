package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.RecipientRole;
import lombok.Builder;
import org.bson.types.ObjectId;

@Builder
public record NotificationStatusResponseDto(
        ObjectId statusId,
        ObjectId notificationId,
        Long targetId,
        NotificationDomainType targetType,
        RecipientRole role,
        NotificationStatus status,
        Boolean isRead,
        String notificationTitle,
        String notificationMessage,
        String updatedAt,

        Long publisherId,
        NotificationDomainType publisherType,
        Long subscriberId,
        NotificationDomainType subscriberType
) {
}
