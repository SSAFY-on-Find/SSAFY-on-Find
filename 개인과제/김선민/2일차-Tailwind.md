# 🎨 Tailwind CSS 정리

## 📌 개요

> **Tailwind CSS**는 유틸리티-퍼스트(utility-first) CSS 프레임워크입니다.  
> CSS 클래스를 미리 정의해두고, 해당 클래스를 조합하여 UI를 구성합니다.

```html
<!-- 예시: 버튼 -->
<button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600">
  버튼
</button>
```

---

## 🚀 특징

| 항목                     | 설명                                                                 |
| ------------------------ | -------------------------------------------------------------------- |
| **유틸리티 기반**        | CSS 속성 하나하나가 클래스화됨 (`text-lg`, `bg-gray-100`, `mt-4` 등) |
| **빠른 스타일링**        | 클래스만으로 바로 스타일 지정 가능 → 별도 CSS 파일 불필요            |
| **반응형 지원 내장**     | `sm:`, `md:`, `lg:`, `xl:` 접두사로 반응형 클래스 작성 가능          |
| **다크 모드, 상태 지원** | `dark:`, `hover:`, `focus:` 등 상태별 스타일링 지원                  |
| **커스터마이징 용이**    | `tailwind.config.js`를 통해 색상, 폰트, spacing 등 설정 가능         |

---

## ✅ 장점

- **생산성 향상**: 빠른 스타일링 가능
- **일관된 디자인 시스템** 적용 가능
- **CSS 용량 최소화** (Purge 기능)
- **반응형, 다크모드 등 기본 지원**
- **React, Vue 등과의 높은 호환성**

---

## ⚠️ 단점

- 클래스가 **복잡하고 길어질 수 있음**
- **의미 기반 네이밍 부족** (`.primary-btn` 대신 `bg-blue-500 text-white`)
- 기존 CSS에 익숙한 개발자에게는 진입 장벽이 존재

---

## 🧪 사용 예시

```html
<div class="max-w-md mx-auto bg-white p-6 rounded-lg shadow-md">
  <h1 class="text-2xl font-bold mb-4">Tailwind CSS 예시</h1>
  <p class="text-gray-600">이 박스는 Tailwind로 스타일링되었습니다.</p>
</div>
```

---

## ⚙️ 설치 방법 (Vite 기준)

```bash
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

### 📄 tailwind.config.js

```js
module.exports = {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

### 📄 src/index.css

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

---

## 🎨 커스터마이징 예시

```js
// tailwind.config.js
module.exports = {
  theme: {
    extend: {
      colors: {
        honey: '#f9c74f',
      },
      fontFamily: {
        pretendard: ['Pretendard', 'sans-serif'],
      },
    },
  },
}
```

---

## 🛠 함께 쓰면 좋은 도구

| 도구                | 설명                                                  |
| ------------------- | ----------------------------------------------------- |
| **Headless UI**     | Tailwind와 호환되는 UI 컴포넌트 라이브러리            |
| **DaisyUI**         | Tailwind 기반의 컴포넌트 스타일 프리셋                |
| **shadcn/ui**       | Radix + Tailwind 조합의 고급 UI 컴포넌트 (React 전용) |
| **Prettier Plugin** | 클래스 정렬 자동화 (`prettier-plugin-tailwindcss`)    |

---

## 📚 주요 개념 요약

- **Utility-First**: 모든 스타일을 조합 가능한 클래스 단위로 제공
- **JIT 모드**: 실제 사용하는 클래스만 빌드 시 포함 (속도 및 용량 최적화)
- **커스터마이징**: Tailwind 설정 파일로 색상, 폰트, spacing 등 수정 가능
- **반응형/상태 기반 클래스**: `sm:`, `hover:`, `dark:` 등으로 유연한 설계 가능

---

## 🔚 결론

Tailwind CSS는 빠르게 UI를 구축하고, 일관된 스타일 시스템을 유지할 수 있는 강력한 프레임워크입니다.  
디자인 시스템이 필요한 팀, 컴포넌트 기반 개발을 하는 프론트엔드 개발자에게 특히 적합합니다.

---