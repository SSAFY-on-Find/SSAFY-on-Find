package com.sonfind.chelsea.dto.dashboard;

import lombok.Builder;

@Builder
public record RatioResponseDto(
	String name,
	int count
) {
}
