package com.sonfind.chelsea.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authorizeHttpRequests((authorize) -> authorize
			.requestMatchers("/api/v1/auth/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/mates").permitAll()
			.requestMatchers("/api/v1/mates/events").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/teams").permitAll()
			.anyRequest().authenticated());

		return httpSecurity.build();
	}

}
