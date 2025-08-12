package com.sonfind.chelsea.dto.student.response;

import com.sonfind.chelsea.dto.notification.ContextInfo;
import com.sonfind.chelsea.types.NotificationDomainType;
import lombok.Builder;

@Builder
public record StudentUnionForNotificationResponseDto(
		Long studentId,
		String name,
		String position,
		String track,
		String profileImageUrl,
		String isMajor
) implements ContextInfo {
  @Override public String displayName() { return name; }
  @Override public NotificationDomainType domainType() { return NotificationDomainType.STUDENT; }
  @Override public String track() { return track; }
  @Override public String profileImageUrl() { return profileImageUrl; }
  @Override public String position() { return position; }
  @Override public String majorYn() { return isMajor; }
}
