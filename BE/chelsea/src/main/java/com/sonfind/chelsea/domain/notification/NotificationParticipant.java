package com.sonfind.chelsea.domain.notification;

import java.util.Date;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Notification Document의 하위 객체
 * 알림 송/수신자에 대한 정보를 저장하는 객체
 *
 * @field id: pubId 혹은 subId
 * @field type: pubType 혹은 subType -> ["TEAM", "MATE"]
 * @field isRead: 알림 읽음 여부
 * @field readAt: 알림 읽음 시간(isRead가 true일 때만 값이 존재)(eg. 2023-10-01T12:00:00Z)
 * @field notificationTitle: 알람 발송 제목(eg. 김싸피님의 지원)
 * @field notificationMessage: 알람 발신 메시지(eg. 김싸피(BE, 전공)님이 팀A에 합류를 요청했습니다.)
 */
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class NotificationParticipant {
	private final long id;

	private final NotificationDomainType type;

	@Setter
	private boolean isRead;

	@Setter
	private Date readAt;

	@Setter
	private String notificationTitle;

	@Setter
	private String notificationMessage;
}
