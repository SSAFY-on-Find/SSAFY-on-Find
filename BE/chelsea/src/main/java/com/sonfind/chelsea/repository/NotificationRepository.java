package com.sonfind.chelsea.repository;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.types.RecipientRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationDocument, ObjectId> {
	/**
	 * publisherId, publisherType, subscriberId, subscriberType 조건으로
	 * 가장 최신 문서를 가져옵니다.
	 */
	@Query(value = "{ " + "'publisherId': ?0, " + "'publisherType': ?1, " + "'subscriberId': ?2, "
			+ "'subscriberType': ?3" + "}", sort = "{ 'updatedAt': -1 }")
	NotificationDocument findLatest(Long publisherId, String publisherType, Long subscriberId, String subscriberType);

	NotificationDocument findAllBySubscriberIdAndSubscriberType(Long subscriberId, RecipientRole role);

	NotificationDocument findAllByPublisherIdAndPublisherType(Long publisherId, RecipientRole role);
}
