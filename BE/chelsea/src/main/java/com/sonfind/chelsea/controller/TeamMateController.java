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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team-mates")
public class TeamMateController {

	private final NotificationService notificationService;
	private final URI LOCATION = URI.create("/api/v1/team-mates");

	@PostMapping()
	public ResponseEntity<Map<String, Object>> createTeamMateEvent(
		@RequestBody NotificationRequestDto notificationRequestDto
	) {
		log.info("createTeamMateEvent called");

		try {
			Map<String, Object> body = new HashMap<>();
			body.put("status", "SUCCESS");
			body.put("data", Map.of("notification", notificationService.saveNotification(notificationRequestDto)));
			return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, LOCATION.toString()).body(body);
		} catch (Exception e) {
			log.error("Error creating notification: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to create notification"));
		}
	}
}
