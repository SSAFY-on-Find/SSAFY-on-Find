package com.sonfind.chelsea.dto.teams;

import lombok.Builder;

//쿼리 최적화용
@Builder
public record TeamWithMembersDto(
	Long teamId,
	String name,
	String description,
	String trackCode,
	String trackName,
	int majorCount,
	int nonMajorCount
) {
}
