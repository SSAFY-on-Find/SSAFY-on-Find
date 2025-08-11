# 로컬 실행방법

1. 파이썬용 `.env` 파일을 AI 폴더 아래에 둬야합니다.

2. 파이썬 가상환경을 세팅합니다.

```
python -m venv venv
```

3. 가상환경을 동작시킵니다.

```
source venv/Scripts/activate
```

4. 파이썬 app을 실행시킵니다. FAST API로 개발되었고, uvicorn WAS에서 동작합니다.

```
python -m uvicorn main:app --reload
```

5. AI 폴더 밖 프로젝트 루트 디렉토리에서 ai서버 전용 도커 파이프라인을 동작시킵니다. `docker-compose.ai.yml`

```
docker compose -f docker-compose.ai.yml up --build -d
```
