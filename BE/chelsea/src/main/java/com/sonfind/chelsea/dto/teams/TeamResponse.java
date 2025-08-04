package com.sonfind.chelsea.dto.teams;

import java.util.List;

import com.sonfind.chelsea.dto.subcode.SubCodeResponse;

import lombok.Builder;

@Builder
public record TeamResponse(
	String teamName,
	String teamDescription,
	SubCodeResponse teamTrack,
	Long teamCount,
	List<RecruitmentDto> positions,
	List<TeamMemberResponse> members
) {
}
