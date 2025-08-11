package com.sonfind.chelsea.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sonfind.chelsea.dto.notification.NotificationRequestDto;
import com.sonfind.chelsea.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invitations")
public class InvitationController {

	private final NotificationService notificationService;

	@Operation(summary = "알림 저장 및 전송", description = "요청에 대한 알림 로그를 DB에 저장 후 수신자에게 전송합니다.")
	@ApiResponses({@ApiResponse(responseCode = "201", description = "알림 로그 저장 및 발송 성공", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
	@PostMapping()
	public void sendInvitationRequest(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@RequestBody NotificationRequestDto dto
	) {
		// 초대 요청을 보냅니다.
		try {
			notificationService.sendNotification(studentId, dto);
		} catch (Exception e) {
			// 예외 처리 로직을 추가할 수 있습니다.
			e.printStackTrace();
		}
	}

	@Operation(summary = "초대 수락", description = "초대를 수락합니다.")

	@PostMapping("/{notificationId}/accept")
	public void acceptInvitation(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable String notificationId
	) throws BadRequestException {
		// 초대를 수락합니다.
		notificationService.acceptInvitation(studentId, notificationId);
	}

	@Operation(summary = "초대 거절", description = "초대를 거절합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "초대 거절 성공", content = @Content),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
	})
	@PostMapping("/{notificationId}/reject")
	public void rejectInvitation(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable String notificationId
	) throws BadRequestException {
		// 초대를 거절합니다.
		notificationService.rejectInvitation(studentId, notificationId);
	}

	@Operation(summary = "초대 취소", description = "초대를 취소합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "초대 취소 성공", content = @Content),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
	})
	@PostMapping("/{notificationId}/cancel")
	public void cancelInvitation(
		@Parameter(hidden = true)
		@SessionAttribute("loginUser") Long studentId,
		@PathVariable String notificationId
	) throws BadRequestException {
		// 초대를 취소합니다.
		notificationService.cancelInvitation(studentId, notificationId);
	}

}
