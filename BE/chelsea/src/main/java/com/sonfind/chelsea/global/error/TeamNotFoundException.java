package com.sonfind.chelsea.global.error;

public class TeamNotFoundException extends BusinessException {
	public TeamNotFoundException() {
		super(ErrorCode.TEAM_NOT_FOUND);
	}
}
