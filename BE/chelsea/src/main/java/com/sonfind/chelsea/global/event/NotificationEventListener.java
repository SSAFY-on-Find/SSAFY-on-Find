package com.sonfind.chelsea.global.event;

import org.apache.coyote.BadRequestException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.notification.ApplicantDto;
import com.sonfind.chelsea.dto.notification.ApplicationPubData;
import com.sonfind.chelsea.dto.notification.ApplicationSubData;
import com.sonfind.chelsea.dto.notification.InvitationPubData;
import com.sonfind.chelsea.dto.notification.InvitationSubData;
import com.sonfind.chelsea.dto.notification.MergePubData;
import com.sonfind.chelsea.dto.notification.MergeSubData;
import com.sonfind.chelsea.dto.notification.MergeTargetDto;
import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.dto.notification.NotificationMsgDto;
import com.sonfind.chelsea.dto.notification.NotificationResponseDto;
import com.sonfind.chelsea.dto.notification.ParticipantDto;
import com.sonfind.chelsea.dto.student.StudentUnionForNotificationResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.service.NotificationService;
import com.sonfind.chelsea.service.SseService;
import com.sonfind.chelsea.service.TeamService;

import lombok.RequiredArgsConstructor;

/**
 * 알림 이벤트 리스너
 * NotificationEvent를 수신하여 알림을 발송하는 역할을 합니다.
 * 이벤트 타입에 따라 발신자와 수신자에게 알림을 전송합니다.
 */
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

	private final SseService sseService;
	private final StudentFacade studentFacade;
	private final NotificationService notificationService;
	private final TeamService teamService;

	/**
	 * 알림 이벤트 리스너
	 * 이벤트 타입에 따라 발신자와 수신자에게 알림을 전송합니다.
	 */
	@EventListener
	public void onNotification(NotificationEvent e) throws BadRequestException {
		switch (e.getType()) {
			case APPLICATION -> {
				NotificationDto<ApplicationPubData> pubPayload = createApplicationPubPayload(e);
				NotificationDto<ApplicationSubData> subPayload = createApplicationSubPayload(e);
				sseService.sendNotification(e.getPubId(), pubPayload);
				sseService.broadcastToTeam(subPayload.data().subscriber().id(), subPayload);
			}
			case INVITATION -> {
				NotificationDto<InvitationPubData> pubPayload = createInvitationPubPayload(e);
				NotificationDto<InvitationSubData> subPayload = createInvitationSubPayload(e);
				sseService.broadcastToTeam(pubPayload.data().subscriber().id(), pubPayload);
				sseService.sendNotification(e.getSubId(), subPayload);
			}
			case MERGE -> {
				NotificationDto<MergePubData> pubPayload = createMergePubPayload(e);
				NotificationDto<MergeSubData> subPayload = createMergeSubPayload(e);
				sseService.broadcastToTeam(pubPayload.data().subscriber().id(), pubPayload);
				sseService.broadcastToTeam(subPayload.data().subscriber().id(), subPayload);
			}
		}
	}

	// APPLICATION(개인 -> 팀 지원) - 발신자(개인)에게 알림 전송
	private NotificationDto<ApplicationPubData> createApplicationPubPayload(NotificationEvent e) throws
		BadRequestException {

		// 발신자 정보 조회
		Students findPub = studentFacade.findByStudentId(e.getPubId());

		// 발신자 알림 관련 정보 조회
		NotificationResponseDto findNotificationInfo = notificationService.getNotificationInfo(e.getNotificationId());

		// 수신자 정보 조회
		Students findSub = studentFacade.findByStudentId(e.getSubId());

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
	private NotificationDto<ApplicationSubData> createApplicationSubPayload(NotificationEvent e) throws
		BadRequestException {
		// 발신자 정보 조회
		Students findPub = studentFacade.findByStudentId(e.getPubId());

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

	// INVITATION(팀 -> 개인 초대) - 발신자(팀)에게 알림 전송
	private NotificationDto<InvitationPubData> createInvitationPubPayload(NotificationEvent e) throws
		BadRequestException {
		// 발신자 정보
		TeamSimpleResponseDto findPubTeam = teamService.findSimpleTeamInfoByTeamId(e.getPubId());

		// 발신자 추가정보 조회
		NotificationResponseDto findPubTeamInfo = notificationService.getNotificationInfo(e.getNotificationId());

		// 수신자 정보 조회
		Students findSub = studentFacade.findByStudentId(e.getSubId());

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
	private NotificationDto<InvitationSubData> createInvitationSubPayload(NotificationEvent e) throws
		BadRequestException {
		// 발신자 정보
		TeamSimpleResponseDto findPubTeam = teamService.findSimpleTeamInfoByTeamId(e.getPubId());

		// 수신자 정보 조회
		Students findSub = studentFacade.findByStudentId(e.getSubId());

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

	// MERGE(팀 합치기) - 발신자(팀)에게 알림 전송
	private NotificationDto<MergePubData> createMergePubPayload(NotificationEvent e) throws BadRequestException {
		// 발신자 정보 조회
		TeamSimpleResponseDto findPubTeam = teamService.findSimpleTeamInfoByTeamId(e.getPubId());

		// 발신자 추가정보 조회
		NotificationResponseDto findNotification = notificationService.getNotificationInfo(e.getNotificationId());

		// 수신자 정보 조회
		TeamSimpleResponseDto findSubTeam = teamService.findSimpleTeamInfoByTeamId(e.getSubId());

		// 발신자 정보
		NotificationMsgDto pub = NotificationMsgDto.builder()
			.id(findPubTeam.teamId())
			.name(findPubTeam.name())
			.type(e.getPubType())
			.track(findPubTeam.track())
			.notificationTitle(findNotification.pubNotificationTitle())
			.notificationMessage(findNotification.pubNotificationMessage())
			.targetId(e.getSubId())
			.build();

		// 수신자 정보
		MergeTargetDto sub = MergeTargetDto.builder()
			.id(findSubTeam.teamId())
			.name(findSubTeam.name())
			.type(e.getSubType())
			.track(findSubTeam.track())
			.memberCount(calcTeamMemberCount(findSubTeam))
			.majorCount(findSubTeam.majorCount())
			.nonMajorCount(findSubTeam.nonMajorCount())
			.build();

		// 이벤트 페이로드
		MergePubData payload = MergePubData.builder()
			.publisher(pub)
			.subscriber(sub)
			.build();

		// 최종 응답 DTO
		return NotificationDto.<MergePubData>builder()
			.id(e.getNotificationId())
			.event(e.getClass().toString())
			.type(e.getType())         // MERGE 으로
			.time(e.getUpdatedAt().toString())
			.data(payload)
			.build();
	}

	// MERGE(팀 합치기) - 수신자(팀)에게 알림 전송
	private NotificationDto<MergeSubData> createMergeSubPayload(NotificationEvent e) throws BadRequestException {
		// 발신자 정보 조회
		TeamSimpleResponseDto findPubTeam = teamService.findSimpleTeamInfoByTeamId(e.getPubId());

		// 수신자 정보 조회
		TeamSimpleResponseDto findSubTeam = teamService.findSimpleTeamInfoByTeamId(e.getSubId());

		// 수신자 추가정보 조회
		NotificationResponseDto findNotification = notificationService.getNotificationInfo(e.getNotificationId());

		// 발신자 정보
		MergeTargetDto pub = MergeTargetDto.builder()
			.id(findPubTeam.teamId())
			.name(findPubTeam.name())
			.type(e.getPubType())
			.track(findPubTeam.track())
			.memberCount(calcTeamMemberCount(findPubTeam))
			.majorCount(findPubTeam.majorCount())
			.nonMajorCount(findPubTeam.nonMajorCount())
			.build();

		// 수신자 정보
		NotificationMsgDto sub = NotificationMsgDto.builder()
			.id(findSubTeam.teamId())
			.name(findSubTeam.name())
			.type(e.getSubType())
			.notificationTitle(findNotification.subNotificationTitle())
			.notificationMessage(findNotification.subNotificationMessage())
			.build();

		// 이벤트 페이로드
		MergeSubData payload = MergeSubData.builder()
			.publisher(pub)
			.subscriber(sub)
			.build();

		// 최종 응답 DTO
		return NotificationDto.<MergeSubData>builder()
			.id(e.getNotificationId())
			.event(e.getClass().toString())
			.type(e.getType())         // MERGE 으로
			.time(e.getUpdatedAt().toString())
			.data(payload)
			.build();
	}

	private String getIsMajor(Students findSub) {
		return findSub.getMajorYn() ? "전공" : "비전공";
	}

	private static int calcTeamMemberCount(TeamSimpleResponseDto findSubTeamInfo) {
		return findSubTeamInfo.majorCount() + findSubTeamInfo.nonMajorCount();
	}
}
