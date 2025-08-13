package com.sonfind.chelsea.global.event;

import com.sonfind.chelsea.dto.dashboard.PositionChangeRequestDto;
import com.sonfind.chelsea.dto.dashboard.TeamInfoUpdateDto;
import com.sonfind.chelsea.dto.dashboard.TeamMemberChangedDto;
import com.sonfind.chelsea.dto.dashboard.TeamProgressDto;
import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.service.SseService;
import com.sonfind.chelsea.types.EventTargetType;
import com.sonfind.chelsea.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Date;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class DashboardEventListener {

	private final SseService sseService;
	private final EventTargetType TYPE = EventTargetType.DASHBOARD;

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
		String event = "TEAM_PROGRESS_SNAPSHOT";
		NotificationDto<TeamProgressDto> payload = NotificationDto.<TeamProgressDto>builder()
				.event(event)
				.type(TYPE)
				.time(DateUtil.formatKoShort(getCurrentDate()))
				.data(e)
				.build();

		sseService.broadcastToAll(event, payload);
	}

	@EventListener
	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void onTeamInfoUpdateDashboardEvent(TeamInfoUpdateDto e) {
		String event = "TEAM_INFO_SNAPSHOT";
		NotificationDto<TeamInfoUpdateDto> payload = NotificationDto.<TeamInfoUpdateDto>builder()
				.event(event)
				.type(TYPE)
				.time(DateUtil.formatKoShort(getCurrentDate()))
				.data(e)
				.build();

		sseService.broadcastToAll(event, payload);
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
		String event = "TEAM_MEMBER_SNAPSHOT";
		NotificationDto<TeamMemberChangedDto> payload = NotificationDto.<TeamMemberChangedDto>builder()
				.event(event)
				.type(TYPE)
				.time(DateUtil.formatKoShort(getCurrentDate()))
				.data(e)
				.build();

		sseService.broadcastToAll(event, payload);
	}

	/**
	 * 학생의 희망 포지션 대시보드 이벤트 리스너
	 * 이 이벤트는 학생들이 희망하는 포지션에 대한 통계를 처리합니다.
	 * - 자기소개 생성 / 수정
	 */
	@EventListener
	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void onClassStudentWishPositionDashboardEvent(PositionChangeRequestDto e) {
		String event = "POSITION_CHANGE_SNAPSHOT";
		NotificationDto<PositionChangeRequestDto> payload = NotificationDto.<PositionChangeRequestDto>builder()
				.event(event)
				.type(TYPE)
				.time(DateUtil.formatKoShort(getCurrentDate()))
				.data(e)
				.build();

		sseService.broadcastToAll(event, payload);
	}

	private Date getCurrentDate() {
		return new Date();
	}
}
