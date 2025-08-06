package com.sonfind.chelsea.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatRoomController {
	private final ChatRoomService chatRoomService;

	@PostMapping("/{teamId}")
	public ResponseEntity<Void> createChatRoom(@PathVariable Long teamId) {
		chatRoomService.createTeamChatRoom(teamId);
		return null;
	}
}
