package com.sonfind.chelsea.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sonfind.chelsea.global.manager.SseEmitterManager;
import com.sonfind.chelsea.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
	public ResponseEntity<SseEmitter> subscribe(
		@Parameter(hidden = true) @SessionAttribute("loginUser") Long studentId) {
		log.info("subscribe called with pubId: {}", studentId);
		SseEmitter emitter = sseEmitterManager.connect(studentId);
		if (emitter == null) {
			log.error("Failed to create SSE emitter for user ID: {}", studentId);
			return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(null);
		}

		return ResponseEntity.ok(emitter);
	}

	@GetMapping()
	@Operation(summary = "개인 초대/지원 알림 조회", description = "사용자의 개인 초대 및 지원 알림을 조회합니다. " + "알림 타입을 통해 필터링할 수 있습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "알림 조회 성공", content = @Content),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
	})
	public ResponseEntity<? extends Map<String, ? extends Object>> getPersonalInvitationList(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@RequestParam("type") String type
	) {
		try {
			Map<String, Object> body = new HashMap<>();
			body.put("status", "SUCCESS");
			body.put("data", Map.of("notification", notificationService.getMyNotifications(studentId, type)));
			return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.LOCATION, LOCATION.toString())
				.body(body);
		} catch (Exception e) {
			log.error("Error creating notification: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("error", "Failed to find notification"));
		}
	}

	@GetMapping("/{teamId}")
	@Operation(summary = "팀 초대/지원 알림 조회", description = "특정 팀에 대한 알림을 조회합니다. " + "팀 ID와 알림 타입을 통해 필터링할 수 있습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "알림 조회 성공", content = @Content),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
	})
	public ResponseEntity<? extends Map<String, ? extends Object>> getTeamInvitationList(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long teamId,
		@RequestParam("type") String type
	) throws BadRequestException {
		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("notification", notificationService.getTeamNotifications(studentId, teamId, type)));
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.LOCATION, LOCATION.toString())
			.body(body);

	}

	@GetMapping("/count")
	@Operation(summary = "알림 개수 조회", description = "사용자의 읽지 않은 알림 개수를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "알림 개수 조회 성공", content = @Content),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
	})
	public ResponseEntity<? extends Map<String, ? extends Object>> getCountOfNonReadNotifications(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId
	) {
		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("nonReadCount", notificationService.getCountOfNonReadNotifications(studentId)));
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.LOCATION, LOCATION.toString())
			.body(body);

	}
}
