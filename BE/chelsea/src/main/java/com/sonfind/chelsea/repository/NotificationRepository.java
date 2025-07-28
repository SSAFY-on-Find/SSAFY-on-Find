package com.sonfind.chelsea.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationDocument, Long> {
	// 알림 발신자와 수신자, 상태로 알림을 조회
	Optional<NotificationDocument> findByPublisherAndSubscriberAndStatus(
		Long publisherId,
		NotificationDomainType publisherType,
		Long subscriberId,
		NotificationDomainType subscriberType,
		NotificationStatus status);

	// 알림 발신자와 수신자, 상태로 알림이 존재하는지 확인
	boolean existsByPublisherIdAndPublisherTypeAndSubscriberIdAndSubscriberTypeAndStatus(
		long pubId,
		NotificationDomainType pubType,
		long subId,
		NotificationDomainType subType,
		NotificationStatus status
	);
}
