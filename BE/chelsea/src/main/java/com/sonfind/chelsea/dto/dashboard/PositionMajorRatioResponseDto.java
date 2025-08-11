package com.sonfind.chelsea.dto.dashboard;

import java.util.List;

import lombok.Builder;

@Builder
public record PositionMajorRatioResponseDto(
	int totalCount,

	List<RatioResponseDto> teamType
) {
}
