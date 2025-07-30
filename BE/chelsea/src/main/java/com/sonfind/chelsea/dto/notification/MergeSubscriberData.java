package com.sonfind.chelsea.dto.notification;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MergeSubscriberData {
	@JsonUnwrapped
	private NotificationIdentity notificationIdentity;
	@JsonUnwrapped
	private NotificationContent notificationContent;
	private int memberCount;
	private int major;
	private int nonMajor;
}
