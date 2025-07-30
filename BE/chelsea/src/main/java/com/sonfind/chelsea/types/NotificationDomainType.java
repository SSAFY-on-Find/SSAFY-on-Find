package com.sonfind.chelsea.types;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationDomainType {
	TEAM("team"),
	STUDENT("student");

	private final String domainType;

	/**
	 * 알림 도메인 타입을 문자열로부터 변환합니다.
	 * @param value 알림 도메인 타입 문자열(프론트에서 대소문자 구분 없이 전달해도 처리 가능)
	 * @return NotificationDomainType
	 */
	public static NotificationDomainType from(String value) {
		return Arrays.stream(NotificationDomainType.values())
			.filter(e -> e.domainType.equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Invalid domain type: " + value));
	}
}
