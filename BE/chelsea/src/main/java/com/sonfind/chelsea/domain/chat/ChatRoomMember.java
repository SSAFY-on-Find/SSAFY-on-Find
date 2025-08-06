package com.sonfind.chelsea.domain.chat;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.global.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class ChatRoomMember extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoom chatRoom;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Students student;

	public ChatRoomMember(ChatRoom chatRoom, Students student) {
		this.chatRoom = chatRoom;
		this.student = student;
	}
}
