## SSE를 이용한 실시간 알림 시스템 흐름 

### 1. 개요
이 문서는 **SSE(Server-Sent Events)**를 사용하여 React 프론트엔드와 Spring Boot 백엔드 간에 실시간 알림을 주고받는 전체적인 흐름을 정리합니다. SSE는 HTTP를 통해 서버에서 클라이언트로 일방적인 데이터 스트림을 전송하는 기술입니다.

### 2. 전체적인 시스템 흐름
1.  **클라이언트 (React)의 SSE 연결 요청**:
    * 사용자가 웹 애플리케이션에 접속하면, React 컴포넌트가 백엔드 서버의 SSE 엔드포인트로 연결 요청을 보냅니다. 이 요청은 일반적인 HTTP 요청과는 달리, 연결이 유지되는 `EventSource` 객체를 생성합니다.

2.  **서버 (Spring Boot)의 연결 수락 및 관리**:
    * Spring Boot 서버는 클라이언트로부터 들어온 SSE 연결 요청을 받습니다.
    * 서버는 각 클라이언트의 연결을 구분하고 관리하기 위해, 일반적으로 사용자 ID와 같은 고유한 식별자를 사용해 `SseEmitter` 객체를 생성하고 저장합니다.

3.  **서버의 이벤트 발생 및 전송**:
    * 애플리케이션에서 특정 이벤트(예: 새 댓글, 메시지 도착 등)가 발생하면, 서버는 해당 이벤트를 감지합니다.
    * 이벤트 핸들러는 저장된 `SseEmitter` 객체를 찾아 해당 클라이언트로 알림 데이터를 전송합니다. 이 때 `SseEmitter.send()` 메서드를 사용하며, 데이터와 함께 이벤트 이름(event name)을 지정할 수 있습니다.

4.  **클라이언트 (React)의 이벤트 수신**:
    * React의 `EventSource` 객체는 서버로부터 전송된 알림 데이터를 실시간으로 받습니다.
    * 클라이언트는 `EventSource`의 `onmessage` 또는 `addEventListener`를 통해 수신한 데이터를 처리하는 로직을 구현합니다. 이 데이터를 이용해 화면에 알림을 띄우거나, UI를 업데이트합니다.

---

### 3. 기술 스택별 코드 예시

#### **백엔드 (Spring Boot)**

**`SseController.java`** (SSE 엔드포인트)
```java
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class SseController {

    // 사용자 ID를 키로, SseEmitter를 값으로 저장하는 맵
    public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 연결 유지 시간 설정

        try {
            // 연결 시, 첫 데이터 전송 (연결 확인용)
            emitter.send(SseEmitter.event().name("connect").data("connected!"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        // 맵에 SseEmitter 저장
        sseEmitters.put(userId, emitter);

        // 타임아웃 또는 연결 종료 시 SseEmitter 제거
        emitter.onCompletion(() -> sseEmitters.remove(userId));
        emitter.onTimeout(() -> sseEmitters.remove(userId));
        emitter.onError((e) -> sseEmitters.remove(userId));

        return emitter;
    }
}
```
**`NotificationService.java`** (알림 전송 로직)

```java
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

@Service
public class NotificationService {

    public void sendNotification(String userId, String message) {
        SseEmitter emitter = SseController.sseEmitters.get(userId);
        if (emitter != null) {
            try {
                // 특정 사용자에게 알림 데이터 전송
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }
}
```
#### 프론트엔드 (React)

**`NotificationComponent.jsx`**

```javascript
import React, { useEffect, useState } from 'react';

const NotificationComponent = () => {
    const [notifications, setNotifications] = useState([]);
    const userId = "user123"; // 실제로는 로그인한 사용자의 ID를 사용

    useEffect(() => {
        // 백엔드 SSE 엔드포인트 URL
        const eventSource = new EventSource(`http://localhost:8080/subscribe/${userId}`);

        // 'connect' 이벤트 리스너
        eventSource.addEventListener('connect', (event) => {
            console.log('SSE 연결 성공:', event.data);
        });

        // 'notification' 이벤트 리스너 (서버에서 보낸 알림)
        eventSource.addEventListener('notification', (event) => {
            const newNotification = event.data;
            console.log('새로운 알림:', newNotification);
            setNotifications(prevNotifications => [...prevNotifications, newNotification]);
        });

        // 에러 이벤트 리스너
        eventSource.onerror = (error) => {
            console.error('SSE 오류 발생:', error);
            eventSource.close();
        };

        // 컴포넌트 언마운트 시 연결 종료
        return () => {
            eventSource.close();
        };
    }, [userId]);

    return (
        <div>
            <h2>알림</h2>
            <ul>
                {notifications.map((note, index) => (
                    <li key={index}>{note}</li>
                ))}
            </ul>
        </div>
    );
};

export default NotificationComponent;
```

### 4. 핵심 정리
- HTTP 연결 유지: SSE는 일반적인 HTTP 요청과 달리 연결이 유지되므로, 서버가 클라이언트에게 실시간으로 데이터를 보낼 수 있습니다.

- 단방향 통신: 클라이언트가 요청하고 서버가 응답하는 단방향 통신입니다. 클라이언트에서 서버로 데이터를 보낼 수는 없습니다.

- EventSource 객체: React에서 new EventSource()를 사용해 서버와의 연결을 시작합니다.

- SseEmitter 객체: Spring Boot에서 SseEmitter를 사용해 클라이언트에게 이벤트를 전송하고 연결을 관리합니다.

- 재연결 기능: EventSource는 연결이 끊어졌을 때 자동으로 재연결을 시도하는 기능이 내장되어 있어 안정적입니다.