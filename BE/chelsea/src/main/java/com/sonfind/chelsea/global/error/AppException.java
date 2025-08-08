package com.sonfind.chelsea.global.error;

// 🔥 이 클래스 하나만 만들면 끝!
public class AppException extends BusinessException {

	public AppException(ErrorCode errorCode) {
		super(errorCode);
	}
	
	// 학생 관련
	public static AppException studentNotFound() {
		return new AppException(ErrorCode.STUDENT_NOT_FOUND);
	}

	public static AppException studentInfoNotFound() {
		return new AppException(ErrorCode.STUDENT_INFO_NOT_FOUND);
	}

	public static AppException studentInfoAlreadyExists() {
		return new AppException(ErrorCode.STUDENT_INFO_ALREADY_EXISTS);
	}

	// 팀 관련
	public static AppException teamNotFound() {
		return new AppException(ErrorCode.TEAM_NOT_FOUND);
	}

	public static AppException teamAlreadyJoined() {
		return new AppException(ErrorCode.TEAM_ALREADY_JOINED);
	}

	public static AppException teamFull() {
		return new AppException(ErrorCode.TEAM_FULL);
	}

	public static AppException teamPermissionDenied() {
		return new AppException(ErrorCode.TEAM_PERMISSION_DENIED);
	}

	public static AppException teamDeletedAccess() {
		return new AppException(ErrorCode.TEAM_DELETED_ACCESS);
	}

	public static AppException teamMergeNotAllowed() {
		return new AppException(ErrorCode.TEAM_MERGE_NOT_ALLOWED);
	}

	public static AppException teamNotInTeam() {
		return new AppException(ErrorCode.TEAM_NOT_IN_TEAM);
	}

	public static AppException teamSameTeamMerge() {
		return new AppException(ErrorCode.TEAM_SAME_TEAM_MERGE);
	}

	// 코드 관련
	public static AppException subCodeNotFound() {
		return new AppException(ErrorCode.SUBCODE_NOT_FOUND);
	}

	public static AppException trackNotFound() {
		return new AppException(ErrorCode.TRACK_NOT_FOUND);
	}

	public static AppException positionNotFound() {
		return new AppException(ErrorCode.POSITION_NOT_FOUND);
	}

	// 채팅 관련
	public static AppException chatRoomNotFound() {
		return new AppException(ErrorCode.CHATROOM_NOT_FOUND);
	}

	public static AppException chatRoomAlreadyExists() {
		return new AppException(ErrorCode.CHATROOM_ALREADY_EXISTS);
	}

	public static AppException chatRoomMemberNotFound() {
		return new AppException(ErrorCode.CHATROOM_MEMBER_NOT_FOUND);
	}

	public static AppException chatRoomMemberAlreadyExists() {
		return new AppException(ErrorCode.CHATROOM_MEMBER_ALREADY_EXISTS);
	}

	public static AppException selfChatNotAllowed() {
		return new AppException(ErrorCode.SELF_CHAT_NOT_ALLOWED);
	}

	// 파일 관련
	public static AppException fileUploadError() {
		return new AppException(ErrorCode.FILE_UPLOAD_ERROR);
	}

	public static AppException fileSizeExceeded() {
		return new AppException(ErrorCode.FILE_SIZE_EXCEEDED);
	}

	public static AppException fileNotFound() {
		return new AppException(ErrorCode.FILE_NOT_FOUND);
	}

	public static AppException unsupportedFileType() {
		return new AppException(ErrorCode.UNSUPPORTED_FILE_TYPE);
	}

	// 좋아요 관련
	public static AppException selfFavoriteNotAllowed() {
		return new AppException(ErrorCode.SELF_FAVORITE_NOT_ALLOWED);
	}

	public static AppException favoriteNotFound() {
		return new AppException(ErrorCode.FAVORITE_NOT_FOUND);
	}

	// 알림 관련
	public static AppException notificationNotFound() {
		return new AppException(ErrorCode.NOTIFICATION_NOT_FOUND);
	}

	public static AppException notificationPermissionDenied() {
		return new AppException(ErrorCode.NOTIFICATION_PERMISSION_DENIED);
	}

	public static AppException notificationAlreadyProcessed() {
		return new AppException(ErrorCode.NOTIFICATION_ALREADY_PROCESSED);
	}

	public static AppException notificationPendingExists() {
		return new AppException(ErrorCode.NOTIFICATION_PENDING_EXISTS);
	}
}