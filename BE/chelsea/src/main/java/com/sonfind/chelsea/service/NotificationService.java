package com.sonfind.chelsea.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.dto.notification.NotificationAndNotificationStatusResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.dto.notification.NotificationResponseDto;
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
public class NotificationService {

	private final NotificationStatusRepository statusRepo;
	private final NotificationQueryService notificationQueryService;
	private final NotificationCommandService notificationCommandService;

	/**
	 * 알림 발송 메소드
	 * 알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성하고,
	 * NotificationDocument 객체를 생성하여 MongoDB에 저장 후 sse로 발송
	 * @param dto: NotificationRequestDto
	 */
	public void sendNotification(Long studentId, NotificationRequestDto dto) throws BadRequestException {
		notificationCommandService.sendNotification(studentId, dto);
	}

	public NotificationResponseDto getNotificationInfo(ObjectId notificationId) throws
		BadRequestException {
		return notificationQueryService.getNotificationInfo(notificationId);
	}

	/**
	 * 개인 또는 팀의 알림을 조회합니다.(알림 창)
	 * 알림은 발신자 또는 수신자의 역할에 따라 조회됩니다.
	 * @param studentId
	 * @param type
	 * @return
	 */
	public List<NotificationAndNotificationStatusResponseDto> getMyNotifications(Long studentId, String type) throws
		BadRequestException {
		return notificationQueryService.getMyNotifications(studentId, type);
	}

	/**
	 * 팀의 알림을 조회합니다.(팀 상세보기 페이지)
	 * @param teamId
	 * @param type
	 * @return
	 */
	public List<NotificationAndNotificationStatusResponseDto> getTeamNotifications(Long studentId, Long teamId,
		String type) throws
		BadRequestException {
		return notificationQueryService.getTeamNotifications(studentId, teamId, type);
	}

	/**
	 * 학생의 읽지 않은 알림 개수를 조회합니다.
	 * @param studentId
	 * @return 읽지 않은 알림 개수
	 */
	public int getCountOfNonReadNotifications(Long studentId) {
		return notificationQueryService.getCountOfNonReadNotifications(studentId);
	}

	/**
	 * 알림을 수락합니다.
	 * 알림 상태를 PENDING에서 ACCEPTED로 변경하고, 읽음 상태를 true로 설정합니다.
	 * @param studentId
	 * @param statusId
	 */
	public void acceptInvitation(Long studentId, String statusId) throws BadRequestException {
		notificationCommandService.acceptInvitation(studentId, statusId);
	}

	/**
	 * 알림을 거절합니다.
	 * 알림 상태를 PENDING에서 REJECTED로 변경하고, 읽음 상태를 true로 설정합니다.
	 * @param studentId
	 * @param statusId
	 */
	public void rejectInvitation(Long studentId, String statusId) throws BadRequestException {
		notificationCommandService.rejectInvitation(studentId, statusId);
	}

	/**
	 * 알림을 취소합니다.
	 * 알림 상태를 PENDING에서 CANCELED로 변경하고, 읽음 상태를 true로 설정합니다.
	 * @param studentId
	 * @param statusId
	 */
	public void cancelInvitation(Long studentId, String statusId) throws BadRequestException {
		notificationCommandService.cancelInvitation(studentId, statusId);
	}

	/**
	 * 알림 상태를 조회합니다.
	 * @param notificationId
	 * @param studentId
	 * @param role
	 * @param status
	 * @return NotificationStatusDocument
	 * @throws BadRequestException 알림 상태가 존재하지 않는 경우
	 */
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
}
