package com.sonfind.chelsea.dto.subcode;

import lombok.Builder;

@Builder
public record SubCodeResponseDto(
	String subcode,
	String subcodeName
) {
}
