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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
@Slf4j
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
		//기존 전체 목록 조회
		// List<TeamListResponseDto> teamListResponse = teamService.getAllTeams(studentId);

		List<TeamListResponseDto> teamListResponse = teamService.getAllTeamsFetchJoin(studentId);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", teamListResponse);

		return ResponseEntity.ok().body(body);
	}

	// TeamController.java에 추가할 메서드들

	//팀 전체 목록 조회 - 기존 N+1 방식
	@GetMapping("/n-plus-1")
	@Operation(summary = "팀 목록 조회 - 기존 N+1 방식", description = "기존 방식으로 팀 목록을 조회합니다. (N+1 문제 발생)")
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
	public ResponseEntity<Map<String, Object>> getAllTeamsNPlusOne(
		@Parameter(hidden = true) @SessionAttribute("loginUser") Long studentId) {

		long startTime = System.currentTimeMillis();
		List<TeamListResponseDto> teams = teamService.getAllTeams(studentId);
		long endTime = System.currentTimeMillis();

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", teams);
		body.put("executionTime", endTime - startTime);
		body.put("method", "n-plus-1");
		body.put("queryOptimization", false);
		body.put("description", "기존 방식 - N+1 문제 발생 예상");

		log.info("기존 N+1 방식 실행시간: {}ms, 데이터 수: {}개", endTime - startTime, teams.size());
		return ResponseEntity.ok().body(body);
	}

	//팀 전체 목록 조회 - Fetch Join 방식
	@GetMapping("/fetch-join")
	@Operation(summary = "팀 목록 조회 - Fetch Join 방식", description = "Fetch Join을 사용한 최적화된 방식으로 팀 목록을 조회합니다.")
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
	public ResponseEntity<Map<String, Object>> getAllTeamsFetchJoin(
		@Parameter(hidden = true) @SessionAttribute("loginUser") Long studentId) {

		long startTime = System.currentTimeMillis();
		List<TeamListResponseDto> teams = teamService.getAllTeamsFetchJoin(studentId);
		long endTime = System.currentTimeMillis();

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", teams);
		body.put("executionTime", endTime - startTime);
		body.put("method", "fetch-join");
		body.put("queryOptimization", true);
		body.put("description", "Fetch Join 방식 - 연관 엔티티 한번에 조회");

		log.info("Fetch Join 방식 실행시간: {}ms, 데이터 수: {}개", endTime - startTime, teams.size());
		return ResponseEntity.ok().body(body);
	}

	//팀 전체 목록 조회 - DTO Projection 방식
	@GetMapping("/dto-projection")
	@Operation(summary = "팀 목록 조회 - DTO Projection 방식", description = "DTO Projection을 사용한 최적화된 방식으로 팀 목록을 조회합니다.")
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
	public ResponseEntity<Map<String, Object>> getAllTeamsDtoProjection(
		@Parameter(hidden = true) @SessionAttribute("loginUser") Long studentId) {

		long startTime = System.currentTimeMillis();
		List<TeamListResponseDto> teams = teamService.getAllTeamsOptimized(studentId);
		long endTime = System.currentTimeMillis();

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", teams);
		body.put("executionTime", endTime - startTime);
		body.put("method", "dto-projection");
		body.put("queryOptimization", true);
		body.put("description", "DTO Projection 방식 - 필요한 데이터만 조회");

		log.info("DTO Projection 방식 실행시간: {}ms, 데이터 수: {}개", endTime - startTime, teams.size());
		return ResponseEntity.ok().body(body);
	}

	//성능 비교 API
	@GetMapping("/performance-comparison")
	@Operation(summary = "3가지 방식 성능 비교", description = "N+1, Fetch Join, DTO Projection 방식의 성능을 비교합니다.")
	public ResponseEntity<Map<String, Object>> performanceComparison(
		@Parameter(hidden = true) @SessionAttribute("loginUser") Long studentId) {

		log.info("=== 3가지 방식 성능 비교 시작 (studentId: {}) ===", studentId);

		Map<String, Object> results = new HashMap<>();

		// 1. N+1 방식
		long nPlusOneStart = System.currentTimeMillis();
		List<TeamListResponseDto> nPlusOneResult = teamService.getAllTeams(studentId);
		long nPlusOneTime = System.currentTimeMillis() - nPlusOneStart;

		results.put("nPlusOne", Map.of(
			"method", "N+1 방식",
			"executionTime", nPlusOneTime,
			"dataCount", nPlusOneResult.size(),
			"queryOptimization", false
		));

		log.info("N+1 방식: {}ms, 데이터 {}개", nPlusOneTime, nPlusOneResult.size());

		// 잠시 대기
		sleep(500);

		// 2. Fetch Join 방식
		long fetchJoinStart = System.currentTimeMillis();
		List<TeamListResponseDto> fetchJoinResult = teamService.getAllTeamsFetchJoin(studentId);
		long fetchJoinTime = System.currentTimeMillis() - fetchJoinStart;

		double fetchJoinImprovement = calculateImprovement(nPlusOneTime, fetchJoinTime);

		results.put("fetchJoin", Map.of(
			"method", "Fetch Join 방식",
			"executionTime", fetchJoinTime,
			"dataCount", fetchJoinResult.size(),
			"queryOptimization", true,
			"improvement", String.format("%.1f%%", fetchJoinImprovement)
		));

		log.info("Fetch Join 방식: {}ms, 데이터 {}개, 개선율: {:.1f}%",
			fetchJoinTime, fetchJoinResult.size(), fetchJoinImprovement);

		// 잠시 대기
		sleep(500);

		// 3. DTO Projection 방식
		long dtoProjectionStart = System.currentTimeMillis();
		List<TeamListResponseDto> dtoProjectionResult = teamService.getAllTeamsOptimized(studentId);
		long dtoProjectionTime = System.currentTimeMillis() - dtoProjectionStart;

		double dtoProjectionImprovement = calculateImprovement(nPlusOneTime, dtoProjectionTime);

		results.put("dtoProjection", Map.of(
			"method", "DTO Projection 방식",
			"executionTime", dtoProjectionTime,
			"dataCount", dtoProjectionResult.size(),
			"queryOptimization", true,
			"improvement", String.format("%.1f%%", dtoProjectionImprovement)
		));

		log.info("DTO Projection 방식: {}ms, 데이터 {}개, 개선율: {:.1f}%",
			dtoProjectionTime, dtoProjectionResult.size(), dtoProjectionImprovement);

		// 종합 결과
		String bestMethod = determineBestMethod(nPlusOneTime, fetchJoinTime, dtoProjectionTime);
		double maxImprovement = Math.max(fetchJoinImprovement, dtoProjectionImprovement);

		Map<String, Object> summary = Map.of(
			"bestPerformer", bestMethod,
			"maxImprovement", String.format("%.1f%%", maxImprovement),
			"totalTestTime", nPlusOneTime + fetchJoinTime + dtoProjectionTime,
			"recommendation", maxImprovement > 50 ? "최적화 효과가 매우 큽니다!" : "최적화 효과가 있습니다."
		);

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", results);
		body.put("summary", summary);
		body.put("testInfo", Map.of(
			"studentId", studentId,
			"timestamp", System.currentTimeMillis(),
			"note", "각 측정 간 500ms 대기"
		));

		log.info("=== 성능 비교 완료 - 최고 성능: {}, 최대 개선율: {:.1f}% ===", bestMethod, maxImprovement);

		return ResponseEntity.ok().body(body);
	}

	// 헬퍼 메서드들
	private double calculateImprovement(long before, long after) {
		if (before == 0)
			return 0;
		return ((double)(before - after) / before) * 100;
	}

	private String determineBestMethod(long nPlusOneTime, long fetchJoinTime, long dtoProjectionTime) {
		if (dtoProjectionTime <= fetchJoinTime && dtoProjectionTime < nPlusOneTime) {
			return "DTO Projection";
		} else if (fetchJoinTime <= dtoProjectionTime && fetchJoinTime < nPlusOneTime) {
			return "Fetch Join";
		} else {
			return "N+1 (예상치 못한 결과)";
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.warn("Thread interrupted during sleep");
		}
	}
}
