package com.sonfind.chelsea.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.dto.teams.CreateTeamRequestDto;
import com.sonfind.chelsea.dto.teams.MyTeamResponseDto;
import com.sonfind.chelsea.dto.teams.TeamListResponseDto;
import com.sonfind.chelsea.dto.teams.TeamResponseDto;
import com.sonfind.chelsea.dto.teams.UpdateTeamRequestDto;
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
	public ResponseEntity<Map<String, Object>> createTeam(@RequestBody @Valid CreateTeamRequestDto request,
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
		@RequestBody @Valid UpdateTeamRequestDto request) {

		teamService.updateTeam(teamId, studentId, request);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("teamId", teamId));

		return ResponseEntity.ok().body(body);
	}

	//타 팀 상세조회
	@GetMapping("/{teamId}")
	public ResponseEntity<Map<String, Object>> getTeamDetail(
		@CookieValue("sessionId") Long studentId,
		@PathVariable Long teamId) {
		TeamResponseDto teamResponse = teamService.getTeamDetail(teamId, studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", teamResponse);

		return ResponseEntity.ok().body(body);
	}

	//내 팀 상세조회
	@GetMapping("/me")
	public ResponseEntity<Map<String, Object>> getMyTeamDetail(@CookieValue("sessionId") Long studentId) {
		MyTeamResponseDto myTeamResponse = teamService.getMyTeamDetail(studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", myTeamResponse);

		return ResponseEntity.ok().body(body);
	}

	//팀 전체 목록 조회
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllTeams(@CookieValue("sessionId") Long studentId) {
		List<TeamListResponseDto> teamListResponse = teamService.getAllTeams(studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", teamListResponse);

		return ResponseEntity.ok().body(body);
	}

}
