package com.sonfind.chelsea.controller;

import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invitations")
public class InvitationController {

  private final NotificationService notificationService;

  @PostMapping
  public void sendInvitationRequest(
          @CookieValue("sessionId") Long studentId,
          @RequestBody NotificationRequestDto dto
  ) {
    // 초대 요청을 보냅니다.
    try {notificationService.sendNotification(studentId, dto);
    } catch (Exception e) {
      // 예외 처리 로직을 추가할 수 있습니다.
      e.printStackTrace();
    }
  }

}
