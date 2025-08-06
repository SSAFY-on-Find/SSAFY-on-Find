package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController {
	private final ChatRoomService chatRoomService;

	@PostMapping("/{teamId}")
	public ResponseEntity<Map<String, Object>> createChatRoom(@PathVariable Long teamId) {
		Long roomId = chatRoomService.createTeamChatRoom(teamId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("roomId", roomId));

		return ResponseEntity.ok().body(body);
	}
}
