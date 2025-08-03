package com.sonfind.chelsea.global.event;

import java.util.Date;

import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEvent;

import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationType;

import lombok.Getter;

@Getter
@Builder
public class NotificationEvent extends ApplicationEvent {
	private final ObjectId notificationId;
	private final long pubId;
	private final NotificationDomainType pubType;
	private final long subId;
	private final NotificationDomainType subType;
	private final NotificationType type;   // APPLICATION, INVITATION, MERGE
	private final Date updatedAt;

	public NotificationEvent(Object source, ObjectId notificationId, long pubId, NotificationDomainType pubType, long subId,
		NotificationDomainType subType,
		Date updatedAt,
		NotificationType type) {
		super(source);
		this.notificationId = notificationId;
		this.pubId = pubId;
		this.pubType = pubType;
		this.subId = subId;
		this.subType = subType;
		this.type = type;
		this.updatedAt = updatedAt;
	}
}
