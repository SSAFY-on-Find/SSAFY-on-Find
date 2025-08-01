package com.sonfind.chelsea.service;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.types.NotificationContext;
import com.sonfind.chelsea.types.RecipientRole;

@Service
public class NotificationContentService {

	public String buildTitle(NotificationDocument notif, RecipientRole role,
		NotificationContext ctx) {

		return switch (notif.getType()) {
			case APPLICATION -> buildApplicationTitle(role, ctx);
			case INVITATION -> buildInvitationTitle(role, ctx);
			case MERGE -> buildMergeTitle(role, ctx);
			default -> throw new IllegalArgumentException("Unsupported notification type: " + notif.getType());
		};
	}

	private String buildMergeTitle(RecipientRole role, NotificationContext ctx) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s에 팀 합치기 제안".formatted(ctx.subscriber().name());
		}

		return "%s의 팀 합치기 제안".formatted(ctx.publisher().name());
	}

	private String buildInvitationTitle(RecipientRole role, NotificationContext ctx) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s님에게 초대".formatted(ctx.subscriber().name());
		}

		return "%s의 초대".formatted(ctx.publisher().name());
	}

	private String buildApplicationTitle(RecipientRole role, NotificationContext ctx) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s에 지원".formatted(ctx.subscriber().name());
		}

		return "%s의 지원".formatted(ctx.publisher().name());
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
			return "%s(%s, %s)님을 %s(%s)에 초대했습니다.".formatted(
				ctx.subscriber().name(),
				ctx.subscriber().isMajor(),
				ctx.subscriber().position(),
				ctx.publisher().name(),
				ctx.publisher().track()
			);
		}

		return "%s(%s)에서 %s(%s, %s)님을 초대했습니다.".formatted(
			ctx.publisher().name(),
			ctx.publisher().track(),
			ctx.subscriber().name(),
			ctx.subscriber().isMajor(),
			ctx.subscriber().position()
		);
	}

	private String buildInvitationMessage(RecipientRole role, NotificationContext ctx) {
		if (role == RecipientRole.PUBLISHER) {
			return "%s(%s, %s)님을 %s(%s)에 초대했습니다.".formatted(
				ctx.subscriber().name(),
				ctx.subscriber().isMajor(),
				ctx.subscriber().position(),
				ctx.publisher().name(),
				ctx.publisher().track()
			);
		}

		return "%s(%s)에서 %s(%s, %s)님을 초대했습니다.".formatted(
			ctx.publisher().name(),
			ctx.publisher().track(),
			ctx.subscriber().name(),
			ctx.subscriber().isMajor(),
			ctx.subscriber().position()
		);
	}

	private String buildApplicationMessage(NotificationContext ctx) {
		return "%s(%s, %s)님이 %s(%s)에 지원했습니다.".formatted(
			ctx.subscriber().name(),
			ctx.subscriber().isMajor(),
			ctx.subscriber().position(),
			ctx.publisher().name(),
			ctx.publisher().track()
		);
	}

}
