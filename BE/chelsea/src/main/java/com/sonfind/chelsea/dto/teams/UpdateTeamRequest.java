package com.sonfind.chelsea.dto.teams;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTeamRequest {

	@NotBlank
	private String description;

	@NotBlank
	private String trackCode;

	@NotEmpty
	private List<String> positions;
}

