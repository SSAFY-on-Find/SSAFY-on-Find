package com.sonfind.chelsea.service;

import com.sonfind.chelsea.dto.notification.NotificationAndNotificationStatusResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationResponseDto;
import org.bson.types.ObjectId;

import java.util.List;

public interface NotificationQueryService {
	NotificationResponseDto getNotificationInfo(ObjectId notificationId);

	List<NotificationAndNotificationStatusResponseDto> getMyNotifications(
			Long studentId, String type);

	List<NotificationAndNotificationStatusResponseDto> getTeamNotifications(
			Long studentId, Long teamId, String type);

	int getCountOfNonReadNotifications(Long studentId);
}
