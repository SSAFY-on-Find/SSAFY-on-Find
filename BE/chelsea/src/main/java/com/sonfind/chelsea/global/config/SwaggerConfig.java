package com.sonfind.chelsea.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
			.info(new Info()
				.title("sonfind API")
				.description("팀 빌딩 서비스 Sonfind의 API 문서입니다.")
				.version("v0.0.1")
				.contact(new Contact().name("Sonfind").email("ssafy@ssafy.com"))
			)
			.servers(List.of(
				new Server().url("https://i13a704.p.ssafy.io/api/v1").description("Production Server"),
				new Server().url("http://localhost:8080/api/v1").description("Local Server")
			));
	}
}
