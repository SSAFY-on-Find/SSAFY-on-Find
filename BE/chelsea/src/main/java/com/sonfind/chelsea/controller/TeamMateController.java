package com.sonfind.chelsea.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
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
@RequestMapping("/team-mates")
@Tag(name = "Team-mate Controller", description = "팀 합류 지원/초대 요청 API")
public class TeamMateController {

	private final NotificationService notificationService;
	private final URI LOCATION = URI.create("/api/v1/team-mates");

	@Operation(
		summary = "알림 로그 저장",
		description = "요청에 대한 알림 로그를 DB에 저장합니다. " + "알림 발신자와 수신자의 정보를 NotificationParticipant 객체로 생성하고, "
			+ "NotificationDocument 객체를 생성하여 MongoDB에 저장합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "알림 로그 저장 성공", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
	})
	@PostMapping()
	public ResponseEntity<Map<String, Object>> createTeamMateEvent(
		@RequestBody NotificationRequestDto notificationRequestDto) {
		log.info("createTeamMateEvent called");

		try {
			Map<String, Object> body = new HashMap<>();
			body.put("status", "SUCCESS");
			body.put("data", Map.of("notification", notificationService.saveNotification(notificationRequestDto)));
			return ResponseEntity.status(HttpStatus.CREATED)
				.header(HttpHeaders.LOCATION, LOCATION.toString())
				.body(body);
		} catch (Exception e) {
			log.error("Error creating notification: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("error", "Failed to create notification"));
		}
	}
}
