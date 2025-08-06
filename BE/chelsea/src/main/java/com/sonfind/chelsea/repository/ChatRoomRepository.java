package com.sonfind.chelsea.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonfind.chelsea.domain.chat.ChatRoom;
import com.sonfind.chelsea.domain.chat.ChatRoomType;
import com.sonfind.chelsea.domain.teams.Team;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	boolean existsByTeamAndType(Team team, ChatRoomType type);
}
