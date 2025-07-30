package com.sonfind.chelsea.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 개인 -> 팀 지원하기 발생자(Publisher - 개인) 응답 DTO
 * publisher: 개인
 * subscriber: 팀
 * publisher.id로 알림 이벤트 전송
 */
@Getter
@AllArgsConstructor
@Builder
public class ApplicationPublisherResponseDto {
	// publisher: 개인 -> 알림 수신자
	private ApplicationPublisherData publisher;
	// subscriber: 팀 -> 알림에 수신자 정보를 담아 전송하기 위한 DTO
	private NotificationIdentity subscriber;
}
