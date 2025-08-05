package com.sonfind.chelsea.dto.teams;

import java.util.List;

import com.sonfind.chelsea.dto.subcode.SubCodeResponse;

import lombok.Builder;

@Builder
public record TeamListResponseDto(
	Long teamId,
	String teamName,
	String description,
	SubCodeResponse track,
	List<String> memberProfileImages,
	List<RecruitmentDto> recruitments,
	boolean isRecruitingComplete,
	boolean isFavorite
) {
	public TeamListResponseDto(Long teamId, String teamName, String description, SubCodeResponse track,
		List<String> memberProfileImages, List<RecruitmentDto> recruitments, boolean isRecruitingComplete,
		boolean isFavorite) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.description = description;
		this.track = track;
		this.memberProfileImages = memberProfileImages;
		this.recruitments = recruitments;
		this.isRecruitingComplete = isRecruitingComplete;
		this.isFavorite = isFavorite;
	}
}
