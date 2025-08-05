package com.sonfind.chelsea.dto.studentInfo;

import java.util.List;

import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.dto.student.StudentResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;

import lombok.Builder;

@Builder
public record StudentInfoGetResponseDto(

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
	
	TeamSimpleResponseDto teamInfo
) {
}
