package com.sonfind.chelsea.global.error;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

	// 비즈니스 예외 - 여러 에러를 지원하도록 확장 가능
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest req) {
		ErrorCode ec = ex.getErrorCode();

		if (ec.status.is5xxServerError()) {
			log.error("Business exception: {} - {}", ec.name(), ex.getMessage());
		} else {
			log.warn("Business exception: {} - {}", ec.name(), ex.getMessage());
		}

		ApiErrorResponse errorResponse = ApiErrorResponse.of(ex.getMessage(), req.getRequestURI());
		return ResponseEntity.status(ec.status).body(errorResponse);
	}

	// @Valid DTO 바인딩 실패 - 개선된 버전
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
		HttpServletRequest req) {
		Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				fe -> Optional.ofNullable(fe.getDefaultMessage()).orElse("잘못된 값입니다."),
				(existing, replacement) -> existing // 중복 키 처리
			));

		// 스마트한 메시지 생성 사용
		ApiErrorResponse errorResponse = ApiErrorResponse.ofValidationSmart(req.getRequestURI(), fieldErrors);

		log.warn("Validation failed for {}: {} errors", req.getRequestURI(), fieldErrors.size());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 쿼리 파라미터/경로 변수 Validation 실패
	// Constraint Violation - 개선된 버전
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
		HttpServletRequest req) {
		Map<String, String> fieldErrors = ex.getConstraintViolations().stream()
			.collect(Collectors.toMap(
				v -> getSimplePropertyPath(v.getPropertyPath().toString()),
				v -> v.getMessage(),
				(existing, replacement) -> existing
			));

		ApiErrorResponse errorResponse = ApiErrorResponse.ofValidationSmart(req.getRequestURI(), fieldErrors);

		log.warn("Constraint violation for {}: {} errors", req.getRequestURI(), fieldErrors.size());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 파라미터 관련 예외들
	@ExceptionHandler({
		MissingServletRequestParameterException.class,
		HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class
	})
	public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex, HttpServletRequest req) {
		String message = switch (ex) {
			case MissingServletRequestParameterException e -> "필수 파라미터가 누락되었습니다: " + e.getParameterName();
			case MethodArgumentTypeMismatchException e -> "잘못된 파라미터 형식입니다: " + e.getName();
			default -> "요청 값이 올바르지 않습니다.";
		};

		log.warn("Bad request for {}: {} - {}", req.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of(message, req.getRequestURI());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 파일 업로드 크기 초과
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiErrorResponse> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex,
		HttpServletRequest req) {
		log.warn("File size exceeded for {}: max size = {}", req.getRequestURI(), ex.getMaxUploadSize());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("파일 크기가 너무 큽니다.", req.getRequestURI());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 파일 업로드 관련 예외
	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<ApiErrorResponse> handleFileUpload(FileUploadException ex, HttpServletRequest req) {
		log.warn("File upload error for {}: {}", req.getRequestURI(), ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("파일 업로드 중 오류가 발생했습니다.", req.getRequestURI());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// NOTE: Security 예외는 RestAuthenticationEntryPoint와 RestAccessDeniedHandler에서 처리
	// 이 핸들러는 컨트롤러 이후 레이어에서 발생한 극히 드문 경우만 처리
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
		log.warn("Access denied in controller layer for {}: {}", req.getRequestURI(), ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("접근 권한이 없습니다.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	}

	// DB 제약 위반
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,
		HttpServletRequest req) {
		log.warn("Data integrity violation for {}: {}", req.getRequestURI(), ex.getMessage());

		// 구체적인 에러 메시지는 보안상 숨기고 일반적인 메시지로 대체
		ApiErrorResponse errorResponse = ApiErrorResponse.of("데이터 처리 중 오류가 발생했습니다.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

	// HTTP 메서드 미지원
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
		HttpServletRequest req) {
		log.warn("Method not allowed for {}: {} (supported: {})", req.getRequestURI(), ex.getMethod(),
			ex.getSupportedMethods());

		String message = String.format("지원하지 않는 HTTP 메서드입니다. (%s)", ex.getMethod());
		ApiErrorResponse errorResponse = ApiErrorResponse.of(message, req.getRequestURI());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
	}

	// JPA EntityNotFoundException
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req) {
		log.warn("Entity not found for {}: {}", req.getRequestURI(), ex.getMessage());

		ApiErrorResponse errorResponse = ApiErrorResponse.of("요청한 리소스를 찾을 수 없습니다.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	// 기존 ResponseStatusException 호환성 유지
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiErrorResponse> handleResponseStatusException(ResponseStatusException ex,
		HttpServletRequest req) {
		log.warn("ResponseStatusException for {}: {} - {}", req.getRequestURI(), ex.getStatusCode(), ex.getReason());

		String message = Optional.ofNullable(ex.getReason()).orElse("서버 오류가 발생했습니다.");
		ApiErrorResponse errorResponse = ApiErrorResponse.of(message, req.getRequestURI());
		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}

	// 최후의 안전망
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest req) {
		log.error("Unhandled exception for {}: ", req.getRequestURI(), ex);

		ApiErrorResponse errorResponse = ApiErrorResponse.of("서버 내부 오류가 발생했습니다.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	/**
	 * ConstraintViolation의 propertyPath에서 간단한 필드명만 추출
	 * 예: "createStudent.request.name" -> "name"
	 */
	private String getSimplePropertyPath(String propertyPath) {
		String[] parts = propertyPath.split("\\.");
		return parts[parts.length - 1];
	}
}