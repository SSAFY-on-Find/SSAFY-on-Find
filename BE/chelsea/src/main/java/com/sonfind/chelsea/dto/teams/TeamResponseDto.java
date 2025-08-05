package com.sonfind.chelsea.dto.teams;

import java.util.List;

import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;

import lombok.Builder;

@Builder
public record TeamResponseDto(
	String teamName,
	String teamDescription,
	SubCodeResponseDto teamTrack,
	int majorCount,
	int nonMajorCount,
	Long teamCount,
	List<RecruitmentDto> positions,
	List<TeamMemberResponseDto> members
) {
}
