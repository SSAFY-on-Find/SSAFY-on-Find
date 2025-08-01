package com.sonfind.chelsea.types;

import com.sonfind.chelsea.dto.student.StudentUnionForNotificationResponseDto;

import lombok.Builder;

@Builder
public record NotificationContext(
	StudentUnionForNotificationResponseDto publisher,
	StudentUnionForNotificationResponseDto subscriber
) {
}
