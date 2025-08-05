package com.sonfind.chelsea.service;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubCodeService {

	public SubCodeResponseDto createSubCodeResponse(String code, String codeName) {
		return new SubCodeResponseDto(code, codeName);
	}
}
