package com.sonfind.chelsea.domain.notification;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.RecipientRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Notification 상태 정보를 저장하는 Document
 * @field id: NotificationStatusDocument의 고유 ID
 * @field notificationId: 참조하는 NotificationDocument의 ID
 * @field targetId: 알림 대상 ID (발신자 또는 수신자 ID)
 * @field role: 알림 발신/수신 역할 (eg. PUBLISHER, SUBSCRIBER)
 * @field status: 알림 상태 (eg. PENDING, ACCEPTED 등)
 * @field isRead: 읽음 여부
 * @field readAt: 읽은 시각
 * @field notificationTitle: 사용자별로 다른 알림 제목
 * @field notificationMessage: 사용자별로 다른 알림 메시지
 * @field createdAt: Document 생성 시각
 * @field updatedAt: Document 업데이트 시각
 *
 */
@Document(collection = "notification_statuses")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationStatusDocument {
	@Id
	private ObjectId id;

	private ObjectId notificationId;

	private Long targetId;
	private NotificationDomainType targetType;

	private RecipientRole role;
	@Setter
	private NotificationStatus status;

	@Setter
	private boolean isRead;
	@Setter
	private Date readAt;

	private Date createdAt;
	@Setter
	private Date updatedAt;
}
