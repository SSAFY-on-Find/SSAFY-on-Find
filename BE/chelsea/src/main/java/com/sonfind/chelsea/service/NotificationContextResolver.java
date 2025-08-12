package com.sonfind.chelsea.service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.dto.notification.ContextInfo;
import com.sonfind.chelsea.dto.notification.NotificationContext;
import com.sonfind.chelsea.dto.student.response.StudentUnionForNotificationResponseDto;
import com.sonfind.chelsea.dto.teams.TeamSimpleResponseDto;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import com.sonfind.chelsea.types.NotificationDomainType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationContextResolver {
	private final StudentFacade studentFacade;
	private final TeamService teamService;

	public NotificationContext resolve(NotificationDocument doc) {
		ContextInfo pub = resolveContextInfo(doc.getPublisherId(), doc.getPublisherType());
		ContextInfo sub = resolveContextInfo(doc.getSubscriberId(), doc.getSubscriberType());
		return NotificationContext.builder()
				.publisher(pub)
				.subscriber(sub)
				.build();
	}

	private ContextInfo resolveContextInfo(Long id, NotificationDomainType type) {
		switch (type) {
			case STUDENT -> {
				StudentUnionForNotificationResponseDto findStudent = studentFacade.findByStudentIdForSse(id);
				return StudentUnionForNotificationResponseDto.builder()
						.studentId(findStudent.studentId())
						.name(findStudent.name())
						.position(findStudent.position())
						.track(findStudent.track())
						.profileImageUrl(findStudent.profileImageUrl())
						.isMajor(findStudent.isMajor())
						.build();
			}
			case TEAM -> {
				TeamSimpleResponseDto findTeam = teamService.findSimpleTeamInfoByTeamId(id);
				return TeamSimpleResponseDto.builder()
						.teamId(findTeam.teamId())
						.name(findTeam.name())
						.track(findTeam.track())
						.majorCount(findTeam.majorCount())
						.nonMajorCount(findTeam.nonMajorCount())
						.build();
			}
			default -> {
				log.error("지원하지 않는 도메인 타입: {}: {}", type, this.getClass().getMethods());
				throw new AppException(ErrorCode.NOT_SUPPORTED_TYPE);
			}
		}
	}
}
