package com.sonfind.chelsea.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;
import org.springframework.data.mongodb.MongoManagedTypes;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * MongoDB에서 필드명을 자동으로 snake_case로 변환시켜주기 위한 설정 파일 입니다.
 * 변수명과 필드명이 상이할 시 `@Field` Annotation을 사용하여 커스텀을 수행하시면 됩니다.
 */
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
	@Override
	protected String getDatabaseName() {
		return "sof";  // spring.data.mongodb.database 대신
	}

	@Override
	public MongoMappingContext mongoMappingContext(
		MongoCustomConversions customConversions,
		MongoManagedTypes mongoManagedTypes) {
		MongoMappingContext ctx = super.mongoMappingContext(customConversions, mongoManagedTypes);
		ctx.setFieldNamingStrategy(new SnakeCaseFieldNamingStrategy());
		return ctx;
	}
}
