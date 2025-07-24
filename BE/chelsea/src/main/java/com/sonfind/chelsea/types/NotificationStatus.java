package com.sonfind.chelsea.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationStatus {
	PENDING("pending"),
	ACCEPTED("accepted"),
	REJECTED("rejected"),
	CANCELLED("cancelled");

	private final String status;
}
