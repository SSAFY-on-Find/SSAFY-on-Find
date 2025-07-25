package com.sonfind.chelsea.dto.teams;

import java.util.List;

import com.sonfind.chelsea.domain.teams.Track;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTeamRequest {

	private String description;
	private Track track;
	private List<WishPosition> wishPositions;
}

