package com.sonfind.chelsea.util;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.dto.notification.NotificationTypeInfo;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationType;

@Component
public class NotificationTypeConverter {

	public NotificationTypeInfo convert(NotificationRequestDto dto) {
		try {
			NotificationType type = NotificationType.valueOf(dto.type()
				.toUpperCase(Locale.ROOT));
			NotificationDomainType pubDomain = NotificationDomainType.valueOf(dto.pubType()
				.toUpperCase(Locale.ROOT));
			NotificationDomainType subDomain = NotificationDomainType.valueOf(dto.subType()
				.toUpperCase(Locale.ROOT));

			return NotificationTypeInfo.builder()
				.type(type)
				.pubType(pubDomain)
				.subType(subDomain)
				.build();
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				throw new IllegalArgumentException("Invalid notification type or domain type", e);
			} else {
				throw new RuntimeException("Unexpected error while converting notification type", e);
			}
		}
	}
}
