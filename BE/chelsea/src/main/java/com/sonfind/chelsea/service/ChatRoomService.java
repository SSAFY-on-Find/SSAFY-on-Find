package com.sonfind.chelsea.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonfind.chelsea.domain.chat.ChatMessage;
import com.sonfind.chelsea.domain.chat.ChatRoom;
import com.sonfind.chelsea.domain.chat.ChatRoomMember;
import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.chat.ChatMessageResponseDto;
import com.sonfind.chelsea.dto.chat.ChatRoomListResponseDto;
import com.sonfind.chelsea.dto.chat.ChatRoomListResponseDto.DirectChatRoomInfoDto;
import com.sonfind.chelsea.repository.ChatMessageRepository;
import com.sonfind.chelsea.repository.ChatRoomMemberRepository;
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
	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;

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

		List<ChatRoomMember> myMemberships = chatRoomMemberRepository.findByStudentAndChatRoomIn(student,
			directChatRooms);

		Map<Long, ChatRoomMember> chatRoomMemberMap = myMemberships.stream()
			.collect(Collectors.toMap(member -> member.getChatRoom().getId(), member -> member));

		List<DirectChatRoomInfoDto> directChatRoomInfoDtos = directChatRooms.stream()
			.map(chatRoom -> {
				Student opponent = chatRoom.getOpponent(student);
				StudentInfo opponentInfo = opponentInfoMap.get(opponent.getStudentId());
				ChatRoomMember myMember = chatRoomMemberMap.get(chatRoom.getId());

				if (opponentInfo == null || myMember == null) {
					return null;
				}

				Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findTopByRoomIdOrderByPublishedAtDesc(
					chatRoom.getId());

				boolean isRead = true;
				LocalDateTime lastChatAt = LocalDateTime.MIN;
				if (lastMessageOpt.isPresent()) {
					ChatMessage lastMessage = lastMessageOpt.get();
					LocalDateTime myReadAt = myMember.getReadAt();
					if (myReadAt == null || lastMessage.getPublishedAt().isAfter(myReadAt)) {
						isRead = false;
					}
					lastChatAt = lastMessage.getPublishedAt();
				}
				return DirectChatRoomInfoDto.of(chatRoom, opponent, opponentInfo, isRead, lastChatAt);
			})
			.filter(Objects::nonNull)
			.sorted(Comparator.comparing(DirectChatRoomInfoDto::lastChatAt,
				Comparator.nullsLast(Comparator.reverseOrder())))
			.toList();

		return new ChatRoomListResponseDto(directChatRoomInfoDtos);
	}

	@Transactional
	public void leaveStudent(Long studentId, Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

		Student student = studentRepository.findByStudentId(studentId)
			.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

		ChatRoomMember memberToRemove = chatRoom.getChatRoomMembers().stream()
			.filter(m -> m.getStudent().equals(student))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("팀 채팅에 존재하지 않는 학생입니다."));

		chatRoom.removeChatRoomMember(memberToRemove);
	}

	@Transactional
	public void enterStudent(Long studentId, Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

		Student student = studentRepository.findByStudentId(studentId)
			.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

		if (chatRoom.hasStudent(student)) {
			throw new IllegalStateException("이미 팀 채팅에 존재하는 학생입니다.");
		}

		chatRoom.addChatRoomMember(new ChatRoomMember(chatRoom, student));
	}

	public Long findRoomByTeam(Long teamId) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

		ChatRoom chatRoom = chatRoomRepository.findByTeam(team)
			.orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

		return chatRoom.getId();
	}

	public List<ChatMessageResponseDto> getMessages(Long studentId, Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

		Student student = studentRepository.findByStudentId(studentId)
			.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

		ChatRoomMember chatRoomMember = chatRoom.getChatRoomMembers().stream()
			.filter(member -> member.getStudent().equals(student))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("사용자는 이 채팅방의 멤버가 아닙니다."));

		LocalDateTime joinedAt = chatRoomMember.getCreated_at();
		return chatMessageRepository.findByRoomIdAndPublishedAtAfterOrderByPublishedAtAsc(roomId, joinedAt)
			.stream()
			.map(ChatMessageResponseDto::of)
			.toList();

	}

	@Transactional
	public void updateLastReadAt(Long studentId, Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

		Student student = studentRepository.findByStudentId(studentId)
			.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

		ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndStudent(chatRoom, student)
			.orElseThrow(() -> new IllegalArgumentException("채팅방에 학생이 존재하지 않습니다."));

		chatRoomMember.changeReadAt(LocalDateTime.now());
	}
}
