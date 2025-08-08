package com.sonfind.chelsea.dto.teams;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
@Schema(description = "팀 합치기 요청 DTO")
public record MergeTeamsRequestDto(

	@Schema(description = "합쳐질 팀 ID (삭제될 팀)", example = "1")
	@NotNull(message = "합쳐질 팀 ID는 필수입니다.")
	@Positive(message = "팀 ID는 양수여야 합니다.")
	Long sourceTeamId,

	@Schema(description = "합칠 팀 ID (유지될 팀)", example = "2")
	@NotNull(message = "합칠 팀 ID는 필수입니다.")
	@Positive(message = "팀 ID는 양수여야 합니다.")
	Long targetTeamId

) {
}
