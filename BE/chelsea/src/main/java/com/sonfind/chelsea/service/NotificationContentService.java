package com.sonfind.chelsea.service;

import java.util.Map;

import com.sonfind.chelsea.types.NotificationType;
import com.sonfind.chelsea.types.RecipientRole;

public class NotificationContentService {

	public String buildTitle(NotificationType type, RecipientRole role, Map<String, Object> ctx) {
		return "Chelsea Notification";
	}

	public String buildMessage(NotificationType type, RecipientRole role, Map<String, Object> ctx) {
		return "You have a new notification from Chelsea.";
	}
}
