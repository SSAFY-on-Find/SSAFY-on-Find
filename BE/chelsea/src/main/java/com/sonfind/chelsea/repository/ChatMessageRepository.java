package com.sonfind.chelsea.repository;

import org.springframework.data.repository.CrudRepository;

import com.sonfind.chelsea.domain.chat.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, String> {
	
}
