package com.sonfind.chelsea.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sonfind.chelsea.dto.Favorite.StudentFavoriteRequestDto;
import com.sonfind.chelsea.dto.Favorite.StudentFavoriteResponseDto;
import com.sonfind.chelsea.dto.Favorite.TeamFavoriteRequestDto;
import com.sonfind.chelsea.dto.Favorite.TeamFavoriteResponseDto;
import com.sonfind.chelsea.service.FavoriteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
@Tag(name = "favorites", description = "즐겨찾기 관련 API")
public class FavoriteController {
	private final FavoriteService favoriteService;

	@PostMapping("/teams/toggle")
	@Operation(
		summary = "팀 즐겨찾기 토글",
		description = "팀 ID를 통해 해당 팀의 즐겨찾기를 토글합니다. 이미 즐겨찾기한 팀은 제거하고, 그렇지 않은 팀은 추가합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "즐겨찾기 수정 완료",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = TeamFavoriteResponseDto.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "학생 정보가 없거나, 요청한 팀이 삭제된 상태입니다.",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = "{ \"status\": \"FAIL\", \"message\": \"학생 없음\" }"
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "존재하지 않는 팀입니다.",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = "{ \"status\": \"FAIL\", \"message\": \"존재하지 않는 팀입니다.\" }"
				)
			)
		)
	})
	public ResponseEntity<Map<String, Object>> toggleTeamFavorite(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@RequestBody @Valid TeamFavoriteRequestDto request) {

		TeamFavoriteResponseDto response = favoriteService.toggleFavoriteTeam(studentId, request.teamId());

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", response);

		return ResponseEntity.ok().body(body);
	}

	@PostMapping("/students/toggle")
	@Operation(
		summary = "교육생 즐겨찾기 토글",
		description = "교육생 ID를 통해 해당 교육생의 즐겨찾기를 토글합니다. 이미 즐겨찾기한 교육생은 제거하고, 그렇지 않은 교육생은 추가합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "교육생 즐겨찾기 수정 완료",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = StudentFavoriteResponseDto.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "학생 정보가 없거나, 본인을 좋아요할 수 없습니다.",
			content = @Content(
				mediaType = "application/json",
				examples = {
					@ExampleObject(
						name = "학생 없음",
						value = "{ \"status\": \"FAIL\", \"message\": \"학생 없음\" }"
					),
					@ExampleObject(
						name = "본인 좋아요 방지",
						value = "{ \"status\": \"FAIL\", \"message\": \"본인을 좋아요할 수 없습니다.\" }"
					)
				}
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "존재하지 않는 교육생입니다.",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = "{ \"status\": \"FAIL\", \"message\": \"존재하지 않는 교육생입니다.\" }"
				)
			)
		)
	})
	public ResponseEntity<Map<String, Object>> toggleStudentFavorite(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@RequestBody @Valid StudentFavoriteRequestDto request) {

		StudentFavoriteResponseDto response = favoriteService.toggleFavoriteStudent(studentId,
			request.targetStudentId());

		Map<String, Object> body = new HashMap<>();
		body.put("status", "SUCCESS");
		body.put("data", response);

		return ResponseEntity.ok().body(body);
	}
}
