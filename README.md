# SSAFY On Find

**SSAFY 2학기 공통 프로젝트 전용 팀 빌딩 서비스**

## 📋 프로젝트 개요

SSAFY On Find는 삼성 청년 SW 아카데미(SSAFY) 교육생들의 효율적인 팀 빌딩을 지원하는 통합 플랫폼입니다. 기존 팀 빌딩 과정에서 발생하는 의사소통의 번거로움, 교육생 정보 파악의 어려움, 실시간 현황 파악 불가 등의 문제를 해결합니다.

## 👥 팀 소개

**서울 7반 A704팀**

<table>
<tr>
<td align="center" width="33%">
<img src="https://avatars.githubusercontent.com/u/127717117?v=4" width="120" height="120" style="border-radius: 50%;">
<br/>
<b>김선민</b>
<br/>
<i>Frontend</i>
<br/>
<a href="https://github.com/seonmiki">
<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
</a>
</td>
<td align="center" width="33%">
<img src="https://avatars.githubusercontent.com/u/95961799?v=4" width="120" height="120" style="border-radius: 50%;">
<br/>
<b>김정연</b>
<br/>
<i>Frontend & AI </i>
<br/>
<a href="https://github.com/blueconecell">
<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
</a>
</td>
<td align="center" width="33%">
<img src="https://avatars.githubusercontent.com/u/67894738?v=4" width="120" height="120" style="border-radius: 50%;">
<br/>
<b>박의균</b>
<br/>
<i>infra</i>
<br/>
<a href="https://github.com/pug9483">
<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
</a>
</td>
</tr>
<tr>
<td align="center" width="33%">
<img src="https://avatars.githubusercontent.com/u/87176677?v=4" width="120" height="120" style="border-radius: 50%;">
<br/>
<b>서재곤</b>
<br/>
<i>Backend</i>
<br/>
<a href="https://github.com/Jgone2">
<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
</a>
</td>
<td align="center" width="33%">
<img src="https://avatars.githubusercontent.com/u/113505664?v=4" width="120" height="120" style="border-radius: 50%;">
<br/>
<b>석예은</b>
<br/>
<i>Backend</i>
<br/>
<a href="https://github.com/yenseok">
<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
</a>
</td>
<td align="center" width="33%">
<img src="https://avatars.githubusercontent.com/u/77107811?v=4" width="120" height="120" style="border-radius: 50%;">
<br/>
<b>조현지</b>
<br/>
<i>Backend</i>
<br/>
<a href="https://github.com/hyunji321">
<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
</a>
</td>
</tr>
</table>

## 🎯 기획 배경

### 팀 빌딩 과정의 주요 문제점

설문조사 결과, **93%의 교육생이 팀 빌딩 과정을 어려워함**

- **의사소통의 번거로움** /  **교육생 특성 파악 어려움** / **실시간 팀 빌딩 현황 파악 불가** / **정보 접근성 부족**

## 🚀 주요 기능

### 📊 대시보드
- **트랙별 포지션 비율** 시각화
- **팀 빌딩 진행률** 실시간 모니터링
- **팀 & 팀원 추천** 기능

### 👥 팀 관리
- **팀 전체 목록** 조회
- **트랙 & 모집 포지션** 표시
- **팀 지원하기 & 합치기** 기능
- **좋아요** 기능

### 👤 교육생 관리
- **교육생 전체 목록** 조회
- **전공 & 포지션 정보** 표시
- **검색 & 필터링** 기능
- **상세 프로필** 조회 (전공/비전공, 희망 트랙, 포트폴리오)

### 💬 커뮤니케이션
- **개인 채팅** (실시간)
- **팀 채팅** 기능
- **알림함** (받은/보낸 요청 관리)

### 🔔 실시간 알림
- **Server Sent Event(SSE)** 기반 실시간 업데이트
- 팀 빌딩 현황 실시간 동기화

## 🛠 기술 스택
<img width="2307" height="1359" alt="SSAFY on Find" src="https://github.com/user-attachments/assets/31842378-8dbc-4718-a245-0ae082e3aec0" />

### Frontend
- ![React](https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=react&logoColor=black) ![Zustand](https://img.shields.io/badge/Zustand-FF6B35?style=flat-square&logo=zustand&logoColor=white) ![React Query](https://img.shields.io/badge/React_Query-FF4154?style=flat-square&logo=react-query&logoColor=white) ![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=flat-square&logo=tailwind-css&logoColor=white)

### Backend
- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white) ![SSE](https://img.shields.io/badge/SSE-FF6B6B?style=flat-square&logo=SSE&logoColor=white) ![STOMP](https://img.shields.io/badge/STOMP-010101?style=flat-square&logo=Stomp&logoColor=white) ![FastAPI](https://img.shields.io/badge/FastAPI-009688?style=flat-square&logo=fastapi&logoColor=white) ![LANGCHAIN](https://img.shields.io/badge/LANGCHAIN-009688?style=flat-square&logo=LANGCHAIN&logoColor=white) 

### Database
- ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white) ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=mongodb&logoColor=white) 

### Infras
- ![Amazon EC2](https://img.shields.io/badge/Amazon_EC2-FF9900?style=flat-square&logo=amazon-ec2&logoColor=white) ![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=jenkins&logoColor=white) ![Nginx](https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=nginx&logoColor=white) ![GItLab](https://img.shields.io/badge/GItLab-FF6B35?style=flat-square&logo=GItLab&logoColor=white)

## 📈 기대 효과

### 🤔 교육생 특성 파악 어려움 해결
- **기존**: 트랙, 포지션, 전공, 취업 준비 등 정보 부족
- **개선**: 교육생 통합 자기소개서를 통한 세부 정보 파악

### 💬 의사소통의 번거로움 해결
- **기존**: 여러 플랫폼을 통한 번거로운 메시지 교환
- **개선**: 초대하기, 지원하기, 팀 합치기, 채팅 등 통합 플랫폼 제공

### 📋 교육생 정보 접근의 불편함 해결
- **기존**: MM에 올라온 자기소개 정보를 하나씩 검색
- **개선**: 필터링을 통한 원하는 팀원 정보 빠른 파악

### 📊 실시간 현황 파악 불가 문제 해결
- **기존**: 이미 팀 빌딩 완료된 교육생에게 중복 초대 요청
- **개선**: 대시보드를 통한 실시간 팀 빌딩 진행률 확인


