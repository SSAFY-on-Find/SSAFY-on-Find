package com.sonfind.chelsea.global.event;

import org.apache.coyote.BadRequestException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.notification.ApplicantDto;
import com.sonfind.chelsea.dto.notification.ApplicationPubData;
import com.sonfind.chelsea.dto.notification.ApplicationSubData;
import com.sonfind.chelsea.dto.notification.InvitationNotificationResponseDto;
import com.sonfind.chelsea.dto.notification.InvitationPubData;
import com.sonfind.chelsea.dto.notification.InvitationSubData;
import com.sonfind.chelsea.dto.notification.MergePubData;
import com.sonfind.chelsea.dto.notification.MergeSubData;
import com.sonfind.chelsea.dto.notification.MergeTargetDto;
import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.dto.notification.NotificationMsgDto;
import com.sonfind.chelsea.dto.notification.NotificationResponseDto;
import com.sonfind.chelsea.dto.notification.ParticipantDto;
import com.sonfind.chelsea.dto.student.response.StudentUnionForNotificationResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.service.NotificationService;
import com.sonfind.chelsea.service.SseService;
import com.sonfind.chelsea.service.TeamService;
import com.sonfind.chelsea.types.NotificationDomainType;

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
	public void onRequest(InvitationRequestEvent e) throws BadRequestException {
		switch (e.getType()) {
			case APPLICATION -> {
				NotificationDto<ApplicationPubData> pubPayload = createApplicationPubPayload(e);
				NotificationDto<ApplicationSubData> subPayload = createApplicationSubPayload(e);

				sseService.dispatch(
					pubPayload.data().publisher().id(),
					pubPayload.data().publisher().type(),
					pubPayload
				);
				sseService.dispatch(
					subPayload.data().subscriber().id(),
					subPayload.data().subscriber().type(),
					subPayload
				);
			}
			case INVITATION -> {
				NotificationDto<InvitationPubData> pubPayload = createInvitationPubPayload(e);
				NotificationDto<InvitationSubData> subPayload = createInvitationSubPayload(e);

				sseService.dispatch(
					pubPayload.data().publisher().id(),
					pubPayload.data().publisher().type(),
					pubPayload
				);
				sseService.dispatch(
					subPayload.data().subscriber().id(),
					subPayload.data().subscriber().type(),
					subPayload
				);
			}
			case MERGE -> {
				NotificationDto<MergePubData> pubPayload = createMergePubPayload(e);
				NotificationDto<MergeSubData> subPayload = createMergeSubPayload(e);
				
				sseService.dispatch(
					pubPayload.data().publisher().id(),
					pubPayload.data().publisher().type(),
					pubPayload
				);
				sseService.dispatch(
					subPayload.data().subscriber().id(),
					subPayload.data().subscriber().type(),
					subPayload
				);
			}
			default -> {
				// 지원, 초대, 병합 외의 타입은 처리하지 않음
				throw new BadRequestException("Unsupported invitation request type: " + e.getType());
			}
		}
	}

	@EventListener
	public void onResponse(InvitationResponseEvent e) throws BadRequestException {
		switch (e.getStatus()) {
			case ACCEPTED, REJECTED, CANCELED -> {
				NotificationDto<InvitationNotificationResponseDto> payload = createInvitationResponsePayload(e);

				sendBoth(
					payload.data().pubId(),
					payload.data().pubType(),
					payload.data().subId(),
					payload.data().subType(),
					payload
				);
			}
			default -> {
				// 수락/거절/취소 외의 상태는 처리하지 않음
				throw new BadRequestException("Unsupported invitation response status: " + e.getStatus());
			}
		}
	}

	private <T> void sendBoth(Long pubId, NotificationDomainType pubType,
		Long subId, NotificationDomainType subType,
		NotificationDto<T> payload) {
		sseService.dispatch(pubId, pubType, payload);
		sseService.dispatch(subId, subType, payload);
	}

	// 수락/거절/취소 응답 이벤트에 대한 알림 페이로드 생성
	private NotificationDto<InvitationNotificationResponseDto> createInvitationResponsePayload(
		InvitationResponseEvent e) throws BadRequestException {

		InvitationNotificationResponseDto createResponse = InvitationNotificationResponseDto.builder()
			.pubId(e.getPubId())
			.pubType(e.getPubType())
			.subId(e.getSubId())
			.subType(e.getSubType())
			.updatedAt(e.getUpdatedAt().toString())
			.build();

		return NotificationDto.<InvitationNotificationResponseDto>builder()
			.id(e.getNotificationId())
			.event(e.getClass().toString())
			.status(e.getStatus())
			.time(e.getUpdatedAt().toString())
			.data(createResponse)
			.build();
	}

	// APPLICATION(개인 -> 팀 지원) - 발신자(개인)에게 알림 전송
	private NotificationDto<ApplicationPubData> createApplicationPubPayload(InvitationRequestEvent e) throws
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
	private NotificationDto<ApplicationSubData> createApplicationSubPayload(InvitationRequestEvent e) throws
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
	private NotificationDto<InvitationPubData> createInvitationPubPayload(InvitationRequestEvent e) throws
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
	private NotificationDto<InvitationSubData> createInvitationSubPayload(InvitationRequestEvent e) throws
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
	private NotificationDto<MergePubData> createMergePubPayload(InvitationRequestEvent e) throws BadRequestException {
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
	private NotificationDto<MergeSubData> createMergeSubPayload(InvitationRequestEvent e) throws BadRequestException {
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
