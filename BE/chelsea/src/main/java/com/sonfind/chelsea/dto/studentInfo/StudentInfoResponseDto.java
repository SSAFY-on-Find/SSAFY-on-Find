package com.sonfind.chelsea.dto.studentInfo;

import java.util.List;

import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.dto.student.StudentResponse;
import com.sonfind.chelsea.dto.subcode.SubCodeResponse;
import com.sonfind.chelsea.dto.teams.TeamResponseDto;

import lombok.Builder;

@Builder
public record StudentInfoResponseDto(

	StudentResponse student,

	SubCodeResponse position,
	SubCodeResponse track,
	SubCodeResponse goal,
	SubCodeResponse mbti,
	List<SubCodeResponse> techStack,
	List<String> strength,
	String description,
	String profileImageUrl,
	UploadedFile portfolio,

	TeamResponseDto teamInfo
) {
}
