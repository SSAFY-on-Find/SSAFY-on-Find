package com.sonfind.chelsea.dto.teams;

import java.util.List;

import lombok.Builder;

@Builder
public record MyTeamResponseDto(
	TeamResponseDto teamInfo,
	int majorCount,
	int nonMajorCount,
	List<TeamRuleResponseDto> ruleStatuses
) {
	public MyTeamResponseDto(TeamResponseDto teamInfo, int majorCount, int nonMajorCount,
		List<TeamRuleResponseDto> ruleStatuses) {
		this.teamInfo = teamInfo;
		this.majorCount = majorCount;
		this.nonMajorCount = nonMajorCount;
		this.ruleStatuses = ruleStatuses;
	}
}

