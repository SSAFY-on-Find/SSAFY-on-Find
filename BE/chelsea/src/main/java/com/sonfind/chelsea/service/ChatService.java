package com.sonfind.chelsea.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.chat.ChatMessage;
import com.sonfind.chelsea.dto.chat.ChatMessageRequestDto;
import com.sonfind.chelsea.repository.ChatMessageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public void saveChatMessage(Long studentId, ChatMessageRequestDto chat) {
		ChatMessage chatMessage = ChatMessage.builder()
			.writerId(studentId)
			.roomId(chat.roomId())
			.content(chat.content())
			.publishedAt(LocalDateTime.now())
			.build();

		chatMessageRepository.save(chatMessage);
	}
}
