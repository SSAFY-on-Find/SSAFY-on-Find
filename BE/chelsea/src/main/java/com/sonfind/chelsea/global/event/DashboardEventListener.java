package com.sonfind.chelsea.global.event;

import com.sonfind.chelsea.dto.dashboard.PositionChangeRequestDto;
import com.sonfind.chelsea.dto.dashboard.TeamMemberChangedDto;
import com.sonfind.chelsea.dto.dashboard.TeamProgressDto;
import com.sonfind.chelsea.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.Map;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class DashboardEventListener {

	private final SseService sseService;

	/**
	 * 팀 빌딩 비율 대시보드 이벤트 리스너
	 * 이 이벤트는 팀 빌딩 비율에 대한 통계를 처리합니다.
	 * - 팀 빌딩 비율
	 * - 팀에 속한 비전공 학생의 수
	 * - 팀에 속한 전공 학생의 수
	 */
	@EventListener
	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void onTeamBuildingRateDashboardEvent(TeamProgressDto e) {
		Map<String, Object> payload = Map.of(
				"type", "TEAM_PROGRESS_SNAPSHOT",
				"ts", Instant.now().toEpochMilli(),
				"data", e
		);

		sseService.broadcastToAll("TeamProgress", payload);
	}

	/**
	 * 팀 대시보드 이벤트 리스너
	 * 이 이벤트는 팀원 변경 사항을 처리합니다.
	 * - 팀원 추가
	 * - 팀원 삭제
	 *
	 * @param e
	 */
	@EventListener
	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void onJoinAndLeaveTeamDashboardEvent(TeamMemberChangedDto e) {
		Map<String, Object> payload = Map.of(
				"type", "TEAM_MEMBER_CHANGED",
				"ts", Instant.now().toEpochMilli(),
				"data", e
		);

		sseService.broadcastToAll("TeamMemberChanged", payload);
	}

	/**
	 * 학생의 희망 포지션 대시보드 이벤트 리스너
	 * 이 이벤트는 학생들이 희망하는 포지션에 대한 통계를 처리합니다.
	 * - 자기소개 생성 / 수정
	 */
	@EventListener
	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void onClassStudentWishPositionDashboardEvent(PositionChangeRequestDto e) {
		Map<String, Object> payload = Map.of(
				"type", "",
				"ts", Instant.now().toEpochMilli(),
				"data", e
		);

		sseService.broadcastToAll("PositionChanged", payload);
	}
}
