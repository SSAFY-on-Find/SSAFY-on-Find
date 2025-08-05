package com.sonfind.chelsea.dto.student.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StudentSignInRequestDto(

	@NotBlank(message = "학번은 필수 항목 입니다.")
	@Pattern(regexp = "^\\d{7}$", message = "학번은 7자리 숫자입니다.")
	String studentId
) {
}
