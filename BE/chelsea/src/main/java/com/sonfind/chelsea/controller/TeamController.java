package com.sonfind.chelsea.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sonfind.chelsea.dto.teams.CreateTeamRequestDto;
import com.sonfind.chelsea.dto.teams.LeaveTeamResponseDto;
import com.sonfind.chelsea.dto.teams.MyTeamResponseDto;
import com.sonfind.chelsea.dto.teams.TeamCreatePageDto;
import com.sonfind.chelsea.dto.teams.TeamListResponseDto;
import com.sonfind.chelsea.dto.teams.TeamResponseDto;
import com.sonfind.chelsea.dto.teams.UpdateTeamRequestDto;
import com.sonfind.chelsea.service.TeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

	private final TeamService teamService;

	//팀 생성
	@PostMapping
	@Operation(summary = "팀 생성", description = "새로운 팀을 생성합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "팀 생성 성공",
			content = @io.swagger.v3.oas.annotations.media.Content(
				schema = @io.swagger.v3.oas.annotations.media.Schema(
					type = "object",
					example = "{\"status\": \"SUCCESS\", \"data\": {\"teamId\": 1}}"
				)
			)
		),
	})
	public ResponseEntity<Map<String, Object>> createTeam(@RequestBody @Valid CreateTeamRequestDto request,
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId) {

		Long teamId = teamService.createTeam(studentId, request);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("teamId", teamId));

		URI location = URI.create("/teams/" + teamId);
		return ResponseEntity.created(location).body(body);
	}

	@GetMapping("warmup")
	@Operation(summary = "팀 생성 페이지 조회", description = "팀 생성에 필요한 기본 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "팀 생성 페이지 조회 성공",
			content = @Content(
				schema = @Schema(
					implementation = TeamCreatePageDto.class
				)
			)
		)
	})
	public ResponseEntity<Map<String, Object>> getTeamCreatePage() {
		TeamCreatePageDto teamCreatePageDto = teamService.getTeamCreatePage();

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", teamCreatePageDto);

		URI location = URI.create("/teams/warmup");
		return ResponseEntity.created(location).body(body);
	}

	//팀 수정
	@PatchMapping("/{teamId}")
	@Operation(summary = "팀 수정", description = "기존 팀 정보를 수정합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "팀 수정 성공",
			content = @Content
		),
		@ApiResponse(
			responseCode = "404",
			description = "팀이 존재하지 않거나, 수정 권한이 없는 경우",
			content = @Content(
				schema = @Schema(
					type = "object",
					example = "{\"status\": \"FAIL\", \"message\": \"존재하지 않는 팀입니다.\"}"
				)))
	})
	public ResponseEntity<Map<String, Object>> updateTeam(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long teamId,
		@RequestBody @Valid UpdateTeamRequestDto request) {

		teamService.updateTeam(teamId, studentId, request);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", Map.of("teamId", teamId));

		return ResponseEntity.ok().body(body);
	}

	//팀 나가기
	@DeleteMapping("/leave")
	@Operation(summary = "팀 나가기", description = "현재 소속된 팀에서 나갑니다. 팀이 빈 팀이 되면 자동으로 삭제됩니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "팀 나가기 성공",
			content = @Content(
				schema = @Schema(
					implementation = LeaveTeamResponseDto.class
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "팀에 속해 있지 않습니다.",
			content = @Content(
				schema = @Schema(
					type = "object",
					example = "{\"status\": \"FAIL\", \"message\": \"팀에 속해 있지 않습니다.\"}"
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "존재하지 않는 팀입니다.",
			content = @Content(
				schema = @Schema(
					type = "object",
					example = "{\"status\": \"FAIL\", \"message\": \"존재하지 않는 팀입니다.\"}"
				)
			)
		)
	})
	public ResponseEntity<Map<String, Object>> leaveTeam(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId) {

		LeaveTeamResponseDto response = teamService.leaveTeam(studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", response);

		return ResponseEntity.ok().body(body);
	}

	//팀 합치기 api 테스트용
	// @PostMapping("/merge")
	// public ResponseEntity<Map<String, Object>> mergeTeams(
	// 	@RequestBody @Valid MergeTeamsRequestDto request,
	// 	@Parameter(hidden = true)
	// 	@SessionAttribute("loginUser") Long studentId) {
	//
	// 	teamService.mergeTeams(request.sourceTeamId(), request.targetTeamId());
	//
	// 	Map<String, Object> body = new HashMap<>();
	// 	body.put("status", "SUCCESS");
	// 	body.put("message", "팀이 성공적으로 합쳐졌습니다.");
	// 	body.put("data", Map.of(
	// 		"sourceTeamId", request.sourceTeamId(),
	// 		"targetTeamId", request.targetTeamId()
	// 	));
	//
	// 	return ResponseEntity.ok().body(body);
	// }

	//타 팀 상세조회
	@GetMapping("/{teamId}")
	@Operation(summary = "팀 상세조회", description = "특정 팀의 상세 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "팀 상세조회 성공",
			content = @Content(
				schema = @Schema(
					implementation = TeamResponseDto.class
				)
			)),
		@ApiResponse(
			responseCode = "404",
			description = "존재하지 않는 팀입니다.",
			content = @Content(
				schema = @Schema(
					type = "object",
					example = "{\"status\": \"FAIL\", \"message\": \"존재하지 않는 팀입니다.\"}"
				)))
	})
	public ResponseEntity<Map<String, Object>> getTeamDetail(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable Long teamId) {
		TeamResponseDto teamResponse = teamService.getTeamDetail(teamId, studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", teamResponse);

		return ResponseEntity.ok().body(body);
	}

	//내 팀 상세조회
	@GetMapping("/me")
	@Operation(summary = "내 팀 상세조회", description = "본인의 팀 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "내 팀 상세조회 성공",
			content = @Content(
				schema = @Schema(
					implementation = MyTeamResponseDto.class
				)
			)),
		@ApiResponse(
			responseCode = "404",
			description = "본인의 팀이 존재하지 않습니다.",
			content = @Content(
				schema = @Schema(
					type = "object",
					example = "{\"status\": \"FAIL\", \"message\": \"본인의 팀이 존재하지 않습니다.\"}"
				)))
	})
	public ResponseEntity<Map<String, Object>> getMyTeamDetail(@Parameter(hidden = true)
	@SessionAttribute("loginUser") Long studentId) {
		MyTeamResponseDto myTeamResponse = teamService.getMyTeamDetail(studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", myTeamResponse);

		return ResponseEntity.ok().body(body);
	}

	//팀 전체 목록 조회
	@GetMapping
	@Operation(summary = "팀 전체 목록 조회", description = "모든 팀의 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "팀 목록 조회 성공",
			content = @Content(
				schema = @Schema(
					implementation = TeamListResponseDto.class
				)
			)),
		@ApiResponse(
			responseCode = "404",
			description = "팀이 존재하지 않습니다.",
			content = @Content(
				schema = @Schema(
					type = "object",
					example = "{\"status\": \"FAIL\", \"message\": \"팀이 존재하지 않습니다.\"}"
				)))
	})
	public ResponseEntity<Map<String, Object>> getAllTeams(@Parameter(hidden = true)
	@SessionAttribute("loginUser") Long studentId) {
		List<TeamListResponseDto> teamListResponse = teamService.getAllTeams(studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", teamListResponse);

		return ResponseEntity.ok().body(body);
	}
}
