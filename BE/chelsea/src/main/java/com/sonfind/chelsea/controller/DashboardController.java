package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sonfind.chelsea.dto.dashboard.PositionMajorRatioResponseDto;
import com.sonfind.chelsea.dto.dashboard.TeamRatioDto;
import com.sonfind.chelsea.dto.teams.TeamListResponseDto;
import com.sonfind.chelsea.service.StudentInfoService;
import com.sonfind.chelsea.service.StudentService;
import com.sonfind.chelsea.service.TeamService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
@Tag(name = "dashboard", description = "대시보드와 관련된 API")
public class DashboardController {

	private final StudentInfoService studentInfoService;
	private final StudentService studentService;
	private final TeamService teamService;

	@GetMapping("/position-ratio")
	public ResponseEntity<Map<String, Object>> getTrackPositionRatio() {

		Map<String, PositionMajorRatioResponseDto> data = studentInfoService.getPositionRatio();

		HashMap<String, Object> body = new HashMap<>();

		body.put("status", "SUCCESS");
		body.put("data", data);

		return ResponseEntity.ok().body(body);
	}

	@GetMapping("/team-ratio")
	public ResponseEntity<Map<String, Object>> getTeamRatio() {

		Map<String, TeamRatioDto> data = studentService.getTeamRatio();

		HashMap<String, Object> body = new HashMap<>();

		body.put("status", "SUCCESS");
		body.put("data", data);

		return ResponseEntity.ok().body(body);
	}

	@GetMapping("/team-recommend")
	public ResponseEntity<Map<String, Object>> getTeamRecommend(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId
	) {

		HashMap<String, Object> body = new HashMap<>();

		List<TeamListResponseDto> data = teamService.getRecommendTeamList(studentId);

		body.put("status", "SUCCESS");
		if (data.getFirst().teamId() == 0) {
			body.put("data", Map.of("message", "생성된 팀이 없습니다."));
		} else if (data.isEmpty()) {
			body.put("data", Map.of("message", "추천된 팀이 없습니다."));
		} else {
			body.put("data", data);
		}

		return ResponseEntity.ok().body(body);
	}

}
