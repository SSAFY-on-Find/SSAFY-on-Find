package com.sonfind.chelsea.factory;

import com.sonfind.chelsea.dto.notification.InvitationNotificationResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.global.event.InvitationResponseEvent;
import com.sonfind.chelsea.types.NotificationStatus;
import org.springframework.stereotype.Component;

@Component
public class InvitationResponsePayloadFactory implements ResponsePayloadFactory {
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
				.event(e.getClass().getName())
				.status(e.getStatus())
				.time(e.getUpdatedAt())
				.data(createResponse)
				.build();
	}
}
