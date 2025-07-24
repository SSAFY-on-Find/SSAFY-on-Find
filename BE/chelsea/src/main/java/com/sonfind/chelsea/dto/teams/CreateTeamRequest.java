package com.sonfind.chelsea.dto.teams;

import java.util.List;

import lombok.Data;

@Data
public class CreateTeamRequest {
	private Long mateId;
	private String teamName;
	private String description;
	private String track;
	private List<WishPosition> wishPositions;

	@Data
	public static class WishPosition {
		private String position;
		private int count;
	}
}
