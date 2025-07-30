package com.sonfind.chelsea.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.notification.NotificationDocument;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationDocument, Long> {
    /**
    * publisherId, publisherType, subscriberId, subscriberType 조건으로
    * 가장 최신 문서를 가져옵니다.
    */
    @Query(value = "{ "
          + "'publisherId': ?0, "
          + "'publisherType': ?1, "
          + "'subscriberId': ?2, "
          + "'subscriberType': ?3"
          + "}",
          sort  = "{ 'createdAt': -1 }")
    NotificationDocument findLatest(
          Long publisherId,
          String publisherType,
          Long subscriberId,
          String subscriberType
    );
}
