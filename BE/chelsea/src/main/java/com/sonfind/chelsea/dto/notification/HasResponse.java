package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

public interface HasResponse {
	Long pubId();

	NotificationDomainType pubType();

	Long subId();

	NotificationDomainType subType();
}
