package com.sonfind.chelsea.dto.dashboard;

import lombok.Builder;

@Builder
public record TeamProgressDto(
		int progressRate,
		int totalNonMajorCount,
		int teamMemberNonMajorCount,
		int totalMajorCount,
		int teamMemberMajorCount
) {
}
