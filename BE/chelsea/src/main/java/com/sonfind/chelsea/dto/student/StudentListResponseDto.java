package com.sonfind.chelsea.dto.student;

import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;

public record StudentListResponseDto(
	StudentResponseDto student,
	SubCodeResponseDto position,
	SubCodeResponseDto track,
	SubCodeResponseDto goal,
	String profileImageUrl,
	Boolean isFavorite,
	String teamName
) {
}
