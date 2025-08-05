package com.sonfind.chelsea.global.commonSwagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation
@ApiResponses(value = {
	@ApiResponse(responseCode = "201", description = "데이터 생성 성공"),
	@ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content)
})
public @interface ApiCreateOperation {

	@AliasFor(annotation = Operation.class, attribute = "summary")
	String summary() default "";

	@AliasFor(annotation = Operation.class, attribute = "description")
	String description() default "";
}
