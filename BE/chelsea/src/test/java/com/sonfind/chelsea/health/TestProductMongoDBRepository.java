package com.sonfind.chelsea.health;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestProductMongoDBRepository extends MongoRepository<TestProduct, String> {
}
