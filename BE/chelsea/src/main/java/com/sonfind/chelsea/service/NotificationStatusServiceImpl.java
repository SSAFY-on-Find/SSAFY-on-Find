package com.sonfind.chelsea.service;

import java.util.Date;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.repository.NotificationRepository;
import com.sonfind.chelsea.repository.NotificationStatusRepository;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.RecipientRole;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationStatusServiceImpl implements NotificationStatusService {

	private final NotificationStatusRepository statusRepo;
	private final NotificationRepository notificationRepo;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	public void createInitialStatuses(NotificationDocument notif) {
		// 이벤트 발행 시점의 날짜를 가져옴
		Date now = getCurrentDate();

		// 1) Publisher 상태 생성 (읽음=true)
		NotificationStatusDocument pub = NotificationStatusDocument.builder()
			.notificationId(notif.getId())
			.targetId(notif.getPublisherId())
			.targetType(notif.getPublisherType())
			.role(RecipientRole.PUBLISHER)
			.status(NotificationStatus.PENDING)
			.isRead(true)
			.readAt(now)
			.createdAt(now)
			.updatedAt(now)
			.build();
		statusRepo.save(pub);

		// 그룹아이디 설정
		notif.setGroupId(pub.getId());
		notificationRepo.save(notif);

		// 2) Subscriber 상태 생성 (읽음=false)
		NotificationStatusDocument sub = NotificationStatusDocument.builder()
			.notificationId(notif.getId())
			.targetId(notif.getSubscriberId())
			.targetType(notif.getSubscriberType())
			.role(RecipientRole.SUBSCRIBER)
			.status(NotificationStatus.PENDING)
			.isRead(false)
			.readAt(null)
			.createdAt(now)
			.updatedAt(now)
			.build();
		statusRepo.save(sub);
	}

	@Override
	public void acceptInvitation(Long studentId, String statusId) throws BadRequestException {
		ObjectId objId = new ObjectId(statusId);
		Date now = getCurrentDate();

		// 내 상태 조회·검증
		NotificationStatusDocument me = statusRepo.findById(objId)
			.orElseThrow(() -> new BadRequestException("잘못된 알림입니다."));
		// TODO: 권한 검증 로직 추가

		// 나만 ACCEPTED
		me.setStatus(NotificationStatus.ACCEPTED);
		me.setRead(true);
		me.setReadAt(now);
		me.setUpdatedAt(now);
		statusRepo.save(me);

		// 나머지 모두 ACCEPTED
		List<NotificationStatusDocument> others = statusRepo.findByNotificationId(me.getNotificationId());
		others.stream()
			.filter(s -> !s.getId().equals(objId))
			.forEach(s -> {
				s.setStatus(NotificationStatus.ACCEPTED);
				s.setUpdatedAt(now);
			});
		statusRepo.saveAll(others);

		// 이벤트 발행
		NotificationDocument doc = notificationRepo.findById(me.getNotificationId())
			.orElseThrow(() -> new BadRequestException("알림 조회 실패"));
		eventPublisher.publishEvent(doc);
	}

	@Override
	public void rejectInvitation(Long studentId, String statusId) throws BadRequestException {
		ObjectId objId = new ObjectId(statusId);
		Date now = getCurrentDate();

		// 내 상태 조회·검증 (발신자만 가능)
		NotificationStatusDocument me = statusRepo.findById(objId)
			.orElseThrow(() -> new BadRequestException("잘못된 알림입니다."));
		if (me.getRole() != RecipientRole.PUBLISHER
			|| !me.getTargetId().equals(studentId)) {
			throw new BadRequestException("거절 권한이 없습니다.");
		}

		// me → REJECTED
		me.setStatus(NotificationStatus.REJECTED);
		me.setUpdatedAt(now);
		statusRepo.save(me);

		// others → REJECTED
		List<NotificationStatusDocument> others = statusRepo.findByNotificationId(me.getNotificationId());
		others.stream()
			.filter(s -> !s.getId().equals(objId))
			.forEach(s -> {
				s.setStatus(NotificationStatus.REJECTED);
				s.setUpdatedAt(now);
			});
		statusRepo.saveAll(others);

		// 이벤트 발행
		NotificationDocument doc = notificationRepo.findById(me.getNotificationId())
			.orElseThrow(() -> new BadRequestException("알림 조회 실패"));
		eventPublisher.publishEvent(doc);
	}

	@Override
	public void cancelInvitation(Long studentId, String statusId) throws BadRequestException {
		ObjectId objId = new ObjectId(statusId);
		Date now = getCurrentDate();

		// 내 상태 조회·검증 (발신자만 가능)
		NotificationStatusDocument me = statusRepo.findById(objId)
			.orElseThrow(() -> new BadRequestException("잘못된 알림입니다."));
		if (me.getRole() != RecipientRole.PUBLISHER
			|| !me.getTargetId().equals(studentId)) {
			throw new BadRequestException("취소 권한이 없습니다.");
		}

		// me → CANCELED
		me.setStatus(NotificationStatus.CANCELED);
		me.setUpdatedAt(now);
		statusRepo.save(me);

		// others → CANCELED
		List<NotificationStatusDocument> others = statusRepo.findByNotificationId(me.getNotificationId());
		others.stream()
			.filter(s -> !s.getId().equals(objId))
			.forEach(s -> {
				s.setStatus(NotificationStatus.CANCELED);
				s.setUpdatedAt(now);
			});
		statusRepo.saveAll(others);

		// 이벤트 발행
		NotificationDocument doc = notificationRepo.findById(me.getNotificationId())
			.orElseThrow(() -> new BadRequestException("알림 조회 실패"));
		eventPublisher.publishEvent(doc);
	}

	/**
	 * 현재 날짜를 반환합니다.
	 * @return Date - 현재 날짜
	 */
	private Date getCurrentDate() {
		return new Date();
	}
}
