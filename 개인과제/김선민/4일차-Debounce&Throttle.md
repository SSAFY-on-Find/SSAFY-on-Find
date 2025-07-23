# ⏱️ Debounce vs Throttle

## 📌 개요

> Debounce와 Throttle은 **함수 실행 빈도를 제어**하는 기법입니다.  
> 사용자의 이벤트 입력(스크롤, 키 입력, 리사이즈 등)이 **과도하게 발생하는 경우 성능 최적화**를 위해 사용됩니다.

---

## 🔁 Debounce (디바운스)

> **이벤트가 연속으로 발생하면 마지막 이벤트 이후 일정 시간 동안만 실행**  
> 즉, **지연 후 1회만 실행**

### ✅ 특징

- 입력이 멈출 때까지 기다렸다가 실행
- 이벤트 **종료 시점**에 실행
- **검색창 자동완성**, **입력값 검증** 등에 주로 사용

### 💡 예시: 500ms 동안 입력이 없을 때만 API 요청

```ts
function debounce(fn, delay) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
}

const handleInput = debounce((value) => {
  console.log("검색:", value);
}, 500);
```

---

## 🚀 Throttle (스로틀)

> **지정된 시간 간격마다 함수 실행을 1회로 제한**  
> 즉, **주기적으로 실행**

### ✅ 특징

- 일정 주기로 실행
- 이벤트 **진행 중에도 실행됨**
- **스크롤 감지**, **윈도우 리사이즈**, **버튼 연타 방지** 등에 사용

### 💡 예시: 300ms마다 한 번씩만 실행

```ts
function throttle(fn, interval) {
  let lastTime = 0;
  return (...args) => {
    const now = Date.now();
    if (now - lastTime >= interval) {
      lastTime = now;
      fn(...args);
    }
  };
}

const handleScroll = throttle(() => {
  console.log("스크롤 이벤트");
}, 300);
```

---

## 🔍 차이점 비교

| 항목           | **Debounce**                 | **Throttle**           |
| -------------- | ---------------------------- | ---------------------- |
| 실행 시점      | 마지막 이벤트 후 일정 시간   | 일정 주기마다          |
| 사용 예시      | 검색 자동완성, 비밀번호 확인 | 무한스크롤, 리사이즈   |
| 이벤트 처리 수 | **최소 1회**                 | **정해진 주기로 제한** |
| 제어 방식      | **지연 후 단일 실행**        | **정기적 실행 제한**   |
