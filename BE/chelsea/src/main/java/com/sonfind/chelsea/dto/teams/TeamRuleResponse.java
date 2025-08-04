package com.sonfind.chelsea.dto.teams;

import lombok.Builder;

@Builder
public record TeamRuleResponse(
	String ruleCode,
	String ruleName,
	String ruleDescription,
	boolean isOk,
	String requiredStatus
) {
	public TeamRuleResponse(String ruleCode, String ruleName, String ruleDescription, boolean isOk,
		String requiredStatus) {
		this.ruleCode = ruleCode;
		this.ruleName = ruleName;
		this.ruleDescription = ruleDescription;
		this.isOk = isOk;
		this.requiredStatus = requiredStatus;
	}
}
