package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.dto.student.StudentUnionForNotificationResponseDto;

import lombok.Builder;

@Builder
public record NotificationContext(
	StudentUnionForNotificationResponseDto publisher,
	StudentUnionForNotificationResponseDto subscriber
) {
}
