package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;
import lombok.Builder;

@Builder
public record MergeTargetDto(
        Long id,
        String name,
        NotificationDomainType type,
        String track,
        int memberCount,
        int major,
        int nonMajor
) {
}
