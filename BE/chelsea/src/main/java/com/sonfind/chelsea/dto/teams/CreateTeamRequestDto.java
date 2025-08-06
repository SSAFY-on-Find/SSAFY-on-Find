package com.sonfind.chelsea.dto.teams;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateTeamRequestDto(
	@NotBlank String description,
	@NotBlank String track,
	List<String> positions
) {
}
