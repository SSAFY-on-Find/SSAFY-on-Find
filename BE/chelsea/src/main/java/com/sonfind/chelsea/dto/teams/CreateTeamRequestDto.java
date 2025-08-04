package com.sonfind.chelsea.dto.teams;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateTeamRequestDto {

	@NotBlank
	private String description;

	@NotBlank
	private String trackCode;

	@NotEmpty
	private List<String> positions;
}
