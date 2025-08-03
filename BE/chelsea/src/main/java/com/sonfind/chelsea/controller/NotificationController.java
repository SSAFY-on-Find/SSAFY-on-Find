package com.sonfind.chelsea.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sonfind.chelsea.global.manager.SseEmitterManager;
import com.sonfind.chelsea.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Tag(name = "Notification Controller", description = "팀 합류 지원/초대 요청 API")
public class NotificationController {

	private final SseEmitterManager sseEmitterManager;
	private final NotificationService notificationService;
	private final URI LOCATION = URI.create("/api/v1/notifications");

	@GetMapping("/subscribe")
	@Operation(summary = "SSE 구독", description = "SSE를 통해 알림을 구독합니다. " + "구독자는 자신의 ID를 통해 알림을 받을 수 있습니다.")
	@ApiResponses({@ApiResponse(responseCode = "200", description = "SSE 구독 성공", content = @Content),
					@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)})
	public ResponseEntity<SseEmitter> subscribe(Long subId) {
		log.info("subscribe called with pubId: {}", subId);
		SseEmitter emitter = sseEmitterManager.connect(subId);
		if (emitter == null) {
			log.error("Failed to create SSE emitter for user ID: {}", subId);
			return ResponseEntity.badRequest().body(null);
		}

		return ResponseEntity.ok(emitter);
	}

	@GetMapping()
	public ResponseEntity<? extends Map<String, ? extends Object>> getPersonalInvitationList(
					@CookieValue("sessionId") Long studentId,
					@RequestParam("type") String type
	) {
		try {
			Map<String, Object> body = new HashMap<>();
			body.put("status", "SUCCESS");
			body.put("data", Map.of("notification", notificationService.getMyNotifications(studentId, type)));
			return ResponseEntity.status(HttpStatus.OK)
							.header(HttpHeaders.LOCATION, LOCATION.toString())
							.body(body);
		} catch (Exception e) {
			log.error("Error creating notification: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(Map.of("error", "Failed to find notification"));
		}
	}

	@GetMapping("/{teamId}")
	public ResponseEntity<? extends Map<String, ? extends Object>> getTeamInvitationList(
					@CookieValue("sessionId") Long studentId,
					@PathVariable Long teamId,
					@RequestParam("type") String type
	) throws BadRequestException {
		try {
			Map<String, Object> body = new HashMap<>();
			body.put("status", "SUCCESS");
			body.put("data", Map.of("notification", notificationService.getTeamNotifications(studentId, teamId, type)));
			return ResponseEntity.status(HttpStatus.OK)
							.header(HttpHeaders.LOCATION, LOCATION.toString())
							.body(body);
		} catch (Exception e) {
			log.error("Error creating notification: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(Map.of("error", "Failed to find notification"));
		}
	}
}
