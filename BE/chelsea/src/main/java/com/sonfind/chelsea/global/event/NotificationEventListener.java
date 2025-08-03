package com.sonfind.chelsea.global.event;

import com.sonfind.chelsea.dto.notification.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sonfind.chelsea.service.SseService;

import lombok.RequiredArgsConstructor;

/**
 * 알림 이벤트 리스너
 * NotificationEvent를 수신하여 알림을 발송하는 역할을 합니다.
 * 이벤트 타입에 따라 발신자와 수신자에게 알림을 전송합니다.
 */
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

	private final SseService sseService;

	/**
	 * 알림 이벤트 리스너
	 * 이벤트 타입에 따라 발신자와 수신자에게 알림을 전송합니다.
	 */
	@EventListener
	public void onNotification(NotificationEvent e) {
			switch (e.getType()) {
					case APPLICATION -> {
							NotificationDto<ApplicationPubData> pubPayload = createApplicationPubPayload(e);
							NotificationDto<ApplicationSubData> subPayload = createApplicationSubPayload(e);
							sseService.sendNotification(e.getPubId(), pubPayload);
							sseService.sendNotification(e.getSubId(), subPayload);
					}
					case INVITATION -> {
							NotificationDto<InvitationPubData> pubPayload = createInvitationPubPayload(e);
							NotificationDto<InvitationSubData> subPayload = createInvitationSubPayload(e);
							sseService.sendNotification(e.getPubId(), pubPayload);
							sseService.sendNotification(e.getSubId(), subPayload);
					}
					case MERGE -> {
							NotificationDto<MergePubData> pubPayload = createMergePubPayload(e);
							NotificationDto<MergeSubData> subPayload = createMergeSubPayload(e);
							sseService.sendNotification(e.getPubId(), pubPayload);
							sseService.sendNotification(e.getSubId(), subPayload);
					}
			}
	}

	// APPLICATION(개인 -> 팀 지원) - 발신자(개인)에게 알림 전송
	private static NotificationDto<ApplicationPubData> createApplicationPubPayload(NotificationEvent e) {
			String pubName = "김싸피";

			String subName = "팀 A";
			String subTrack = "웹기술";

			// 발신자 정보
			NotificationMsgDto pub = NotificationMsgDto.builder()
						.id(e.getPubId())
						.name(pubName)
						.type(e.getPubType())
						.notificationTitle("e.getPublisher().getNotificationTitle()")
						.notificationMessage("e.getPublisher().getNotificationMessage()")
						.targetId(e.getSubId())
						.build();

			// 이벤트 페이로드
			ApplicationPubData payload = ApplicationPubData.builder()
						.publisher(pub)
						.build();

			// 최종 응답 DTO
			return NotificationDto.<ApplicationPubData>builder()
						.id(e.getNotificationId())
						.event(e.getClass().toString())
						.type(e.getType())         // APPLICATION 으로
						.time(e.getUpdatedAt().toString())
						.data(payload)
						.build();
	}

	// APPLICATION(개인 -> 팀 지원) - 수신자(팀)에게 알림 전송
	private static NotificationDto<ApplicationSubData> createApplicationSubPayload(NotificationEvent e) {
			String pubName = "김싸피";
			String pubTrack = "웹기술";
			Boolean pubIsMajor = true;
			String pubPosition = "BE";

			String subName = "팀 A";
			String subTrack = "웹기술";

			// 발신자 정보
			ApplicantDto pub = ApplicantDto.builder()
						.id(e.getPubId())
						.name(pubName)
						.type(e.getPubType())
						.track(pubTrack)
						.isMajor(pubIsMajor)
						.position(pubPosition)
						.build();

			// 수신자 정보
			NotificationMsgDto sub = NotificationMsgDto.builder()
						.id(e.getSubId())
						.name(subName)
						.type(e.getSubType())
						.notificationTitle("e.getSubscriber().getNotificationTitle()")
						.notificationMessage("e.getSubscriber().getNotificationMessage()")
						.build();

			// 이벤트 페이로드
			ApplicationSubData payload = ApplicationSubData.builder()
						.publisher(pub)
						.subscriber(sub)
						.build();

			// 최종 응답 DTO
			return NotificationDto.<ApplicationSubData>builder()
						.id(e.getNotificationId())
						.event(e.getClass().toString())
						.type(e.getType())         // APPLICATION 으로
						.time(e.getUpdatedAt().toString())
						.data(payload)
						.build();
	}

	// INVITATION(팀 -> 개인 초대) - 발신자(팀)에게 알림 전송
	private static NotificationDto<InvitationPubData> createInvitationPubPayload(NotificationEvent e) {
			String pubName = "김싸피";
			String pubTrack = "웹기술";
			Boolean pubIsMajor = true;
			String pubPosition = "BE";

			String subName = "김싸피";
			String subTrack = "웹기술";

			// 발신자 정보
			NotificationMsgDto pub = NotificationMsgDto.builder()
						.id(e.getPubId())
						.name(pubName)
						.type(e.getPubType())
						.track(pubTrack)
						.notificationTitle("e.getPublisher().getNotificationTitle()")
						.notificationMessage("e.getPublisher().getNotificationMessage()")
						.targetId(e.getSubId())
						.build();

			// 수신자 정보
			ApplicantDto sub = ApplicantDto.builder()
						.id(e.getSubId())
						.name(subName)
						.type(e.getSubType())
						.track(subTrack)
						.isMajor(pubIsMajor)
						.position(pubPosition)
						.build();

			// 이벤트 페이로드
			InvitationPubData payload = InvitationPubData.builder()
						.publisher(pub)
						.subscriber(sub)
						.build();

			// 최종 응답 DTO
			return NotificationDto.<InvitationPubData>builder()
						.id(e.getNotificationId())
						.event(e.getClass().toString())
						.type(e.getType())         // INVITATION 으로
						.time(e.getUpdatedAt().toString())
						.data(payload)
						.build();
	}

	// INVITATION(팀 -> 개인 초대) - 수신자(개인)에게 알림 전송
	private static NotificationDto<InvitationSubData> createInvitationSubPayload(NotificationEvent e) {
			String pubName = "김싸피";
			String pubTrack = "웹기술";
			Boolean pubIsMajor = true;
			String pubPosition = "BE";

			String subName = "김싸피";
			String subTrack = "웹기술";

			// 발신자 정보
			ParticipantDto pub = ParticipantDto.builder()
						.id(e.getPubId())
						.name(pubName)
						.type(e.getPubType())
						.track(pubTrack)
						.build();

			// 수신자 정보
			NotificationMsgDto sub = NotificationMsgDto.builder()
						.id(e.getSubId())
						.name(subName)
						.type(e.getSubType())
						.notificationTitle("e.getSubscriber().getNotificationTitle()")
						.notificationMessage("e.getSubscriber().getNotificationMessage()")
						.build();

			// 이벤트 페이로드
			InvitationSubData payload = InvitationSubData.builder()
						.publisher(pub)
						.subscriber(sub)
						.build();

			// 최종 응답 DTO
			return NotificationDto.<InvitationSubData>builder()
						.id(e.getNotificationId())
						.event(e.getClass().toString())
						.type(e.getType())         // INVITATION 으로
						.time(e.getUpdatedAt().toString())
						.data(payload)
						.build();
	}

	// MERGE(팀 합치기) - 발신자(팀)에게 알림 전송
	private static NotificationDto<MergePubData> createMergePubPayload(NotificationEvent e) {
			String pubName = "김싸피";
			String pubTrack = "웹기술";
			Boolean pubIsMajor = true;
			String pubPosition = "BE";

			String subName = "김싸피";
			String subTrack = "웹기술";

			// 발신자 정보
			NotificationMsgDto pub = NotificationMsgDto.builder()
					.id(e.getPubId())
					.name(pubName)
					.type(e.getPubType())
					.track(pubTrack)
					.notificationTitle("e.getPublisher().getNotificationTitle()")
					.notificationMessage("e.getPublisher().getNotificationMessage()")
					.targetId(e.getSubId())
					.build();

			// 수신자 정보
			MergeTargetDto sub = MergeTargetDto.builder()
						.id(e.getSubId())
						.name(subName)
						.type(e.getSubType())
						.track(subTrack)
						.memberCount(4)
						.major(2)
						.nonMajor(2)
						.build();

			// 이벤트 페이로드
			MergePubData payload = MergePubData.builder()
						.publisher(pub)
						.subscriber(sub)
						.build();

			// 최종 응답 DTO
			return NotificationDto.<MergePubData>builder()
						.id(e.getNotificationId())
						.event(e.getClass().toString())
						.type(e.getType())         // MERGE 으로
						.time(e.getUpdatedAt().toString())
						.data(payload)
						.build();
	}

	// MERGE(팀 합치기) - 수신자(팀)에게 알림 전송
	private static NotificationDto<MergeSubData> createMergeSubPayload(NotificationEvent e) {
			String pubName = "김싸피";
			String pubTrack = "웹기술";
			Boolean pubIsMajor = true;
			String pubPosition = "BE";

			String subName = "김싸피";
			String subTrack = "웹기술";

			// 발신자 정보
			MergeTargetDto pub = MergeTargetDto.builder()
						.id(e.getPubId())
						.name(pubName)
						.type(e.getPubType())
						.track(pubTrack)
						.memberCount(4)
						.major(2)
						.nonMajor(2)
						.build();

			// 수신자 정보
			NotificationMsgDto sub = NotificationMsgDto.builder()
						.id(e.getSubId())
						.name(subName)
						.type(e.getSubType())
						.notificationTitle("e.getSubscriber().getNotificationTitle()")
						.notificationMessage("e.getSubscriber().getNotificationMessage()")
						.build();


			// 이벤트 페이로드
			MergeSubData payload = MergeSubData.builder()
						.publisher(pub)
						.subscriber(sub)
						.build();

			// 최종 응답 DTO
			return NotificationDto.<MergeSubData>builder()
						.id(e.getNotificationId())
						.event(e.getClass().toString())
						.type(e.getType())         // MERGE 으로
						.time(e.getUpdatedAt().toString())
						.data(payload)
						.build();
	}
}
