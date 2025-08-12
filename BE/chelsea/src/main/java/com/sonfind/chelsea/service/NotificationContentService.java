package com.sonfind.chelsea.service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import com.sonfind.chelsea.types.RecipientRole;
import org.springframework.stereotype.Service;

@Service
public class NotificationContentService {

	public String buildTitle(NotificationDocument notif, RecipientRole role) {

		return switch (notif.getType()) {
			case APPLICATION -> buildApplicationTitle(role);
			case INVITATION -> buildInvitationTitle(role);
			case MERGE -> buildMergeTitle(role);
			default -> throw new AppException(ErrorCode.NOTIFICATION_TYPE_NOT_SUPPORTED);
		};
	}

	private String buildMergeTitle(RecipientRole role) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s에 팀 합치기 제안".formatted("ctx.subscriber().name()");
		}

		return "%s의 팀 합치기 제안".formatted("ctx.publisher().name()");
	}

	private String buildInvitationTitle(RecipientRole role) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s님에게 초대".formatted("ctx.subscriber().name()");
		}

		return "%s의 초대".formatted("ctx.publisher().name()");
	}

	private String buildApplicationTitle(RecipientRole role) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s에 지원".formatted("ctx.subscriber().name()");
		}

		return "%s의 지원".formatted("ctx.publisher().name()");
	}


	public String buildMessage(NotificationDocument notif, RecipientRole role) {
		return switch (notif.getType()) {
			case APPLICATION -> buildApplicationMessage();
			case INVITATION -> buildInvitationMessage(role);
			case MERGE -> buildMergeMessage(role);
		};
	}

	private String buildMergeMessage(RecipientRole role) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s(%s, %s)님을 %s(%s)에 초대했습니다.".formatted(
					"ctx.subscriber().name()",
					"ctx.subscriber().isMajor()",
					"ctx.subscriber().position()",
					"ctx.publisher().name()",
					"ctx.publisher().track()"
			);
		}

		return "%s(%s)에서 %s(%s, %s)님을 초대했습니다.".formatted(
				"ctx.publisher().name()",
				"ctx.publisher().track()",
				"ctx.subscriber().name()",
				"ctx.subscriber().isMajor()",
				"ctx.subscriber().position()"
		);
	}

	private String buildInvitationMessage(RecipientRole role) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s(%s, %s)님을 %s(%s)에 초대했습니다.".formatted(
					"ctx.subscriber().name()",
					"ctx.subscriber().isMajor()",
					"ctx.subscriber().position()",
					"ctx.publisher().name()",
					"ctx.publisher().track()"
			);
		}

		return "%s(%s)에서 %s(%s, %s)님을 초대했습니다.".formatted(
				"ctx.publisher().name()",
				"ctx.publisher().track()",
				"ctx.subscriber().name()",
				"ctx.subscriber().isMajor()",
				"ctx.subscriber().position()"
		);
	}

	private String buildApplicationMessage() {
		return "%s(%s, %s)님이 %s(%s)에 지원했습니다.".formatted(
				"ctx.subscriber().name()",
				"ctx.subscriber().isMajor()",
				"ctx.subscriber().position()",
				"ctx.publisher().name()",
				"ctx.publisher().track()"
		);
	}

}
