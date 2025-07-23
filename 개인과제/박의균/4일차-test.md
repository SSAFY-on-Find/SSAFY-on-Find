## ☑️ **1. AcceptanceTestTemplate**

### ✅ 1_1. **목적**

`AcceptanceTestTemplate` 클래스는 인수 테스트의 공통 템플릿을 정의합니다. 테스트 실행 전에 데이터베이스를 정리하고, 테스트에 필요한 데이터를 로드하는 역할을 합니다.

### ✅ 1_2. **주요 동작**

- **`@SpringBootTest`**: Spring Boot 애플리케이션을 통합 테스트 모드로 실행합니다. `webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT` 옵션을 사용하여 테스트가 임의의 포트에서 실행됩니다.
- **`@ActiveProfiles("test")`**: `test` 프로파일을 활성화하여 테스트 환경에서만 이 클래스가 실행되도록 합니다.
- **`@Autowired`**: Spring 컨테이너에서 `DatabaseCleanUp`과 `DataLoader` 빈을 자동으로 주입받습니다.
- **`@BeforeEach`**: 각 테스트가 실행되기 전에 `init()` 메서드를 실행하여, 데이터베이스 정리 및 데이터를 로딩합니다.

### ✅ 1_3. **메서드**

- **`init()`**: 테스트 시작 전 매번 호출되며, 데이터베이스 정리(`cleanUp.execute()`) 및 테스트 데이터를 로딩(`loader.loadData()`)을 수행합니다.

### ✅ 1_4. **코드**

```java
package com.sample.acceptance.utils;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestTemplate {

    @Autowired
    private DatabaseCleanUp cleanUp;

    @Autowired
    private DataLoader loader;

    @BeforeEach
    public void init() {
        cleanUp.execute();
        loader.loadData();
    }
}
```

---

## ☑️ **2. UserAcceptanceSteps**

### ✅ 2_1. **목적**

`UserAcceptanceSteps` 클래스는 사용자 관련 API의 요청을 처리하는 인수 테스트 스텝을 정의합니다. 주로 RestAssured를 사용하여 API 호출을 하고 응답을 추출하는 역할을 합니다.

### ✅ 2_2.**주요 동작**

- **`createUser()`**: 사용자 생성 요청을 처리합니다. `CreateUserRequestDto`를 요청 본문으로 보내 `/user` 엔드포인트에 POST 요청을 보냅니다. 응답을 `ExtractableResponse` 형태로 반환합니다.
- **`followUser()`**: 사용자 간의 팔로우 관계를 생성하는 요청을 처리합니다. `FollowUserRequestDto`를 요청 본문으로 보내 `/relation/follow` 엔드포인트에 POST 요청을 보냅니다. 응답을 `ExtractableResponse` 형태로 반환합니다.

### ✅ 2_3.**메서드**

- **`createUser()`**: 사용자 생성 API 호출
- **`followUser()`**: 팔로우 관계 설정 API 호출

### ✅ 2_4 .**코드**

```java
package com.sample.acceptance.steps;

import com.sample.user.application.dto.CreateUserRequestDto;
import com.sample.user.application.dto.FollowUserRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class UserAcceptanceSteps {
    public static ExtractableResponse<Response> createUser(CreateUserRequestDto dto) {
        return RestAssured
                .given()
                .body(dto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/user")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> followUser(FollowUserRequestDto dto) {
        return RestAssured
                .given()
                .body(dto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/relation/follow")
                .then()
                .extract();
    }
}

```

---

## ☑️ **3. DatabaseCleanUp**

### ✅ 3_1. **목적**

`DatabaseCleanUp` 클래스는 인수 테스트에서 데이터베이스의 데이터를 초기화하는 작업을 처리합니다. 테스트 전에 데이터베이스를 정리하고 기본 키를 리셋하여 깨끗한 상태로 테스트를 시작할 수 있도록 합니다.

### ✅ 3_2. **주요 동작**

- **`@Profile("test")`**: `test` 프로파일에서만 활성화되어, 테스트 환경에서만 실행됩니다.
- **`@Component`**: Spring의 컴포넌트로 등록되어, Spring IoC 컨테이너에서 관리됩니다.
- **`@Transactional`**: 트랜잭션을 통해 데이터베이스 작업을 처리합니다.
- **`@PersistenceContext`**: `EntityManager`를 주입받아 데이터베이스와 상호작용합니다.

### ✅ 3_3. **메서드**

- **`afterPropertiesSet()`**: `InitializingBean` 인터페이스의 메서드로, Spring이 빈을 초기화한 후에 호출됩니다. 데이터베이스의 모든 엔티티 테이블 이름을 조회하여 `tableNames` 리스트에 저장하고, 기본 키를 리셋하지 않아야 할 테이블들을 `notGeneratedTableNames`에 저장합니다.
- **`execute()`**:
    - `entityManager.clear()`로 영속성 컨텍스트를 초기화하여 이전에 관리되었던 엔티티들을 모두 지웁니다.
    - `SET REFERENTIAL_INTEGRITY FALSE` 명령을 통해 외래 키 제약 조건을 비활성화하여, `TRUNCATE` 명령이 실패하지 않도록 합니다.
    - 각 테이블에 대해 `TRUNCATE TABLE` 명령을 실행하여 데이터를 삭제하고, `ALTER TABLE ... RESTART WITH 1` 명령을 통해 `id` 컬럼을 리셋합니다. `notGeneratedTableNames`에 포함된 테이블은 제외합니다.
    - 작업이 끝나면 `SET REFERENTIAL_INTEGRITY TRUE` 명령을 통해 외래 키 제약 조건을 다시 활성화합니다.

### ✅ 3_4. **코드**

```java
package com.sample.acceptance.utils;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 인수 테스트를 위헤 데이터베이스의 데이터를 지우는 작업을 한다.
 */
@Profile("test")
@Component
@Slf4j
public class DatabaseCleanUp implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;
    private List<String> tableNames;
    private List<String> notGeneratedTableNames;

    @Override
    public void afterPropertiesSet() throws Exception {
        tableNames = entityManager.getMetamodel().getEntities()
                .stream()
                .filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
                .map(entity -> Objects.requireNonNull(entity.getJavaType().getAnnotation(Table.class)).name())
                .toList();

        notGeneratedTableNames = List.of("community_user_relation", "community_like");
    }

    @Transactional
    public void execute() {
        entityManager.clear();;
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            if(!notGeneratedTableNames.contains(tableName)) {
                entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
            }
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
```

---

## ☑️ **4. DataLoader**

### ✅ 4_1. **목적**

`DataLoader` 클래스는 테스트 데이터를 로드하는 작업을 담당합니다. 이 클래스는 테스트 실행 전 필요한 데이터를 데이터베이스에 로드하여, 테스트가 정상적으로 진행될 수 있도록 합니다.

### ✅ 4_2. **주요 동작**

- **`loadData()`**:
    - 세 명의 사용자 데이터를 생성합니다. 각 사용자는 `CreateUserRequestDto` 객체로 생성되며, `createUser()` 메서드를 호출하여 사용자 정보를 `/user` API에 전송합니다.
    - `FollowUserRequestDto`를 사용하여 1번 사용자가 2번과 3번 사용자를 팔로우하는 관계를 생성합니다. `followUser()` 메서드를 호출하여 팔로우 요청을 처리합니다.

### ✅ 4_3. **메서드**

- **`loadData()`**: 테스트에 필요한 데이터를 데이터베이스에 로드합니다. 사용자 생성 및 팔로우 관계를 설정하는 작업을 포함합니다.

### ✅ 4_4. **코드**

```java
package com.sample.acceptance.utils;

import com.sample.user.application.dto.CreateUserRequestDto;
import com.sample.user.application.dto.FollowUserRequestDto;
import org.springframework.stereotype.Component;

import static com.sample.acceptance.steps.UserAcceptanceSteps.createUser;
import static com.sample.acceptance.steps.UserAcceptanceSteps.followUser;

@Component
public class DataLoader {

    public void loadData() {
        CreateUserRequestDto dto = new CreateUserRequestDto("test user", "");
        createUser(dto);
        createUser(dto);
        createUser(dto);

        followUser(new FollowUserRequestDto(1L, 2L));
        followUser(new FollowUserRequestDto(1L, 3L));
    }
}
```

---