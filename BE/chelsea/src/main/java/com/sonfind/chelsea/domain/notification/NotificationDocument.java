package com.sonfind.chelsea.domain.notification;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.sonfind.chelsea.types.NotificationStatus;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Notification 관련 정보를 저장하는 컬렉션
 *
 * @field id: 알림 ID(PK - notification_id)
 * @field publisher: 알림 발신자 정보(NotificationParticipant)
 * @field subscriber: 알림 수신자 정보(NotificationParticipant)
 * @field status: 알림 상태(PENDING, ACCEPTED, REJECTED, CANCELLED)
 * @field createdAt: 알림 생성 시간
 * @field updatedAt: 알림 수정 시간
 */
@Document(collection = "Notifications")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDocument {
	@Id
	@Field("notification_id")
	private long id;

	@Field("notification_publisher")
	private NotificationParticipant publisher;
	@Field("notification_subscriber")
	private NotificationParticipant subscriber;

	@Field("notification_status")
	private NotificationStatus status;

	private Date createdAt;
	private Date updatedAt;
}
