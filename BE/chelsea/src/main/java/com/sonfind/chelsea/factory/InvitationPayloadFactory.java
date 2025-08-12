package com.sonfind.chelsea.factory;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.dto.notification.*;
import com.sonfind.chelsea.dto.student.response.StudentUnionForNotificationResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.event.InvitationRequestEvent;
import com.sonfind.chelsea.service.NotificationService;
import com.sonfind.chelsea.service.TeamService;
import com.sonfind.chelsea.types.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvitationPayloadFactory implements RequestPayloadFactory {

	private final StudentFacade studentFacade;
	private final NotificationService notificationService;
	private final TeamService teamService;

	@Override
	public NotificationType supportType() {
		return NotificationType.INVITATION;
	}

	// INVITATION(팀 -> 개인 초대) - 발신자(팀)에게 알림 전송
	@Override
	public NotificationDto<InvitationPubData> createPublisherPayload(
			InvitationRequestEvent e) {
		// 발신자 정보
		TeamSimpleResponseDto findPubTeam = teamService.findSimpleTeamInfoByTeamId(e.getPubId());

		// 발신자 추가정보 조회
		NotificationResponseDto findPubTeamInfo = notificationService.getNotificationInfo(e.getNotificationId());

		// 수신자 정보 조회
		Student findSub = studentFacade.findByStudentId(e.getSubId());

		// 수신자 추가정보 조회
		StudentUnionForNotificationResponseDto findSubInfo = studentFacade.findByStudentIdForSse(
				findSub.getStudentId());

		// 발신자 정보
		NotificationMsgDto pub = NotificationMsgDto.builder()
				.id(findPubTeam.teamId())
				.name(findPubTeam.name())
				.type(e.getPubType())
				.track(findPubTeam.track())
				.notificationTitle(findPubTeamInfo.pubNotificationTitle())
				.notificationMessage(findPubTeamInfo.pubNotificationMessage())
				.targetId(findSub.getStudentId())
				.build();

		// 수신자 정보
		ApplicantDto sub = ApplicantDto.builder()
				.id(findSub.getStudentId())
				.name(findSub.getName())
				.type(e.getSubType())
				.track(findSubInfo.track())
				.isMajor(getIsMajor(findSub))
				.position(findSubInfo.position())
				.build();

		// 이벤트 페이로드
		InvitationPubData payload = InvitationPubData.builder()
				.publisher(pub)
				.subscriber(sub)
				.build();

		// 최종 응답 DTO
		return NotificationDto.<InvitationPubData>builder()
				.id(e.getNotificationId())
				.event(e.getClass().toString())
				.type(e.getType())         // INVITATION 으로
				.time(e.getUpdatedAt().toString())
				.data(payload)
				.build();
	}

	// INVITATION(팀 -> 개인 초대) - 수신자(개인)에게 알림 전송
	@Override
	public NotificationDto<InvitationSubData> createSubscriberPayload(
			InvitationRequestEvent e) {
		// 발신자 정보
		TeamSimpleResponseDto findPubTeam = teamService.findSimpleTeamInfoByTeamId(e.getPubId());

		// 수신자 정보 조회
		Student findSub = studentFacade.findByStudentId(e.getSubId());

		// 수신자 추가정보 조회
		NotificationResponseDto findNotificationInfo = notificationService.getNotificationInfo(e.getNotificationId());

		// 발신자 정보
		ParticipantDto pub = ParticipantDto.builder()
				.id(findPubTeam.teamId())
				.name(findPubTeam.name())
				.type(e.getPubType())
				.track(findPubTeam.track())
				.build();

		// 수신자 정보
		NotificationMsgDto sub = NotificationMsgDto.builder()
				.id(findSub.getStudentId())
				.name(findSub.getName())
				.type(e.getSubType())
				.notificationTitle(findNotificationInfo.subNotificationTitle())
				.notificationMessage(findNotificationInfo.subNotificationMessage())
				.build();

		// 이벤트 페이로드
		InvitationSubData payload = InvitationSubData.builder()
				.publisher(pub)
				.subscriber(sub)
				.build();

		// 최종 응답 DTO
		return NotificationDto.<InvitationSubData>builder()
				.id(e.getNotificationId())
				.event(e.getClass().toString())
				.type(e.getType())         // INVITATION 으로
				.time(e.getUpdatedAt().toString())
				.data(payload)
				.build();
	}

	private String getIsMajor(Student findSub) {
		return findSub.getMajorYn() ? "전공" : "비전공";
	}
}
