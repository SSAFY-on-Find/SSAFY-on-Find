package com.sonfind.chelsea.health;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader {
	private final TestUserMysqlRepository testUserMysqlRepository;
	private final TestProductMongoDBRepository testProductMongoDBRepository;
	private final StringRedisTemplate redisTemplate;

	public void loadTestData() {
		log.info("테스트 데이터 생성 시작");
		checkAndLoadRedisData();
		checkAndLoadMysqlData();
		checkAndLoadMongoData();
		log.info("모든 테스트 데이터 생성 완료");
	}

	private void checkAndLoadRedisData() {
		try {
			assert redisTemplate.getConnectionFactory() != null;
			String pong = redisTemplate.getConnectionFactory().getConnection().ping();
			log.info("[Redis] 연결 성공: {}", pong);

			String key = "auth:token:user:1";
			String value = "dummy-jwt-token-for-user-1-abcdefg";
			redisTemplate.opsForValue().set(key, value, 1, TimeUnit.HOURS);
			log.info("[Redis] 데이터 생성 완료 (Key: {})", key);

		} catch (Exception e) {
			log.error("[Redis] 연결 또는 데이터 생성 실패", e);
		}
	}

	private void checkAndLoadMysqlData() {
		try {
			testUserMysqlRepository.deleteAll();
			testUserMysqlRepository.save(new TestUser("testUser1", "test1@test.com"));
			testUserMysqlRepository.save(new TestUser("testUser2", "test2@test.com"));
			log.info("[MySQL] 데이터 생성 완료");

		} catch (Exception e) {
			log.error("[MySQL] 연결 또는 데이터 생성 실패", e);
		}
	}

	private void checkAndLoadMongoData() {
		try {
			testProductMongoDBRepository.deleteAll();
			testProductMongoDBRepository.save(new TestProduct("Test Product 1", 99.99, List.of("test", "sample")));
			log.info("[MongoDB] 'TestProduct' 데이터 생성 완료");

		} catch (Exception e) {
			log.error("[MongoDB] 연결 또는 데이터 생성 실패", e);
		}
	}
}
