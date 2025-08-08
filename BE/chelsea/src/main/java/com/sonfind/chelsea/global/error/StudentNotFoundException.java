package com.sonfind.chelsea.global.error;

public class StudentNotFoundException extends BusinessException {
	public StudentNotFoundException() {
		super(ErrorCode.STUDENT_NOT_FOUND);
	}
}
