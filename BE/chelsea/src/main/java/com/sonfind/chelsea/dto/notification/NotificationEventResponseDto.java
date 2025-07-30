package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NotificationEventResponseDto<T> {
	private Long id;
	private String event;
	private NotificationType type;
	private String time;
	private T data;
}
