package com.sonfind.chelsea.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.sonfind.chelsea.dto.chat.ChatMessageRequestDto;
import com.sonfind.chelsea.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final ChatService chatService;

	/**
	 * 팀 채팅 메시지 처리
	 * 클라이언트가 /pub/team/message로 메시지를 보내면 이 메서드가 처리합니다.
	 */
	@MessageMapping("/team/message")
	public void send(ChatMessageRequestDto request) {
		chatService.saveChatMessage(request.studentId(), request);
		simpMessageSendingOperations.convertAndSend("/topic/chatroom/" + request.roomId(), request.content());
	}

	/**
	 * 1:1 채팅 메시지 처리
	 * 클라이언트가 /pub/direct/message로 메시지를 보내면 이 메서드가 처리합니다.
	 */
	@MessageMapping("/direct/message")
	public void sendDirectMessage(ChatMessageRequestDto request) {
		chatService.saveChatMessage(request.studentId(), request);
		simpMessageSendingOperations.convertAndSend("/queue/chatroom/" + request.roomId(), request);
	}
}
