package com.sonfind.chelsea.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.types.NotificationStatus;

@Repository
public interface NotificationStatusRepository extends MongoRepository<NotificationStatusDocument, Long> {

	List<NotificationStatusDocument> findByNotificationId(ObjectId notificationLogId);

	Boolean existsByNotificationIdAndStatus(ObjectId notificationId, NotificationStatus status);
}
