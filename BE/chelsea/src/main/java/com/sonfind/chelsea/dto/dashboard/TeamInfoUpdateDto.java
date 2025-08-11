package com.sonfind.chelsea.dto.dashboard;

import java.util.List;

import lombok.Builder;

@Builder
public record TeamInfoUpdateDto(
	Long teamId,
	String track,
	String description,
	List<String> afterNeedByPosition
) {
}
