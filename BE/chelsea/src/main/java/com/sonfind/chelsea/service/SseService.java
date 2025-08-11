package com.sonfind.chelsea.service;

import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.manager.SseEmitterManager;
import com.sonfind.chelsea.types.NotificationDomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SseService {

	private final SseEmitterManager emitterManager;
	private final StudentFacade studentFacade;

	// 개인용
	public void sendNotification(Long userId, Object payload) {
		emitterManager.sendTo(userId, payload);
	}

	// 팀용
	public void broadcastToTeam(Long teamId, Object payload) {
		studentFacade.findAllByTeamId(teamId)
				.forEach(member -> emitterManager.sendTo(member.getStudentId(), payload));
	}

	// 모든 사용자에게 브로드캐스트(eg. 대시보드)
	public void broadcastToAll(String eventName, Object payload) {
		emitterManager.broadcast(eventName, payload);
	}

	// domainType/id 에 따라 자동으로 분기 처리
	public void dispatch(
			Long id,
			NotificationDomainType domainType,
			Object payload
	) {
		if (domainType == NotificationDomainType.TEAM) {
			broadcastToTeam(id, payload);
		} else {
			sendNotification(id, payload);
		}
	}

}
