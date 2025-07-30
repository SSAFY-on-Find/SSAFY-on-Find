package com.sonfind.chelsea.domain.notification;

import java.util.Date;

import com.sonfind.chelsea.types.NotificationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.sonfind.chelsea.types.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Notification 관련 불변 정보를 저장하는 Document
 * @field id: NotificationDocument의 고유 ID
 * @field groupId: 같은 이벤트 묶음 ID
 * @field type: 알림 종류 (예: Application, Invitation 등)
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
	private long id;

	private Long groupId;                 // 같은 이벤트 묶음
	private NotificationType type;        // INVITE, MESSAGE 등
	private Date createdAt;
	private Date updatedAt;
}
