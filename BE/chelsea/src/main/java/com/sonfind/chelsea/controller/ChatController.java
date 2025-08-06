package com.sonfind.chelsea.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.sonfind.chelsea.dto.chat.ChatMessageRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	private final SimpMessageSendingOperations simpMessageSendingOperations;

	@MessageMapping("/room/{roomId}")
	public void send(@DestinationVariable Long roomId, ChatMessageRequestDto request) {
		log.info("roomId : {} ChatMessageRequestDto send", roomId);
		simpMessageSendingOperations.convertAndSend("/sub/room/" + roomId, request);
	}
}
