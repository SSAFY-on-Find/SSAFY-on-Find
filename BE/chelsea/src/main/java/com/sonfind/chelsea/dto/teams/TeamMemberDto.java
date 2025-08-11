package com.sonfind.chelsea.dto.teams;

import lombok.Builder;

//쿼리 최적화용
@Builder
public record TeamMemberDto(
	Long teamId,
	Long studentId,
	String name,
	Boolean majorYn,
	String profileImageUrl,
	String positionCode,
	String positionName
) {
}
