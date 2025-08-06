package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sonfind.chelsea.dto.Favorite.TeamFavoriteRequestDto;
import com.sonfind.chelsea.dto.Favorite.TeamFavoriteResponseDto;
import com.sonfind.chelsea.service.FavoriteService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {
	private final FavoriteService favoriteService;

	@PostMapping("/toggle")
	public ResponseEntity<Map<String, Object>> toggleFavorite(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@RequestBody @Valid TeamFavoriteRequestDto request) {

		TeamFavoriteResponseDto response = favoriteService.toggleFavoriteTeam(studentId, request.teamId());

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", response);

		return ResponseEntity.ok().body(body);
	}

}
