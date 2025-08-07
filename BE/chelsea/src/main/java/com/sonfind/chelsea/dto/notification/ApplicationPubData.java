package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

@Builder
public record ApplicationPubData(
	NotificationMsgDto publisher
	//        , ParticipantDto subscriber
) implements HasPublisher {
}
