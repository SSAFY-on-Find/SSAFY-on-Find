package com.sonfind.chelsea.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.chat.ChatRoom;
import com.sonfind.chelsea.domain.chat.ChatRoomMember;
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
	public Long createTeamChatRoom(Long teamId) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

		if (chatRoomRepository.existsByTeam(team)) {
			throw new IllegalStateException("이미 해당 팀의 채팅방이 존재합니다.");
		}

		ChatRoom chatRoom = ChatRoom.createTeamChatRoom(team);
		ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

		List<ChatRoomMember> chatRoomMembers = studentRepository.findAllByTeamId(teamId)
			.stream()
			.map(student -> new ChatRoomMember(chatRoom, student))
			.toList();
		chatRoomMemberRepository.saveAll(chatRoomMembers);
		return savedChatRoom.getId();
	}
}
