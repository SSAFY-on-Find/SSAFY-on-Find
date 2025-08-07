package com.sonfind.chelsea.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonfind.chelsea.domain.chat.ChatRoom;
import com.sonfind.chelsea.domain.chat.ChatRoomMember;
import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.chat.ChatRoomListResponseDto;
import com.sonfind.chelsea.dto.chat.ChatRoomListResponseDto.DirectChatRoomInfoDto;
import com.sonfind.chelsea.repository.ChatRoomRepository;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.StudentRepository;
import com.sonfind.chelsea.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
	private final StudentRepository studentRepository;
	private final StudentInfoRepository studentInfoRepository;
	private final TeamRepository teamRepository;
	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public Long createTeamChatRoom(Long teamId) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

		if (chatRoomRepository.existsByTeam(team)) {
			throw new IllegalStateException("이미 해당 팀의 채팅방이 존재합니다.");
		}

		ChatRoom chatRoom = ChatRoom.createTeamChatRoom(team);
		List<Student> students = studentRepository.findAllByTeamId(teamId);
		students.forEach(student -> {
			ChatRoomMember member = new ChatRoomMember(chatRoom, student);
			chatRoom.addChatRoomMember(member);
		});

		ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
		return savedChatRoom.getId();
	}

	@Transactional
	public Long createDirectChatRoom(Long studentId, Long targetStudentId) {
		if (studentId.equals(targetStudentId)) {
			throw new IllegalArgumentException("자기 자신과는 채팅방을 만들 수 없습니다.");
		}

		Student student = studentRepository.findById(studentId)
			.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));
		Student targetStudent = studentRepository.findById(targetStudentId)
			.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

		boolean isPresent = chatRoomRepository.findDirectChatRoomBy(student, targetStudent).isPresent();
		if (isPresent) {
			throw new IllegalStateException("이미 존재하는 채팅방입니다.");
		}

		ChatRoom chatRoom = ChatRoom.createDirectChatRoom();
		chatRoom.addChatRoomMember(new ChatRoomMember(chatRoom, student));
		chatRoom.addChatRoomMember(new ChatRoomMember(chatRoom, targetStudent));

		ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
		return savedChatRoom.getId();
	}

	public ChatRoomListResponseDto getDirectChatRooms(Long studentId) {
		Student student = studentRepository.findById(studentId)
			.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

		List<ChatRoom> directChatRooms = chatRoomRepository.findDirectChatRoomsWithMembersBy(student);
		if (directChatRooms.isEmpty()) {
			return new ChatRoomListResponseDto(Collections.emptyList());
		}

		List<Student> opponents = directChatRooms.stream()
			.map(chatRoom -> chatRoom.getOpponent(student))
			.toList();

		List<StudentInfo> opponentInfos = studentInfoRepository.findAllByStudentIn(opponents);
		Map<Long, StudentInfo> opponentInfoMap = opponentInfos.stream()
			.collect(Collectors.toMap(info -> info.getStudent().getStudentId(), info -> info));

		List<DirectChatRoomInfoDto> directChatRoomInfoDtos = directChatRooms.stream()
			.map(chatRoom -> {
				Student opponent = chatRoom.getOpponent(student);
				StudentInfo opponentInfo = opponentInfoMap.get(opponent.getStudentId());
				return DirectChatRoomInfoDto.of(chatRoom.getId(), opponent, opponentInfo);
			})
			.toList();

		return new ChatRoomListResponseDto(directChatRoomInfoDtos);
	}
}
