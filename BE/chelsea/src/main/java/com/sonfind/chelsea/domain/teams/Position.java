package com.sonfind.chelsea.domain.teams;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Position {
	BE, FE, FS, INFRA, AI, IOT, MOBILE;

	//serialization
	@JsonValue
	public String toValue() {
		return name();
	}

	//바인딩 에러
	@JsonCreator
	public static Position from(String name) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("해당 포지션 없음 " + name));
	}
}
