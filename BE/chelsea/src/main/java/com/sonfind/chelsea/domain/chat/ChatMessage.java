package com.sonfind.chelsea.domain.chat;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
	@Id
	private String id;

	@Indexed
	private Long roomId;

	private String content;

	private Long writerId;

	@Indexed
	private LocalDateTime publishedAt;
}
