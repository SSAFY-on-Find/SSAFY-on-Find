package com.sonfind.chelsea.service;

import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.manager.SseEmitterManager;
import com.sonfind.chelsea.types.NotificationDomainType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

	private final SseEmitterManager emitterManager;
	private final StudentFacade studentFacade;

	// 개인용
	public void sendNotification(Long userId, String eventName, Object payload) {
		emitterManager.sendTo(userId, eventName, payload);
		log.info("SSE로 사용자 {}에게 알림을 전송했습니다.", userId);
	}

	// 팀용
	public void broadcastToTeam(Long teamId, String eventName, Object payload) {
		studentFacade.findAllByTeamId(teamId)
				.forEach(member -> emitterManager.sendTo(member.getStudentId(), eventName, payload));
	}

	// 모든 사용자에게 브로드캐스트(eg. 대시보드)
	public void broadcastToAll(String eventName, Object payload) {
		emitterManager.broadcast(eventName, payload);
	}

	// domainType/id 에 따라 자동으로 분기 처리
	public void dispatch(
			Long id,
			NotificationDomainType domainType,
			String eventType,
			Object payload
	) {
		if (domainType == NotificationDomainType.TEAM) {
			broadcastToTeam(id, eventType, payload);
		} else {
			sendNotification(id, eventType, payload);
		}

	}

}
