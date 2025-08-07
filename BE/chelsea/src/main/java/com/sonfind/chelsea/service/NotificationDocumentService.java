package com.sonfind.chelsea.service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.dto.notification.NotificationTypeInfo;

public interface NotificationDocumentService {
	/**
	 * 기본 NotificationDocument 틀만 생성 (제목·메시지는 빈 상태)
	 */
	NotificationDocument buildBaseDocument(NotificationRequestDto dto, NotificationTypeInfo info);

	/**
	 * buildBaseDocument 로 만든 도큐먼트에
	 * 제목·메시지를 채워서 최종 저장용 도큐먼트로 완성
	 */
	NotificationDocument fillContent(NotificationDocument base);
}
