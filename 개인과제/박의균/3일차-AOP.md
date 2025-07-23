## ☑️ **AOP를 이용한 자동 조회란?**

AOP를 사용하면 **비즈니스 로직(User 조회)과 공통 처리 로직(예외 처리 등)을 분리**할 수 있다.

- `@RequireUser` 어노테이션만 붙이면 **자동으로 User를 찾아서 주입**할 수 있다.
- `username`을 직접 조회하는 코드 없이 **메서드 실행 전 자동으로 주입**된다.
- `UserRepository.findByUsername()`을 직접 호출하는 중복을 제거할 수 있다.

## ☑️ **AOP 동작 원리**

AOP는 **Spring의 프록시 패턴**을 활용하여 메서드 실행 전후에 추가적인 로직을 삽입한다.

🔹 **기존 방식**

```java
public void updateUserEmail(String username, String newEmail) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));

    user.setEmail(newEmail);
    userRepository.save(user);
}
```

➡ **매번 `findByUsername()`을 직접 호출해야 하므로 중복이 발생한다.**

🔹 **AOP 적용 후**

```java
@RequireUser
public void updateUserEmail(User user, String newEmail) {
    user.setEmail(newEmail);
    userRepository.save(user);
}
```

➡ **`@RequireUser`만 붙이면 자동으로 User를 조회하여 주입한다.**

## ☑️ **AOP 적용 방법**

### ✅ **1 - 커스텀 어노테이션 생성**

AOP에서 사용할 **커스텀 어노테이션**을 생성한다.

```java
@Target(ElementType.METHOD)  // 메서드에만 적용 가능
@Retention(RetentionPolicy.RUNTIME)  // 실행 중에도 유지됨
public @interface RequireUser {}
```

📌 **설명**

- `@Target(ElementType.METHOD)`: **메서드에만 적용 가능**하게 지정한다.
- `@Retention(RetentionPolicy.RUNTIME)`: 런타임까지 유지되도록 설정한다.

### ✅ **2 - AOP Aspect 클래스 생성**

AOP를 적용하여 **메서드 실행 전에 User를 찾아서 자동으로 주입**하도록 한다.

```java
@Aspect
@Component
@RequiredArgsConstructor
public class UserAspect {

    private final UserRepository userRepository;

    @Around("@annotation(RequireUser) && args(username,..)")
    public Object findUserAndProceed(ProceedingJoinPoint joinPoint, String username)
																																     throws Throwable {
        // DB에서 username으로 User 조회
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다");

        // 원래 메서드의 인자 배열을 가져와서 첫 번째 인자를 User로 변경
        Object[] args = joinPoint.getArgs();
        args[0] = user; // username 대신 User 객체로 변경

        // 변경된 인자로 원래 메서드를 실행
        return joinPoint.proceed(args);
    }
}
```

📌 **설명**

- `@Aspect`: 이 클래스가 AOP 역할을 한다고 선언한다.
- `@Component`: Spring이 관리할 수 있도록 **Bean 등록**한다.
- `@Around("@annotation(RequireUser) && args(username,..)")`
    - `@RequireUser`가 붙어 있는 메서드에서
    - 첫 번째 매개변수가 `username`일 때 실행된다.
- `joinPoint.getArgs()`: 원래 메서드의 인자를 가져온다.
- `args[0] = user;`: 첫 번째 인자를 `User` 객체로 변경한다.
- `joinPoint.proceed(args)`: 변경된 인자를 넣어서 **원래 메서드를 실행**한다.

### ✅ **3 - UserService에서 AOP 적용**

AOP를 적용한 **UserService 예제**

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @RequireUser
    public UserResponse getUserByUsername(User user, ADto adto) {
    
		    Post post = postRepository.findByUserAndY(user, adto.y).orElseThrow(new BUdfsasdafl());
		    
        return new UserResponse(user);
    }

    @RequireUser
    public void updateUserEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        userRepository.save(user);
    }
}
```

📌 **설명**

- `@RequireUser`를 붙이면 **username이 아닌 User 객체를 자동으로 주입**받는다.
- `findByUsername()`을 호출하지 않아도 AOP가 알아서 찾아준다.

## ☑️ **AOP 적용 후 장점**

| **장점** | **설명** |
| --- | --- |
| 🚀 **코드 중복 제거** | `findByUsername()`을 반복 호출하지 않아도 된다. |
| 🛠 **코드 유지보수 용이** | User 조회 로직을 AOP에서 한 곳에서 관리할 수 있다. |
| ⚡ **가독성 증가** | 서비스 로직이 **User 조회**가 아닌 **비즈니스 로직**만 신경 쓰면 된다. |
| 🔥 **자동 조회** | `@RequireUser`만 붙이면 자동으로 User를 주입받을 수 있다. |

## ☑️ **AOP 적용 후의 한계점 및 해결책**

| **단점** | **해결책** |
| --- | --- |
| 🛠 **AOP 설정이 복잡할 수 있다.** | 한 번 설정하면 이후엔 자동 적용된다. |
| 🔥 **디버깅이 어려울 수 있다.** | AOP 로그를 남기거나, 디버깅을 위해 `joinPoint.getSignature()` 활용한다. |
| 🏗 **모든 메서드에 적용해야 한다.** | AOP 범위를 조정하여 특정 서비스에만 적용할 수 있다. |

## ☑️ **AOP 추가 기능: 여러 개의 인자를 처리**

만약 **username 외에도 추가적인 인자가 있을 경우** 처리하는 방법은 다음과 같다.

```java
@Around("@annotation(RequireUser) && args(username,..)")
public Object findUserAndProceed(ProceedingJoinPoint joinPoint, String username) throws Throwable {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다: " + username));

    Object[] args = joinPoint.getArgs();
    args[0] = user; // 첫 번째 인자를 User 객체로 변경

    return joinPoint.proceed(args);
}

```

📌 **설명**

- `args(username,..)`: **첫 번째 인자가 username일 때만 실행된다.**
- **추가적인 인자가 있어도 자동으로 유지된다.**

## ☑️ **결론**

1️⃣ **기존 방식**

- `findByUsername()`을 여러 번 호출해야 해서 **중복 코드가 증가**한다.🚨

2️⃣ **AOP 적용 방식**

- `@RequireUser` 어노테이션 하나만 붙이면 **자동으로 User를 주입**한다.🔥
- `UserService` 코드가 **더욱 깔끔**하고 **가독성이 증가**한다.
- **비즈니스 로직에 집중**할 수 있다.

🚀 **대규모 프로젝트에서는 강력한 효과를 볼 수 있다.**

🔥 **적절한 상황에서 AOP를 활용하면 유지보수성과 가독성이 급격히 증가한다.**