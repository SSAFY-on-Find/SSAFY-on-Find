package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonfind.chelsea.dto.dashboard.TeamRatioDto;
import com.sonfind.chelsea.dto.dashboard.TrackPositionMajorRatioResponseDto;
import com.sonfind.chelsea.service.StudentInfoService;
import com.sonfind.chelsea.service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@Tag(name = "dashboard", description = "대시보드와 관련된 API")
public class DashboardController {

	private final StudentInfoService studentInfoService;
	private final StudentService studentService;

	@GetMapping("/track-position-ratio")
	public ResponseEntity<Map<String, Object>> getTrackPositionRatio() {

		List<TrackPositionMajorRatioResponseDto> data = studentInfoService.getTrackPositionRatio();

		HashMap<String, Object> body = new HashMap<>();

		body.put("status", "SUCCESS");
		body.put("data", data);

		return ResponseEntity.ok().body(body);
	}

	@GetMapping("/team-ratio")
	public ResponseEntity<Map<String, Object>> getTeamRatio() {

		List<TeamRatioDto> data = studentService.getTeamRatio();

		HashMap<String, Object> body = new HashMap<>();

		body.put("status", "SUCCESS");
		body.put("data", data);

		return ResponseEntity.ok().body(body);
	}

}
