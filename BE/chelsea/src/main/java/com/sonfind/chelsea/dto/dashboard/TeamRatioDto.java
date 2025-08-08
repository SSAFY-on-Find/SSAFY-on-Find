package com.sonfind.chelsea.dto.dashboard;

import lombok.Builder;

@Builder
public record TeamRatioDto(
	String type,
	int totalStudentCount,
	int teamMemberCount
) {
}
