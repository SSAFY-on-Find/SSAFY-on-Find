package com.sonfind.chelsea.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping
@Tag(name = "health-check", description = "Health Check API")
public class HealthCheckApplication {

	@GetMapping("/health-check")
	@Operation(summary = "Health Check", description = "Checks the health status of the application.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Application is healthy"),
		@ApiResponse(responseCode = "500", description = "Application is not healthy")
	})
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("updated-healthy");
	}
}
