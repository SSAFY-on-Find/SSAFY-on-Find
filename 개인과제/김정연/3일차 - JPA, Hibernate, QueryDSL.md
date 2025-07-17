## JPA (Java Persistence API)

JPA는 Java에서 관계형 데이터베이스를 다루기 위한 표준 명세입니다. 객체와 데이터베이스 테이블 간의 매핑을 정의하고, 객체 지향적인 방식으로 데이터베이스 작업을 할 수 있게 해줍니다.

**주요 특징:**
- 표준 API로 벤더 독립적
- 어노테이션 기반 매핑 (`@Entity`, `@Table`, `@Column` 등)
- JPQL(Java Persistence Query Language) 지원
- 자동 DDL 생성 및 스키마 관리

## Hibernate

Hibernate는 JPA의 가장 널리 사용되는 구현체입니다. JPA 표준을 구현하면서 추가적인 기능들을 제공합니다.

**주요 특징:**
- JPA 표준 구현 + 확장 기능
- 강력한 캐싱 시스템 (1차, 2차 캐시)
- Lazy Loading 지원
- HQL(Hibernate Query Language) 제공
- 다양한 데이터베이스 방언(Dialect) 지원

**기본 사용 예시:**
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username")
    private String username;
    
    // getter, setter...
}

// Repository에서 사용
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
}
```

## QueryDSL

QueryDSL은 타입 안전한 쿼리를 작성할 수 있게 해주는 프레임워크입니다. 컴파일 타임에 쿼리 오류를 검출할 수 있어 안정성이 높습니다.

**주요 특징:**
- 타입 안전성 보장
- IDE의 자동완성 지원
- 메소드 체이닝을 통한 직관적인 쿼리 작성
- 동적 쿼리 작성에 유리
- 다양한 데이터 소스 지원 (JPA, MongoDB, SQL 등)

**기본 사용 예시:**
```java
// Q클래스 자동 생성 (QUser)
QUser user = QUser.user;

// QueryDSL을 사용한 쿼리
List<User> users = queryFactory
    .selectFrom(user)
    .where(user.username.eq("john")
        .and(user.age.gt(18)))
    .orderBy(user.username.asc())
    .fetch();

// 동적 쿼리 예시
BooleanBuilder builder = new BooleanBuilder();
if (username != null) {
    builder.and(user.username.eq(username));
}
if (minAge != null) {
    builder.and(user.age.goe(minAge));
}

List<User> results = queryFactory
    .selectFrom(user)
    .where(builder)
    .fetch();
```

## 조합 사용의 장점

이 세 기술을 함께 사용하면:

1. **JPA**로 기본적인 CRUD 및 간단한 쿼리 처리
2. **Hibernate**의 고급 기능 활용 (캐싱, 성능 최적화)
3. **QueryDSL**로 복잡한 동적 쿼리 및 타입 안전한 쿼리 작성