## JPA와 MyBatis 비교 정리

### 1. 정리

* **JPA (Java Persistence API):** 자바 ORM(Object-Relational Mapping) 기술의 표준으로, 객체와 관계형 데이터베이스의 매핑을 정의하는 명세입니다. 개발자는 SQL 중심이 아닌 객체 중심의 개발을 지향합니다. SQL은 **JPA 구현체(Hibernate 등)**가 자동으로 생성해줍니다.
* **MyBatis:** SQL Mapper 프레임워크로, 개발자가 직접 SQL을 작성하여 자바 객체와 데이터베이스를 연결하는 기술입니다. 개발자가 직접 SQL을 작성하여 **쿼리를 세밀하게 제어**할 수 있으며, SQL과 자바 코드가 분리되어 관리됩니다.



### 2. 주요 차이점

| 구분 | JPA | MyBatis |
| :--- | :--- | :--- |
| **개발 방식** | 객체 중심 | SQL 중심 |
| **SQL 작성** | 자동으로 생성 | 개발자가 직접 작성 |
| **학습 곡선** | 높음 | 낮음 |
| **유지보수** | 객체만 수정하면 되므로 편리 | SQL과 자바 코드를 함께 수정해야 하므로 번거로울 수 있음 |
| **성능 튜닝** | SQL 자동 생성으로 인해 성능 튜닝이 어려울 수 있음 | 개발자가 직접 SQL을 작성하므로 세밀한 성능 튜닝이 가능 |
| **복잡한 쿼리** | JPQL, QueryDSL 등으로 복잡한 쿼리 작성 | XML, 어노테이션에 직접 작성하므로 편리 |
| **데이터베이스 종속성** | 데이터베이스에 독립적 | SQL에 데이터베이스 종속성이 존재 |

---

### 3. 예제 코드 비교

#### **JPA (Spring Data JPA 사용 예시)**

* **User 엔티티 (객체)**
    ```java
    @Entity
    @Table(name = "users")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String email;
        // Getters and Setters
    }
    ```

* **UserRepository (JPA Repository)**
    ```java
    public interface UserRepository extends JpaRepository<User, Long> {
        // SQL 작성 없이 메서드 이름으로 쿼리 생성
        User findByEmail(String email);
    }
    ```

* **서비스 로직**
    ```java
    @Service
    public class UserService {
        @Autowired
        private UserRepository userRepository;

        public User saveUser(User user) {
            // save() 메서드 호출 시, JPA가 자동으로 INSERT SQL 생성
            return userRepository.save(user);
        }

        public User findUserByEmail(String email) {
            // findByEmail() 메서드 호출 시, JPA가 자동으로 SELECT SQL 생성
            return userRepository.findByEmail(email);
        }
    }
    ```

#### **MyBatis**

* **User 모델 (객체)**
    ```java
    public class User {
        private Long id;
        private String name;
        private String email;
        // Getters and Setters
    }
    ```

* **UserMapper (Mapper 인터페이스)**
    ```java
    @Mapper
    public interface UserMapper {
        // @Insert 어노테이션으로 INSERT SQL 직접 작성
        @Insert("INSERT INTO users (name, email) VALUES (#{name}, #{email})")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        void insertUser(User user);

        // @Select 어노테이션으로 SELECT SQL 직접 작성
        @Select("SELECT id, name, email FROM users WHERE email = #{email}")
        User findByEmail(@Param("email") String email);
    }
    ```
* **XML Mapper (XML 파일)**
    ```xml
    <mapper namespace="com.example.mapper.UserMapper">
        <insert id="insertUser" parameterType="com.example.model.User" useGeneratedKeys="true" keyProperty="id">
            INSERT INTO users (name, email) VALUES (#{name}, #{email})
        </insert>
        
        <select id="findByEmail" resultType="com.example.model.User">
            SELECT id, name, email FROM users WHERE email = #{email}
        </select>
    </mapper>
    ```

* **서비스 로직**
    ```java
    @Service
    public class UserService {
        @Autowired
        private UserMapper userMapper;

        public void saveUser(User user) {
            // insertUser() 메서드 호출
            userMapper.insertUser(user);
        }

        public User findUserByEmail(String email) {
            // findByEmail() 메서드 호출
            return userMapper.findByEmail(email);
        }
    }
    ```

---

### 4. 결론

* **JPA:** 객체 지향적인 개발과 생산성 극대화를 목표로 한다면 JPA가 적합합니다. 복잡한 비즈니스 로직을 가진 애플리케이션에 유리하며, **자동으로 생성되는 SQL로 인해 개발 속도가 빠릅니다.**
* **MyBatis:** SQL에 대한 높은 제어권이 필요하거나, 복잡하고 특화된 쿼리가 많은 애플리케이션에 적합합니다. **개발자가 직접 작성하는 SQL로 인해 성능 튜닝이 용이하고** 레거시 시스템과의 연동에도 유리합니다.

두 기술 모두 장단점이 명확하므로, 프로젝트의 특성과 팀의 숙련도에 따라 적절한 기술을 선택하는 것이 중요합니다.