package com.sonfind.chelsea.service;

import static com.sonfind.chelsea.domain.chat.ChatRoomType.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.chat.ChatRoom;
import com.sonfind.chelsea.domain.chat.ChatRoomMember;
import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.repository.ChatRoomMemberRepository;
import com.sonfind.chelsea.repository.ChatRoomRepository;
import com.sonfind.chelsea.repository.StudentRepository;
import com.sonfind.chelsea.repository.TeamRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final StudentRepository studentRepository;
	private final TeamRepository teamRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;

	@Transactional
	public void createTeamChatRoom(Long teamId) {
		List<Students> students = studentRepository.findAllByTeamId(teamId);
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

		ChatRoom chatRoom = new ChatRoom(team.getName() + "팀", TEAM);
		chatRoomRepository.save(chatRoom);

		for (Students student : students) {
			chatRoomMemberRepository.save(new ChatRoomMember(chatRoom, student));
		}
	}
}
