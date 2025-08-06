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
	public void saveTeamChatMessage(Long studentId, ChatMessageRequestDto request) {
		ChatMessage chatMessage = ChatMessage.builder()
			.writerId(studentId)
			.roomId(request.roomId())
			.content(request.content())
			.publishedAt(LocalDateTime.now())
			.build();

		chatMessageRepository.save(chatMessage);
	}

	@Transactional
	public void saveDirectChatMessage(Long studentId, ChatMessageRequestDto chat) {

	}
}
