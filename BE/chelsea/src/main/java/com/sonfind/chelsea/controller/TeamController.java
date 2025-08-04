package com.sonfind.chelsea.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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

	//팀 생성
	@PostMapping
	public ResponseEntity<Map<String, Object>> createTeam(@RequestBody @Valid CreateTeamRequest request,
		@CookieValue("sessionId") Long studentId) {

		Long teamId = teamService.createTeam(studentId, request);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("teamId", teamId));

		URI location = URI.create("/teams/" + teamId);
		return ResponseEntity.created(location).body(body);
	}

	//팀 수정
	@PatchMapping("/{teamId}")
	public ResponseEntity<Map<String, Object>> updateTeam(
		@CookieValue("sessionId") Long studentId,
		@PathVariable Long teamId,
		@RequestBody @Valid UpdateTeamRequest request) {

		teamService.updateTeam(teamId, studentId, request);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("teamId", teamId));

		return ResponseEntity.ok().body(body);
	}
}
