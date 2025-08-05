package com.sonfind.chelsea.dto.chat;

import lombok.Data;

@Data
public class ChatMessageDto {
	private String content;
	private String sender;

	public ChatMessageDto(String content, String sender) {
		this.content = content;
		this.sender = sender;
	}
}
