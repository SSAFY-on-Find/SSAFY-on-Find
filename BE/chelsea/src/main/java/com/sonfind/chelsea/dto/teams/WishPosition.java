package com.sonfind.chelsea.dto.teams;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sonfind.chelsea.domain.teams.Position;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WishPosition {

	@JsonProperty("position")
	private Position position;

	@JsonProperty("count")
	private int count;
}