package com.sonfind.chelsea.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.manager.SseEmitterManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseService {

	private final SseEmitterManager emitterManager;
	private final StudentFacade studentFacade;

	public void sendNotification(Long subId, Object data) {
		emitterManager.sendTo(subId, data);
	}

	public void broadcastToTeam(Long teamId, NotificationDto<?> payload) {
		List<Students> members = studentFacade.findAllByTeamId(teamId);
		members.forEach(member -> emitterManager.sendTo(member.getStudentId(), payload));
	}

}
