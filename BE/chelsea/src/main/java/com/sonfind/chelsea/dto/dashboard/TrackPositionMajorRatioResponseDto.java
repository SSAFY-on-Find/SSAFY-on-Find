package com.sonfind.chelsea.dto.dashboard;

import java.util.List;

import lombok.Builder;

@Builder
public record TrackPositionMajorRatioResponseDto(
	String track,
	int totalCount,

	List<RatioResponseDto> majorType,
	List<RatioResponseDto> positionType
) {
}
