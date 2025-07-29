package com.sonfind.chelsea.global.event;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sonfind.chelsea.service.SseService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

	private final SseService sseService;

	/**
	 * 알림 이벤트 리스너
	 * TODO: 수/발신용 payload 자료형 생성 후 변경 필요
	 */
	@EventListener
	public void onNotification(NotificationEvent e) {
		switch (e.getType()) {
			case APPLICATION -> {
				var pubPayload = createApplicationPubPayload(e);
				var subPayload = createApplicationSubPayload(e);
				sseService.sendNotification(e.getPubId(), pubPayload);
				sseService.sendNotification(e.getSubId(), subPayload);
			}
			case INVITATION -> {
				var pubPayload = createInvitationPubPayload(e);
				var subPayload = createInvitationSubPayload(e);
				sseService.sendNotification(e.getPubId(), pubPayload);
				sseService.sendNotification(e.getSubId(), subPayload);
			}
			case MERGE -> {
				var pubPayload = createMergePubPayload(e);
				var subPayload = createMergeSubPayload(e);
				sseService.sendNotification(e.getPubId(), pubPayload);
				sseService.sendNotification(e.getSubId(), subPayload);
			}
		}
	}

	// APPLICATION(개인 -> 팀 지원)
	private static Map<String, Object> createApplicationPubPayload(NotificationEvent e) {
		return Map.of(
			"event", "application_requested",
			"title", e.getPubType() + "님이 지원",
			"message", e.getPubType() + "님이 " + e.getSubType() + "에 지원했습니다.",
			"timestamp", System.currentTimeMillis()
		);
	}

	private static Map<String, Object> createApplicationSubPayload(NotificationEvent e) {
		return Map.of(
			"event", "application_requested",
			"title", e.getSubType() + "의 지원",
			"message", e.getSubType() + "에서 " + e.getPubType() + "님의 지원을 받았습니다.",
			"timestamp", System.currentTimeMillis()
		);
	}

	// INVITATION(팀 -> 개인 초대)
	private static Map<String, Object> createInvitationPubPayload(NotificationEvent e) {
		return Map.of(
			"event", "invitation_requested",
			"title", e.getPubType() + "님에게 초대",
			"message", e.getPubType() + "님을 " + e.getSubType() + "에 초대했습니다.",
			"timestamp", System.currentTimeMillis()
		);
	}

	private static Map<String, Object> createInvitationSubPayload(NotificationEvent e) {
		return Map.of(
			"event", "invitation_requested",
			"title", e.getSubType() + "의 초대",
			"message", e.getSubType() + "에서 " + e.getPubType() + "님의 초대를 받았습니다.",
			"timestamp", System.currentTimeMillis()
		);
	}

	// MERGE(팀 합치기)
	private static Map<String, Object> createMergePubPayload(NotificationEvent e) {
		return Map.of(
			"event", "merge_requested",
			"title", e.getPubType() + "에 합치기 제안",
			"message", e.getPubType() + "에서 " + e.getSubType() + "에 합치기를 제안했습니다.",
			"timestamp", System.currentTimeMillis()
		);
	}

	private static Map<String, Object> createMergeSubPayload(NotificationEvent e) {
		return Map.of(
			"event", "merge_requested",
			"title", e.getSubType() + "의 합치기 제안",
			"message", e.getSubType() + "에서 " + e.getPubType() + "팀 합치기를 제안받았습니다.",
			"timestamp", System.currentTimeMillis()
		);
	}
}
