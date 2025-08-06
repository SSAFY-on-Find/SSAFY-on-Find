package com.sonfind.chelsea.domain.chat;

import static com.sonfind.chelsea.domain.chat.ChatRoomType.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.global.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_rooms")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class ChatRoom extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "team_id", unique = true)
	private Team team;

	@Enumerated(STRING)
	@Column(name = "room_type", nullable = false)
	private ChatRoomType type;

	public static ChatRoom createTeamChatRoom(Team team) {
		return ChatRoom.builder()
			.team(team)
			.type(TEAM)
			.build();
	}

	public static ChatRoom createOneToOneChatRoom() {
		return ChatRoom.builder()
			.type(ONE_TO_ONE)
			.build();
	}
}

