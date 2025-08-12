package com.sonfind.chelsea.dto.teams;

import lombok.Builder;

//쿼리 최적화용
@Builder
public record TeamRecruitmentDto(
	Long teamId,
	String positionCode,
	String positionName
) {
}
