package com.sonfind.chelsea.dto.teams;

import java.util.List;

import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;

import lombok.Builder;

@Builder
public record TeamListResponseDto(
	Long teamId,
	String teamName,
	String description,
	SubCodeResponseDto track,
	List<RecruitmentDto> recruitments,
	List<TeamMemberResponseDto> members,
	boolean isRecruitingComplete,
	boolean isFavorite
) {
	public TeamListResponseDto(Long teamId, String teamName, String description, SubCodeResponseDto track,
		List<RecruitmentDto> recruitments, List<TeamMemberResponseDto> members,
		boolean isRecruitingComplete, boolean isFavorite) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.description = description;
		this.track = track;
		this.recruitments = recruitments;
		this.members = members;
		this.isRecruitingComplete = isRecruitingComplete;
		this.isFavorite = isFavorite;
	}
}
