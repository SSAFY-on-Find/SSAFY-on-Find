package com.sonfind.chelsea.dto.teams;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDetailResponse {
	private Long teamId;
	private String teamName;
	private String description;

	private String trackCode;
	private String trackName;
	private List<RecruitmentDto> recruitments;

	private int memberCount;
	private List<MemberDto> members;
}
