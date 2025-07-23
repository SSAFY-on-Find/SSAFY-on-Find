package com.sonfind.chelsea.health;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class HealthCheckIntegrationTest {
    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private TestUserMysqlRepository testUserMysqlRepository;
    @Autowired
    private TestProductMongoDBRepository testProductMongoDBRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("데이터 로더를 통해 MySQL에 사용자 데이터가 정상적으로 저장된다.")
    void loadAndVerifyMysqlData() {
        // given
        dataLoader.loadTestData();

        // when
        List<TestUser> users = testUserMysqlRepository.findAll();
        List<TestProduct> products = testProductMongoDBRepository.findAll();
        String redisToken = redisTemplate.opsForValue().get("auth:token:user:1");

        // then
        assertThat(users).hasSize(2);
        assertThat(users)
                .extracting(TestUser::getUsername)
                .containsExactlyInAnyOrder("testUser1", "testUser2");

        assertThat(products).hasSize(1);
        TestProduct product = products.get(0);
        assertThat(product.getProductName()).isEqualTo("Test Product 1");
        assertThat(product.getPrice()).isEqualTo(99.99);

        assertThat(redisToken).isEqualTo("dummy-jwt-token-for-user-1-abcdefg");
    }
}
