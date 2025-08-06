package com.sonfind.chelsea.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.sonfind.chelsea.dto.chat.ChatMessageRequestDto;
import com.sonfind.chelsea.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final ChatService chatService;

	@MessageMapping("/message")
	public void send(ChatMessageRequestDto request) {
		chatService.saveChatMessage(request.studentId(), request);
		simpMessageSendingOperations.convertAndSend("/sub/chatroom/" + request.roomId(), request.content());
	}
}
