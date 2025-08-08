package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sonfind.chelsea.domain.chat.ChatRoom;
import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.teams.Team;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	boolean existsByTeam(Team team);

	@Query("SELECT cr FROM ChatRoom cr " +
		"WHERE cr.team IS NULL " +
		"AND EXISTS (SELECT crm FROM ChatRoomMember crm WHERE crm.chatRoom = cr AND crm.student = :student1) " +
		"AND EXISTS (SELECT crm FROM ChatRoomMember crm WHERE crm.chatRoom = cr AND crm.student = :student2) " +
		"AND (SELECT COUNT(crm) FROM ChatRoomMember crm WHERE crm.chatRoom = cr) = 2")
	Optional<ChatRoom> findDirectChatRoomBy(@Param("student1") Student student1, @Param("student2") Student student2);

	@Query("SELECT cr FROM ChatRoom cr " +
		"LEFT JOIN FETCH cr.chatRoomMembers crm " +
		"LEFT JOIN FETCH crm.student " +
		"WHERE cr.type = com.sonfind.chelsea.domain.chat.ChatRoomType.ONE_TO_ONE " +
		"AND cr.id IN (SELECT crm2.chatRoom.id FROM ChatRoomMember crm2 WHERE crm2.student = :student)"
	)
	List<ChatRoom> findDirectChatRoomsWithMembersBy(@Param("student") Student student);

	Optional<ChatRoom> findByTeam(Team team);
}
