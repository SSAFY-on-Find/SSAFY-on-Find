package com.sonfind.chelsea.global.event;

import org.springframework.context.ApplicationEvent;

import com.sonfind.chelsea.types.NotificationType;

import lombok.Getter;

@Getter
public class NotificationEvent extends ApplicationEvent {
	private final long pubId;
	private final String pubType;
	private final long subId;
	private final String subType;
	private final NotificationType type;   // APPLICATION, INVITATION, MERGE

	public NotificationEvent(Object source, long pubId, String pubType, long subId, String subType,
		NotificationType type) {
		super(source);
		this.pubId = pubId;
		this.pubType = pubType;
		this.subId = subId;
		this.subType = subType;
		this.type = type;
	}
}
