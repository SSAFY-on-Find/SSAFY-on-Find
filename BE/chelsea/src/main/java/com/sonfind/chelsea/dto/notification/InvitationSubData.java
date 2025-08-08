package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

@Builder
public record InvitationSubData(
	ParticipantDto publisher,
	NotificationMsgDto subscriber
) implements HasPublisher, HasSubscriber {
}
