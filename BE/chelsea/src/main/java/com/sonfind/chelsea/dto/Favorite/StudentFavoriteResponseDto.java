package com.sonfind.chelsea.dto.Favorite;

import lombok.Builder;

@Builder
public record StudentFavoriteResponseDto(
	Boolean isFavorite
) {
}
