package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MergeSubscriberDataBase implements BaseNotificationData {
	private Long id;
	private String name;
	private NotificationDomainType type;
	private String track;
	private int memberCount;
	private int major;
	private int nonMajor;
	private String notificationTitle;
	private String notificationMessage;
}
