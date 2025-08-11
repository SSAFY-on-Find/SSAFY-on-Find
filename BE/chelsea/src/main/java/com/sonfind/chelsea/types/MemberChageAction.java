package com.sonfind.chelsea.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberChageAction {
	JOINED("joined"),
	LEFT("left");

	private final String action;
}
