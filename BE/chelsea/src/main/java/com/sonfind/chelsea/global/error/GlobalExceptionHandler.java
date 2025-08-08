package com.sonfind.chelsea.global.error;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private ProblemDetail pd(ErrorCode ec, String detail, HttpServletRequest req) {
		ProblemDetail p = ProblemDetail.forStatus(ec.status);
		p.setTitle(ec.code);
		p.setDetail(Optional.ofNullable(detail).orElse(ec.message));
		p.setProperty("path", req.getRequestURI());
		return p;
	}

	//비즈니스 예외
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException ex, HttpServletRequest req) {
		ErrorCode ec = ex.getErrorCode();
		return ResponseEntity.status(ec.status).body(pd(ec, ex.getMessage(), req));
	}

	// @Valid Dto 바인딩 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> handleInvalid(MethodArgumentNotValidException ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.VALIDATION_FAILED;
		ProblemDetail p = pd(ec, ec.message, req);
		Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
			.collect(Collectors.toMap(FieldError::getField,
				fe -> Optional.ofNullable(fe.getDefaultMessage()).orElse("invalid"), (a, b) -> a));
		p.setProperty("errors", errors);
		return ResponseEntity.badRequest().body(p);
	}

	//파라미터 타입/누락/json 파싱 실패
	@ExceptionHandler({
		MissingServletRequestParameterException.class,
		HttpMessageNotReadableException.class
	})
	public ResponseEntity<ProblemDetail> handleBadRequest(Exception ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.VALIDATION_FAILED;
		return ResponseEntity.badRequest().body(pd(ec, ex.getMessage(), req));
	}

	//쿼리 파라미터/경로 변수에 대한 @Validated(메소드 레벨) 위반
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex,
		HttpServletRequest req) {
		ErrorCode ec = ErrorCode.VALIDATION_FAILED;
		ProblemDetail p = pd(ec, ex.getMessage(), req);
		Map<String, String> errors = ex.getConstraintViolations().stream()
			.collect(Collectors.toMap(v -> v.getPropertyPath().toString(),
				v -> v.getMessage(), (a, b) -> a));
		p.setProperty("errors", errors);
		return ResponseEntity.badRequest().body(p);
	}

	//Security (컨트롤러 이후 레이어에서 발생한 경우)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.ACCESS_DENIED;
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(pd(ec, ec.message, req));
	}

	//DB 제약 위반 충돌
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ProblemDetail> handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
		ErrorCode ec = ErrorCode.CONFLICT;
		return ResponseEntity.status(HttpStatus.CONFLICT).body(pd(ec, "데이터 제약 조건을 위반했습니다.", req));
	}

	//HTTP 메서드 미지원
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ProblemDetail> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
		HttpServletRequest req) {
		ProblemDetail p = ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED);
		p.setTitle("METHOD NOT ALLOWED");
		p.setDetail(ex.getMessage());
		p.setProperty("path", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(p);
	}

	//안전망
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> handleEtc(Exception ex, HttpServletRequest req) {
		log.error("Unhandled Exception", ex);
		ErrorCode ec = ErrorCode.INTERNAL_ERROR;
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd(ec, ec.message, req));
	}
}
