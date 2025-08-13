package com.sonfind.chelsea.service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.dto.notification.NotificationContext;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import com.sonfind.chelsea.types.RecipientRole;
import org.springframework.stereotype.Service;

@Service
public class NotificationContentService {

	public String buildTitle(NotificationDocument notif, RecipientRole role,
	                         NotificationContext ctx) {

		return switch (notif.getType()) {
			case APPLICATION -> buildApplicationTitle(role, ctx);
			case INVITATION -> buildInvitationTitle(role, ctx);
			case MERGE -> buildMergeTitle(role, ctx);
			default -> throw new AppException(ErrorCode.NOTIFICATION_TYPE_NOT_SUPPORTED);
		};
	}

	private String buildMergeTitle(RecipientRole role, NotificationContext ctx) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s에 팀 합치기 제안".formatted(ctx.subscriber().displayName());
		}

		return "%s의 팀 합치기 제안".formatted(ctx.publisher().displayName());
	}

	private String buildInvitationTitle(RecipientRole role, NotificationContext ctx) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s님을 초대".formatted(ctx.subscriber().displayName());
		}

		return "%s의 초대".formatted(ctx.publisher().displayName());
	}

	private String buildApplicationTitle(RecipientRole role, NotificationContext ctx) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s에 지원".formatted(ctx.subscriber().displayName());
		}

		return "%s님의 지원".formatted(ctx.publisher().displayName());
	}

	public String buildMessage(NotificationDocument notif, RecipientRole role, NotificationContext ctx) {
		return switch (notif.getType()) {
			case APPLICATION -> buildApplicationMessage(ctx);
			case INVITATION -> buildInvitationMessage(role, ctx);
			case MERGE -> buildMergeMessage(role, ctx);
		};
	}

	private String buildMergeMessage(RecipientRole role, NotificationContext ctx) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s(%s)에서 %s(%s)에게 팀 합치기를 제안했습니다.".formatted(
					ctx.publisher().displayName(),
					ctx.publisher().track(),
					ctx.subscriber().displayName(),
					ctx.subscriber().track()
			);
		}

		return "%s(%s)에서 %s(%s)에게 팀 합치기를 제안했습니다.".formatted(
				ctx.publisher().displayName(),
				ctx.publisher().track(),
				ctx.subscriber().displayName(),
				ctx.subscriber().track()
		);
	}

	private String buildInvitationMessage(RecipientRole role, NotificationContext ctx) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s(%s, %s)님을 %s(%s)에 초대했습니다.".formatted(
					ctx.subscriber().displayName(),
					ctx.subscriber().majorYn(),
					ctx.subscriber().position(),
					ctx.publisher().displayName(),
					ctx.publisher().track()
			);
		}

		return "%s(%s)에서 %s(%s, %s)님을 초대했습니다.".formatted(
				ctx.publisher().displayName(),
				ctx.publisher().track(),
				ctx.subscriber().displayName(),
				ctx.subscriber().majorYn(),
				ctx.subscriber().position()
		);
	}

	private String buildApplicationMessage(NotificationContext ctx) {
		return "%s(%s, %s)님이 %s(%s)에 지원했습니다.".formatted(
				ctx.publisher().displayName(),
				ctx.publisher().majorYn(),
				ctx.publisher().position(),
				ctx.subscriber().displayName(),
				ctx.subscriber().track()
		);
	}

}
