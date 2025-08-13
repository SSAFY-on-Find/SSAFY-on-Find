package com.sonfind.chelsea.global.event;

import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationType;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEvent;

@Getter
public class InvitationRequestEvent extends ApplicationEvent {
	private final ObjectId notificationId;
	private final long pubId;
	private final NotificationDomainType pubType;
	private final long subId;
	private final NotificationDomainType subType;
	private final NotificationType type;   // APPLICATION, INVITATION, MERGE
	private final String updatedAt;

	public InvitationRequestEvent(
			Object source,
			ObjectId notificationId,
			long pubId,
			NotificationDomainType pubType,
			long subId,
			NotificationDomainType subType,
			String updatedAt,
			NotificationType type    // APPLICATION, INVITATION, MERGE
	) {
		super(source);
		this.notificationId = notificationId;
		this.pubId = pubId;
		this.pubType = pubType;
		this.subId = subId;
		this.subType = subType;
		this.type = type;
		this.updatedAt = updatedAt;
	}

	public static InvitationRequestEvent of(
			Object source,
			ObjectId notificationId,
			long pubId,
			NotificationDomainType pubType,
			long subId,
			NotificationDomainType subType,
			String updatedAt,
			NotificationType type
	) {
		return new InvitationRequestEvent(source, notificationId, pubId, pubType, subId, subType, updatedAt, type);
	}
}
