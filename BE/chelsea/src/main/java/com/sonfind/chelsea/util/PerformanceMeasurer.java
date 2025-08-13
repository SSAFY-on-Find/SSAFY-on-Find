package com.sonfind.chelsea.util;

import java.util.function.Supplier;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PerformanceMeasurer {

	@PersistenceContext
	private EntityManager entityManager;

	public PerformanceResult measureWithHibernateStats(String operationName, Supplier<Object> operation) {
		SessionFactory sessionFactory = null;
		Statistics stats = null;

		try {
			// EntityManager를 통해 SessionFactory 가져오기
			sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
			stats = sessionFactory.getStatistics();

			// 통계 초기화
			stats.clear();

		} catch (Exception e) {
			log.warn("Hibernate Statistics를 사용할 수 없습니다. 단순 시간 측정으로 진행: {}", e.getMessage());
			return measureSimple(operationName, operation);
		}

		// 측정 시작
		long startTime = System.currentTimeMillis();
		long startQueries = stats.getQueryExecutionCount();
		long startEntityLoads = stats.getEntityLoadCount();

		log.info("=== {} 측정 시작 ===", operationName);

		// 실제 작업 실행
		Object result = operation.get();

		// 측정 종료
		long endTime = System.currentTimeMillis();
		long endQueries = stats.getQueryExecutionCount();
		long endEntityLoads = stats.getEntityLoadCount();

		long executionTime = endTime - startTime;
		long queryCount = endQueries - startQueries;
		long entityLoadCount = endEntityLoads - startEntityLoads;

		log.info("=== {} 측정 완료 ===", operationName);
		log.info("실행시간: {}ms", executionTime);
		log.info("쿼리 실행 횟수: {}회", queryCount);
		log.info("엔티티 로드 횟수: {}회", entityLoadCount);
		log.info("추가 통계 - 캐시 히트: {}, 미스: {}", stats.getSecondLevelCacheHitCount(),
			stats.getSecondLevelCacheMissCount());

		return PerformanceResult.builder()
			.operationName(operationName)
			.executionTime(executionTime)
			.queryCount(queryCount)
			.entityLoadCount(entityLoadCount)
			.cacheHitCount(stats.getSecondLevelCacheHitCount())
			.cacheMissCount(stats.getSecondLevelCacheMissCount())
			.build();
	}

	public PerformanceResult measureSimple(String operationName, Supplier<Object> operation) {
		log.info("=== {} 간단 측정 시작 ===", operationName);

		long startTime = System.currentTimeMillis();
		Object result = operation.get();
		long executionTime = System.currentTimeMillis() - startTime;

		log.info("=== {} 간단 측정 완료 - {}ms ===", operationName, executionTime);

		return PerformanceResult.builder()
			.operationName(operationName)
			.executionTime(executionTime)
			.queryCount(-1) // 미측정 표시
			.entityLoadCount(-1)
			.build();
	}

	@lombok.Data
	@lombok.Builder
	@lombok.AllArgsConstructor
	@lombok.NoArgsConstructor
	public static class PerformanceResult {
		private String operationName;
		public long executionTime;
		public long queryCount;
		public long entityLoadCount;
		private long cacheHitCount;
		private long cacheMissCount;
	}
}