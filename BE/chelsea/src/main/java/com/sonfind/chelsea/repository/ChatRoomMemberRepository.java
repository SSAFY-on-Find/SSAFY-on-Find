package com.sonfind.chelsea.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonfind.chelsea.domain.chat.ChatRoomMember;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
}
