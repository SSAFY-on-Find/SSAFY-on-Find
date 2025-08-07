package com.sonfind.chelsea.dto.Favorite;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record StudentFavoriteRequestDto(
	@NotNull
	Long targetStudentId
) {
}
