package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonfind.chelsea.domain.chat.ChatRoom;
import com.sonfind.chelsea.domain.chat.ChatRoomMember;
import com.sonfind.chelsea.domain.student.Student;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
	Optional<ChatRoomMember> findByChatRoomAndStudent(ChatRoom chatRoom, Student student);

	List<ChatRoomMember> findByStudentAndChatRoomIn(Student student, List<ChatRoom> directChatRooms);
}
