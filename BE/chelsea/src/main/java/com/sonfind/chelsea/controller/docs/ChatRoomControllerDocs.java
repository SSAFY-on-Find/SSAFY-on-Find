package com.sonfind.chelsea.controller.docs;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.sonfind.chelsea.dto.chat.DirectChatRoomRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface ChatRoomControllerDocs {

	@Operation(summary = "팀 채팅방 생성", description = "팀을 생성한 후, teamId을 이용하여 팀 내 채팅방을 생성할 수 있습니다.")
	ResponseEntity<Map<String, Object>> createTeamChatRoom(@Parameter(hidden = true) Long studentId, Long teamId);

	@Operation(summary = "팀 채팅방에서 나가기", description = "팀에서 나갈 때, roomId을 이용하여 팀 내 채팅방에서 나갈 수 있습니다.")
	ResponseEntity<Map<String, Object>> leaveTeamChatRoom(@Parameter(hidden = true) Long studentId, Long roomId);

	@Operation(summary = "팀 채팅방에 입장하기", description = "팀에 초대되었을 때, roomId을 이용하여 팀의 채팅방에 들어갈 수 있습니다.")
	ResponseEntity<Map<String, Object>> enterTeamChatRoom(@Parameter(hidden = true) Long studentId, Long roomId);

	@Operation(summary = "팀에 해당하는 팀 채팅 방 번호 반환하기", description = "팀을 통해 roomId을 알 수 있습니다.")
	ResponseEntity<Map<String, Object>> getTeamChatRoom(@Parameter(hidden = true) Long studentId, Long teamId);

	@Operation(summary = "1:1 채팅방 생성", description = "다른 사람의 id를 이용하여 1:1 채팅방을 생성할 수 있습니다.")
	ResponseEntity<Map<String, Object>> createDirectChatRoom(@Parameter(hidden = true) Long studentId,
		DirectChatRoomRequestDto request);

	@Operation(summary = "1:1 채팅방 목록 보기", description = "자신이 속한 1:1 채팅방 목록을 볼 수 있습니다.")
	ResponseEntity<Map<String, Object>> getDirectChatRoom(@Parameter(hidden = true) Long studentId);

	@Operation(summary = "채팅 내역 보기", description = "자신이 속한 채팅방의 전체 텍스트 내용을 볼 수 있습니다.")
	ResponseEntity<Map<String, Object>> getChatRoomMessages(@Parameter(hidden = true) Long studentId, Long roomId);

	@Operation(summary = "채팅방 마지막 읽은 시각 갱신")
	ResponseEntity<Map<String, Object>> updateLastReadAt(@Parameter(hidden = true) Long studentId, Long roomId);
}

