package com.sonfind.chelsea.service;

import org.apache.coyote.BadRequestException;

import com.sonfind.chelsea.domain.notification.NotificationDocument;

public interface NotificationStatusService {
	/**
	 * 새 알림 생성 직후, PENDING 상태로 publisher + subscriber 상태를 저장
	 */
	void createInitialStatuses(NotificationDocument savedNotification);

	/**
	 * 수신자의 ACCEPT 동작
	 */
	void acceptInvitation(Long studentId, String statusId) throws BadRequestException;

	/**
	 * 발신자의 REJECT 동작
	 */
	void rejectInvitation(Long studentId, String statusId) throws BadRequestException;

	/**
	 * 발신자의 CANCEL 동작
	 */
	void cancelInvitation(Long studentId, String statusId) throws BadRequestException;
}
