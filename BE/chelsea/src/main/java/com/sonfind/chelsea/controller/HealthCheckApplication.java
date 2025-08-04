package com.sonfind.chelsea.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HealthCheckApplication {

	@GetMapping("/health-check")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("updated-healthy");
	}
}
