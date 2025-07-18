
# 🧭 Jira & JQL 요약

## 📌 Why Jira?

Jira는 **이슈 트래킹**, **애자일 개발**, **DevOps**, **SRE** 문화를 실현하는 데 필수적인 협업 도구입니다.

---

## 📋 Issue Tracking & Project Management

* 개인 작업(To-do)부터 팀 단위 협업까지 업무를 시각화하고 관리
* 이슈 등록 → 우선순위 → 상태 변경 → 완료까지 전 과정 추적 가능
* 대표 도구: Jira Software, Redmine, Trello 등

---

## 🌀 Agile

**애자일 소프트웨어 개발 선언**

* 공정과 도구보다 **개인과 상호작용**
* 포괄적인 문서보다 **작동하는 소프트웨어**
* 계약 협상보다 **고객과의 협력**
* 계획을 따르기보다 **변화에 대응**

---

## ⚙️ Scrum vs Kanban

| 구분 | Scrum                | Kanban                     |
| -- | -------------------- | -------------------------- |
| 특징 | 일정한 스프린트 주기(1\~4주)   | 연속적 흐름 관리                  |
| 구성 | 백로그 → 스프린트 → 데일리 스크럼 | To Do → In Progress → Done |
| 장점 | 반복적 개발, 명확한 목표       | 유연한 우선순위 조정                |

---

## 🧑‍🤝‍🧑 Scrum Meeting

* **월요일 오전**

  * Product Backlog Grooming
  * Sprint Planning Meeting
  * Sprint Backlog 확정

* **월\~금**

  * Sprint (Daily Scrum 포함)

* **금요일 종료 전**

  * Sprint Review + Retrospective

---

## 📦 Backlog

* **Product Backlog**: 전체 요구사항 리스트
* **Sprint Backlog**: 이번 스프린트에서 처리할 작업들
* **Burndown Chart**: 남은 작업량 시각화 그래프

---

## 🧮 스토리 포인트 (Story Point)

* 작업 구현에 필요한 **상대적인 노력** 추정 단위
* 보통 피보나치 수열 기반 (1, 2, 3, 5, 8, 13...)
* 고려 요소: 복잡도, 불확실성, 리스크, 작업량

---

## 🚀 DevOps & SRE

* **DevOps**: 개발(Dev)과 운영(Ops)의 협업 문화
* **SRE(Site Reliability Engineering)**: 시스템 안정성을 위한 운영 자동화 중심의 접근법
* 기존 Silos(사일로) 조직문화 해소가 핵심

🪛 DevOps Cycle:

> Plan → Code → Build → Test → Release → Deploy → Operate → Monitor (무한 루프)

---

## 🔍 JQL (Jira Query Language)

* Jira 내 이슈를 **검색·필터링**하기 위한 언어
* 기본 문법 예시:

```sql
project = "OneFit" AND assignee = currentUser() AND status != Done ORDER BY created DESC
```

| 키워드        | 설명      |
| ---------- | ------- |
| `project`  | 프로젝트 이름 |
| `assignee` | 담당자     |
| `status`   | 이슈 상태   |
| `ORDER BY` | 정렬 기준   |

예은님께서 공유해주신 이미지들을 바탕으로 Jira & JQL 수업 내용을 확장해 `README.md` 형식으로 정리한 버전을 아래에 제공합니다. 이전 요약본에 DevOps, SRE, AI 기능, 이슈 생성 관련 내용을 보완했습니다.

---


## 🔍 Why Jira?

* **Issue Tracking**(이슈 트래킹)
* **Agile**(애자일)
* **DevOps**(개발-운영 협업)
* **SRE**(Site Reliability Engineering) 구현 기반

---

## 📋 Issue Tracking & Project Management

* **To-do 관리**부터 팀 단위 백로그 관리까지 유연하게 지원
* 상태 추적 (Open, In Progress, Resolved 등)
* 우선순위, 마감일, 담당자 등 메타정보 포함
* 예시 도구: Jira, Trello, Asana, Notion 등

---

## 🌀 Agile

### ✅ 애자일 소프트웨어 개발 선언

> 우리는 공정과 도구보다 **개인과 상호작용**,
> 포괄적인 문서보다 **작동하는 소프트웨어**,
> 계약 협상보다 **고객과의 협력**,
> 계획을 따르기보다 **변화에 대응**을 중시한다.

🔗 출처: [Agile Manifesto](https://agilemanifesto.org/iso/ko/manifesto.html)

---

## 🔄 Scrum vs Kanban

| 항목   | Scrum                     | Kanban          |
| ---- | ------------------------- | --------------- |
| 프로세스 | 반복되는 Sprint 중심            | 연속적인 흐름         |
| 구성   | Sprint, Backlog, Review 등 | Board 형태의 상태 전이 |
| 장점   | 예측 가능한 개발 주기              | 유연한 우선순위 조정     |

---

## 🧑‍🤝‍🧑 Scrum 운영 예시

* **월요일**

  * Product Backlog Grooming
  * Sprint Planning
  * Sprint Backlog 확정

* **화\~금**

  * Sprint 진행 (Daily Scrum 포함)

* **금요일 종료 전**

  * Sprint Review & Retrospective

---

## 📦 Backlog & Story Point

* **Backlog**: 처리해야 할 작업의 리스트
* **Story Point**: 상대적인 작업량 추정치 (보통 피보나치 수열 기반: 1, 2, 3, 5, 8, 13…)

---

## ⚙️ DevOps란?

Dev + Ops를 연결하여 **자동화된 협업 파이프라인** 구축

### DevOps 잘 수행하기 위한 조건

* 반복 작업은 **툴로 자동화**
* **공유된 지표/보드**로 팀원 전체가 같은 그림을 봐야 함
* 장애/이슈는 **팀 전체와 공유**하여 빠르게 대응

---

## 🧰 DevOps 툴 체계 (예시)

* **Plan**: Jira, Confluence, Trello, Lucidchart
* **Build**: Git, GitLab, Jenkins, npm
* **Test**: Selenium, TestFairy, Postman
* **Deploy/Operate**: Kubernetes, Docker, AWS, Azure, Datadog, Zabbix
* **Monitor**: Sentry, New Relic, Slack 등

---

## 🧠 Atlassian DevOps 생태계

* **Jira Software**: 개발 프로세스 관리
* **Bitbucket**: Git 저장소 연동
* **Confluence**: 문서 협업
* **Bamboo**: CI/CD 파이프라인
* **StatusPage**: 상태 알림 및 장애 대응

---

## 🔧 SRE (Site Reliability Engineering)

### SRE의 핵심 실천 항목

| 항목                      | 내용                     |
| ----------------------- | ---------------------- |
| 📊 Metrics & Monitoring | SLA, 대시보드, 분석          |
| 📈 Capacity Planning    | 예측, 성능 기반 자원 조정        |
| 🔄 Change Management    | 자동화된 릴리즈, 리뷰           |
| 🚨 Emergency Response   | On-call, Postmortem 분석 |
| 👥 Culture              | 책임 공유, 무결점 조직 지향       |


---

## 🧠 AI 기반 Jira 기능

* **Atlassian Intelligence**:

  * 자연어로 이슈 검색
  * 트렌드 분석 및 인사이트 생성
  * 알림/이슈 자동 분류

* **Rovo**:

  * 버튼 한 번으로 작업 생성
  * 빠른 프로젝트 관리 및 요구사항 추적 지원

---

## 🛠️ How to Create Issue

1. 우측 상단 `Create` 클릭
2. Project, Issue Type 선택
3. 필수 항목 입력: Summary, Description, Assignee, Priority 등
4. 필요 시 Sprint, Story Point 등도 추가
5. `Create` 버튼 클릭

👉 **AI 기능 사용 시**:
이슈 설명 작성 시 Atlassian Intelligence 활용하여 자동 작성 가능

---

## 🔍 JQL (Jira Query Language)

* 복잡한 조건으로 이슈 검색을 가능하게 하는 쿼리 언어

```sql
project = "OneFit" 
AND status NOT IN ("Closed", "Done") 
AND assignee = currentUser() 
ORDER BY created DESC
```

| 키워드        | 설명    |
| ---------- | ----- |
| `project`  | 프로젝트명 |
| `status`   | 이슈 상태 |
| `assignee` | 담당자   |
| `ORDER BY` | 정렬 조건 |

---


## 1️⃣ Jira란?

* Atlassian에서 만든 **이슈 트래킹 도구**
* **DevOps**, **Agile**, **SRE** 문화에 핵심적인 역할
* 개발뿐 아니라 기획, 마케팅, 운영 등 다양한 부서와 협업 가능

---

## 2️⃣ Jira의 핵심 기능

| 기능        | 설명                                     |
| --------- | -------------------------------------- |
| 📌 이슈 트래킹 | 버그, 작업, 요청 등을 관리                       |
| 📋 백로그    | 해야 할 일의 목록 (Product / Sprint Backlog)  |
| 🌀 보드     | Kanban 또는 Scrum 방식 시각화                 |
| 📅 스프린트   | 기간 내 완료할 작업 묶음 (주로 1\~2주 단위)           |
| ✅ 완료 기준   | DoD(Definition of Done) 기준으로 명확한 완료 처리 |
| 📈 차트     | Burndown Chart로 남은 작업량 추적              |
| 🔧 자동화    | 이슈 상태 전이, 알림 등 Rule 설정 가능              |
| 💬 코멘트    | 팀원 간 실시간 협업 가능                         |
| 🧠 AI     | Atlassian Intelligence로 이슈 추천 및 요약     |

---

## 3️⃣ Agile & Scrum

### 📜 애자일 선언문 (Agile Manifesto)

> * **개인과 상호작용**을 프로세스와 도구보다,
> * **작동하는 소프트웨어**를 포괄적인 문서보다,
> * **고객과 협력**을 계약 협상보다,
> * **변화에 대응**을 계획 따르기보다 더 가치 있게 여긴다.


### 🔁 Scrum 프로세스

1. **스프린트 계획** (월요일)
2. **스프린트 진행** (Daily Scrum)
3. **리뷰 & 회고** (금요일)
4. **다음 스프린트 준비**

---

## 4️⃣ DevOps와 Jira

* DevOps는 \*\*개발(Dev)\*\*과 **운영(Ops)** 간의 협업과 자동화를 통해 제품을 빠르게 전달하는 문화

### 🔁 DevOps 툴체인 예시

| 단계      | 툴                      |
| ------- | ---------------------- |
| Plan    | Jira, Confluence       |
| Code    | Bitbucket, Git         |
| Build   | Jenkins, Gradle        |
| Test    | JUnit, Postman         |
| Deploy  | Docker, Kubernetes     |
| Operate | AWS, GCP, Azure        |
| Monitor | Datadog, Sentry, Slack |

---

## 5️⃣ SRE (Site Reliability Engineering)

* Google에서 제안한 운영 방식
* **운영 자동화**, **지표 기반 안정성 관리**

### SRE의 핵심 5요소

| 항목                      | 설명                   |
| ----------------------- | -------------------- |
| 📊 Metrics & Monitoring | SLA 설정, 대시보드 모니터링    |
| 📈 Capacity Planning    | 자원 예측 및 관리           |
| 🔁 Change Management    | 변경 요청 및 배포 자동화       |
| 🚨 Emergency Response   | 장애 대응, Postmortem 공유 |
| 🧑‍🤝‍🧑 팀 문화           | 무결점 지향, 공동 책임        |

---

## 6️⃣ AI 기능 (Atlassian Intelligence)

| 기능        | 설명                 |
| --------- | ------------------ |
| ✍️ 요약     | 긴 이슈나 회의 내용을 요약    |
| 🤖 이슈 생성  | 자연어로 입력 시 자동 이슈 작성 |
| 🔍 스마트 검색 | 검색 쿼리 없이도 이슈 추천    |
| 📊 트렌드 분석 | 팀 작업량 패턴 자동 인식     |

---

## 7️⃣ JQL (Jira Query Language)

Jira에서 이슈를 **검색/필터링**하기 위한 쿼리 언어

### 📌 기본 구조

```sql
project = "OneFit"
AND status != Done
AND assignee = currentUser()
ORDER BY created DESC
```

| 요소         | 설명                                          |
| ---------- | ------------------------------------------- |
| `project`  | 프로젝트명                                       |
| `status`   | 이슈 상태 (`To Do`, `In Progress`, `Done`, ...) |
| `assignee` | 담당자                                         |
| `ORDER BY` | 정렬 기준                                       |

### 📌 자주 쓰는 조건

| 조건                | 설명             |
| ----------------- | -------------- |
| `text ~ "검색어"`    | 내용 포함 검색       |
| `labels in (...)` | 특정 라벨 포함       |
| `created >= -1w`  | 최근 1주일간 생성된 이슈 |

---

## 8️⃣ 실습 팁 요약

* **이슈 생성 실습**: 우측 상단 `Create` → 필수 정보 입력 → `Create`
* **보드 실습**: 드래그로 상태 변경하며 워크플로우 이해
* **자동화 실습**: 이슈 생성 시 특정 조건이면 자동 배정
* **JQL 필터 실습**: 나만의 필터 만들어 저장 & 재사용




