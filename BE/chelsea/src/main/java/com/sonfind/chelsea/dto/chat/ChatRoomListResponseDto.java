package com.sonfind.chelsea.dto.chat;

import java.util.List;

import com.sonfind.chelsea.domain.studentInfo.StudentInfo;

public record ChatRoomListResponseDto(
	List<DirectChatRoomInfoDto> chatRooms
) {

	public record DirectChatRoomInfoDto(
		Long chatRoomId,
		String targetUsername,
		String targetProfileImageUrl
	) {
		public DirectChatRoomInfoDto(Long chatRoomId, StudentInfo info) {
			this(chatRoomId, null, info.getProfileImageUrl());
		}
	}
}
