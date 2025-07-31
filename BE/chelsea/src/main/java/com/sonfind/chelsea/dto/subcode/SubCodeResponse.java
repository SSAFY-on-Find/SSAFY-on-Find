package com.sonfind.chelsea.dto.subcode;

import lombok.Builder;

@Builder
public record SubCodeResponse(
	String subcode,
	String subcodeName
) {
}
