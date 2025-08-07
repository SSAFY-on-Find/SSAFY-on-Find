package com.sonfind.chelsea.factory;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import com.sonfind.chelsea.dto.notification.InvitationNotificationResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.global.event.InvitationResponseEvent;
import com.sonfind.chelsea.types.NotificationStatus;

@Component
public class InvitationResponsePayloadFactory implements ResponsePayloadFactory {
	@Override
	public NotificationStatus supportStatus(InvitationResponseEvent e) {
		return e.getStatus();
	}

	@Override
	public NotificationDto<?> createPayload(InvitationResponseEvent e) throws BadRequestException {
		InvitationNotificationResponseDto createResponse = InvitationNotificationResponseDto.builder()
			.pubId(e.getPubId())
			.pubType(e.getPubType())
			.subId(e.getSubId())
			.subType(e.getSubType())
			.updatedAt(e.getUpdatedAt().toString())
			.build();

		return NotificationDto.<InvitationNotificationResponseDto>builder()
			.id(e.getNotificationId())
			.event(e.getClass().toString())
			.status(e.getStatus())
			.time(e.getUpdatedAt().toString())
			.data(createResponse)
			.build();
	}
}
