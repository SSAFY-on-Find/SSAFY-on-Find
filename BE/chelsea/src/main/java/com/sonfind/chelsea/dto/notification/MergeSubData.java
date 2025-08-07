package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

@Builder
public record MergeSubData(
	MergeTargetDto publisher,
	NotificationMsgDto subscriber
) implements HasPublisher, HasSubscriber {
}
