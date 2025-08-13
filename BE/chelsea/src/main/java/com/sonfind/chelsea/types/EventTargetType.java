package com.sonfind.chelsea.types;

import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventTargetType {
	DASHBOARD("dashboard"),
	NOTIFICATION("notification");

	private final String type;

	public static EventTargetType from(String type) {
		for (EventTargetType eventTargetType : EventTargetType.values()) {
			if (eventTargetType.type.equalsIgnoreCase(type)) {
				return eventTargetType;
			}
		}
		throw new AppException(ErrorCode.NOTIFICATION_TYPE_NOT_SUPPORTED);
	}
}
