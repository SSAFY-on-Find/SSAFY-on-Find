package com.sonfind.chelsea.service.validator;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.dto.notification.NotificationTypeInfo;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.util.NotificationTypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationValidator {

	private final StudentFacade studentFacade;
	private final NotificationTypeConverter typeConverter;

	/**
	 * 발신자(dto.pubId)가 올바른 권한을 가졌는지 검증합니다.
	 * - team 타입이면 해당 팀의 멤버인지
	 * - student 타입이면 자기 자신인지
	 * 잘못되었으면 400 BadRequestException 발생
	 */
	public void validatePublisher(Long studentId, NotificationRequestDto dto) {
		NotificationTypeInfo info = typeConverter.convert(dto);
		NotificationDomainType pubDomain = info.pubType();

		switch (pubDomain) {
			case TEAM -> validateTeamPublisher(studentId, dto);
			case STUDENT -> validateStudentPublisher(studentId, dto);
			default -> throwUnsupportedDomain(dto.pubType());
		}
	}

	// 발신자(dto.pubId)가 올바른 권한을 가졌는지 검증
	// - team 타입이면 해당 팀의 멤버인지
	// - student 타입이면 자기 자신인지
	// - 그 외는 예외 발생
	private void validateTeamPublisher(Long studentId, NotificationRequestDto dto) {
		List<Student> members = studentFacade.findAllByTeamId(dto.pubId());
		Student me = studentFacade.findByStudentId(studentId);
		if (!members.contains(me)) {
			log.info("HttpStatus: {} | 팀에 속해 있지 않은 사용자입니다.", HttpStatus.BAD_REQUEST);
			throw new AppException(ErrorCode.TEAM_NOT_IN_TEAM);
		}
	}

	// 발신자(dto.pubId)가 올바른 권한을 가졌는지 검증
	// - student 타입이면 자기 자신인지
	private void validateStudentPublisher(Long studentId, NotificationRequestDto dto) {
		if (!dto.pubId().equals(studentId)) {
			log.info("HttpStatus: {} | 본인 이외의 사용자로 요청할 수 없습니다.", HttpStatus.BAD_REQUEST);
			throw new AppException(ErrorCode.BAD_REQUEST);
		}
	}

	// 지원하지 않는 pubType에 대한 예외를 발생
	private void throwUnsupportedDomain(String pubType) {
		log.info("HttpStatus: {} | 지원하지 않는 pubType: {}", HttpStatus.BAD_REQUEST, pubType);
		throw new AppException(ErrorCode.NOT_SUPPORTED_TYPE);
	}
}
