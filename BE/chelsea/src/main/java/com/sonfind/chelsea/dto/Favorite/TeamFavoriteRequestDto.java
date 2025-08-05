package com.sonfind.chelsea.dto.Favorite;

import jakarta.validation.constraints.NotNull;

public record TeamFavoriteRequestDto(
	@NotNull
	Long teamId
) {
}
