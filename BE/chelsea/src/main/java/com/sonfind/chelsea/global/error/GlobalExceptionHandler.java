package com.sonfind.chelsea.global.error;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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

	private ProblemDetail pd(ErrorCode ec, String detail, HttpServletRequest req) {
		ProblemDetail p = ProblemDetail.forStatus(ec.status);
		p.setDetail(Optional.ofNullable(detail).orElse(ec.message));
		p.setProperty("path", req.getRequestURI());
		return p;
	}

	// 비즈니스 예외
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException ex, HttpServletRequest req) {
		ErrorCode ec = ex.getErrorCode();
		log.warn("Business exception: {} - {}", ex.getMessage());
		return ResponseEntity.status(ec.status).body(pd(ec, ex.getMessage(), req));
	}

	// @Valid DTO 바인딩 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> handleInvalid(MethodArgumentNotValidException ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.VALIDATION_FAILED;
		ProblemDetail p = pd(ec, ec.message, req);
		Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
			.collect(Collectors.toMap(FieldError::getField,
				fe -> Optional.ofNullable(fe.getDefaultMessage()).orElse("invalid"), (a, b) -> a));
		p.setProperty("errors", errors);
		log.warn("Validation failed: {}", errors);
		return ResponseEntity.badRequest().body(p);
	}

	// 파라미터 타입/누락/json 파싱 실패
	@ExceptionHandler({
		MissingServletRequestParameterException.class,
		HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class
	})
	public ResponseEntity<ProblemDetail> handleBadRequest(Exception ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.VALIDATION_FAILED;
		log.warn("Bad request: {}", ex.getMessage());
		return ResponseEntity.badRequest().body(pd(ec, ex.getMessage(), req));
	}

	// 쿼리 파라미터/경로 변수에 대한 @Validated(메소드 레벨) 위반
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex,
		HttpServletRequest req) {
		ErrorCode ec = ErrorCode.VALIDATION_FAILED;
		ProblemDetail p = pd(ec, ex.getMessage(), req);
		Map<String, String> errors = ex.getConstraintViolations().stream()
			.collect(Collectors.toMap(v -> v.getPropertyPath().toString(),
				v -> v.getMessage(), (a, b) -> a));
		p.setProperty("errors", errors);
		log.warn("Constraint violation: {}", errors);
		return ResponseEntity.badRequest().body(p);
	}

	// 파일 업로드 크기 초과
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ProblemDetail> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex,
		HttpServletRequest req) {
		ErrorCode ec = ErrorCode.FILE_SIZE_EXCEEDED;
		log.warn("File size exceeded: {}", ex.getMessage());
		return ResponseEntity.badRequest().body(pd(ec, ec.message, req));
	}

	// 파일 업로드 관련 예외 (Apache Commons FileUpload)
	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<ProblemDetail> handleFileUpload(FileUploadException ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.FILE_UPLOAD_ERROR;
		log.warn("File upload error: {}", ex.getMessage());
		return ResponseEntity.badRequest().body(pd(ec, ex.getMessage(), req));
	}

	// Security (컨트롤러 이후 레이어에서 발생한 경우)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.ACCESS_DENIED;
		log.warn("Access denied: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(pd(ec, ec.message, req));
	}

	// DB 제약 위반 충돌
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ProblemDetail> handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.CONFLICT;
		log.warn("Data integrity violation: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(pd(ec, "데이터 제약 조건을 위반했습니다.", req));
	}

	// HTTP 메서드 미지원
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ProblemDetail> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
		HttpServletRequest req) {
		ProblemDetail p = ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED);
		p.setTitle("METHOD_NOT_ALLOWED");
		p.setDetail(ex.getMessage());
		p.setProperty("path", req.getRequestURI());
		log.warn("Method not allowed: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(p);
	}

	// 기존 ResponseStatusException 호환성 유지
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException ex,
		HttpServletRequest req) {
		ProblemDetail p = ProblemDetail.forStatus(ex.getStatusCode());
		p.setTitle(ex.getStatusCode().toString());
		p.setDetail(ex.getReason());
		p.setProperty("path", req.getRequestURI());
		log.warn("ResponseStatusException: {} - {}", ex.getStatusCode(), ex.getReason());
		return ResponseEntity.status(ex.getStatusCode()).body(p);
	}

	// Apache Coyote BadRequestException (NotificationService에서 사용)
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ProblemDetail> handleBadRequestException(BadRequestException ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.BAD_REQUEST;
		log.warn("BadRequestException: {}", ex.getMessage());
		return ResponseEntity.badRequest().body(pd(ec, ex.getMessage(), req));
	}

	// JPA EntityNotFoundException
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ProblemDetail> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req) {
		// 일반적인 NOT_FOUND로 처리하거나, 구체적인 에러코드가 필요하면 분기 처리
		ProblemDetail p = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		p.setTitle("ENTITY_NOT_FOUND");
		p.setDetail(ex.getMessage());
		p.setProperty("path", req.getRequestURI());
		log.warn("Entity not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(p);
	}

	// 안전망
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> handleEtc(Exception ex, HttpServletRequest req) {
		log.error("Unhandled Exception", ex);
		ErrorCode ec = ErrorCode.INTERNAL_ERROR;
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd(ec, ec.message, req));
	}
}