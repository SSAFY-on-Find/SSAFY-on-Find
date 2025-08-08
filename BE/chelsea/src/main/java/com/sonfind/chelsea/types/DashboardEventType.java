package com.sonfind.chelsea.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DashboardEventType {
	TEAM_MEMBER_CHANGED("team_member_changed"), // 팀원 합류 / 퇴장
	TEAM_PROGRESS_UPDATED("team_progress_updated"), // 팀 빌딩 진행률
	TEAM_UPDATED("team_updated"), // 팀 정보 업데이트
	STUDENT_INFO_UPDATED("student_info_updated"); // 학생 정보 업데이트(희망 트랙별 통계)

	private final String value;
}
