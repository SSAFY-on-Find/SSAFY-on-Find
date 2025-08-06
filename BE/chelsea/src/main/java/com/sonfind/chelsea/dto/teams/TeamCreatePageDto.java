package com.sonfind.chelsea.dto.teams;

import java.util.List;

import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;

public record TeamCreatePageDto(
	List<SubCodeResponseDto> tracks,
	List<SubCodeResponseDto> positions
) {
}
