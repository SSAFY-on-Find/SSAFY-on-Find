package com.sonfind.chelsea.global.event;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEvent;

import com.sonfind.chelsea.types.NotificationStatus;

import lombok.Getter;

@Getter
public class InvitationResponseEvent extends ApplicationEvent {
	private final ObjectId notificationId;
	private final NotificationStatus status;
	private final Date updatedAt;

	public InvitationResponseEvent(
		Object source,
		ObjectId notificationId,
		NotificationStatus status,
		Date updatedAt
	) {
		super(source);
		this.notificationId = notificationId;
		this.status = status;
		this.updatedAt = updatedAt;
	}

	public static InvitationResponseEvent of(
		Object source,
		ObjectId notificationId,
		NotificationStatus status,
		Date updatedAt
	) {
		return new InvitationResponseEvent(source, notificationId, status, updatedAt);
	}
}
