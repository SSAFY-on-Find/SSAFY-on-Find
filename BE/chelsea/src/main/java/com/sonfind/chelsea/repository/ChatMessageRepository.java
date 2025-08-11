package com.sonfind.chelsea.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sonfind.chelsea.domain.chat.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, String> {
	List<ChatMessage> findByRoomIdAndPublishedAtAfterOrderByPublishedAtAsc(Long roomId, LocalDateTime publishedAt);
}
