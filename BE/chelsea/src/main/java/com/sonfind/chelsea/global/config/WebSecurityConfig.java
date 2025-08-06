package com.sonfind.chelsea.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/api/v1/auth/**").permitAll()
				.requestMatchers("/api/v1/students/**").permitAll()
				.requestMatchers("/api/v1/mates/events").permitAll()
				.requestMatchers("/api/v1/teams", "/api/v1/teams/**").permitAll()
				.requestMatchers("/api/v1/notifications/**").permitAll()
				.requestMatchers("/api/v1/invitations/**").permitAll()
				.requestMatchers(
					"/swagger-ui/**",
					"/swagger-ui.html",
					"/v3/api-docs/**",
					"/webjars/**"
				).permitAll()
				.requestMatchers("/**").permitAll()
				.anyRequest().authenticated());

		return httpSecurity.build();
	}
}
