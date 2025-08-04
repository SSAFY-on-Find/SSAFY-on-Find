package com.sonfind.chelsea.dto.teams;

import java.util.List;

import lombok.Builder;

@Builder
public record MyTeamResponse(
	TeamResponse teamInfo,
	int majorCount,
	int nonMajorCount,
	List<TeamRuleResponse> ruleStatuses
) {
	public MyTeamResponse(TeamResponse teamInfo, int majorCount, int nonMajorCount,
		List<TeamRuleResponse> ruleStatuses) {
		this.teamInfo = teamInfo;
		this.majorCount = majorCount;
		this.nonMajorCount = nonMajorCount;
		this.ruleStatuses = ruleStatuses;
	}
}

