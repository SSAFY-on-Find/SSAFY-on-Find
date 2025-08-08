package com.sonfind.chelsea.global.error;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// 비즈니스 예외
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest req) {
		ErrorCode ec = ex.getErrorCode();
		log.warn("Business exception: {} - {}", ec.name(), ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of(ex.getMessage(), req.getRequestURI());
		return ResponseEntity.status(ec.status).body(errorResponse);
	}

	// @Valid DTO 바인딩 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalid(MethodArgumentNotValidException ex, HttpServletRequest req) {
		Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
			.collect(Collectors.toMap(FieldError::getField,
				fe -> Optional.ofNullable(fe.getDefaultMessage()).orElse("invalid"), (a, b) -> a));

		String message = fieldErrors.isEmpty()
			? "요청 값이 올바르지 않습니다."
			: fieldErrors.values().iterator().next();

		ApiErrorResponse errorResponse = ApiErrorResponse.ofValidation(message, req.getRequestURI(), fieldErrors);

		log.warn("Validation failed: {}", fieldErrors);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 파라미터 타입/누락/json 파싱 실패
	@ExceptionHandler({
		MissingServletRequestParameterException.class,
		HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class
	})
	public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex, HttpServletRequest req) {
		log.warn("Bad request: {}", ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("요청 값이 올바르지 않습니다.", req.getRequestURI());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 쿼리 파라미터/경로 변수에 대한 @Validated(메소드 레벨) 위반
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
		HttpServletRequest req) {

		Map<String, String> fieldErrors = ex.getConstraintViolations().stream()
			.collect(Collectors.toMap(v -> v.getPropertyPath().toString(),
				v -> v.getMessage(), (a, b) -> a));

		String message = fieldErrors.isEmpty()
			? "요청 값이 올바르지 않습니다."
			: fieldErrors.values().iterator().next();

		ApiErrorResponse errorResponse = ApiErrorResponse.ofValidation(message, req.getRequestURI(), fieldErrors);

		log.warn("Constraint violation: {}", fieldErrors);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 파일 업로드 크기 초과
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiErrorResponse> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex,
		HttpServletRequest req) {
		log.warn("File size exceeded: {}", ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("파일 크기가 너무 큽니다.", req.getRequestURI());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 파일 업로드 관련 예외 (Apache Commons FileUpload)
	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<ApiErrorResponse> handleFileUpload(FileUploadException ex, HttpServletRequest req) {
		log.warn("File upload error: {}", ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("파일 업로드 중 오류가 발생했습니다.", req.getRequestURI());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// Security (컨트롤러 이후 레이어에서 발생한 경우)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
		log.warn("Access denied: {}", ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("접근 권한이 없습니다.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	}

	// DB 제약 위반 충돌
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
		log.warn("Data integrity violation: {}", ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("데이터 제약 조건을 위반했습니다.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

	// HTTP 메서드 미지원
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
		HttpServletRequest req) {
		log.warn("Method not allowed: {}", ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("지원하지 않는 HTTP 메서드입니다.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
	}

	// 기존 ResponseStatusException 호환성 유지
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiErrorResponse> handleResponseStatusException(ResponseStatusException ex,
		HttpServletRequest req) {
		log.warn("ResponseStatusException: {} - {}", ex.getStatusCode(), ex.getReason());

		String message = Optional.ofNullable(ex.getReason()).orElse("서버 오류가 발생했습니다.");
		ApiErrorResponse errorResponse = ApiErrorResponse.of(message, req.getRequestURI());
		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}

	// Apache Coyote BadRequestException (NotificationService에서 사용)
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest req) {
		log.warn("BadRequestException: {}", ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of(ex.getMessage(), req.getRequestURI());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// JPA EntityNotFoundException
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req) {
		log.warn("Entity not found: {}", ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("요청한 리소스를 찾을 수 없습니다.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	// 안전망
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleEtc(Exception ex, HttpServletRequest req) {
		log.error("Unhandled Exception", ex);

		ApiErrorResponse errorResponse = ApiErrorResponse.of("서버 내부 오류가 발생했습니다.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}