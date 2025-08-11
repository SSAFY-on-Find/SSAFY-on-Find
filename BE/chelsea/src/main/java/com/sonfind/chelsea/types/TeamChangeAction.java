package com.sonfind.chelsea.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamChangeAction {
	CREATED("created"), // 팀 생성
	DELETED("deleted"); // 팀 삭제

	private final String action;
}
