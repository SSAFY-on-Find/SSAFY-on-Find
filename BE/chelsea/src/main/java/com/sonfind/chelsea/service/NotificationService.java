package com.sonfind.chelsea.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.notification.NotificationAndNotificationStatusResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationContext;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.dto.notification.NotificationResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationStatusResponseDto;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.event.NotificationEvent;
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
	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 알림 발송 메소드
	 * 알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성하고,
	 * NotificationDocument 객체를 생성하여 MongoDB에 저장 후 sse로 발송
	 * @param dto: NotificationRequestDto
	 */
	public void sendNotification(Long studentId, NotificationRequestDto dto) throws BadRequestException {
		if (dto.pubType().equals("team")) {
			List<Students> findTeamMembers = studentFacade.findAllByTeamId(dto.pubId());
			if (!findTeamMembers.contains(studentFacade.findByStudentId(studentId))) {
				throw new BadRequestException("HttpStatus: " + HttpStatus.BAD_REQUEST + " | 잘못된 요청입니다.");
			}
		} else {
			if (!dto.pubId().equals(studentId)) {
				throw new BadRequestException("HttpStatus: " + HttpStatus.BAD_REQUEST + " | 잘못된 요청입니다.");
			}
		}
		// String → Enum 변환
		NotificationType type =
			NotificationType.valueOf(dto.type().toUpperCase());
		NotificationDomainType pubType =
			NotificationDomainType.valueOf(dto.pubType().toUpperCase());
		NotificationDomainType subType =
			NotificationDomainType.valueOf(dto.subType().toUpperCase());

		// 이벤트 발행 시점의 현재 날짜를 가져옴
		Date now = getCurrentDate();

		// 알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성
		NotificationDocument findNotificationLog = findLatestNotification(dto);
		// 해당 알림 중 PENDING 상태의 알림이 있는지 확인
		hasPendingStatus(findNotificationLog.getId());

		NotificationDocument temp = NotificationDocument.builder()
			.type(type)
			.publisherId(dto.pubId())
			.publisherType(pubType)
			.subscriberId(dto.subId())
			.subscriberType(subType)
			.createdAt(now)
			.updatedAt(now)
			.build();

		// 2) 컨텍스트 생성
		NotificationContext ctx = new NotificationContext(
			studentFacade.findByStudentIdForSse(dto.pubId()),
			studentFacade.findByStudentIdForSse(dto.subId())
		);

		// 3) 발·수신자용 제목·메시지 미리 생성
		String pubTitle = contentService.buildTitle(temp, RecipientRole.PUBLISHER, ctx);
		String pubMsg = contentService.buildMessage(temp, RecipientRole.PUBLISHER, ctx);
		String subTitle = contentService.buildTitle(temp, RecipientRole.SUBSCRIBER, ctx);
		String subMsg = contentService.buildMessage(temp, RecipientRole.SUBSCRIBER, ctx);

		// 4) 실제 NotificationDocument에 제목·메시지 주입
		NotificationDocument notificationLog = NotificationDocument.builder()
			.type(type)
			.publisherId(dto.pubId())
			.publisherType(pubType)
			.pubNotificationTitle(pubTitle)
			.pubNotificationMessage(pubMsg)
			.subscriberId(dto.subId())
			.subscriberType(subType)
			.subNotificationTitle(subTitle)
			.subNotificationMessage(subMsg)
			.createdAt(now)
			.updatedAt(now)
			.build();

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

		// 알림 발송 이벤트를 발행
		NotificationEvent event = NotificationEvent.of(
			this,
			savedNotification.getId(),
			savedNotification.getPublisherId(),
			savedNotification.getPublisherType(),
			savedNotification.getSubscriberId(),
			savedNotification.getSubscriberType(),
			savedNotification.getUpdatedAt(),
			type
		);
		eventPublisher.publishEvent(event);
	}

	public NotificationResponseDto getNotificationInfo(ObjectId notificationId) throws
		BadRequestException {
		NotificationDocument findNotification = notificationRepo.findById(notificationId)
			.orElseThrow(() -> new BadRequestException(
				"HttpStatus: " + HttpStatus.BAD_REQUEST + " | 알림을 찾을 수 없습니다."
			));
		log.info("알림 조회 성공: {}", findNotification.getId());

		return NotificationResponseDto.builder()
			.type(findNotification.getType())
			.publisherId(findNotification.getPublisherId())
			.publisherType(findNotification.getPublisherType())
			.pubNotificationTitle(findNotification.getPubNotificationTitle())
			.pubNotificationMessage(findNotification.getPubNotificationMessage())
			.subscriberId(findNotification.getSubscriberId())
			.subscriberType(findNotification.getSubscriberType())
			.subNotificationTitle(findNotification.getSubNotificationTitle())
			.subNotificationMessage(findNotification.getSubNotificationMessage())
			.build();
	}

	/**
	 * 개인 또는 팀의 알림을 조회합니다.(알림 창)
	 * 알림은 발신자 또는 수신자의 역할에 따라 조회됩니다.
	 * @param studentId
	 * @param role
	 * @return
	 */
	public List<NotificationAndNotificationStatusResponseDto> getMyNotifications(Long studentId, String role) {
		RecipientRole recipientRole = RecipientRole.valueOf(role.toUpperCase());
		NotificationDomainType domain = NotificationDomainType.STUDENT;
		List<NotificationStatusDocument> findNotifications = statusRepo.findAllByTargetIdAndTargetTypeAndRole(studentId,
			domain, recipientRole);

		if (findNotifications.isEmpty()) {
			log.info("알림이 존재하지 않습니다. studentId: {}, role: {}", studentId, role);
			return Collections.singletonList(NotificationAndNotificationStatusResponseDto.builder()
				.notificationStatusList(List.of())
				.unReadCount(0)
				.build());
		}

		// 미읽음 개수 조회
		int unreadCount = statusRepo.countByTargetIdAndTargetTypeAndRoleAndIsReadFalse(
			studentId, domain, recipientRole
		);

		// 미읽음 상태를 읽음 처리하고 readAt 설정
		Date now = getCurrentDate();
		List<NotificationStatusDocument> toRead = findNotifications.stream()
			.filter(s -> !s.isRead())
			.peek(s -> {
				s.setRead(true);
				s.setReadAt(now);
			})
			.toList();
		if (!toRead.isEmpty()) {
			statusRepo.saveAll(toRead);
		}

		// 관련 NotificationDocument 일괄 조회
		List<ObjectId> notiIds = findNotifications.stream()
			.map(NotificationStatusDocument::getNotificationId)
			.distinct()
			.toList();
		List<NotificationDocument> docs = notificationRepo.findAllById(notiIds);
		Map<ObjectId, NotificationDocument> docMap = docs.stream()
			.collect(Collectors.toMap(NotificationDocument::getId, Function.identity()));

		// DTO 매핑
		List<NotificationStatusResponseDto> dtos = findNotifications.stream()
			.map(status -> {
				NotificationDocument doc = docMap.get(status.getNotificationId());
				return NotificationStatusResponseDto.builder()
					.statusId(status.getId())
					.notificationId(doc.getId())
					.targetId(status.getTargetId())
					.targetType(status.getTargetType())
					.role(status.getRole())
					.status(status.getStatus())
					.isRead(status.isRead())
					.updatedAt(status.getUpdatedAt().toString())
					.publisherId(doc.getPublisherId())
					.publisherType(doc.getPublisherType())
					.subscriberId(doc.getSubscriberId())
					.subscriberType(doc.getSubscriberType())
					.build();
			})
			.toList();

		return Collections.singletonList(NotificationAndNotificationStatusResponseDto.builder()
			.notificationStatusList(dtos)
			.unReadCount(unreadCount)
			.build());
	}

	/**
	 * 팀의 알림을 조회합니다.(팀 상세보기 페이지)
	 * @param teamId
	 * @param role
	 * @return
	 */
	public List<NotificationAndNotificationStatusResponseDto> getTeamNotifications(Long studentId, Long teamId,
		String role) throws
		BadRequestException {
		if (!studentFacade.isMemberOfTeam(studentId, teamId)) {
			log.info("학생이 팀의 멤버가 아닙니다. studentId: {}, teamId: {}", studentId, teamId);
			throw new BadRequestException("HttpStatus: " + HttpStatus.BAD_REQUEST + " | 잘못된 요청입니다.");
		}

		RecipientRole recipientRole = RecipientRole.valueOf(role.toUpperCase());
		NotificationDomainType domain = NotificationDomainType.TEAM;

		List<NotificationStatusDocument> findNotifications = statusRepo.findAllByTargetIdAndTargetTypeAndRole(teamId,
			domain, recipientRole);

		if (findNotifications.isEmpty()) {
			log.info("알림이 존재하지 않습니다. teamId: {}, role: {}", teamId, role);
			return Collections.singletonList(NotificationAndNotificationStatusResponseDto.builder()
				.notificationStatusList(List.of())
				.unReadCount(0)
				.build());
		}

		int unreadCount = statusRepo.countByTargetIdAndTargetTypeAndRoleAndIsReadFalse(
			studentId, domain, recipientRole
		);

		List<ObjectId> notiIds = findNotifications.stream()
			.map(NotificationStatusDocument::getNotificationId)
			.distinct()
			.toList();
		List<NotificationDocument> docs = notificationRepo.findAllById(notiIds);
		Map<ObjectId, NotificationDocument> docMap = docs.stream()
			.collect(Collectors.toMap(NotificationDocument::getId, Function.identity()));

		List<NotificationStatusResponseDto> dtos = findNotifications.stream()
			.map(status -> {
				NotificationDocument doc = docMap.get(status.getNotificationId());
				return NotificationStatusResponseDto.builder()
					.statusId(status.getId())
					.notificationId(doc.getId())
					.targetId(status.getTargetId())
					.targetType(status.getTargetType())
					.role(status.getRole())
					.status(status.getStatus())
					.isRead(status.isRead())
					.updatedAt(status.getUpdatedAt().toString())
					.publisherId(doc.getPublisherId())
					.publisherType(doc.getPublisherType())
					.subscriberId(doc.getSubscriberId())
					.subscriberType(doc.getSubscriberType())
					.build();
			})
			.toList();

		return Collections.singletonList(NotificationAndNotificationStatusResponseDto.builder()
			.notificationStatusList(dtos)
			.unReadCount(unreadCount)
			.build());
	}

	/**
	 * 학생의 읽지 않은 알림 개수를 조회합니다.
	 * @param studentId
	 * @return 읽지 않은 알림 개수
	 */
	public int getCountOfNonReadNotifications(Long studentId) {
		return statusRepo.countByTargetIdAndTargetTypeAndRoleAndIsReadFalse(
			studentId, NotificationDomainType.STUDENT, RecipientRole.SUBSCRIBER
		);
	}

	/**
	 * 알림을 수락합니다.
	 * 알림 상태를 PENDING에서 ACCEPTED로 변경하고, 읽음 상태를 true로 설정합니다.
	 * @param studentId
	 * @param statusId
	 */
	public void acceptInvitation(Long studentId, String statusId) throws BadRequestException {
		ObjectId ststusObjId = new ObjectId(statusId);
		Date now = getCurrentDate();

		// 1) 내 상태 조회·검증
		NotificationStatusDocument me = statusRepo.findById(ststusObjId)
			.orElseThrow(() -> new BadRequestException("잘못된 알림입니다."));
		/**
		 * TODO: 후순위 개발
		 */
	}

	/**
	 * 알림을 거절합니다.
	 * 알림 상태를 PENDING에서 REJECTED로 변경하고, 읽음 상태를 true로 설정합니다.
	 * @param studentId
	 * @param statusId
	 */
	public void rejectInvitation(Long studentId, String statusId) throws BadRequestException {
		ObjectId statusObjId = new ObjectId(statusId);
		Date now = getCurrentDate();

		// 1) 내 상태 조회·검증
		NotificationStatusDocument me = statusRepo.findById(statusObjId)
			.orElseThrow(() -> new BadRequestException("잘못된 알림입니다."));
		if (me.getRole() != RecipientRole.PUBLISHER ||
			!me.getTargetId().equals(studentId)) {
			throw new BadRequestException("알림 거절 권한이 없습니다.");
		}

		// 2) 내 상태만 먼저 변경
		me.setStatus(NotificationStatus.REJECTED);
		me.setUpdatedAt(now);
		statusRepo.save(me);

		// 3) 동일 notificationId를 가진 모든 상태 조회
		ObjectId notificationId = me.getNotificationId();
		List<NotificationStatusDocument> allStatuses =
			statusRepo.findByNotificationId(notificationId);

		// 4) 다른 상태들도 일괄 변경
		allStatuses.stream()
			.filter(s -> !s.getId().equals(statusObjId))
			.forEach(s -> {
				s.setStatus(NotificationStatus.REJECTED);
				s.setUpdatedAt(now);
			});
		statusRepo.saveAll(allStatuses);

		// 5) SSE 이벤트 발행
		NotificationDocument doc = notificationRepo.findById(notificationId)
			.orElseThrow(() -> new BadRequestException("알림 조회 실패"));

		/**
		 * TODO: 브로드 캐스트로 변경
		 */
		// publisher → subscriber
		eventPublisher.publishEvent(new NotificationEvent(
			this,
			notificationId,
			doc.getPublisherId(), doc.getPublisherType(),
			doc.getSubscriberId(), doc.getSubscriberType(),
			now,
			doc.getType()
		));

		log.info("초대/지원이 거절되었습니다: notificationId={}, statusId={}", notificationId, statusId);
	}

	/**
	 * 알림을 취소합니다.
	 * 알림 상태를 PENDING에서 CANCELED로 변경하고, 읽음 상태를 true로 설정합니다.
	 * @param studentId
	 * @param statusId
	 */
	public void cancelInvitation(Long studentId, String statusId) throws BadRequestException {
		ObjectId statusObjId = new ObjectId(statusId);
		Date now = getCurrentDate();

		// 1) 내 상태 조회·검증
		NotificationStatusDocument me = statusRepo.findById(statusObjId)
			.orElseThrow(() -> new BadRequestException("잘못된 알림입니다."));
		if (me.getRole() != RecipientRole.PUBLISHER ||
			!me.getTargetId().equals(studentId)) {
			throw new BadRequestException("알림 취소 권한이 없습니다.");
		}

		// 2) 내 상태만 먼저 변경
		me.setStatus(NotificationStatus.CANCELED);
		statusRepo.save(me);

		// 3) 동일 notificationId를 가진 모든 상태 조회
		ObjectId notificationId = me.getNotificationId();
		List<NotificationStatusDocument> allStatuses =
			statusRepo.findByNotificationId(notificationId);

		// 4) 다른 상태들도 일괄 변경
		allStatuses.stream()
			.filter(s -> !s.getId().equals(statusObjId))
			.forEach(s -> {
				s.setStatus(NotificationStatus.CANCELED);
				s.setUpdatedAt(now);
			});
		statusRepo.saveAll(allStatuses);

		// 5) SSE 이벤트 발행
		NotificationDocument doc = notificationRepo.findById(notificationId)
			.orElseThrow(() -> new BadRequestException("알림 조회 실패"));

		/**
		 * TODO: 브로드 캐스트로 변경
		 */
		// publisher → subscriber
		eventPublisher.publishEvent(new NotificationEvent(
			this,
			notificationId,
			doc.getPublisherId(), doc.getPublisherType(),
			doc.getSubscriberId(), doc.getSubscriberType(),
			now,
			doc.getType()
		));

		log.info("알림이 취소되었습니다: notificationId={}, statusId={}", notificationId, statusId);
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

		return NotificationStatusDocument.builder()
			.notificationId(notif.getId())
			.targetId(targetId)
			.targetType(role == RecipientRole.PUBLISHER
				? notif.getPublisherType()
				: notif.getSubscriberType())
			.role(role)
			.status(NotificationStatus.PENDING)
			.isRead(role == RecipientRole.PUBLISHER)
			.readAt(role == RecipientRole.PUBLISHER ? now : null)
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
			dto.pubId(),
			dto.pubType(),
			dto.subId(),
			dto.subType()
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

	public NotificationStatusDocument getNotificationStatus(ObjectId notificationId, Long studentId, RecipientRole role,
		NotificationStatus status
	) throws BadRequestException {
		NotificationStatusDocument findStatus = statusRepo.findByNotificationIdAndTargetIdAndRoleAndStatus(
			notificationId, studentId, role, status
		);

		if (findStatus == null) {
			log.info("알림 상태가 존재하지 않습니다: {}, {}, {}", notificationId, studentId, role);
			throw new BadRequestException("HttpStatus: " + HttpStatus.BAD_REQUEST + " | 알림 상태가 존재하지 않습니다.");
		}

		log.info("알림 상태 조회 성공: {}, {}, {}", notificationId, studentId, role);
		return findStatus;
	}

	/**
	 * 현재 날짜를 반환합니다.
	 * @return Date - 현재 날짜
	 */
	private Date getCurrentDate() {
		return new Date();
	}
}
