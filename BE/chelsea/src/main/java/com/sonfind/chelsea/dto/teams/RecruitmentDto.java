package com.sonfind.chelsea.dto.teams;

import lombok.Builder;

@Builder
public record RecruitmentDto(
	String positionCode,
	String positionName
) {

	public RecruitmentDto(String positionCode, String positionName) {
		this.positionCode = positionCode;
		this.positionName = positionName;
	}
}


