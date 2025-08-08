package com.sonfind.chelsea.global.event;

import com.sonfind.chelsea.types.DashboardEventType;
import lombok.Builder;

@Builder
public record DashboardMessage<T>(
		DashboardEventType eventType,
		T data
) {
}
