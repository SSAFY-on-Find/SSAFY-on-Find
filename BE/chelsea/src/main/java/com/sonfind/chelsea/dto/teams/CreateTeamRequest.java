package com.sonfind.chelsea.dto.teams;

import java.util.List;

import com.sonfind.chelsea.domain.teams.Track;

import lombok.Getter;
import lombok.NoArgsConstructor;

//data없앴음
@Getter
@NoArgsConstructor
public class CreateTeamRequest {
	private String description;
	private Track track; //enum으로 수정
	private List<WishPosition> wishPositions;
}
