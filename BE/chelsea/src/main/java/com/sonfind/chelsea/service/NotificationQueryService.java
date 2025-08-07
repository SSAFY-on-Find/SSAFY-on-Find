package com.sonfind.chelsea.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;

import com.sonfind.chelsea.dto.notification.NotificationAndNotificationStatusResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationResponseDto;

public interface NotificationQueryService {
	NotificationResponseDto getNotificationInfo(ObjectId notificationId)
		throws BadRequestException;

	List<NotificationAndNotificationStatusResponseDto> getMyNotifications(
		Long studentId, String type) throws BadRequestException;

	List<NotificationAndNotificationStatusResponseDto> getTeamNotifications(
		Long studentId, Long teamId, String type) throws BadRequestException;

	int getCountOfNonReadNotifications(Long studentId);
}
