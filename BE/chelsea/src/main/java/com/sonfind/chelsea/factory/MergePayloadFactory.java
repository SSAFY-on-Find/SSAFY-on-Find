package com.sonfind.chelsea.factory;

import com.sonfind.chelsea.dto.notification.*;
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
public class MergePayloadFactory implements RequestPayloadFactory {

	private final StudentFacade studentFacade;
	private final NotificationService notificationService;
	private final TeamService teamService;

	@Override
	public NotificationType supportType() {
		return NotificationType.MERGE;
	}

	// MERGE(팀 합치기) - 발신자(팀)에게 알림 전송
	@Override
	public NotificationDto<MergePubData> createPublisherPayload(InvitationRequestEvent e) {
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
				.id(e.getNotificationId().toHexString())
				.event(e.getClass().toString())
				.type(e.getType())         // MERGE 으로
				.time(e.getUpdatedAt().toString())
				.data(payload)
				.build();
	}

	// MERGE(팀 합치기) - 수신자(팀)에게 알림 전송
	@Override
	public NotificationDto<MergeSubData> createSubscriberPayload(InvitationRequestEvent e) {
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
				.id(e.getNotificationId().toHexString())
				.event(e.getClass().toString())
				.type(e.getType())         // MERGE 으로
				.time(e.getUpdatedAt().toString())
				.data(payload)
				.build();
	}

	private static int calcTeamMemberCount(TeamSimpleResponseDto findSubTeamInfo) {
		return findSubTeamInfo.majorCount() + findSubTeamInfo.nonMajorCount();
	}
}
