package com.sonfind.chelsea.service;

import com.sonfind.chelsea.dto.notification.NotificationRequestDto;

public interface NotificationCommandService {
	/**
	 * 새 알림 생성 & 발송
	 */
	void sendNotification(Long studentId, NotificationRequestDto dto);

	/**
	 * 초대/지원 수락
	 */
	void acceptInvitation(Long studentId, String notificationId);

	/**
	 * 초대/지원 거절
	 */
	void rejectInvitation(Long studentId, String notificationId);

	/**
	 * 초대/지원 취소
	 */
	void cancelInvitation(Long studentId, String notificationId);
}
