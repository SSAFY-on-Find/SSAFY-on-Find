package com.sonfind.chelsea.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationDomainType {
	TEAM("team"),
	MATE("mate");

	private final String domainType;
}
