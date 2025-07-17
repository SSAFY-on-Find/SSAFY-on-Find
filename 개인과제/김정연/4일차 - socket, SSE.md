## Socket (소켓)

소켓은 네트워크 통신을 위한 양방향 통신 채널입니다. Java에서는 TCP/UDP 소켓을 제공합니다.

### TCP 소켓 예시

**서버 측:**
```java
// ServerSocket 생성
ServerSocket serverSocket = new ServerSocket(8080);
System.out.println("서버가 포트 8080에서 대기 중...");

while (true) {
    // 클라이언트 연결 수락
    Socket clientSocket = serverSocket.accept();
    
    // 새 스레드에서 클라이언트 처리
    new Thread(() -> {
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(
                clientSocket.getOutputStream(), true);
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("받은 메시지: " + inputLine);
                out.println("Echo: " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }).start();
}
```

**클라이언트 측:**
```java
Socket socket = new Socket("localhost", 8080);

PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
BufferedReader in = new BufferedReader(
    new InputStreamReader(socket.getInputStream()));

// 메시지 전송
out.println("안녕하세요!");

// 응답 받기
String response = in.readLine();
System.out.println("서버 응답: " + response);
```

### 소켓의 특징
- **양방향 통신**: 클라이언트와 서버 모두 데이터 전송 가능
- **실시간 통신**: 지속적인 연결 유지
- **낮은 지연시간**: 직접적인 TCP/UDP 연결
- **복잡한 관리**: 연결 상태, 스레드 관리 필요

## SSE (Server-Sent Events)

SSE는 서버에서 클라이언트로 실시간 이벤트를 스트리밍하는 웹 표준입니다.

### Spring Boot에서 SSE 구현

```java
@RestController
public class SSEController {
    
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        // 비동기로 이벤트 전송
        CompletableFuture.runAsync(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    // 이벤트 전송
                    emitter.send(SseEmitter.event()
                        .name("message")
                        .data("메시지 " + i)
                        .id(String.valueOf(i)));
                    
                    Thread.sleep(1000); // 1초 간격
                }
                emitter.complete(); // 스트림 종료
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        
        return emitter;
    }
    
    // 실시간 채팅 예시
    @GetMapping("/chat")
    public SseEmitter chatStream() {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30분 타임아웃
        
        // 클라이언트 연결 관리
        chatClients.add(emitter);
        
        emitter.onCompletion(() -> chatClients.remove(emitter));
        emitter.onTimeout(() -> chatClients.remove(emitter));
        
        return emitter;
    }
    
    // 모든 클라이언트에게 메시지 브로드캐스트
    @PostMapping("/broadcast")
    public void broadcastMessage(@RequestBody String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        
        chatClients.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                    .name("chat")
                    .data(message));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        
        chatClients.removeAll(deadEmitters);
    }
}
```

### 클라이언트 측 (JavaScript)

```javascript
// SSE 연결
const eventSource = new EventSource('/events');

// 이벤트 리스너 등록
eventSource.addEventListener('message', function(event) {
    console.log('받은 메시지:', event.data);
});

// 특정 이벤트 타입 리스너
eventSource.addEventListener('chat', function(event) {
    const chatDiv = document.getElementById('chat');
    chatDiv.innerHTML += '<div>' + event.data + '</div>';
});

// 에러 처리
eventSource.onerror = function(event) {
    console.error('SSE 연결 오류:', event);
};
```

## 비교 분석

### 소켓 vs SSE

| 특징 | Socket | SSE |
|------|--------|-----|
| **통신 방향** | 양방향 | 단방향 (서버→클라이언트) |
| **프로토콜** | TCP/UDP | HTTP |
| **구현 복잡도** | 높음 | 낮음 |
| **브라우저 지원** | WebSocket 필요 | 네이티브 지원 |
| **방화벽 통과** | 어려움 | 쉬움 (HTTP 기반) |
| **재연결** | 수동 구현 | 자동 재연결 |

### 사용 시나리오

**Socket 사용 시:**
- 실시간 게임
- 채팅 애플리케이션 (양방향)
- 파일 전송
- 실시간 협업 도구

**SSE 사용 시:**
- 실시간 알림
- 라이브 피드 (뉴스, 주식)
- 서버 모니터링 대시보드
- 실시간 로그 스트리밍

### 실제 사용 예시 (알림 시스템)

```java
@Service
public class NotificationService {
    private final Map<String, SseEmitter> userConnections = new ConcurrentHashMap<>();
    
    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        userConnections.put(userId, emitter);
        
        emitter.onCompletion(() -> userConnections.remove(userId));
        emitter.onTimeout(() -> userConnections.remove(userId));
        
        return emitter;
    }
    
    public void sendNotification(String userId, String message) {
        SseEmitter emitter = userConnections.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                    .name("notification")
                    .data(message));
            } catch (IOException e) {
                userConnections.remove(userId);
            }
        }
    }
}
```