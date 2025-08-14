package com.sonfind.chelsea.repository;

import com.sonfind.chelsea.domain.notification.NotificationDocument;
import com.sonfind.chelsea.types.NotificationDomainType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationDocument, ObjectId> {
	Optional<NotificationDocument>
	findTopByPublisherIdAndPublisherTypeAndSubscriberIdAndSubscriberTypeOrderByUpdatedAtDesc(
			Long publisherId,
			NotificationDomainType publisherType,
			Long subscriberId,
			NotificationDomainType subscriberType);

	List<NotificationDocument> findAllBySubscriberIdAndSubscriberTypeOrderByUpdatedAtDesc(Long subscriberId, NotificationDomainType type);

	List<NotificationDocument> findAllByPublisherIdAndPublisherTypeOrderByUpdatedAtDesc(Long publisherId, NotificationDomainType type);
}
