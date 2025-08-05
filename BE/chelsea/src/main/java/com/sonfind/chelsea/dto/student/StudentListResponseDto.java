package com.sonfind.chelsea.dto.student;

import com.sonfind.chelsea.dto.subcode.SubCodeResponse;

public record StudentListResponseDto(
	StudentResponseDto student,
	SubCodeResponse position,
	SubCodeResponse track,
	SubCodeResponse goal,
	String profileImageUrl,
	Boolean isFavorite,
	String teamName
) {
}
