package com.sonfind.chelsea.dto.teams;

import com.sonfind.chelsea.dto.subcode.SubCodeResponse;

import lombok.Builder;

@Builder
public record TeamMemberResponseDto(
	Long studentId,
	String name,
	String major,
	String profileImageUrl,
	SubCodeResponse position
) {

}