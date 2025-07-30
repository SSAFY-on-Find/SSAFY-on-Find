package com.sonfind.chelsea.service;

import java.util.Date;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.domain.notification.NotificationParticipant;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.repository.NotificationRepository;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	/**
	 * 알림을 저장합니다.
	 * 알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성하고,
	 * NotificationDocument 객체를 생성하여 MongoDB에 저장합니다.
	 * @param dto
	 */
	public Long saveNotification(NotificationRequestDto dto) throws BadRequestException {
		// dto에서 발신자와 수신자의 정보를 추출하여 NotificationParticipant 객체를 생성.
		NotificationParticipant pub = new NotificationParticipant(dto.getPubId(),
			NotificationDomainType.from(dto.getPubType()));
		NotificationParticipant sub = new NotificationParticipant(dto.getSubId(),
			NotificationDomainType.from(dto.getSubType()));

		if (notificationRepository.existsByPublisherIdAndPublisherTypeAndSubscriberIdAndSubscriberTypeAndStatus(
			pub.getId(), pub.getType(), sub.getId(), sub.getType(), NotificationStatus.PENDING)) {
			log.info("Notification already exists");
			throw new BadRequestException("Notification already exists! code=" + HttpStatus.BAD_REQUEST);
		}

		/**
		 * TODO: 알림 발신자와 수신자 구분 방식 고려
		 * - 1. TEAM_TEAM: 팀 합치기 제안
		 * - 2. MATE_TEAM: 팀에 지원
		 * - 3. TEAM_MATE: 팀에 초대
		 * 와 같은 ENUM을 하나 더 생성하여 분기 고려 중입니다.
		 * 이 부분은 추후에 개선할 예정입니다. 혹시 더 좋은 의견이 있으시면 코드리뷰로 남겨주시면 감사하겠습니다
		 */
		// title과 메시지를 설정
		if (pub.getType() == NotificationDomainType.TEAM && sub.getType() == NotificationDomainType.TEAM) {
			// 발신자
			pub.setNotificationTitle("OOO" + "에 합치기 제안");
			pub.setNotificationMessage("OOO" + "에 팀 합치기를 제안했습니다.");
			// 수신자
			sub.setNotificationTitle("OOO" + "의 합치기 제안");
			sub.setNotificationMessage("OOO" + "에서 팀 합치기를 제안했습니다.");
		} else if (pub.getType() == NotificationDomainType.MATE && sub.getType() == NotificationDomainType.TEAM) {
			pub.setNotificationTitle("OOO" + "에 지원");
			pub.setNotificationMessage("OOO" + "에 지원했습니다.");

			sub.setNotificationTitle("OOO" + "님의 지원");
			sub.setNotificationMessage("OOO" + "님이 지원했습니다.");
		} else if (pub.getType() == NotificationDomainType.TEAM && sub.getType() == NotificationDomainType.MATE) {
			pub.setNotificationTitle("OOO" + "님에게 초대");
			pub.setNotificationMessage("OOO" + "님을 팀에 초대했습니다.");

			sub.setNotificationTitle("OOO" + "의 초대");
			sub.setNotificationMessage("OOO" + "에서 초대했습니다.");
		} else {
			log.error("Invalid notification type combination: pubType={}, subType={}", pub.getType(), sub.getType());
			throw new BadRequestException("Invalid notification type combination! code=" + HttpStatus.BAD_REQUEST);
		}
		log.info("Creating notification: pub={}, sub={}", pub, sub);

		try {
			Date now = getCurrentDate();
			// NotificationDocument 객체를 생성하고 MongoDB에 저장
			NotificationDocument notification = NotificationDocument.builder()
				.publisher(pub)
				.subscriber(sub)
				.status(NotificationStatus.PENDING)
				.createdAt(now)
				.updatedAt(now)
				.build();
			NotificationDocument savedNotification = notificationRepository.save(notification);
			log.info("Notification saved successfully: {}", savedNotification);
			return savedNotification.getId();
		} catch (Exception e) {
			log.error("Failed to save notification: {}", e.getMessage());
			throw new BadRequestException("Failed to save notification! code=" + HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Date getCurrentDate() {
		return new Date();
	}
}
