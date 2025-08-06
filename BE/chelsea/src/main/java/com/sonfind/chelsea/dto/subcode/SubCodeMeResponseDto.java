package com.sonfind.chelsea.dto.subcode;

import java.util.List;

import lombok.Builder;

@Builder
public record SubCodeMeResponseDto(
	List<SubCodeResponseDto> position,
	List<SubCodeResponseDto> track,
	List<SubCodeResponseDto> techStack,
	List<SubCodeResponseDto> goal,
	List<SubCodeResponseDto> mbti
) {
}
