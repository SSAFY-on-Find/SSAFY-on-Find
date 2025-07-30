package com.sonfind.chelsea.dto.notification;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InvitationSubscriberData {
	@JsonUnwrapped
	private NotificationIdentity notificationIdentity;
	@JsonUnwrapped
	private NotificationContent notificationContent;
	private Boolean isMajor;
	private String position;

}
