package com.sonfind.chelsea.repository;

import com.sonfind.chelsea.domain.notification.NotificationStatusDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.types.RecipientRole;
import com.sonfind.chelsea.types.NotificationStatus;

@Repository
public interface NotificationStatusRepository extends MongoRepository<NotificationStatusDocument, Long> {

}
