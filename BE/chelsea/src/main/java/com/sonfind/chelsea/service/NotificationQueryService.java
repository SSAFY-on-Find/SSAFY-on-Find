package com.sonfind.chelsea.service;

import com.sonfind.chelsea.dto.notification.NotificationAndNotificationStatusForMyNotifResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationAndNotificationStatusForMyTeamResponseDto;
import com.sonfind.chelsea.dto.notification.NotificationResponseDto;
import org.bson.types.ObjectId;

import java.util.List;

public interface NotificationQueryService {
	NotificationResponseDto getNotificationInfo(ObjectId notificationId);

	List<NotificationAndNotificationStatusForMyNotifResponseDto> getMyNotifications(
			Long studentId, String type);

	List<NotificationAndNotificationStatusForMyTeamResponseDto> getTeamNotifications(
			Long studentId, Long teamId, String type);

	int getCountOfNonReadNotifications(Long studentId);
}
