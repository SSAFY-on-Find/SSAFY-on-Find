package com.sonfind.chelsea.repository;

import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import com.sonfind.chelsea.dto.notification.StatusValue;
import com.sonfind.chelsea.types.NotificationDomainType;
import com.sonfind.chelsea.types.NotificationStatus;
import com.sonfind.chelsea.types.RecipientRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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

	StatusValue findTopByNotificationIdOrderByUpdatedAtDesc(ObjectId notificationId);
}
