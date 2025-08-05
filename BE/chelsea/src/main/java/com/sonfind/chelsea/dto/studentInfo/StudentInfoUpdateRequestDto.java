package com.sonfind.chelsea.dto.studentInfo;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "자기소개 수정 요청 DTO")
public record StudentInfoUpdateRequestDto(
	@NotBlank String track,
	@NotBlank String position,
	@NotBlank String goal,
	String description,
	String mbti,
	@NotNull List<@NotBlank String> techStack,
	@Size(max = 3) List<@NotBlank @Length(max = 5) String> strength
) {
}
