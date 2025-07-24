package com.sonfind.chelsea.domain.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

/**
 * Notification Document의 하위 객체
 * 알림 송/수신자에 대한 정보를 저장하는 객체
 *
 * @field id: pubId 혹은 subId
 * @field type: pubType 혹은 subType -> ["TEAM", "MATE"]
 * @field notificationTitle: 알람 발송 제목(eg. 김싸피님의 지원)
 * @field notificationMessage: 알람 발신 메시지(eg. 김싸피(BE, 전공)님이 팀A에 합류를 요청했습니다.)
 */
public class NotificationParticipant {
	private long id;

	private NotificationDomainType type;

	private String notificationTitle;

	private String notificationMessage;
}
