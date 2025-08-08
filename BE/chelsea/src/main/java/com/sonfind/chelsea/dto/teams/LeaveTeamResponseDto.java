package com.sonfind.chelsea.dto.teams;

import lombok.Builder;

@Builder
public record LeaveTeamResponseDto(
	String message,
	Boolean teamDeleted
) {
}
