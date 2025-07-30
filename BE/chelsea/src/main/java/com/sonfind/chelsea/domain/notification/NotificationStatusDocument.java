package com.sonfind.chelsea.domain.notification;

import java.util.Date;

import com.sonfind.chelsea.types.NotificationDomainType;

import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.RecipientRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Notification 상태 정보를 저장하는 Document
 * @field id: NotificationStatusDocument의 고유 ID
 * @field notificationId: 참조하는 NotificationDocument의 ID
 * @field studentId: 발신자 또는 수신자의 ID
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
	private long id;

	@Field("notification_id")
	private long notificationId;

	@Field("target_id")
	private long targetId;

	private RecipientRole role;
	private NotificationStatus status;

	private boolean isRead;
	private Date readAt;

	private String notificationTitle;
	private String notificationMessage;

	private Date createdAt;
	private Date updatedAt;
}