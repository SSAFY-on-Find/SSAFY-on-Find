## Redis를 이용한 '좋아요' 기능 구현

### 1. Redis를 사용하는 이유 (핵심)

* **빠른 속도**: 좋아요와 같은 빈번한 요청을 처리할 때, 데이터가 메모리에 저장되어 있어 매우 빠르게 응답합니다.
* **간단한 데이터 구조**: 좋아요 상태를 저장하고 조회하는 데 특화된 **Set (집합)** 타입을 사용합니다.
* **원자적 연산**: 여러 사용자가 동시에 '좋아요'를 눌러도 데이터의 충돌 없이 안전하게 처리됩니다.

### 2. 핵심 Redis 명령어와 Spring Boot 연동

'좋아요' 기능은 Redis의 **Set** 데이터 타입과 다음 네 가지 명령어로 구현합니다.

| 명령어 | 용도 | Spring Boot 코드 |
| :--- | :--- | :--- |
| **SADD** | 좋아요 추가 | `redisTemplate.opsForSet().add(key, userId);` |
| **SREM** | 좋아요 취소 | `redisTemplate.opsForSet().remove(key, userId);` |
| **SISMEMBER** | 좋아요 상태 확인 | `redisTemplate.opsForSet().isMember(key, userId);` |
| **SCARD** | 좋아요 개수 조회 | `redisTemplate.opsForSet().size(key);` |

### 3. Spring Boot 구현 흐름 (간소화)

1.  **설정**: `application.properties`에 Redis 서버 정보를 추가합니다.
    ```properties
    spring.data.redis.host=localhost
    spring.data.redis.port=6379
    ```

2.  **서비스 로직**: `LikeService`에서 `RedisTemplate`을 주입받아 핵심 로직을 구현합니다.
    * **좋아요/취소**: `toggleLike()` 메서드에서 `SISMEMBER`로 좋아요 상태를 확인한 후, `SADD`나 `SREM`을 호출해 상태를 변경합니다.
    * **개수 조회**: `getLikeCount()` 메서드에서 `SCARD`를 이용해 좋아요 수를 반환합니다.

3.  **컨트롤러**: `LikeController`에서 REST API를 통해 클라이언트 요청을 받고, `LikeService`를 호출합니다.
    * **POST /api/likes/{itemId}/toggle**: 좋아요를 누르거나 취소하는 API.
    * **GET /api/likes/{itemId}/count**: 특정 아이템의 좋아요 개수를 조회하는 API.

### 4. 전체적인 데이터 흐름

* **1단계**: 클라이언트(프론트엔드)에서 '좋아요' 버튼을 클릭하면, Spring Boot 백엔드의 `POST /api/likes/{itemId}/toggle` API를 호출합니다.

* **2단계**: `LikeController`가 요청을 받아 `LikeService`의 `toggleLike` 메서드를 실행합니다.

* **3단계**: `LikeService`는 `redisTemplate`을 이용해 Redis 서버에 `SADD` 또는 `SREM` 명령어를 전달합니다. 이때, 키(key)는 `likes:item:{itemId}` 형식을 사용하고, 값(value)은 사용자의 ID를 저장합니다.

* **4단계**: Redis는 해당 명령어를 즉시 처리하고, `LikeService`는 결과를 받아 컨트롤러에 반환합니다. 클라이언트는 이 응답을 바탕으로 좋아요 버튼의 상태와 개수를 업데이트합니다.

이러한 흐름을 통해 데이터베이스에 직접 접근하는 방식보다 훨씬 빠르고 효율적인 '좋아요' 기능을 구현할 수 있습니다.