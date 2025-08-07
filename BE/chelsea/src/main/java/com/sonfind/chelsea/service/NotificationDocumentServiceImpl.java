package com.sonfind.chelsea.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.dto.notification.NotificationContext;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.dto.notification.NotificationTypeInfo;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.types.RecipientRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDocumentServiceImpl implements NotificationDocumentService {

	private final StudentFacade studentFacade;
	private final NotificationContentService contentService;

	@Override
	public NotificationDocument buildBaseDocument(NotificationRequestDto dto, NotificationTypeInfo info) {
		Date now = getCurrentDate();

		return NotificationDocument.builder()
			.type(info.type())
			.publisherId(dto.pubId())
			.publisherType(info.pubType())
			.subscriberId(dto.subId())
			.subscriberType(info.subType())
			.createdAt(now)
			.updatedAt(now)
			// .groupId 는 아직 설정하지 않음 (statusService 에서 설정)
			.build();
	}

	@Override
	public NotificationDocument fillContent(NotificationDocument base) {
		// 1) 컨텍스트 준비 (publisher/subscriber 정보 조회)
		NotificationContext ctx = new NotificationContext(
			studentFacade.findByStudentIdForSse(base.getPublisherId()),
			studentFacade.findByStudentIdForSse(base.getSubscriberId())
		);

		// 2) 제목·메시지 생성
		String pubTitle = contentService.buildTitle(base, RecipientRole.PUBLISHER, ctx);
		String pubMsg = contentService.buildMessage(base, RecipientRole.PUBLISHER, ctx);
		String subTitle = contentService.buildTitle(base, RecipientRole.SUBSCRIBER, ctx);
		String subMsg = contentService.buildMessage(base, RecipientRole.SUBSCRIBER, ctx);

		// 3) 새로운 빌더로 복사 + 콘텐츠 주입
		return NotificationDocument.builder()
			.id(base.getId())                          // (만약 builder 가 id 지원하면)
			.groupId(base.getGroupId())
			.type(base.getType())
			.publisherId(base.getPublisherId())
			.publisherType(base.getPublisherType())
			.pubNotificationTitle(pubTitle)
			.pubNotificationMessage(pubMsg)
			.subscriberId(base.getSubscriberId())
			.subscriberType(base.getSubscriberType())
			.subNotificationTitle(subTitle)
			.subNotificationMessage(subMsg)
			.createdAt(base.getCreatedAt())
			.updatedAt(base.getUpdatedAt())
			.build();
	}

	/**
	 * 현재 날짜를 반환합니다.
	 * @return Date - 현재 날짜
	 */
	private Date getCurrentDate() {
		return new Date();
	}
}
