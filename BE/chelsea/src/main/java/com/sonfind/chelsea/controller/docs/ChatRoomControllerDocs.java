package com.sonfind.chelsea.controller.docs;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.sonfind.chelsea.dto.chat.DirectChatRoomRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface ChatRoomControllerDocs {

	@Operation(summary = "팀 채팅방 생성", description = "팀을 생성한 후, teamId을 이용하여 팀 내 채팅방을 생성할 수 있습니다.")
	ResponseEntity<Map<String, Object>> createTeamChatRoom(@Parameter(hidden = true) Long studentId, Long teamId);

	@Operation(summary = "1:1 채팅방 생성", description = "다른 사람의 id를 이용하여 1:1 채팅방을 생성할 수 있습니다.")
	ResponseEntity<Map<String, Object>> createDirectChatRoom(@Parameter(hidden = true) Long studentId,
		DirectChatRoomRequestDto request);
}

