package com.sonfind.chelsea.dto.teams;

import lombok.Builder;

@Builder
public record TeamSimpleResponseDto(
	Long teamId,
	String name,
	String track,
	int majorCount,
	int nonMajorCount
) {
}
