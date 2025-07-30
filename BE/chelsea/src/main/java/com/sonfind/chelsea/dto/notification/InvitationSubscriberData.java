package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvitationSubscriberDataBase extends BaseNotificationData {
	private Boolean isMajor;
	private String position;

	public InvitationSubscriberDataBase(Long id, String name, NotificationDomainType type, String track,
		String notificationTitle, String notificationMessage, Boolean isMajor, String position) {
		super(id, name, type, track, notificationTitle, notificationMessage);
		this.isMajor = isMajor;
		this.position = position;
	}
}
