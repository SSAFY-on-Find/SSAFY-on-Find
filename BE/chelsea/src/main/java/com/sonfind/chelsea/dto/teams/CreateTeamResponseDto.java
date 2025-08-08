package com.sonfind.chelsea.dto.teams;

import lombok.Builder;

@Builder
public record CreateTeamResponseDto(
	Long teamId
) {
}
