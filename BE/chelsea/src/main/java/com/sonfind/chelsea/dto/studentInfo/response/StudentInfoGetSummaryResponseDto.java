package com.sonfind.chelsea.dto.studentInfo.response;

import com.sonfind.chelsea.dto.student.response.StudentResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;

import lombok.Builder;

@Builder
public record StudentInfoGetSummaryResponseDto(

	StudentResponseDto student,

	SubCodeResponseDto position,
	SubCodeResponseDto track,

	TeamSimpleResponseDto team
) {
}
