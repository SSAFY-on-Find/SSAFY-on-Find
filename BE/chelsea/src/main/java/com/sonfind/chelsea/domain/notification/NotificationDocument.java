package com.sonfind.chelsea.domain.notification;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Notification 관련 불변 정보를 저장하는 Document
 * @field id: NotificationDocument의 고유 ID
 * @field groupId: 같은 이벤트 묶음 ID
 * @field type: 알림 종류 (예: Application, Invitation 등)
 * @field publisherId: 알림 발신자 ID
 * @field publisherType: 알림 발신자 도메인 타입 (예: STUDENT, TEAM )
 * @field subscriberId: 알림 수신자 ID
 * @field subscriberType: 알림 수신자 도메인 타입 (예: STUDENT, TEAM )
 * @field createdAt: 알림 생성 시간
 * @field updatedAt: 알림 정보 업데이트 시간
 */
@Document(collection = "notifications")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDocument {
	@Id
	private ObjectId id;

	@Setter
	private ObjectId groupId;
	private NotificationType type;

	private long publisherId;
	private NotificationDomainType publisherType;
	private String pubNotificationTitle;
	private String pubNotificationMessage;

	private long subscriberId;
	private NotificationDomainType subscriberType;
	private String subNotificationTitle;
	private String subNotificationMessage;

	private Date createdAt;
	private Date updatedAt;
}
