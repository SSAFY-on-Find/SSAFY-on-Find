package com.sonfind.chelsea.service;

import java.util.Date;

import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.dto.notification.NotificationContext;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.facade.StudentFacade;
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
	private final StudentFacade studentFacade;
	private final NotificationContentService contentService;

	/**
	 * 알림을 저장
	 * 알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성하고,
	 * NotificationDocument 객체를 생성하여 MongoDB에 저장
	 * @param dto: NotificationRequestDto
	 */
	public NotificationDocument saveNotification(NotificationRequestDto dto) throws BadRequestException {
		// String → Enum 변환 (대소문자 구분이 있다면 toUpperCase() 등으로 맞춰주세요)
		NotificationType type =
			NotificationType.valueOf(dto.getType().toUpperCase());
		NotificationDomainType pubType =
			NotificationDomainType.valueOf(dto.getPubType().toUpperCase());
		NotificationDomainType subType =
			NotificationDomainType.valueOf(dto.getSubType().toUpperCase());

		// 이벤트 발행 시점의 현재 날짜를 가져옴
		Date now = getCurrentDate();

		// 알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성
		NotificationDocument findNotificationLog = findLatestNotification(dto);
		// 해당 알림 중 PENDING 상태의 알림이 있는지 확인
		hasPendingStatus(findNotificationLog.getId());

		// NotificationDocument 객체를 생성
		NotificationDocument notificationLog = NotificationDocument.builder()
			.type(type)
			.publisherId(dto.getPubId())
			.publisherType(pubType)
			.subscriberId(dto.getSubId())
			.subscriberType(subType)
			.createdAt(now)
			.updatedAt(now)
			.build();

		// 알림을 저장
		NotificationDocument savedNotification = notificationRepo.save(notificationLog);
		log.info("알림이 저장되었습니다: {}", savedNotification.getId());

		switch (type) {
			case APPLICATION, INVITATION, MERGE -> {
				// 발신자(팀 or 개인) → createStatus 내부에서 deriveTargetIds로 팀원 브로드캐스트도 처리
				NotificationStatusDocument pub = createStatus(savedNotification, now, RecipientRole.PUBLISHER);
				// 수신자(팀 or 개인)
				NotificationStatusDocument sub = createStatus(savedNotification, now, RecipientRole.SUBSCRIBER);

				statusRepo.save(pub);
				savedNotification.setGroupId(pub.getId());
				savedNotification = notificationRepo.save(savedNotification);
				statusRepo.save(sub);
			}
			default -> throw new IllegalArgumentException("지원하지 않는 NotificationType: " + type);
		}

		log.info("알림 상태가 저장되었습니다: {}, {}", savedNotification.getId(), type);

		return savedNotification;
	}

	/**
	 * 알림 상태를 생성합니다.
	 * 발신자와 수신자의 역할에 따라 알림 상태를 설정하고,
	 * 알림 제목과 메시지를 생성합니다.
	 * 발신자의 경우 읽음 상태로 설정하고, 읽은 시각을 현재 날짜로 설정합니다.
	 * 수신자의 경우 읽음 상태는 false로 설정하고,
	 * 읽은 시각은 null로 설정합니다.
	 * @param notif: NotificationDocument - 알림  문서 객체
	 * @param now: Date - 현재 날짜
	 * @param role: RecipientRole - 알림 발신자 또는 수신자의 역할 (PUBLISHER 또는 SUBSCRIBER)
	 * @return NotificationStatusDocument -알림 상태 문서 객체
	 */
	private NotificationStatusDocument createStatus(
		NotificationDocument notif,
		Date now,
		RecipientRole role
	) {
		long targetId = (role == RecipientRole.PUBLISHER)
			? notif.getPublisherId()
			: notif.getSubscriberId();

		NotificationContext ctx = new NotificationContext(
			studentFacade.findByStudentIdForSse(notif.getPublisherId()),
			studentFacade.findByStudentIdForSse(notif.getSubscriberId())
		);

		// 제목/메시지 생성 로직을 분리: type + role 조합에 따라 다른 처리
		String title = contentService.buildTitle(notif, role, ctx);
		String message = contentService.buildMessage(notif, role, ctx);

		return NotificationStatusDocument.builder()
			.notificationId(notif.getId())
			.targetId(targetId)
			.role(role)
			.status(NotificationStatus.PENDING)
			.isRead(role == RecipientRole.PUBLISHER)
			.readAt(role == RecipientRole.PUBLISHER ? now : null)
			.notificationTitle(title)
			.notificationMessage(message)
			.createdAt(now)
			.updatedAt(now)
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

	/**
	 * 현재 날짜를 반환합니다.
	 * @return Date - 현재 날짜
	 */
	private Date getCurrentDate() {
		return new Date();
	}
}
