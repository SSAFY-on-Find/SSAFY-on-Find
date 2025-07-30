package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InvitationPublisherDataBase implements BaseNotificationData {
	private Long id;
	private String name;
	private NotificationDomainType type;
	private String track;
	private String notificationTitle;
	private String notificationMessage;
}
