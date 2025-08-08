package com.sonfind.chelsea.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;

public record DirectChatRoomRequestDto(
	@Schema(description = "1:1 채팅을 할 상대방의 학번", example = "1300010")
	Long targetStudentId
) {
}
