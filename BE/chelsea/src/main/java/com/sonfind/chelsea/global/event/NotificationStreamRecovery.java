package com.sonfind.chelsea.global.event;

import com.sonfind.chelsea.global.event.handler.NotificationStreamHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationStreamRecovery {

	private final StringRedisTemplate t;
	private final NotificationStreamHandler handler;

	private final String RECLAIMER_NAME = "reclaimer-" + java.util.UUID.randomUUID();

	@Value("${notification.streams.stream:notification_stream}")
	private String stream;
	@Value("${notification.streams.group:notif_group}")
	private String group;
	@Value("${notification.streams.dlq:notification_stream:dlq}")
	private String dlq;
	@Value("${notification.streams.min-idle-ms:60000}")
	private long minIdleMs;
	@Value("${notification.streams.reclaim-interval-ms:30000}")
	private long reclaimIntervalMs;
	@Value("${notification.streams.scan-pending-interval-ms:60000}")
	private long scanPendingIntervalMs;
	@Value("${notification.streams.max-deliveries:5}")
	private long maxDeliveries;

	/**
	 * 1) 오래된 Pending 메시지 자동 회수 & 재처리
	 */
	@Scheduled(fixedDelayString = "${notification.streams.reclaim-interval-ms:30000}")
	public void reclaimWithXClaim() {
		try {
			// Pending 목록 조회 (최대 200개로 셋팅)
			PendingMessages pending = t.opsForStream()
					.pending(stream, group, Range.unbounded(), 200);
			if (pending.isEmpty()) return;

			// 인스턴스 고유 이름을 필드로 보관하는 걸 추천 (여기선 간단히 로컬 변수)
			String newOwner = RECLAIMER_NAME;

			for (PendingMessage pm : pending) {
				// 오래 idle 된 것만 회수
				long idleMs = pm.getElapsedTimeSinceLastDelivery().toMillis();
				if (idleMs < minIdleMs) continue;

				RecordId rid = RecordId.of(pm.getIdAsString());

				// CLAIM: 소유권을 나(newOwner)로 변경하며 본문을 가져옴(minIdle 기준 충족 안 되면 빈 리스트 반환 가능)
				List<MapRecord<String, Object, Object>> claimed = t.opsForStream()
						.claim(stream, group, newOwner, Duration.ofMillis(minIdleMs), rid);

				if (claimed.isEmpty()) {
					// minIdle 불충족 등으로 claim 실패 → 다음 후보로
					continue;
				}

				MapRecord<String, Object, Object> rec = claimed.getFirst();

				// 공용 핸들러 호출 → 성공 시 ACK
				boolean ok = handler.handle(rec);
				if (ok) {
					t.opsForStream().acknowledge(stream, group, rec.getId());
				}
				// 실패면 ACK하지 않음 → 이후 라운드에서 재시도 or DLQ 스캐너 대상
			}
		} catch (Exception e) {
			log.error("[reclaimWithXClaim] error {}", e.getMessage(), e);
		}
	}

	/**
	 * application.yml에서 설정한 횟수를 넘기면 Pending을 DLQ로 이동
	 */
	@Scheduled(fixedDelayString = "${notification.streams.scan-pending-interval-ms:60000}")
	public void moveExcessiveFailuresToDlq() {
		try {
			// Pending 상세 조회 (최대 200개씩 예시)
			PendingMessages pending = t.opsForStream()
					.pending(stream, group, Range.unbounded(), 200);
			if (pending.isEmpty()) return;

			for (PendingMessage pm : pending) {
				if (pm.getTotalDeliveryCount() >= maxDeliveries) {

					// count 없는 오버로드 사용 (id ~ id 조회면 보통 1건만 옴)
					List<MapRecord<String, Object, Object>> body = t.opsForStream()
							.range(stream, Range.closed(pm.getIdAsString(), pm.getIdAsString()));

					if (body == null || body.isEmpty()) {
						// 본문 없으면 청소
						t.opsForStream().acknowledge(stream, group, pm.getId());
						log.warn("[DLQ] body not found; ACK id={}", pm.getIdAsString());
						continue;
					}

					Map<Object, Object> v = body.get(0).getValue();

					// Map.ofEntries 로 10쌍 초과 처리
					t.opsForStream().add(MapRecord.create(dlq, Map.ofEntries(
							Map.entry("origId", pm.getIdAsString()),
							Map.entry("deliveries", String.valueOf(pm.getTotalDeliveryCount())),
							Map.entry("phase", v.getOrDefault("phase", "UNKNOWN")),
							Map.entry("notificationId", v.getOrDefault("notificationId", "")),
							Map.entry("pubId", v.getOrDefault("pubId", "")),
							Map.entry("pubType", v.getOrDefault("pubType", "")),
							Map.entry("subId", v.getOrDefault("subId", "")),
							Map.entry("subType", v.getOrDefault("subType", "")),
							Map.entry("eventType", v.getOrDefault("eventType", "")),
							Map.entry("status", v.getOrDefault("status", "")),
							Map.entry("ts", v.getOrDefault("ts", String.valueOf(System.currentTimeMillis())))
					)));

					// 원본은 ACK하여 Pending에서 제거
					t.opsForStream().acknowledge(stream, group, pm.getId());
					log.warn("[DLQ] moved id={} deliveries={}", pm.getIdAsString(), pm.getTotalDeliveryCount());
				}
			}
		} catch (Exception e) {
			log.error("[DLQ-scan] error {}", e.getMessage(), e);
		}
	}
}
