package com.sonfind.chelsea.dto.notification;

import com.sonfind.chelsea.types.NotificationDomainType;

public interface ContextInfo {
  String displayName();
  String track();
  NotificationDomainType domainType();


  default String profileImageUrl() { return null; }
  default String position() { return null; }
  default String majorYn() { return null; }
  default Integer memberCount() { return null; }
}
