package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

@Builder
public record ApplicationSubData(
	ApplicantDto publisher,
	NotificationMsgDto subscriber
) implements HasPublisher, HasSubscriber {
}
