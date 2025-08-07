package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sonfind.chelsea.controller.docs.ChatRoomControllerDocs;
import com.sonfind.chelsea.dto.chat.ChatRoomListResponseDto;
import com.sonfind.chelsea.dto.chat.DirectChatRoomRequestDto;
import com.sonfind.chelsea.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController implements ChatRoomControllerDocs {
	private final ChatRoomService chatRoomService;

	/**
	 * 팀 내 채팅방 생성
	 */
	@PostMapping("teams/{teamId}")
	public ResponseEntity<Map<String, Object>> createTeamChatRoom(
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long teamId) {
		Long roomId = chatRoomService.createTeamChatRoom(teamId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("roomId", roomId));

		return ResponseEntity.ok().body(body);
	}

	/**
	 * 1:1 채팅방 만들기
	 */
	@PostMapping("/individual")
	public ResponseEntity<Map<String, Object>> createDirectChatRoom(
		@SessionAttribute("loginUser") Long studentId,
		@RequestBody DirectChatRoomRequestDto request
	) {
		Long roomId = chatRoomService.createDirectChatRoom(studentId, request.targetStudentId());

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("roomId", roomId));

		return ResponseEntity.ok().body(body);
	}

	/**
	 * 1:1 참여 중인 채팅방 목록 조회
	 */
	@GetMapping("/individual/me")
	public ResponseEntity<Map<String, Object>> getDirectChatRoom(
		@SessionAttribute("loginUser") Long studentId
	) {
		ChatRoomListResponseDto dto = chatRoomService.getDirectChatRooms(studentId);
		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", dto);

		return ResponseEntity.ok().body(body);
	}

}
