package com.sonfind.chelsea.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonCustomizer {
	@Bean("jacksonNullOmitCustomizer")
	Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {

		return builder -> {
			builder.serializationInclusion(JsonInclude.Include.NON_NULL);
			builder.featuresToDisable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		};
	}
}
