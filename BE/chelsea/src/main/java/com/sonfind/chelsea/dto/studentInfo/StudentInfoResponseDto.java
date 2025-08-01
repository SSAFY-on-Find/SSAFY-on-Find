package com.sonfind.chelsea.dto.studentInfo;

import java.util.List;

import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.dto.student.StudentResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponse;
import com.sonfind.chelsea.dto.teams.TeamResponse;

import lombok.Builder;

@Builder
public record StudentInfoResponseDto(

	StudentResponseDto student,

	SubCodeResponse position,
	SubCodeResponse track,
	SubCodeResponse goal,
	SubCodeResponse mbti,
	List<SubCodeResponse> techStack,
	List<String> strength,
	String description,
	String profileImageUrl,
	UploadedFile portfolio,

	TeamResponse teamInfo
) {
}
