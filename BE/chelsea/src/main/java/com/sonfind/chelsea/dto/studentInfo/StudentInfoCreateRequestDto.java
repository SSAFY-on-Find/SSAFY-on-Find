package com.sonfind.chelsea.dto.studentInfo;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentInfoCreateRequestDto(
	@NotBlank String track,
	@NotBlank String position,
	@NotBlank String goal,
	String description,
	String mbti,
	@NotNull List<@NotBlank String> techStack,
	@Size(max = 3) List<@NotBlank @Length(max = 5) String> strength
) {
}
