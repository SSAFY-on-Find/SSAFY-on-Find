# 🔔 알림 기능 구현을 위한 SSE + React Query 정리

## 📌 개요

> 실시간 알림 기능을 구현하기 위한 방법으로 **SSE(Server-Sent Events)** 와 **React Query**를 조합하여 사용 가능함.

- **SSE(Server-Sent Events)**: 서버가 클라이언트에 실시간으로 데이터를 Push하는 단방향 통신 방식 (HTTP 기반)
- **React Query**: 서버 상태(데이터) 관리를 담당하는 클라이언트 측 라이브러리  
→ 알림 리스트 등 **초기 데이터 조회**나 **새 알림 갱신**에 활용

---

## 🧩 기술 역할 분담

| 기술                          | 역할                                                            |
| ----------------------------- | --------------------------------------------------------------- |
| **SSE**                       | 서버에서 **새 알림 이벤트를 실시간으로 전달**                   |
| **React Query**               | 알림 목록 **초기 조회, 캐싱, 갱신 처리**                        |
| **상태관리 도구(Zustand 등)** | 새 알림 도착 여부, 읽음 처리 등 UI 상태 관리에 보조적 사용 가능 |

---

## 🔄 동작 흐름

```text
[1] 클라이언트에서 React Query로 알림 목록 조회
    └ GET /api/notifications → useQuery(['notifications'], ...)
    
[2] 클라이언트에서 SSE 연결 시작
    └ const evtSource = new EventSource("/api/notifications/subscribe");

[3] 서버에서 새로운 알림 발생 시 클라이언트로 이벤트 전송
    └ event: "message" → 클라이언트가 수신

[4] 클라이언트는 알림 수신 시 React Query의 invalidateQueries 또는 수동 갱신 수행
    └ queryClient.invalidateQueries(['notifications']);
```

---

## 📡 SSE 기본 개념

- **단방향 통신**: 서버 → 클라이언트
- **HTTP/1.1 기반**, WebSocket보다 구현 간단
- **자동 재연결 지원**
- MIME type은 `text/event-stream`

### ✅ 클라이언트 코드 예시

```ts
useEffect(() => {
  const evtSource = new EventSource('/api/notifications/subscribe');

  evtSource.onmessage = (event) => {
    const data = JSON.parse(event.data);
    queryClient.invalidateQueries(['notifications']);
  };

  evtSource.onerror = (err) => {
    console.error('SSE 연결 오류:', err);
    evtSource.close(); // 필요시 재연결 로직 추가
  };

  return () => {
    evtSource.close();
  };
}, []);
```

---

## 🚀 React Query로 알림 목록 관리

### ✅ 알림 목록 불러오기

```ts
const { data: notifications } = useQuery(['notifications'], fetchNotifications);

async function fetchNotifications() {
  const res = await fetch('/api/notifications');
  return res.json();
}
```

### ✅ 수신 시 캐시 무효화

```ts
queryClient.invalidateQueries(['notifications']);
```

또는 수동으로 갱신:

```ts
queryClient.setQueryData(['notifications'], (old: Notification[]) => [
  newNotification,
  ...old,
]);
```

---

## 🔒 인증이 필요한 경우

- `EventSource`는 기본적으로 `withCredentials: false`
- 서버에서 인증된 사용자만 SSE 연결 허용하도록 처리 필요

```ts
// 서버 측 예시 (Express + JWT)
app.get('/api/notifications/subscribe', authenticateJWT, (req, res) => {
  res.setHeader('Content-Type', 'text/event-stream');
  ...
});
```

---

## ✅ 장점

- WebSocket보다 **구현이 단순**
- **HTTP 기반**이므로 인프라 호환성 높음
- 브라우저가 **자동으로 재연결 처리**

---

## ⚠️ 단점

| 항목                           | 설명                                                                          |
| ------------------------------ | ----------------------------------------------------------------------------- |
| **단방향 통신만 가능**         | 클라이언트 → 서버 메시지 전송 불가 (대신 REST API 사용)                       |
| **브라우저 지원 제한**         | 모든 브라우저에서 완벽히 동일하게 동작하지는 않음 (특히 IE, 일부 모바일 환경) |
| **CORS + 인증 처리 주의 필요** | JWT 인증 시 CORS 및 쿠키 설정 확인 필요                                       |

---

## 🧠 요약

| 항목            | 역할                                         |
| --------------- | -------------------------------------------- |
| **SSE**         | 실시간 알림 수신                             |
| **React Query** | 알림 데이터 관리 및 갱신 처리                |
| **Zustand 등**  | 새 알림 뱃지 표시, 읽음 처리 등 UI 상태 관리 |

