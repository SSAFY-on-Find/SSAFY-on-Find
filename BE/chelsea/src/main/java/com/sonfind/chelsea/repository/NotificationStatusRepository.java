package com.sonfind.chelsea.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.RecipientRole;

@Repository
public interface NotificationStatusRepository extends MongoRepository<NotificationStatusDocument, ObjectId> {

	List<NotificationStatusDocument> findByNotificationId(ObjectId notificationLogId);

	Boolean existsByNotificationIdAndStatus(ObjectId notificationId, NotificationStatus status);

	List<NotificationStatusDocument> findAllByTargetIdAndTargetTypeAndRole(Long studentId,
		NotificationDomainType targetType, RecipientRole role);

	int countByTargetIdAndTargetTypeAndRoleAndIsReadFalse(Long studentId, NotificationDomainType targetType,
		RecipientRole role);

	NotificationStatusDocument findByNotificationIdAndTargetIdAndRoleAndStatus(ObjectId notificationId,
		Long targetId, RecipientRole role, NotificationStatus status);
}
