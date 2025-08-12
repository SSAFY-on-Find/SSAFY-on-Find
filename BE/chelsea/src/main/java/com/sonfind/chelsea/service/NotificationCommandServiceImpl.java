package com.sonfind.chelsea.service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.dto.notification.NotificationResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationTypeInfo;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import com.sonfind.chelsea.global.event.InvitationRequestEvent;
import com.sonfind.chelsea.global.event.InvitationResponseEvent;
import com.sonfind.chelsea.repository.NotificationRepository;
import com.sonfind.chelsea.repository.NotificationStatusRepository;
import com.sonfind.chelsea.service.validator.NotificationValidator;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.RecipientRole;
import com.sonfind.chelsea.util.NotificationTypeConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {
	private final NotificationRepository notificationRepo;
	private final NotificationStatusRepository statusRepo;
	private final NotificationValidator notificationValidator;
	private final NotificationTypeConverter typeConverter;
	private final ApplicationEventPublisher eventPublisher;
	private final NotificationDocumentService documentService;
	private final TeamService teamService;
	private final NotificationQueryService notificationQueryService;
	private final StudentFacade studentFacade;

	@Override
	public void sendNotification(Long studentId, NotificationRequestDto dto) {
		// 발신자 권한 검증
		notificationValidator.validatePublisher(studentId, dto);

		// String → Enum 변환
		NotificationTypeInfo info = typeConverter.convert(dto);

		// 이벤트 발행 시점의 현재 날짜를 가져옴
		Date now = getCurrentDate();

		// 알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성
		NotificationDocument findNotificationLog = findLatestNotification(dto);
		if (findNotificationLog == null) {

		} else {
			// 해당 알림 중 PENDING 상태의 알림이 있는지 확인
			hasPendingStatus(findNotificationLog.getId());
		}


		log.info("알림 발신자 정보: {}, {}", info.pubType(), info.subType());
		NotificationDocument temp = documentService.buildBaseDocument(dto, info);
		log.info("알림 문서 생성: {}", temp);

		NotificationDocument savedNotification = notificationRepo.save(documentService.fillContent(temp));
		log.info("알림이 저장되었습니다: {}", savedNotification.getId());

		switch (info.type()) {
			case APPLICATION, INVITATION, MERGE -> {
				// 발신자 상태들
				List<NotificationStatusDocument> pubList = createStatuses(savedNotification, now, RecipientRole.PUBLISHER);
				log.info("발신자 상태들 생성: {}", pubList);
				List<NotificationStatusDocument> savedPubList = statusRepo.saveAll(pubList);
				log.info("발신자 상태들이 저장되었습니다: {}", savedPubList);

				// groupId 세팅: 발신자 상태 중 첫 번째 id 사용(추후 별도 키 필요시 변경 가능)
				if (!savedPubList.isEmpty()) {
					savedNotification.setGroupId(savedPubList.get(0).getId());
					log.info("알림 그룹 ID가 설정되었습니다: {}", savedNotification.getGroupId());
					savedNotification = notificationRepo.save(savedNotification);
				}

				// 수신자 상태들
				List<NotificationStatusDocument> subList = createStatuses(savedNotification, now, RecipientRole.SUBSCRIBER);
				log.info("수신자 상태들 생성: {}", subList);
				statusRepo.saveAll(subList);
			}
			default -> throw new AppException(ErrorCode.NOTIFICATION_TYPE_NOT_SUPPORTED);
		}

		log.info("알림 상태가 저장되었습니다: {}, {}", savedNotification.getId(), info.type());

		// 알림 발송 이벤트를 발행
		InvitationRequestEvent event = InvitationRequestEvent.of(
				this,
				savedNotification.getId(),
				savedNotification.getPublisherId(),
				savedNotification.getPublisherType(),
				savedNotification.getSubscriberId(),
				savedNotification.getSubscriberType(),
				savedNotification.getUpdatedAt(),
				savedNotification.getType()
		);

		eventPublisher.publishEvent(event);
	}

	@Override
	public void acceptInvitation(Long studentId, String notificationId) {
		ObjectId objId = new ObjectId(notificationId);

		Date now = getCurrentDate();

		// 1) 내 상태 조회·검증
		NotificationStatusDocument me = statusRepo.findByNotificationIdAndTargetIdAndRoleAndStatus(objId, studentId, RecipientRole.SUBSCRIBER, NotificationStatus.PENDING);

		checkNotificationStatusDocumentNotNull(studentId, notificationId, me);


		// 2) 알림 정보 조회
		NotificationResponseDto findNotification = notificationQueryService.getNotificationInfo(objId);

		exeMergeOrAddMemberAtTeam(studentId, findNotification);

		// 3) 내 상태만 먼저 변경
		me.setStatus(NotificationStatus.ACCEPTED);
		me.setUpdatedAt(now);
		statusRepo.save(me);

		// 4) 동일 notificationId를 가진 모든 상태 조회
		List<NotificationStatusDocument> allStatuses =
				statusRepo.findByNotificationId(objId);

		// 5) 다른 상태들도 일괄 변경
		allStatuses.stream()
				.filter(s -> !s.getId().equals(objId))
				.forEach(s -> {
					s.setStatus(NotificationStatus.ACCEPTED);
					s.setUpdatedAt(now);
				});
		statusRepo.saveAll(allStatuses);

		// 6) SSE 이벤트 발행
		NotificationDocument doc = notificationRepo.findById(objId)
				.orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));


		eventPublisher.publishEvent(InvitationResponseEvent.of(
				this,
				objId,
				doc.getPublisherId(), doc.getPublisherType(),
				doc.getSubscriberId(), doc.getSubscriberType(),
				NotificationStatus.ACCEPTED,
				now
		));

		log.info("초대/지원이 수락되었습니다: notificationId={}", objId);
	}

	@Override
	public void rejectInvitation(Long studentId, String notificationId) {
		ObjectId objId = new ObjectId(notificationId);
		Date now = getCurrentDate();

		// 1) 내 상태 조회·검증
		NotificationStatusDocument me = statusRepo.findByNotificationIdAndTargetIdAndRoleAndStatus(objId, studentId, RecipientRole.SUBSCRIBER, NotificationStatus.PENDING);

		checkNotificationStatusDocumentNotNull(studentId, notificationId, me);

		// 2) 내 상태만 먼저 변경
		me.setStatus(NotificationStatus.REJECTED);
		me.setUpdatedAt(now);
		statusRepo.save(me);

		// 3) 동일 notificationId를 가진 모든 상태 조회
		List<NotificationStatusDocument> allStatuses =
				statusRepo.findByNotificationId(objId);

		// 4) 다른 상태들도 일괄 변경
		allStatuses.stream()
				.filter(s -> !s.getId().equals(objId))
				.forEach(s -> {
					s.setStatus(NotificationStatus.REJECTED);
					s.setUpdatedAt(now);
				});
		statusRepo.saveAll(allStatuses);

		// 5) SSE 이벤트 발행
		NotificationDocument doc = notificationRepo.findById(objId)
				.orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));


		eventPublisher.publishEvent(InvitationResponseEvent.of(
				this,
				objId,
				doc.getPublisherId(), doc.getPublisherType(),
				doc.getSubscriberId(), doc.getSubscriberType(),
				NotificationStatus.REJECTED,
				now
		));

		log.info("초대/지원이 거절되었습니다: notificationId={}", objId);
	}

	@Override
	public void cancelInvitation(Long studentId, String notificationId) {
		ObjectId objId = new ObjectId(notificationId);
		Date now = getCurrentDate();

		// 1) 내 상태 조회·검증
		NotificationStatusDocument me = statusRepo.findByNotificationIdAndTargetIdAndRoleAndStatus(objId, studentId, RecipientRole.PUBLISHER, NotificationStatus.PENDING);

		checkNotificationStatusDocumentNotNull(studentId, notificationId, me);

		// 2) 내 상태만 먼저 변경
		me.setStatus(NotificationStatus.CANCELED);
		statusRepo.save(me);

		// 3) 동일 notificationId를 가진 모든 상태 조회
		List<NotificationStatusDocument> allStatuses =
				statusRepo.findByNotificationId(objId);

		// 4) 다른 상태들도 일괄 변경
		allStatuses.stream()
				.filter(s -> !s.getId().equals(objId))
				.forEach(s -> {
					s.setStatus(NotificationStatus.CANCELED);
					s.setUpdatedAt(now);
				});
		statusRepo.saveAll(allStatuses);

		// 5) SSE 이벤트 발행
		NotificationDocument doc = notificationRepo.findById(objId)
				.orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));


		eventPublisher.publishEvent(InvitationResponseEvent.of(
				this,
				objId,
				doc.getPublisherId(), doc.getPublisherType(),
				doc.getSubscriberId(), doc.getSubscriberType(),
				NotificationStatus.CANCELED,
				now
		));

		log.info("알림이 취소되었습니다: notificationId={}", notificationId);
	}

	private static void checkNotificationStatusDocumentNotNull(Long studentId, String notificationId, NotificationStatusDocument me) {
		if (me == null) {
			log.info("잘못된 알림입니다: notificationId={}, studentId={}", notificationId, studentId);
			throw new AppException(ErrorCode.NOTIFICATION_NOT_FOUND);
		}
	}

	/**
	 * 팀에 학생을 추가하거나 팀을 합치는 로직을 실행합니다.
	 * - 팀 합치기: 발신 팀 ID와 수신 팀 ID를 사용하여 팀을 합칩니다.
	 * - 개인 초대/지원: 발신 팀 ID
	 *
	 * @param studentId
	 * @param findNotification
	 */
	private void exeMergeOrAddMemberAtTeam(Long studentId, NotificationResponseDto findNotification) {
		if (findNotification.publisherType() == NotificationDomainType.TEAM &&
				findNotification.subscriberType() == NotificationDomainType.TEAM) {
			Student findStudent = studentFacade.findByStudentId(studentId);
			// 2) 팀에 학생 추가 및 팀원 변경 이벤트 발행(팀 목록 혹은 팀 상세보기 갱신용) - 팀 합치기(합치기 발신 팀ID, 합치기 수신 팀ID)
			teamService.mergeTeams(findNotification.publisherId(), findStudent.getTeamId());
		} else {
			// 2) 팀에 학생 추가 및 팀원 변경 이벤트 발행(팀 목록 혹은 팀 상세보기 갱신용) - 개인 초대/지원
			teamService.addStudentToTeam(findNotification.subscriberId(), findNotification.publisherId());
		}
	}

	/**
	 * 알림 상태를 생성합니다.
	 * 발신자와 수신자의 역할에 따라 알림 상태를 설정하고,
	 * 알림 제목과 메시지를 생성합니다.
	 * 발신자의 경우 읽음 상태로 설정하고, 읽은 시각을 현재 날짜로 설정합니다.
	 * 수신자의 경우 읽음 상태는 false로 설정하고,
	 * 읽은 시각은 null로 설정합니다.
	 *
	 * @param notif: NotificationDocument - 알림  문서 객체
	 * @param now:   Date - 현재 날짜
	 * @param role:  RecipientRole - 알림 발신자 또는 수신자의 역할 (PUBLISHER 또는 SUBSCRIBER)
	 * @return NotificationStatusDocument -알림 상태 문서 객체
	 */
	private List<NotificationStatusDocument> createStatuses(
			NotificationDocument notif,
			Date now,
			RecipientRole role
	) {
		log.info("알림 상태 생성 시작: {}, {}", notif.getId(), role);
		List<Long> targetStudentIds = deriveTargetStudentIds(notif, role);
		log.info("알림 대상 학생 ID 목록: {}", targetStudentIds);

		boolean readByDefault = (role == RecipientRole.PUBLISHER); // 발신자는 즉시 읽음 처리 유지
		log.info("알림 상태 생성: {}, {}, {}, {}", notif.getId(), role, targetStudentIds, readByDefault);

		return targetStudentIds.stream()
				.map(studentId -> NotificationStatusDocument.builder()
						.notificationId(notif.getId())
						.targetId(studentId)
						.targetType(NotificationDomainType.STUDENT)
						.role(role)
						.status(NotificationStatus.PENDING)
						.isRead(readByDefault)
						.readAt(readByDefault ? now : null)
						.createdAt(now)
						.updatedAt(now)
						.build())
				.toList();
	}

	private List<Long> deriveTargetStudentIds(NotificationDocument notif, RecipientRole role) {
		boolean isPublisher = (role == RecipientRole.PUBLISHER);
		NotificationDomainType type = isPublisher ? notif.getPublisherType() : notif.getSubscriberType();
		Long id = isPublisher ? notif.getPublisherId() : notif.getSubscriberId();

		log.info("알림 대상 학생 ID를 유도합니다: {}, {}, {}", type, id, role);
		return switch (type) {
			case STUDENT -> List.of(id);
			case TEAM -> studentFacade.findStudentIdList(id);
		};
	}

	/**
	 * 알림을 저장하기 전에, 가장 최근에 업데이트된 알림을 찾음
	 *
	 * @param dto
	 * @return NotificationDocument
	 */
	private NotificationDocument findLatestNotification(NotificationRequestDto dto) {
		// 가장 최근에 업데이트된 알림을 찾음
		NotificationDocument lastUpdatedLog = notificationRepo.findLatest(
				dto.pubId(),
				dto.pubType(),
				dto.subId(),
				dto.subType()
		);

		return lastUpdatedLog;
	}

	/**
	 * 알림이 이미 존재하는지 확인합니다.
	 * 알림이 존재하는 경우, 해당 알림의 상태가 PENDING인지 확인
	 * PENDING 상태인 경우, 알림을 저장하지 않고 false를 반환
	 *
	 * @param notificationId
	 * @return boolean
	 * @throws BadRequestException
	 */
	private void hasPendingStatus(ObjectId notificationId) {
		Boolean hasPending = statusRepo.existsByNotificationIdAndStatus(notificationId,
				NotificationStatus.PENDING);
		if (hasPending) {
			log.info("완료 처리되지 않은 알림이 이미 존재하며, 상태가 PENDING입니다. 알림을 저장하지 않습니다.");
			throw new AppException(ErrorCode.NOTIFICATION_PENDING_EXISTS);
		}
	}

	/**
	 * 현재 날짜를 반환합니다.
	 *
	 * @return Date - 현재 날짜
	 */
	private Date getCurrentDate() {
		return new Date();
	}
}
