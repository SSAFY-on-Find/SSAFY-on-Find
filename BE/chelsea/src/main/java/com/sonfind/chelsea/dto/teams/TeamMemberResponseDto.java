package com.sonfind.chelsea.dto.teams;

import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;

import lombok.Builder;

@Builder
public record TeamMemberResponseDto(
	Long studentId,
	String name,
	String major,
	String profileImageUrl,
	SubCodeResponseDto position
) {

}