package com.sonfind.chelsea.global.config;

import com.sonfind.chelsea.global.event.handler.NotificationStreamHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

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
			try {
				// "notification_stream" 스트림에 "notif_group"이라는 그룹을 생성
				// ReadOffset.from("0-0")는 스트림의 시작부터 읽기 시작하도록 설정
				t.opsForStream().createGroup("notification_stream", ReadOffset.from("0-0"), "notif_group");
			} catch (Exception e) {
				// 그룹이 이미 존재하는 경우 예외가 발생할 수 있으므로 무시하고 로그만 남김
				log.warn("Notification stream group already exists or could not be created: {}", e.getMessage());
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
