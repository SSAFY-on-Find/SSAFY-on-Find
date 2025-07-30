package com.sonfind.chelsea.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReceiveApplicationResponseDto {
	private ApplicationSubscriberData subscriber;
	private ApplicationPublisherData publisher;
}
