package com.sonfind.chelsea.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	// 기본 에러
	VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	AUTH_REQUIRED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
	CONFLICT(HttpStatus.CONFLICT, "충돌이 발생했습니다."),
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

	// 학생 관련
	STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "교육생을 찾을 수 없습니다."),
	STUDENT_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "교육생 정보를 찾을 수 없습니다."),
	STUDENT_INFO_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 자기소개서를 작성했습니다."),

	// 팀 관련
	TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
	TEAM_ALREADY_JOINED(HttpStatus.FORBIDDEN, "이미 팀에 속해있습니다."),
	TEAM_FULL(HttpStatus.BAD_REQUEST, "팀 정원이 초과됐습니다."),
	TEAM_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "팀 수정 권한이 없습니다."),
	TEAM_DELETED_ACCESS(HttpStatus.BAD_REQUEST, "삭제된 팀입니다."),
	TEAM_MERGE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "팀 합치기가 불가능합니다."),
	TEAM_NOT_IN_TEAM(HttpStatus.BAD_REQUEST, "팀에 속해 있지 않습니다."),
	TEAM_SAME_TEAM_MERGE(HttpStatus.BAD_REQUEST, "같은 팀끼리는 합칠 수 없습니다."),

	// 코드 관련
	SUBCODE_NOT_FOUND(HttpStatus.NOT_FOUND, "서브코드를 찾을 수 없습니다."),
	TRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "트랙을 찾을 수 없습니다."),
	POSITION_NOT_FOUND(HttpStatus.NOT_FOUND, "포지션을 찾을 수 없습니다."),

	// 채팅 관련
	CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
	CHATROOM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 채팅방이 존재합니다."),
	CHATROOM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방 멤버를 찾을 수 없습니다."),
	CHATROOM_MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 채팅방에 존재하는 멤버입니다."),
	SELF_CHAT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자기 자신과는 채팅방을 만들 수 없습니다."),

	// 파일 관련
	FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "파일 업로드 중 오류가 발생했습니다."),
	FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 너무 큽니다."),
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
	UNSUPPORTED_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
	FILE_DELETE_ERROR(HttpStatus.BAD_REQUEST, "파일을 삭제하는데 실패했습니다."),

	// 좋아요 관련
	SELF_FAVORITE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "본인을 좋아요할 수 없습니다."),
	FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 정보를 찾을 수 없습니다."),

	// 알림 관련
	NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."),
	NOTIFICATION_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "알림 처리 권한이 없습니다."),
	NOTIFICATION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "이미 처리된 알림입니다."),
	NOTIFICATION_PENDING_EXISTS(HttpStatus.BAD_REQUEST, "처리되지 않은 알림이 이미 존재합니다.");

	public final HttpStatus status;
	public final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}