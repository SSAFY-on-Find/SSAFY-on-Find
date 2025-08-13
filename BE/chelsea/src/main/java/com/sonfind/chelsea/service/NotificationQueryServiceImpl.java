package com.sonfind.chelsea.service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.dto.notification.*;
import com.sonfind.chelsea.dto.studentInfo.response.StudentMini;
import com.sonfind.chelsea.dto.teams.TeamMini;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import com.sonfind.chelsea.repository.NotificationRepository;
import com.sonfind.chelsea.repository.NotificationStatusRepository;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.RecipientRole;
import com.sonfind.chelsea.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService {

	private final NotificationRepository notificationRepository;
	private final NotificationStatusRepository statusRepository;
	private final StudentFacade studentFacade;
	private final TeamService teamService;

	@Override
	public NotificationResponseDto getNotificationInfo(ObjectId notificationId) {
		NotificationDocument findNotification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
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

	@Override
	public List<NotificationAndNotificationStatusForMyNotifResponseDto> getMyNotifications(Long studentId, String type) {
		String role = getRole(type);
		RecipientRole recipientRole = RecipientRole.valueOf(role.toUpperCase());
		NotificationDomainType domain = NotificationDomainType.STUDENT;
		List<NotificationStatusDocument> findNotifications = statusRepository.findAllByTargetIdAndTargetTypeAndRole(
				studentId,
				domain, recipientRole);

		if (findNotifications.isEmpty()) {
			log.info("알림이 존재하지 않습니다. studentId: {}, role: {}", studentId, role);
			return Collections.singletonList(NotificationAndNotificationStatusForMyNotifResponseDto.builder()
					.notificationStatusList(List.of())
					.unReadCount(0)
					.build());
		}

		// 미읽음 개수 조회
		int unreadCount = statusRepository.countByTargetIdAndTargetTypeAndRoleAndIsReadFalse(
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
			statusRepository.saveAll(toRead);
		}

		// 관련 NotificationDocument 일괄 조회
		List<ObjectId> notiIds = findNotifications.stream()
				.map(NotificationStatusDocument::getNotificationId)
				.distinct()
				.toList();
		List<NotificationDocument> docs = notificationRepository.findAllById(notiIds);
		Map<ObjectId, NotificationDocument> docMap = docs.stream()
				.collect(Collectors.toMap(NotificationDocument::getId, Function.identity()));

		// DTO 매핑
		List<NotificationStatusResponseDto> dtos = findNotifications.stream()
				.map(status -> {
					NotificationDocument doc = docMap.get(status.getNotificationId());
					return NotificationStatusResponseDto.builder()
							.statusId(status.getId().toHexString())
							.notificationId(doc.getId().toHexString())
							.targetId(status.getTargetId())
							.targetType(status.getTargetType())
							.role(status.getRole())
							.status(status.getStatus())
							.isRead(status.isRead())
							.updatedAt(DateUtil.formatKoShort(status.getUpdatedAt()))
							.publisherId(doc.getPublisherId())
							.publisherType(doc.getPublisherType())
							.pubNotificationTitle(doc.getPubNotificationTitle())
							.pubNotificationMessage(doc.getPubNotificationMessage())
							.subscriberId(doc.getSubscriberId())
							.subscriberType(doc.getSubscriberType())
							.subNotificationTitle(doc.getSubNotificationTitle())
							.subNotificationMessage(doc.getSubNotificationMessage())
							.build();
				})
				.toList();

		return Collections.singletonList(NotificationAndNotificationStatusForMyNotifResponseDto.builder()
				.notificationStatusList(dtos)
				.unReadCount(unreadCount)
				.build());
	}

	@Override
	public List<NotificationAndNotificationStatusForMyTeamResponseDto> getTeamNotifications(
			Long studentId, Long teamId, String type) {

		if (!studentFacade.isMemberOfTeam(studentId, teamId)) {
			log.info("학생이 팀의 멤버가 아닙니다. studentId: {}, teamId: {}", studentId, teamId);
			throw new AppException(ErrorCode.NOTIFICATION_NOT_FOUND);
		}

		// 팀이 보낸/받은 알림 조회 (NotificationDocument 기준)
		final List<NotificationDocument> docs;
		switch (type) {
			case "send" -> // 팀 발신 → 상대는 subscriber
					docs = notificationRepository.findAllByPublisherIdAndPublisherType(
							teamId, NotificationDomainType.TEAM);
			case "receive" -> // 팀 수신 → 상대는 publisher
					docs = notificationRepository.findAllBySubscriberIdAndSubscriberType(
							teamId, NotificationDomainType.TEAM);
			default -> throw new AppException(ErrorCode.NOT_SUPPORTED_TYPE);
		}

		if (docs.isEmpty()) {
			log.info("알림이 존재하지 않습니다. teamId: {}, type: {}", teamId, type);
			return List.of();
		}

		// 1) 상대 주체 id/type 모으기
		final boolean sent = "send".equals(type);
		Set<Long> studentIds = new HashSet<>();
		Set<Long> teamIds = new HashSet<>();
		for (NotificationDocument d : docs) {
			NotificationDomainType counterpartType = sent ? d.getSubscriberType() : d.getPublisherType();
			Long counterpartId = sent ? d.getSubscriberId() : d.getPublisherId();
			if (counterpartType == NotificationDomainType.STUDENT) studentIds.add(counterpartId);
			else teamIds.add(counterpartId);
		}

		// 2) 배치 조회 → Map
		Map<Long, StudentMini> studentMap = studentFacade.findStudentMiniMap(studentIds);
		Map<Long, TeamMini> teamMap = teamService.findTeamMiniMap(teamIds);

		// 3) DTO 매핑 (알림 한 건당 한 DTO)
		return docs.stream().map(d -> {
			NotificationDomainType counterpartType = sent ? d.getSubscriberType() : d.getPublisherType();
			Long counterpartId = sent ? d.getSubscriberId() : d.getPublisherId();
			String notificationId = d.getId().toHexString();
			StatusValue statusVal = statusRepository.findTopByNotificationIdOrderByUpdatedAtDesc(d.getId());

			if (counterpartType == NotificationDomainType.STUDENT) {
				StudentMini s = studentMap.get(counterpartId);
				String isMajor = null;
				if (s != null && s.getMajorYn() != null) {
					isMajor = s.getMajorYn() ? "전공" : "비전공"; // 요구사항: Y/N 문자열
				}
				return NotificationAndNotificationStatusForMyTeamResponseDto.builder()
						.notificationId(notificationId)
						.status(statusVal.status())
						.profileImageUrl(s != null ? s.getProfileImageUrl() : null)
						.name(s != null ? s.getName() : null)
						.isMajor(isMajor)
						.position(s != null ? s.getPosition() : null)
						.build();

			} else { // TEAM
				TeamMini t = teamMap.get(counterpartId);
				return NotificationAndNotificationStatusForMyTeamResponseDto.builder()
						.notificationId(notificationId)
						.status(statusVal.status())
						.name(t != null ? t.getName() : null)
						.majorCount(t != null && t.getMajorCount() != null ? t.getMajorCount() : null)
						.nonMajorCount(t != null && t.getNonMajorCount() != null ? t.getNonMajorCount() : null)
						.build();
			}
		}).toList();
	}

	@Override
	public int getCountOfNonReadNotifications(Long studentId) {
		return statusRepository.countByTargetIdAndTargetTypeAndRoleAndIsReadFalse(
				studentId, NotificationDomainType.STUDENT, RecipientRole.SUBSCRIBER
		);
	}

	/**
	 * 알림의 역할을 반환합니다.
	 *
	 * @param type
	 * @return
	 */
	private String getRole(String type) {
		String NotifiRole = null;
		if (type.equals("receive")) {
			NotifiRole = "subscriber";
		} else if (type.equals("send")) {
			NotifiRole = "publisher";
		} else {
			throw new AppException(ErrorCode.NOTIFICATION_TYPE_NOT_SUPPORTED);
		}
		return NotifiRole;
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
