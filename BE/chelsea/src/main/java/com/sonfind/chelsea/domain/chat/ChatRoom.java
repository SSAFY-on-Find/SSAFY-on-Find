package com.sonfind.chelsea.domain.chat;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.sonfind.chelsea.global.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_rooms")
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String name;

	@Enumerated(STRING)
	@Column(name = "room_type", nullable = false)
	private ChatRoomType type;

	public ChatRoom(String name, ChatRoomType type) {
		this.name = name;
		this.type = type;
	}
}

