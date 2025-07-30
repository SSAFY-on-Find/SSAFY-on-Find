package com.sonfind.chelsea.global.event;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sonfind.chelsea.dto.notification.ApplicationPublisherData;
import com.sonfind.chelsea.dto.notification.ApplicationSubscriberData;
import com.sonfind.chelsea.dto.notification.NotificationContent;
import com.sonfind.chelsea.dto.notification.NotificationEventResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationIdentity;
import com.sonfind.chelsea.dto.notification.ReceiveApplicationResponseDto;
import com.sonfind.chelsea.dto.notification.SentApplicationResponseDto;
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
	private static NotificationEventResponseDto createApplicationPubPayload(NotificationEvent e) {
		// String pubName = studentFacade.getStudentName(e.getPubId());
		// String subName = studentFacade.getStudentName(e.getSubId());
		// String pubTrack = studentFacade.getStudentTrack(e.getPubId());
		// String subTrack = teamFacade.getTeamTrack(e.getSubId());
		String pubName = "김싸피";
		String subName = "팀 A";
		String subTrack = "웹기술";

		// 발신자 기본 정보
		NotificationIdentity pubIdentity = NotificationIdentity.builder()
			.id(e.getPubId())
			.name(pubName)
			.type(e.getPubType())
			.build();

		// 발신자 추가 정보
		NotificationContent common = NotificationContent.builder()
			.notificationTitle("e.getPublisher().getNotificationTitle()")
			.notificationMessage("e.getPublisher().getNotificationMessage()")
			.build();

		// ② ApplicationPublisherData 빌더에 담기
		ApplicationPublisherData pubInfo = ApplicationPublisherData.builder()
			.notificationIdentity(pubIdentity)
			.notificationContent(common)
			.build();

		// 수신자 정보
		NotificationIdentity subInfo = NotificationIdentity.builder()
			.id(e.getSubId())
			.name(subName)
			.type(e.getSubType())
			.track(subTrack)
			.build();

		// data 페이로드 생성
		SentApplicationResponseDto pubData = SentApplicationResponseDto.builder()
			.publisher(pubInfo)
			.subscriber(subInfo)
			.build();

		// ③ 최종 응답 DTO
		return NotificationEventResponseDto.builder()
			.id(e.getNotificationId())
			.event(e.getClass().toString())
			.type(e.getType())         // APPLICATION 으로
			.time(e.getUpdatedAt().toString())
			.data(pubData)
			.build();
	}

	private static NotificationEventResponseDto createApplicationSubPayload(NotificationEvent e) {
		// String pubName = studentFacade.getStudentName(e.getPubId());
		// String subName = studentFacade.getStudentName(e.getSubId());
		// String pubTrack = studentFacade.getStudentTrack(e.getPubId());
		// String subTrack = teamFacade.getTeamTrack(e.getSubId());
		String pubName = "김싸피";
		String pubTrack = "웹기술";
		String subName = "팀 A";
		String subTrack = "웹기술";

		// 발신자 기본 정보
		NotificationIdentity pubIdentity = NotificationIdentity.builder()
			.id(e.getPubId())
			.name(pubName)
			.type(e.getPubType())
			.track(pubTrack)
			.build();

		// ② ApplicationPublisherData 빌더에 담기
		ApplicationPublisherData pubInfo = ApplicationPublisherData.builder()
			.notificationIdentity(pubIdentity)
			.isMajor(true)
			.position("BE")
			.build();

		// 수신자 정보
		NotificationIdentity subInfo = NotificationIdentity.builder()
			.id(e.getSubId())
			.name(subName)
			.type(e.getSubType())
			.build();

		// 수신자 추가 정보
		NotificationContent subMsg = NotificationContent.builder()
			.notificationTitle("e.getSubscriber().getNotificationTitle()")
			.notificationMessage("e.getSubscriber().getNotificationMessage()")
			.build();

		// data 페이로드 생성
		ReceiveApplicationResponseDto subData = ReceiveApplicationResponseDto.builder()
			.publisher(pubInfo)
			.subscriber(subInfo)

		// ③ 최종 응답 DTO
		return NotificationEventResponseDto.builder()
			.id(e.getNotificationId())
			.event(e.getClass().toString())
			.type(e.getType())         // APPLICATION 으로
			.time(e.getUpdatedAt().toString())
			.data(pubData)
			.build();
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
