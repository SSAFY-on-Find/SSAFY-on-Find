package com.sonfind.chelsea.dto.studentInfo;

import java.util.List;

import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.dto.student.StudentResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(title = "자기소개 조회 response DTO")
public record StudentInfoGetResponseDto(

	@Schema(description = "학생 정보")
	StudentResponseDto student,

	@Schema(description = "포지션 정보")
	SubCodeResponseDto position,

	@Schema(description = "트랙 정보")
	SubCodeResponseDto track,

	@Schema(description = "목표 정보")
	SubCodeResponseDto goal,

	@Schema(description = "mbti 정보")
	SubCodeResponseDto mbti,

	@Schema(description = "테크 정보")
	List<SubCodeResponseDto> techStack,

	@Schema(description = "강점 정보")
	List<String> strength,

	@Schema(description = "자기소개 설명")
	String description,

	@Schema(description = "프로필 이미지 정보")
	String profileImageUrl,

	@Schema(description = "포트폴리오 ")
	UploadedFile portfolio,

	@Schema(description = "팀 정보")
	TeamSimpleResponseDto teamInfo
) {
}
