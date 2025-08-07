package com.sonfind.chelsea.factory;

import org.apache.coyote.BadRequestException;

import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.global.event.InvitationResponseEvent;
import com.sonfind.chelsea.types.NotificationStatus;

public interface ResponsePayloadFactory {
	NotificationStatus supportStatus(InvitationResponseEvent e);

	NotificationDto<?> createPayload(InvitationResponseEvent e) throws BadRequestException;
}
