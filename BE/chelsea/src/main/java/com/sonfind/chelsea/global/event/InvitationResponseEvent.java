package com.sonfind.chelsea.global.event;

import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.NotificationType;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEvent;

@Getter
public class InvitationResponseEvent extends ApplicationEvent {
	private final ObjectId notificationId;
	private final long pubId;
	private final NotificationDomainType pubType;
	private final long subId;
	private final NotificationDomainType subType;
	private final NotificationType type;
	private final NotificationStatus status;
	private final String updatedAt;

	public InvitationResponseEvent(
			Object source,
			ObjectId notificationId,
			long pubId,
			NotificationDomainType pubType,
			long subId,
			NotificationDomainType subType,
			NotificationType type,
			NotificationStatus status,
			String updatedAt
	) {
		super(source);
		this.notificationId = notificationId;
		this.pubId = pubId;
		this.pubType = pubType;
		this.subId = subId;
		this.subType = subType;
		this.type = type;
		this.status = status;
		this.updatedAt = updatedAt;
	}

	public static InvitationResponseEvent of(
			Object source,
			ObjectId notificationId,
			long pubId,
			NotificationDomainType pubType,
			long subId,
			NotificationDomainType subType,
			NotificationType type,
			NotificationStatus status,
			String updatedAt
	) {
		return new InvitationResponseEvent(source, notificationId, pubId, pubType, subId, subType, type, status, updatedAt);
	}
}
