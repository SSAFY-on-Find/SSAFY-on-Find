package com.sonfind.chelsea.dto.teams;

import com.sonfind.chelsea.dto.notification.ContextInfo;
import com.sonfind.chelsea.types.NotificationDomainType;
import lombok.Builder;

@Builder
public record TeamSimpleResponseDto(
		Long teamId,
		String name,
		String track,
		int majorCount,
		int nonMajorCount
) implements ContextInfo {
  @Override public String displayName() { return name; }
  @Override public NotificationDomainType domainType() { return NotificationDomainType.TEAM; }
  @Override public String track() { return track; }
  @Override public Integer memberCount() { return majorCount + nonMajorCount; }
}
