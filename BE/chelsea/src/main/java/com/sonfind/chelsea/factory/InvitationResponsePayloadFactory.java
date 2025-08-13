package com.sonfind.chelsea.factory;

import com.sonfind.chelsea.dto.notification.InvitationNotificationResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.global.event.InvitationResponseEvent;
import com.sonfind.chelsea.types.EventTargetType;
import com.sonfind.chelsea.types.NotificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvitationResponsePayloadFactory implements ResponsePayloadFactory {

	private final EventTargetType TYPE = EventTargetType.NOTIFICATION;

	@Override
	public NotificationStatus supportStatus(InvitationResponseEvent e) {
		return e.getStatus();
	}

	@Override
	public NotificationDto<?> createPayload(InvitationResponseEvent e) {
		InvitationNotificationResponseDto createResponse = InvitationNotificationResponseDto.builder()
				.pubId(e.getPubId())
				.pubType(e.getPubType())
				.subId(e.getSubId())
				.subType(e.getSubType())
				.updatedAt(e.getUpdatedAt().toString())
				.build();

		return NotificationDto.<InvitationNotificationResponseDto>builder()
				.id(e.getNotificationId().toHexString())
				.event(e.getType())
				.type(TYPE)
				.status(e.getStatus())
				.time(e.getUpdatedAt())
				.data(createResponse)
				.build();
	}
}
