package com.sonfind.chelsea.dto.dashboard;

import lombok.Builder;

import java.util.Map;

@Builder
public record TeamInfoUpdateDto(
		Long teamId,
		String track,
		Map<String, Integer> needByPosition
) {
}
