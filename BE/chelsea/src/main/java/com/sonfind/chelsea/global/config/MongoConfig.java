package com.sonfind.chelsea.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * MongoDB에서 필드명을 자동으로 snake_case로 변환시켜주기 위한 설정 파일 입니다.
 * 변수명과 필드명이 상이할 시 `@Field` Annotation을 사용하여 커스텀을 수행하시면 됩니다.
 */
@Configuration
public class MongoConfig {

	@Bean
	public MongoMappingContext mongoMappingContext(MongoCustomConversions conversions) {
		MongoMappingContext ctx = new MongoMappingContext();
		ctx.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
		ctx.setFieldNamingStrategy(new SnakeCaseFieldNamingStrategy());
		return ctx;
	}
}
