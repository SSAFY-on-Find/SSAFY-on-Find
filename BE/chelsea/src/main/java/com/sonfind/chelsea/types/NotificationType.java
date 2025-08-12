package com.sonfind.chelsea.types;

import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
	APPLICATION("application"), // 지원
	INVITATION("invitation"), // 초대
	MERGE("merge"); // 병합

	private final String type;

	public static NotificationType from(String value) {
		for (NotificationType notificationType : values()) {
			if (notificationType.type.equalsIgnoreCase(value)) {
				return notificationType;
			}
		}
		throw new AppException(ErrorCode.NOTIFICATION_TYPE_NOT_SUPPORTED);
	}
}
