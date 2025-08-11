package com.sonfind.chelsea.dto.chat;

import java.util.List;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;

public record ChatRoomListResponseDto(
	List<DirectChatRoomInfoDto> chatRooms
) {
	public record DirectChatRoomInfoDto(
		Long chatRoomId,
		Long targetUserId,
		String targetUsername,
		String targetProfileImageUrl,
		String major,
		SubCodeResponseDto position,
		boolean hasTeam
	) {
		public static DirectChatRoomInfoDto of(Long chatRoomId, Student opponent, StudentInfo opponentInfo) {
			String major = opponent.getMajorYn() ? "전공" : "비전공";
			String profileImageUrl =
				opponentInfo.getProfile() == null ? "" : opponentInfo.getProfile().getProfileImageUrl();
			boolean hasTeam = opponent.getTeamId() != null;

			SubCodeResponseDto positionDto = new SubCodeResponseDto(
				opponentInfo.getPositionCode().getSubCode(),
				opponentInfo.getPositionCode().getSubCodeName()
			);
			return new DirectChatRoomInfoDto(chatRoomId, opponent.getStudentId(), opponent.getName(), profileImageUrl, major, positionDto,
				hasTeam);
		}
	}
}
