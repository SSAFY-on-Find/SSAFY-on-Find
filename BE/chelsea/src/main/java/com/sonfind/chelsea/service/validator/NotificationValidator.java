package com.sonfind.chelsea.service.validator;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.dto.notification.NotificationTypeInfo;
import com.sonfind.chelsea.facade.StudentFacade;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.util.NotificationTypeConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	public void validatePublisher(Long studentId, NotificationRequestDto dto) throws BadRequestException {
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
	private void validateTeamPublisher(Long studentId, NotificationRequestDto dto) throws BadRequestException {
		List<Students> members = studentFacade.findAllByTeamId(dto.pubId());
		Students me = studentFacade.findByStudentId(studentId);
		if (!members.contains(me)) {
			log.info("HttpStatus: {} | 팀에 속해 있지 않은 사용자입니다.", HttpStatus.BAD_REQUEST);
			throw new BadRequestException(
				"HttpStatus: " + HttpStatus.BAD_REQUEST + " | 팀에 속해 있지 않은 사용자입니다."
			);
		}
	}

	// 발신자(dto.pubId)가 올바른 권한을 가졌는지 검증
	// - student 타입이면 자기 자신인지
	private void validateStudentPublisher(Long studentId, NotificationRequestDto dto) throws BadRequestException {
		if (!dto.pubId().equals(studentId)) {
			log.info("HttpStatus: {} | 본인 이외의 사용자로 요청할 수 없습니다.", HttpStatus.BAD_REQUEST);
			throw new BadRequestException(
				"HttpStatus: " + HttpStatus.BAD_REQUEST + " | 본인 이외의 사용자로 요청할 수 없습니다."
			);
		}
	}

	// 지원하지 않는 pubType에 대한 예외를 발생
	private void throwUnsupportedDomain(String pubType) throws BadRequestException {
		log.info("HttpStatus: {} | 지원하지 않는 pubType: {}", HttpStatus.BAD_REQUEST, pubType);
		throw new BadRequestException(
			"HttpStatus: " + HttpStatus.BAD_REQUEST + " | 지원하지 않는 pubType: " + pubType
		);
	}
}
