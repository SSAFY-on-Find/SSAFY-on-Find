package com.sonfind.chelsea.service;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.global.manager.SseEmitterManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseService {

	private final SseEmitterManager emitterManager;

	public void sendNotification(Long subId, Object data) {
		emitterManager.sendTo(subId, data);
	}
}
