package com.sonfind.chelsea.dto.chat;

import java.time.LocalDateTime;

import com.sonfind.chelsea.domain.chat.ChatMessage;

public record ChatMessageResponseDto(
	Long studentId,
	Long roomId,
	String content,
	LocalDateTime publishedAt
) {
	public static ChatMessageResponseDto of(ChatMessage chatMessage) {
		return new ChatMessageResponseDto(
			chatMessage.getWriterId(),
			chatMessage.getRoomId(),
			chatMessage.getContent(),
			chatMessage.getPublishedAt()
		);
	}
}
