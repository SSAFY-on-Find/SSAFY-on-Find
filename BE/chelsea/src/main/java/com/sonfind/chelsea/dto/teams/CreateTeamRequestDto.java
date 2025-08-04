package com.sonfind.chelsea.dto.teams;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record CreateTeamRequestDto(
	@NotBlank String description,
	@NotBlank String track,
	@NotEmpty List<String> positions
) {
}
