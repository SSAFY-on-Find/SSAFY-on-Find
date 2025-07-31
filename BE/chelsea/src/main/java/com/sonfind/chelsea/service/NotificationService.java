package com.sonfind.chelsea.service;

import java.util.Date;

import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.repository.NotificationRepository;
import com.sonfind.chelsea.repository.NotificationStatusRepository;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.NotificationType;
import com.sonfind.chelsea.types.RecipientRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepo;
	private final NotificationStatusRepository statusRepo;

	/**
	 * 알림을 저장
	 * 알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성하고,
	 * NotificationDocument 객체를 생성하여 MongoDB에 저장
	 * @param dto: NotificationRequestDto
	 */
	public NotificationDocument saveNotification(NotificationRequestDto dto) throws BadRequestException {
		// String → Enum 변환 (대소문자 구분이 있다면 toUpperCase() 등으로 맞춰주세요)
		NotificationType typeEnum =
			NotificationType.valueOf(dto.getType().toUpperCase());
		NotificationDomainType pubTypeEnum =
			NotificationDomainType.valueOf(dto.getPubType().toUpperCase());
		NotificationDomainType subTypeEnum =
			NotificationDomainType.valueOf(dto.getSubType().toUpperCase());

		Date now = new Date();
		// 이벤트 발행 시점의 현재 날짜를 가져옴
		Date currentDate = getCurrentDate();

		// 알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성
		NotificationDocument findNotificationLog = findLatestNotification(dto);
		// 해당 알림 중 PENDING 상태의 알림이 있는지 확인
		hasPendingStatus(findNotificationLog.getId());

		// NotificationDocument 객체를 생성
		NotificationDocument notificationLog = NotificationDocument.builder()
			.type(typeEnum)
			.publisherId(dto.getPubId())
			.publisherType(pubTypeEnum)
			.subscriberId(dto.getSubId())
			.subscriberType(subTypeEnum)
			.createdAt(now)
			.updatedAt(now)
			.build();

		// 알림을 저장
		NotificationDocument savedNotification = notificationRepo.save(notificationLog);
		log.info("알림이 저장되었습니다: {}", savedNotification.getId());

		// 알림 상태를 PENDING으로 저장
		NotificationStatusDocument pub = NotificationStatusDocument.builder()
			.notificationId(savedNotification.getId())
			.targetId(savedNotification.getPublisherId())
			.role(RecipientRole.PUBLISHER)
			.status(NotificationStatus.PENDING)
			.isRead(true)
			.readAt(currentDate)
			.notificationTitle("dto.getNotificationTitle()")
			.notificationMessage("dto.getNotificationMessage()")
			.createdAt(currentDate)
			.updatedAt(currentDate)
			.build();
	}

	/**
	 * 알림을 저장하기 전에, 가장 최근에 업데이트된 알림을 찾음
	 * @param dto
	 * @return NotificationDocument
	 * @throws BadRequestException
	 */
	private NotificationDocument findLatestNotification(NotificationRequestDto dto) throws BadRequestException {
		// 가장 최근에 업데이트된 알림을 찾음
		NotificationDocument lastUpdatedLog = notificationRepo.findLatest(
			dto.getPubId(),
			dto.getPubType(),
			dto.getSubId(),
			dto.getSubType()
		);

		if (lastUpdatedLog == null) {
			throw new BadRequestException("HttpStatus: " + HttpStatus.BAD_REQUEST + " | 알림을 찾을 수 없습니다.");
		}

		return lastUpdatedLog;
	}

	/**
	 * 알림이 이미 존재하는지 확인합니다.
	 * 알림이 존재하는 경우, 해당 알림의 상태가 PENDING인지 확인
	 * PENDING 상태인 경우, 알림을 저장하지 않고 false를 반환
	 * @param notificationId
	 * @return boolean
	 * @throws BadRequestException
	 */
	private void hasPendingStatus(ObjectId notificationId) throws BadRequestException {
		Boolean hasPending = statusRepo.existsByNotificationIdAndStatus(notificationId,
			NotificationStatus.PENDING);
		if (hasPending) {
			log.info("완료 처리되지 않은 알림이 이미 존재하며, 상태가 PENDING입니다. 알림을 저장하지 않습니다.");
			throw new BadRequestException("HttpStatus: " + HttpStatus.BAD_REQUEST + " | 알림이 이미 존재하며, 상태가 PENDING입니다.");
		}
	}

	private Date getCurrentDate() {
		return new Date();
	}
}
