package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sonfind.chelsea.service.TeamService;
import com.sonfind.chelsea.util.PerformanceMeasurer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/teams")
@Slf4j
public class TeamPerformanceTestController {

	private final TeamService teamService;
	private final PerformanceMeasurer performanceMeasurer;

	@GetMapping("/performance/simple")
	@Operation(summary = "[간단] 현재 구현된 방식들만 테스트", description = "구현된 메서드들만 간단히 성능 테스트")
	public ResponseEntity<Map<String, Object>> simplePerformanceTest(
		@Parameter(hidden = true) @SessionAttribute("loginUser") Long studentId) {

		Map<String, Object> results = new HashMap<>();

		// 기존 방식은 항상 있을 것
		long originalTime = measureExecutionTime(() -> teamService.getAllTeams(studentId));
		results.put("original", Map.of(
			"executionTime", originalTime,
			"method", "기존 N+1 방식"
		));

		log.info("기존 방식 실행시간: {}ms", originalTime);

		// 최적화 방식들 확인
		try {
			sleep(500);
			long optimizedTime = measureExecutionTime(() -> teamService.getAllTeamsOptimized(studentId));
			results.put("optimized", Map.of(
				"executionTime", optimizedTime,
				"method", "DTO 프로젝션 방식",
				"improvement", String.format("%.1f%%", calculateImprovement(originalTime, optimizedTime))
			));
			log.info("DTO 프로젝션 방식 실행시간: {}ms", optimizedTime);
		} catch (Exception e) {
			results.put("optimized", Map.of("error", "구현되지 않음: " + e.getMessage()));
		}

		try {
			sleep(500);
			long fetchJoinTime = measureExecutionTime(() -> teamService.getAllTeamsFetchJoin(studentId));
			results.put("fetchJoin", Map.of(
				"executionTime", fetchJoinTime,
				"method", "Fetch Join 방식",
				"improvement", String.format("%.1f%%", calculateImprovement(originalTime, fetchJoinTime))
			));
			log.info("Fetch Join 방식 실행시간: {}ms", fetchJoinTime);
		} catch (Exception e) {
			results.put("fetchJoin", Map.of("error", "구현되지 않음: " + e.getMessage()));
		}

		return ResponseEntity.ok(Map.of(
			"status", "SUCCESS",
			"data", results,
			"testStudentId", studentId
		));
	}

	@GetMapping("/performance/detailed")
	@Operation(summary = "[상세] 쿼리 수 포함 상세 비교", description = "Hibernate 통계를 포함한 상세한 성능 비교")
	public ResponseEntity<Map<String, Object>> detailedPerformanceTest(
		@Parameter(hidden = true) @SessionAttribute("loginUser") Long studentId) {

		log.info("=== 상세 성능 비교 테스트 시작 (studentId: {}) ===", studentId);

		Map<String, Object> results = new HashMap<>();

		// 1. 기존 방식 측정
		PerformanceMeasurer.PerformanceResult originalResult = performanceMeasurer.measureWithHibernateStats(
			"기존 N+1 방식",
			() -> teamService.getAllTeams(studentId)
		);

		results.put("original", Map.of(
			"executionTime", originalResult.executionTime,
			"queryCount", originalResult.queryCount,
			"entityLoadCount", originalResult.entityLoadCount,
			"method", "N+1 쿼리 방식"
		));

		// 2. 최적화 방식들 테스트
		sleep(1000);

		try {
			PerformanceMeasurer.PerformanceResult optimizedResult = performanceMeasurer.measureWithHibernateStats(
				"DTO 프로젝션 방식",
				() -> teamService.getAllTeamsOptimized(studentId)
			);

			results.put("optimized", Map.of(
				"executionTime", optimizedResult.executionTime,
				"queryCount", optimizedResult.queryCount,
				"entityLoadCount", optimizedResult.entityLoadCount,
				"method", "DTO 프로젝션 방식",
				"timeImprovement", String.format("%.1f%%",
					calculateImprovement(originalResult.executionTime, optimizedResult.executionTime)),
				"queryImprovement",
				String.format("%.1f%%", calculateImprovement(originalResult.queryCount, optimizedResult.queryCount))
			));

		} catch (Exception e) {
			results.put("optimized", Map.of("error", "구현되지 않음: " + e.getMessage()));
		}

		sleep(1000);

		try {
			PerformanceMeasurer.PerformanceResult fetchJoinResult = performanceMeasurer.measureWithHibernateStats(
				"Fetch Join 방식",
				() -> teamService.getAllTeamsFetchJoin(studentId)
			);

			results.put("fetchJoin", Map.of(
				"executionTime", fetchJoinResult.executionTime,
				"queryCount", fetchJoinResult.queryCount,
				"entityLoadCount", fetchJoinResult.entityLoadCount,
				"method", "Fetch Join 방식",
				"timeImprovement", String.format("%.1f%%",
					calculateImprovement(originalResult.executionTime, fetchJoinResult.executionTime)),
				"queryImprovement",
				String.format("%.1f%%", calculateImprovement(originalResult.queryCount, fetchJoinResult.queryCount))
			));

		} catch (Exception e) {
			results.put("fetchJoin", Map.of("error", "구현되지 않음: " + e.getMessage()));
		}

		Map<String, Object> response = Map.of(
			"status", "SUCCESS",
			"data", results,
			"summary", Map.of(
				"testStudentId", studentId,
				"originalQueryCount", originalResult.queryCount,
				"originalExecutionTime", originalResult.executionTime
			)
		);

		log.info("=== 상세 성능 비교 완료 ===");
		return ResponseEntity.ok(response);
	}

	private double calculateImprovement(long before, long after) {
		if (before == 0)
			return 0;
		return ((double)(before - after) / before) * 100;
	}

	private long measureExecutionTime(Supplier<Object> operation) {
		long start = System.currentTimeMillis();
		operation.get();
		return System.currentTimeMillis() - start;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}