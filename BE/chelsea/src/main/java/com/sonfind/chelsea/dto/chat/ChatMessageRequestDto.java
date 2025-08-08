package com.sonfind.chelsea.dto.chat;

public record ChatMessageRequestDto(
	Long studentId,
	Long roomId,
	String content
) {
}
