package com.sonfind.chelsea.factory;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.dto.notification.ApplicantDto;
import com.sonfind.chelsea.dto.notification.ApplicationPubData;
import com.sonfind.chelsea.dto.notification.ApplicationSubData;
import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.dto.notification.NotificationMsgDto;
import com.sonfind.chelsea.dto.notification.NotificationResponseDto;
import com.sonfind.chelsea.dto.student.response.StudentUnionForNotificationResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.event.InvitationRequestEvent;
import com.sonfind.chelsea.service.NotificationService;
import com.sonfind.chelsea.service.TeamService;
import com.sonfind.chelsea.types.NotificationType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationPayloadFactory implements RequestPayloadFactory {

	private final StudentFacade studentFacade;
	private final NotificationService notificationService;
	private final TeamService teamService;

	@Override
	public NotificationType supportType() {
		return NotificationType.APPLICATION;
	}

	// APPLICATION(개인 -> 팀 지원) - 발신자(개인)에게 알림 전송
	@Override
	public NotificationDto<ApplicationPubData> createPublisherPayload(
		InvitationRequestEvent e) throws
		BadRequestException {
		// 발신자 정보 조회
		Student findPub = studentFacade.findByStudentId(e.getPubId());

		// 발신자 알림 관련 정보 조회
		NotificationResponseDto findNotificationInfo = notificationService.getNotificationInfo(e.getNotificationId());

		// 수신자 정보 조회
		Student findSub = studentFacade.findByStudentId(e.getSubId());

		// 발신자 정보
		NotificationMsgDto pub = NotificationMsgDto.builder()
			.id(findPub.getStudentId())
			.name(findPub.getName())
			.type(e.getPubType())
			.notificationTitle(findNotificationInfo.pubNotificationTitle())
			.notificationMessage(findNotificationInfo.pubNotificationMessage())
			.targetId(findSub.getStudentId())
			.build();

		// 이벤트 페이로드
		ApplicationPubData payload = ApplicationPubData.builder()
			.publisher(pub)
			.build();

		// 최종 응답 DTO
		return NotificationDto.<ApplicationPubData>builder()
			.id(e.getNotificationId())
			.event(e.getClass().toString())
			.type(e.getType())         // APPLICATION 으로
			.time(e.getUpdatedAt().toString())
			.data(payload)
			.build();
	}

	// APPLICATION(개인 -> 팀 지원) - 수신자(팀)에게 알림 전송
	@Override
	public NotificationDto<ApplicationSubData> createSubscriberPayload(InvitationRequestEvent e) throws
		BadRequestException {
		// 발신자 정보 조회
		Student findPub = studentFacade.findByStudentId(e.getPubId());

		// 발신자 추가정보 조회
		StudentUnionForNotificationResponseDto findPubInfo = studentFacade.findByStudentIdForSse(
			findPub.getStudentId());

		// 수신팀 정보 조회
		TeamSimpleResponseDto findSubTeam = teamService.findSimpleTeamInfoByTeamId(e.getSubId());

		// 수신정보 조회
		NotificationResponseDto findNotificationInfo = notificationService.getNotificationInfo(e.getNotificationId());

		// 발신자 정보
		ApplicantDto pub = ApplicantDto.builder()
			.id(findPub.getStudentId())
			.name(findPub.getName())
			.type(e.getPubType())
			.track(findPubInfo.track())
			.isMajor(getIsMajor(findPub))
			.position(findPubInfo.position())
			.build();

		// 수신자 정보
		NotificationMsgDto sub = NotificationMsgDto.builder()
			.id(findNotificationInfo.subscriberId())
			.name(findSubTeam.name())
			.type(e.getSubType())
			.notificationTitle(findNotificationInfo.subNotificationTitle())
			.notificationMessage(findNotificationInfo.subNotificationMessage())
			.build();

		// 이벤트 페이로드
		ApplicationSubData payload = ApplicationSubData.builder()
			.publisher(pub)
			.subscriber(sub)
			.build();

		// 최종 응답 DTO
		return NotificationDto.<ApplicationSubData>builder()
			.id(e.getNotificationId())
			.event(e.getClass().toString())
			.type(e.getType())         // APPLICATION 으로
			.time(e.getUpdatedAt().toString())
			.data(payload)
			.build();
	}

	private String getIsMajor(Student findSub) {
		return findSub.getMajorYn() ? "전공" : "비전공";
	}
}
