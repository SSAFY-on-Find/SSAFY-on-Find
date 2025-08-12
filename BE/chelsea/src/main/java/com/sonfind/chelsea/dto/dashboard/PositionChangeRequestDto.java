package com.sonfind.chelsea.dto.dashboard;

import lombok.Builder;

@Builder
public record PositionChangeRequestDto(
	String prePosition,
	String curPosition,
	boolean isTeam
) {
}
