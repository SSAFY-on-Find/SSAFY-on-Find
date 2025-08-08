package com.sonfind.chelsea.dto.dashboard;

import lombok.Builder;

@Builder
public record MemberSummary(
		Long id,
		String name,
		String profileImageUrl,
		String majorType,
		String position
) {
}
