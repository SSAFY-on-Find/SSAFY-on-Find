package com.sonfind.chelsea.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sonfind.chelsea.types.FileType;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${upload.dir:/uploads}")
	private String uploadDirPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//web에서 접근할 URL 경로 설정
		String profileWebPath = "/uploads/profiles/**";
		String portfolioWebPath = "/uploads/portfolios/**";

		String profileResourcePath = "file:///" + uploadDirPath + "/" + FileType.PROFILE.getDirectoryName() + "/";
		String portfolioResourcePath = "file:///" + uploadDirPath + "/" + FileType.PORTFOLIO.getDirectoryName() + "/";

		registry.addResourceHandler(profileWebPath)
			.addResourceLocations(profileResourcePath);

		registry.addResourceHandler(portfolioWebPath)
			.addResourceLocations(portfolioResourcePath);

	}
}
