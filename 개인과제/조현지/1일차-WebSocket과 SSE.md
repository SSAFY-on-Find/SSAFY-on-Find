
# WebSocket vs Server-Sent Events (SSE) 비교

## 1\. Server-Sent Events (SSE)

**SSE**는 서버에서 클라이언트로의 **단방향** 이벤트 스트리밍을 위한 **HTTP 표준** 기술입니다. 클라이언트는 `EventSource` 객체를 통해 서버에 연결하고, 서버는 `text/event-stream` MIME 타입으로 데이터를 전송합니다.

### 특징

  * **HTTP 표준**: 기존 HTTP 프로토콜을 활용하여 구현되므로, 웹 인프라(로드 밸런서, 프록시 등)와 잘 호환됩니다.
  * **이벤트 스트리밍 단방향 처리 (Server → Client)**: 서버에서 클라이언트로만 데이터를 푸시합니다. 클라이언트가 서버로 데이터를 보내려면 별도의 HTTP 요청을 사용해야 합니다.
  * **클라이언트 연결**: JavaScript의 `EventSource` 객체를 통해 서버에 연결합니다.
  * **데이터 형식**: 서버는 `text/event-stream` MIME 타입을 사용하여 이벤트를 전송하며, 각 이벤트는 `data:` 프리픽스로 시작하고 `\n\n`으로 구분됩니다.
  * **자동 재연결**: 네트워크 문제 등으로 연결이 끊기면 `EventSource` 객체가 자동으로 서버에 재연결을 시도하는 내장 기능을 제공합니다.

### Spring MVC에서 SSE 스트리밍

Spring Framework에서는 `SseEmitter`를 사용하여 SSE 스트리밍을 쉽게 구현할 수 있습니다. `SseEmitter`는 비동기 요청을 처리하고 클라이언트에게 이벤트를 전송하는 데 사용됩니다. `ExecutorService`를 정의하여 이벤트를 비동기적으로 처리함으로써 웹 서버의 메인 스레드 블로킹을 방지합니다.

  * `ExecutorService` 선택 시 고려사항:
      * **`newCachedThreadPool()`**: 요청이 많을 때 유연하게 스레드를 생성하고 재활용하지만, 스레드 수가 무한히 늘어날 수 있어 주의가 필요합니다.
      * **`newFixedThreadPool(int nThreads)`**: 고정된 수의 스레드만 사용하여 자원을 효율적으로 관리할 수 있습니다. 요청이 많아지면 큐에 대기할 수 있습니다.
      * **`newSingleThreadExecutor()`**: 단일 스레드로 순차적으로 처리할 때 사용합니다.

#### Server Side Code (Spring)

```java
@Controller
public class SseEmitterController {
    
    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();
    
    @GetMapping("/sse")
    public SseEmitter handleSse() {
         // SseEmitter 객체를 생성합니다. 이 객체를 통해 클라이언트에 이벤트를 보냅니다.
         SseEmitter emitter = new SseEmitter(); 

         // nonBlockingService를 사용하여 별도의 스레드에서 이벤트를 전송하는 작업을 실행합니다.
         nonBlockingService.execute(() -> {
             try {
                 // 데이터를 클라이언트에 보냅니다.
                 emitter.send("/sse" + " @ " + new Date());
                 
                 // 모든 이벤트 전송이 완료되었음을 클라이언트에게 알리고 연결을 종료합니다.
                 // 만약 지속적으로 이벤트를 보낼 예정이라면 이 라인을 제거해야 합니다.
                 emitter.complete(); 
             } catch (Exception ex) {
                 // 이벤트 전송 중 오류 발생 시 연결을 종료하고 오류를 알립니다.
                 emitter.completeWithError(ex);
             }
         });
         // 생성된 SseEmitter 객체를 반환하여 Spring MVC가 이 비동기 요청을 처리하도록 합니다.
         return emitter;
    }   
}
```

#### Client Side (JavaScript)

```javascript
// EventSource 객체를 생성하여 서버의 SSE 엔드포인트에 연결을 시도합니다.
var sse = new EventSource('http://localhost:8080/javamvcasync/sse');

// 서버로부터 메시지를 수신했을 때 호출되는 이벤트 핸들러입니다.
sse.onmessage = function (evt) {
    var el = document.getElementById('sse');
    // 수신된 데이터(evt.data)
    el.appendChild(document.createTextNode(evt.data));
    el.appendChild(document.createElement('br'));
};

```

-----

## 2\. Web Socket

**WebSocket**은 서버와 클라이언트 간의 **양방향(Full-duplex)** 통신을 지원하는 별도의 프로토콜입니다. HTTP `Upgrade` 헤더를 통해 초기 HTTP 연결을 WebSocket 연결로 업그레이드합니다.

### 특징

  * **양방향(Full-duplex) 소통 지원**: 클라이언트와 서버 모두 서로에게 자유롭게 데이터를 주고받을 수 있어 실시간 상호작용에 최적화되어 있습니다.
  * **별도 프로토콜**: `ws://` 또는 `wss://` 스키마를 사용하는 자체 프로토콜을 따릅니다.
  * **에러 처리**: HTTP 기반의 에러 처리 표준(예: 404, 500 상태 코드)을 직접 사용하지 않고, WebSocket 자체 프로토콜 내에 정의된 종료 및 상태 코드를 사용합니다.
  * **낮은 오버헤드**: 초기 핸드셰이크 이후에는 헤더 정보가 거의 없는 경량의 프레임으로 데이터를 주고받아 효율적입니다.
  * **직접 구현 필요**: SSE와 달리 자동 재연결 기능이 내장되어 있지 않아, 필요시 개발자가 직접 구현해야 합니다.

### Spring Code (WebSocket with STOMP)

Spring WebSocket은 일반적으로 \*\*STOMP(Simple Text Oriented Messaging Protocol)\*\*와 같은 상위 프로토콜을 함께 사용하여 메시지 라우팅 및 처리를 용이하게 합니다.

```java
@Configuration
@EnableWebSocketMessageBroker 
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Simple 메시지 브로커를 활성화하고, '/topic'으로 시작하는 메시지를 구독하는 클라이언트에게 메시지를 전달합니다.
        // 이는 발행-구독(pub-sub) 모델을 지원하여 여러 클라이언트가 특정 주제의 메시지를 받을 수 있게 합니다.
        config.enableSimpleBroker("/topic");
        
        // 클라이언트가 서버로 메시지를 보낼 때 '/app'으로 시작하는 목적지를 사용하도록 설정합니다.
        // '/app' 이후의 경로에 따라 @MessageMapping 어노테이션이 붙은 메서드로 라우팅됩니다.
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
         // '/chat' 경로를 WebSocket 연결 엔드포인트로 등록합니다.
         // 클라이언트는 'ws://localhost:8080/chat' 등으로 연결을 시도할 수 있습니다.
         registry.addEndpoint("/chat");
         
         // '/chat' 엔드포인트를 SockJS와 함께 등록합니다.
         // SockJS는 WebSocket을 지원하지 않는 브라우저를 위해 Polling, Streaming 등 다양한 폴백 옵션을 제공하여
         // WebSocket을 지원하지 않는 환경에서도 통신이 가능하게 합니다.
         registry.addEndpoint("/chat").withSockJS();
    }
}
```

```java
@Controller
public class MessageController {
    @MessageMapping("/chat") // 클라이언트가 '/app/chat'으로 메시지를 보내면 이 메서드가 호출됩니다.
    @SendTo("/topic/messages") // 이 메서드의 반환 값을 '/topic/messages'를 구독하는 모든 클라이언트에게 브로드캐스트합니다.
    public OutputMessage send(Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        // 받은 메시지(from, text)에 서버 시간 정보를 추가하여 반환합니다.
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }
}

```

#### JavaScript Code (Client-side with SockJS & STOMP.js)

```javascript
// SockJS와 STOMP.js 라이브러리 로드
<script src="resources/js/sockjs-0.3.4.js"></script>
<script src="resources/js/stomp.js"></script>

<script type="text/javascript">
    var stompClient = null; 
    
    // UI 요소의 연결/연결 해제 상태를 설정하는 함수
    function setConnected(connected) {
        document.getElementById('connect').disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
        document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
        document.getElementById('response').innerHTML = ''; // 응답 영역 초기화
    }
    
    // 서버에 연결하는 함수
    function connect() {
        // SockJS를 사용하여 WebSocket 연결을 시도합니다. WebSocket이 지원되지 않으면 폴백 기술을 사용합니다.
        var socket = new SockJS('/chat');
        // SockJS 소켓을 사용하여 STOMP 클라이언트를 생성합니다.
        stompClient = Stomp.over(socket);  
        
        // STOMP 서버에 연결합니다. 연결 성공 시 콜백 함수가 실행됩니다.
        stompClient.connect({}, function(frame) {
            setConnected(true); 
            console.log('Connected: ' + frame); 
            
            // '/topic/messages' 주제를 구독합니다. 이 주제로 메시지가 오면 콜백 함수가 실행됩니다.
            stompClient.subscribe('/topic/messages', function(messageOutput) {
                showMessageOutput(JSON.parse(messageOutput.body));
            });
        });
    }
    
    // 서버 연결을 해제하는 함수
    function disconnect() {
        if(stompClient != null) {
            stompClient.disconnect(); // STOMP 연결 해제
        }
        setConnected(false); 
        console.log("Disconnected");
    }
    
    // 메시지를 서버로 전송하는 함수
    function sendMessage() {
        var from = document.getElementById('from').value; 
        var text = document.getElementById('text').value; 
        // '/app/chat' 목적지로 메시지를 전송합니다.
        // 메시지 본문은 JSON 문자열로 변환됩니다.
        stompClient.send("/app/chat", {}, 
          JSON.stringify({'from':from, 'text':text}));
    }
    
    // 수신된 메시지를 화면에 표시하는 함수
    function showMessageOutput(messageOutput) {
        var response = document.getElementById('response');
        var p = document.createElement('p');
        p.style.wordWrap = 'break-word';
        p.appendChild(document.createTextNode(messageOutput.from + ": " 
          + messageOutput.text + " (" + messageOutput.time + ")"));
        response.appendChild(p); 
    }
</script>
```

-----

## 3\. 비교

| 항목                 | WebSocket                                 | Server-Sent Events (SSE)                                 |
| :------------------- | :---------------------------------------- | :------------------------------------------------------- |
| **통신 방식** | 양방향(Full Duplex)                       | 단방향(Server → Client)                                  |
| **프로토콜** | 별도 프로토콜 (`ws://`, `wss://`)         | HTTP 기반 (`text/event-stream`)                          |
| **표준화** | RFC 6455 (IETF)                           | HTML5 표준 (WHATWG)                                      |
| **브라우저 지원** | 대부분 지원 (IE 10+, Safari 6+)           | 대부분 지원 (IE/Edge 레거시 버전 미지원, Safari 7+)      |
| **메시지 포맷** | 자유 형식 (텍스트/바이너리)               | 텍스트 스트림 (UTF-8, 이벤트 단위로 전송)                |
| **연결 유지 방식** | 핸드셰이크 후 지속 연결                   | HTTP 연결을 지속 유지 (Long Polling과 유사하나 이벤트 기반) |
| **사용 난이도** | 복잡도 있음 (자체 프로토콜 및 라이브러리 이해 필요) | 구현 간단 (HTTP 기반, `EventSource` API 직관적)         |
| **자동 재연결** | **직접 구현 필요** | **내장 기능으로 자동 재연결 지원** |

-----

## 4\. 시나리오 별 적용 적합 기술

| 시나리오                                     | 추천 기술 | 이유                                                                                             |
| :------------------------------------------- | :-------- | :----------------------------------------------------------------------------------------------- |
| 실시간 채팅, 게임, 협업 도구                 | WebSocket | 양방향 통신이 필수적이며, 빠른 응답 시간과 낮은 오버헤드로 빈번한 데이터 교환에 최적화           |
| 실시간 뉴스 피드, 알림, 주식 시세            | SSE       | 서버에서 클라이언트로의 일방적인 데이터 푸시가 주된 목적일 때, 구현이 간단하고 효율적            |
| 네트워크 제약 환경(방화벽 등)                | SSE       | HTTP 기반으로 기존 웹 인프라(프록시, 로드 밸런서 등)와 높은 호환성, WebSocket보다 우회 용이      |
| 파일 전송, 바이너리 데이터 포함              | WebSocket | 바이너리 데이터 전송에 효율적이며, 큰 데이터 스트림 처리에도 적합                                |
| 클라이언트가 주기적으로 서버에 요청할 필요가 없음 | SSE       | 서버가 변경 사항을 감지하여 자동으로 푸시하고, 클라이언트(`EventSource`)가 자동 재연결을 지원함 |

-----