package com.sonfind.chelsea.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sonfind.chelsea.global.manager.SseEmitterManager;
import com.sonfind.chelsea.service.SseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {

	private final SseEmitterManager sseEmitterManager;
	private final SseService sseService;

	@GetMapping("/subscribe")
	public SseEmitter subscribe(@RequestParam Long subId) {
		SseEmitter emitter = sseEmitterManager.connect(subId);
		if (emitter == null) {
			throw new IllegalArgumentException("Failed to create SSE emitter for user ID: " + subId);
		}
		return emitter;
	}

	@GetMapping("/send")
	public String sendDemo(@RequestParam Long subId) {
		try {
			Map<String, Object> data = Map.of(
				"event", "demo",
				"type", "hello",
				"message", "Hello SSE! subId=" + subId,
				"timestamp", System.currentTimeMillis()
			);
			sseService.sendNotification(subId, data);
			return "SSE event sent successfully to subId: " + subId;
		} catch (Exception e) {
			return "Failed to send SSE event: " + e.getMessage();
		}
	}
}
