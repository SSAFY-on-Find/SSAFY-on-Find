package com.sonfind.chelsea.dto.dashboard;

import com.sonfind.chelsea.types.MemberChageAction;
import lombok.Builder;

@Builder
public record TeamMemberChangedDto(
		Long teamId,
		MemberChageAction action,
		MemberSummary member
) {
}
