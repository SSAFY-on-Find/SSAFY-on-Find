package com.sonfind.chelsea.util;

import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.dto.notification.NotificationTypeInfo;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationType;
import org.springframework.stereotype.Component;

import java.util.Locale;

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
			throw new AppException(ErrorCode.NOTIFICATION_TYPE_NOT_SUPPORTED);
		}
	}
}
