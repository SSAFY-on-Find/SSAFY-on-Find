package com.sonfind.chelsea.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.dto.teams.CreateTeamRequest;
import com.sonfind.chelsea.dto.teams.UpdateTeamRequest;
import com.sonfind.chelsea.service.TeamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

	private final TeamService teamService;

	//공통: mate 추가해야 함

	//팀 생성
	@PostMapping
	public ResponseEntity<Map<String, Object>> createTeam(@RequestBody @Valid CreateTeamRequest request,
		@CookieValue("sessionId") String sessionId) {

		Long teamId = teamService.createTeam(request);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("teamId", teamId));

		URI location = URI.create("/api/v1/teams/" + teamId);
		return ResponseEntity.created(location).body(body);
	}

	//팀 생성
	@PatchMapping("/{teamId}")
	public ResponseEntity<Map<String, Object>> updateTeam(
		@PathVariable Long teamId,
		@CookieValue("sessionId") String sessionId,
		@RequestBody @Valid UpdateTeamRequest request) {

		teamService.updateTeamInfo(teamId, sessionId, request);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("teamId", teamId));

		URI location = URI.create("/api/v1/teams/" + teamId);
		return ResponseEntity.created(location)
			.contentType(MediaType.APPLICATION_JSON)
			.body(body);
	}
}

