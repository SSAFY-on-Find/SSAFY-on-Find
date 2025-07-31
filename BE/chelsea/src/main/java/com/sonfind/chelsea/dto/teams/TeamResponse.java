package com.sonfind.chelsea.dto.teams;

import com.sonfind.chelsea.dto.subcode.SubCodeResponse;

import lombok.Builder;

@Builder
public record TeamResponse(
	String teamName,
	SubCodeResponse teamTrack,
	Long teamCount
) {
}
