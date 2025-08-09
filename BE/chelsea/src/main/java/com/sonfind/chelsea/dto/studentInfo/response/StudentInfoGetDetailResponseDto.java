package com.sonfind.chelsea.dto.studentInfo.response;

import java.util.List;

import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.dto.student.response.StudentResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;

import lombok.Builder;

@Builder
public record StudentInfoGetDetailResponseDto(

	StudentResponseDto student,

	SubCodeResponseDto position,
	SubCodeResponseDto track,
	SubCodeResponseDto goal,
	SubCodeResponseDto mbti,
	List<SubCodeResponseDto> techStack,
	List<String> strength,
	String description,
	String profileImageUrl,
	UploadedFile portfolio,
	Boolean isFavorite,

	TeamSimpleResponseDto teamInfo
) {
}
