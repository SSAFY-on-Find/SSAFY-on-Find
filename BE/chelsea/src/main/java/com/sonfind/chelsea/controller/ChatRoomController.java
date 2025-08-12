package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.List;
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
import com.sonfind.chelsea.dto.chat.ChatMessageResponseDto;
import com.sonfind.chelsea.dto.chat.ChatRoomListResponseDto;
import com.sonfind.chelsea.dto.chat.DirectChatRoomRequestDto;
import com.sonfind.chelsea.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController implements ChatRoomControllerDocs {
	private final ChatRoomService chatRoomService;

	@PostMapping("/teams/{teamId}")
	public ResponseEntity<Map<String, Object>> createTeamChatRoom(
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long teamId) {
		Long roomId = chatRoomService.createTeamChatRoom(teamId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("roomId", roomId));
		return ResponseEntity.ok().body(body);
	}

	@GetMapping("/teams/{teamId}")
	public ResponseEntity<Map<String, Object>> getTeamChatRoom(
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long teamId
	) {
		Long roomId = chatRoomService.findRoomByTeam(teamId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("roomId", roomId));
		return ResponseEntity.ok().body(body);
	}

	@PostMapping("/rooms/{roomId}/join")
	public ResponseEntity<Map<String, Object>> enterTeamChatRoom(
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long roomId
	) {
		chatRoomService.enterStudent(studentId, roomId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", null);
		return ResponseEntity.ok().body(body);
	}

	@PostMapping("/rooms/{roomId}/leave")
	public ResponseEntity<Map<String, Object>> leaveTeamChatRoom(
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long roomId
	) {
		chatRoomService.leaveStudent(studentId, roomId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", null);
		return ResponseEntity.ok().body(body);
	}

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

	@GetMapping("/{roomId}/messages")
	public ResponseEntity<Map<String, Object>> getChatRoomMessages(
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long roomId
	) {
		List<ChatMessageResponseDto> messages = chatRoomService.getMessages(studentId, roomId);
		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", messages);
		return ResponseEntity.ok().body(body);
	}

	@PostMapping("/{roomId}/last-read")
	public ResponseEntity<Map<String, Object>> updateLastReadAt(
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long roomId
	) {
		chatRoomService.updateLastReadAt(studentId, roomId);
		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", null);
		return ResponseEntity.ok().body(body);
	}
}
