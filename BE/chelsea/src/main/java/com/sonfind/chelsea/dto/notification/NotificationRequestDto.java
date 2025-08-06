package com.sonfind.chelsea.dto.notification;

public record NotificationRequestDto(
	Long subId,
	String subType,
	Long pubId,
	String pubType,
	String type
) {
}
