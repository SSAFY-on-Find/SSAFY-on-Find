package com.sonfind.chelsea.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.sonfind.chelsea.dto.chat.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	private final SimpMessagingTemplate template;

	@MessageMapping("/messages")
	public ChatMessageDto send2(@RequestBody ChatMessageDto chatMessageDto) {
		template.convertAndSend("/sub/message", chatMessageDto.getContent());
		return chatMessageDto;
	}
}
