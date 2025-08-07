package com.sonfind.chelsea.domain.chat;

import static com.sonfind.chelsea.domain.chat.ChatRoomType.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.global.domain.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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

	@Builder.Default
	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ChatRoomMember> chatRoomMembers = new HashSet<>();

	public static ChatRoom createTeamChatRoom(Team team) {
		return ChatRoom.builder()
			.team(team)
			.type(TEAM)
			.build();
	}

	public static ChatRoom createDirectChatRoom() {
		return ChatRoom.builder()
			.type(ONE_TO_ONE)
			.build();
	}

	public void addChatRoomMember(ChatRoomMember chatRoomMember) {
		chatRoomMembers.add(chatRoomMember);
		chatRoomMember.changeChatRoom(this);
	}

	public Set<ChatRoomMember> getChatRoomMembersWithout(Student student) {
		return this.chatRoomMembers.stream()
			.filter(member -> !member.getStudent().equals(student))
			.collect(Collectors.toSet());
	}

	public Student getOpponent(Student student) {
		if (this.type != ChatRoomType.ONE_TO_ONE || this.chatRoomMembers.size() != 2) {
			throw new IllegalStateException("1:1 채팅방이 아니거나 멤버가 2명이 아닙니다.");
		}

		return this.chatRoomMembers.stream()
			.map(ChatRoomMember::getStudent)
			.filter(memberStudent -> !memberStudent.equals(student))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("채팅방에서 상대방을 찾을 수 없습니다."));
	}
}

