package com.sonfind.chelsea.util;

import java.util.function.Supplier;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PerformanceMeasurer {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public PerformanceResult measureWithHibernateStats(String operationName, Supplier<Object> operation) {
		// Hibernate 통계 초기화
		SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		Statistics stats = sessionFactory.getStatistics();
		stats.clear();

		long startTime = System.currentTimeMillis();
		long startQueryCount = stats.getQueryExecutionCount();
		long startEntityLoadCount = stats.getEntityLoadCount();

		// 실제 작업 실행
		Object result = operation.get();

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		long queryCount = stats.getQueryExecutionCount() - startQueryCount;
		long entityLoadCount = stats.getEntityLoadCount() - startEntityLoadCount;

		PerformanceResult performanceResult = new PerformanceResult(
			operationName,
			totalTime,
			queryCount,
			entityLoadCount,
			result
		);

		log.info("=== {} 성능 측정 결과 ===", operationName);
		log.info("실행시간: {}ms", totalTime);
		log.info("쿼리 수: {}개", queryCount);
		log.info("엔티티 로드 수: {}개", entityLoadCount);
		log.info("================================");

		return performanceResult;
	}

	public static class PerformanceResult {
		public final String operationName;
		public final long executionTime;
		public final long queryCount;
		public final long entityLoadCount;
		public final Object result;

		public PerformanceResult(String operationName, long executionTime, long queryCount,
			long entityLoadCount, Object result) {
			this.operationName = operationName;
			this.executionTime = executionTime;
			this.queryCount = queryCount;
			this.entityLoadCount = entityLoadCount;
			this.result = result;
		}
	}
}