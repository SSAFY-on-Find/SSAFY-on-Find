package com.sonfind.chelsea.factory;

import org.apache.coyote.BadRequestException;

import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.global.event.InvitationRequestEvent;
import com.sonfind.chelsea.types.NotificationType;

/**
 * Request 이벤트에 대한 Publisher/Subscriber용 Payload를 생성하는 팩토리 인터페이스.
 */
public interface RequestPayloadFactory {
	// 이벤트 타입에 따라 Publisher와 Subscriber의 Payload를 생성하는 메소드들(APPLICATION, INVITATION, MERGE)
	NotificationType supportType();

	// 발신자용 Payload
	NotificationDto<?> createPublisherPayload(
		InvitationRequestEvent e) throws BadRequestException;

	// 수신자용 Payload
	NotificationDto<?> createSubscriberPayload(
		InvitationRequestEvent e) throws
		BadRequestException;
}
