package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationPublisherDataBase extends BaseNotificationData {
	
	public ApplicationPublisherData(Long id, String name, NotificationDomainType type, String track,
		String notificationTitle,
		String notificationMessage) {
		super(id, name, type, track, notificationTitle, notificationMessage);
	}
}
