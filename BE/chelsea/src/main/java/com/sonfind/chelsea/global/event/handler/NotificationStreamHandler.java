package com.sonfind.chelsea.global.event.handler;

import com.sonfind.chelsea.global.event.InvitationRequestEvent;
import com.sonfind.chelsea.global.event.InvitationResponseEvent;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationStreamHandler {

	private final ApplicationEventPublisher eventPublisher;

	// Streams 레코드 파싱 후 적절한 Spring 이벤트로 변환하여 발행
	public boolean handle(MapRecord<String, ?, ?> message) {
		try {
			Map<?, ?> valueMap = message.getValue();

			// 안전하게 String 변환
			String notificationId = req(valueMap, "notificationId");
			long pubId = Long.parseLong(req(valueMap, "pubId"));
			long subId = Long.parseLong(req(valueMap, "subId"));
			var pubType = NotificationDomainType.valueOf(req(valueMap, "pubType"));
			var subType = NotificationDomainType.valueOf(req(valueMap, "subType"));
			var ts = new Date(Long.parseLong(req(valueMap, "ts")));

			String phase = get(valueMap, "phase");
			if ("REQUEST".equalsIgnoreCase(phase)) {
				// REQUEST: use NotificationType
				NotificationType eventType = NotificationType.valueOf(req(valueMap, "eventType"));
				InvitationRequestEvent event = InvitationRequestEvent.of(
						this,
						new ObjectId(notificationId),
						pubId, pubType,
						subId, subType,
						ts,
						eventType
				);
				eventPublisher.publishEvent(event);
				return true;
			} else if ("RESPONSE".equalsIgnoreCase(phase)) {
				// RESPONSE: use NotificationStatus(Accepted, Rejected, etc.)
				NotificationStatus status = NotificationStatus.valueOf(req(valueMap, "status"));
				InvitationResponseEvent event = InvitationResponseEvent.of(
						this,
						new ObjectId(notificationId),
						pubId, pubType,
						subId, subType,
						status,
						ts
				);
				eventPublisher.publishEvent(event);
				return true;
			} else {
				log.warn("[Streams] Unknown phase={} for message id={}", phase, message.getId());
				return false;
			}
		} catch (Exception e) {
			log.error("[Streams] Failed to handle notification_stream id={} - {}", message.getId(), e.getMessage(), e);
			return false;
		}
	}

	private String req(Map<?, ?> m, String k) {
		Object v = m.get(k);
		if (v == null) throw new IllegalArgumentException("missing field: " + k);
		return v.toString();
	}

	private String get(Map<?, ?> m, String k) {
		Object v = m.get(k);
		return v == null ? null : v.toString();
	}
}
