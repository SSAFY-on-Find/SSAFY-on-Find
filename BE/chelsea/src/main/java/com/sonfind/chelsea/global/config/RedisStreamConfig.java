package com.sonfind.chelsea.global.config;

import com.sonfind.chelsea.global.event.handler.NotificationStreamHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Configuration
public class RedisStreamConfig {

	/**
	 * Redis 스트림 초기화 및 그룹 생성(앱 기동 시)
	 *
	 * @param t StringRedisTemplate 인스턴스
	 * @return CommandLineRunner 인스턴스
	 */
	@Bean
	CommandLineRunner initGroups(StringRedisTemplate t) {
		return args -> {
			final String stream = "notification_stream";
			final String group = "notif_group";

			try (RedisConnection conn = t.getConnectionFactory().getConnection()) {
				byte[] key = t.getStringSerializer().serialize(stream);
				byte[] create = "CREATE".getBytes(StandardCharsets.UTF_8);
				byte[] grp = group.getBytes(StandardCharsets.UTF_8);
				byte[] dollar = "$".getBytes(StandardCharsets.UTF_8);
				byte[] mk = "MKSTREAM".getBytes(StandardCharsets.UTF_8);

				// XGROUP CREATE <stream> <group> $ MKSTREAM
				try {
					conn.execute("XGROUP", create, key, grp, dollar, mk);
					log.info("Created consumer group '{}' with MKSTREAM on '{}'", group, stream);
				} catch (Exception e) {
					// 이미 있으면 BUSYGROUP 오류 무시
					log.info("Consumer group '{}' already exists on '{}': {}", group, stream, e.getMessage());
				}
			} catch (Exception e) {
				log.warn("Init stream/group failed: {}", e.getMessage());
			}
		};
	}

	/**
	 * Redis 스트림 메시지 리스너 컨테이너를 생성
	 * 이 컨테이너는 "notification_stream" 스트림에서 메시지를 읽고,
	 * "notif_group" 그룹을 통해 메시지를 처리
	 *
	 * @param t
	 * @param handler
	 * @return StreamMessageListenerContainer 인스턴스
	 */
	@Bean
	StreamMessageListenerContainer<String, MapRecord<String, String, String>> notifContainer(
			StringRedisTemplate t, NotificationStreamHandler handler) {

		StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
				.builder().batchSize(10).pollTimeout(Duration.ofSeconds(2)).build();

		StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(t.getConnectionFactory(), options);

		// 그룹 소비
		container.receive(Consumer.from("notif_group", hostnameOrInstanceId()),
				StreamOffset.create("notification_stream", ReadOffset.lastConsumed()),
				message -> {
					boolean success = handler.handle(message);
					if (success) {
						try {
							t.opsForStream().acknowledge("notification_stream", "notif_group", message.getId());
						} catch (Exception ackEx) {
							log.error("[Streams] ACK failed for id={} - {}", message.getId(), ackEx.getMessage(), ackEx);
						}
					}
					// 실패 시 ACK하지 않음 → 이후 재시도 또는 DLQ 스캔 대상
				}
		);
		container.start();
		log.info("Notification stream listener container started");
		return container;
	}

	private String hostnameOrInstanceId() {
		return UUID.randomUUID().toString();
	}
}
