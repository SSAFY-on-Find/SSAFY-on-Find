package com.sonfind.chelsea.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.dto.teams.CreateTeamRequest;
import com.sonfind.chelsea.service.TeamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

	private final TeamService teamService;

	@PostMapping
	public ResponseEntity<Map<String, Object>> createTeam(@RequestBody @Valid CreateTeamRequest request,
		@CookieValue("sessionId") String sessionId) {
		//sessionId로 mate 받아옴
		// Long teamId = teamService.createTeam(request, sessionId);
		Long teamId = teamService.createTeam(request);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("teamId", teamId));

		URI location = URI.create("/api/v1/teams/" + teamId);
		return ResponseEntity.created(location).body(body);
	}
}
