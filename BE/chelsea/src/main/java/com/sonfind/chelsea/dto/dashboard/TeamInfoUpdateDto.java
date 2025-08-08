package com.sonfind.chelsea.dto.dashboard;

import lombok.Builder;

import java.util.List;

@Builder
public record TeamInfoUpdateDto(
		Long teamId,
		String track,
		List<String> afterNeedByPosition
) {
}
