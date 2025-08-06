package com.sonfind.chelsea.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.dto.subcode.SubCodeMeResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.repository.SubCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubCodeService {

	private final SubCodeRepository subCodeRepository;

	public SubCodeMeResponseDto getAllSubCode() {

		List<SubCode> subCodes = subCodeRepository.findAll();

		List<SubCodeResponseDto> position = getSubCodeResponseByMainCode(subCodes, "POS");
		List<SubCodeResponseDto> track = getSubCodeResponseByMainCode(subCodes, "TRK");
		List<SubCodeResponseDto> goal = getSubCodeResponseByMainCode(subCodes, "GOAL");
		List<SubCodeResponseDto> techStack = getSubCodeResponseByMainCode(subCodes, "TECH");
		List<SubCodeResponseDto> mbti = getSubCodeResponseByMainCode(subCodes, "MBTI");

		return SubCodeMeResponseDto.builder()
			.position(position)
			.track(track)
			.goal(goal)
			.techStack(techStack)
			.mbti(mbti)
			.build();
	}

	private List<SubCodeResponseDto> getSubCodeResponseByMainCode(List<SubCode> subCodes, String mainCode) {
		return subCodes.stream()
			.filter(subCode -> subCode.getMainCode().getMainCode().equals(mainCode))
			.map(subCode -> new SubCodeResponseDto(subCode.getSubCode(), subCode.getSubCodeName())).toList();
	}

	public SubCodeResponseDto createSubCodeResponse(String code, String codeName) {
		return new SubCodeResponseDto(code, codeName);
	}
}
