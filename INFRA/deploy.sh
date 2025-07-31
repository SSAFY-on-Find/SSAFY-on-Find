#!/bin/bash
set -e

IMAGE_TO_DEPLOY=$1

CONTAINER_NAME="chelsea-be-app"
NETWORK_NAME="product_api_server_network"
ENV_FILE_PATH="/home/ubuntu/.env"
COMPOSE_FILE_PATH="/home/ubuntu/docker-compose.yml"

if [ -z "$IMAGE_TO_DEPLOY" ]; then
  echo "오류: 배포할 Docker 이미지가 지정되지 않았습니다."
  exit 1
fi

echo "--- ${IMAGE_TO_DEPLOY} 배포를 시작합니다 ---"

# 네트워크 존재 확인 및 생성
if ! docker network ls | grep -q ${NETWORK_NAME}; then
  echo "네트워크를 생성합니다: ${NETWORK_NAME}"
  docker network create ${NETWORK_NAME}
fi

# 데이터베이스 디렉토리 생성
mkdir -p /home/ubuntu/mysql/data
mkdir -p /home/ubuntu/redis/data  
mkdir -p /home/ubuntu/mongodb/data

# 데이터베이스 컨테이너들 시작 (이미 실행 중이 아닌 경우에만)
echo "데이터베이스 서비스들을 확인하고 시작합니다."
cd /home/ubuntu
docker-compose up -d mysql redis mongodb

# 데이터베이스 준비 완료까지 대기
echo "MySQL 헬스체크 대기 중..."
timeout 60 bash -c 'while ! docker exec chelsea-mysql mysqladmin ping -h localhost -u root -pchelsea_root_pw! --silent; do sleep 2; done'

echo "Redis 연결 확인 중..."
timeout 30 bash -c 'while ! docker exec chelsea-redis redis-cli -a chelsea_root_pw! ping | grep -q PONG; do sleep 2; done'

echo "MongoDB 연결 확인 중..."
timeout 30 bash -c 'while ! docker exec chelsea-mongodb mongosh --eval "db.adminCommand(\"ping\")" >/dev/null 2>&1; do sleep 2; done'

# 기존 애플리케이션 컨테이너 정리
if [ "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
  echo "기존에 실행 중인 컨테이너를 중지합니다: ${CONTAINER_NAME}"
  docker stop ${CONTAINER_NAME}
fi

if [ "$(docker ps -aq -f name=${CONTAINER_NAME})" ]; then
  echo "기존에 중지된 컨테이너를 제거합니다: ${CONTAINER_NAME}"
  docker rm ${CONTAINER_NAME}
fi

echo "최신 이미지를 Docker Hub에서 가져옵니다: ${IMAGE_TO_DEPLOY}"
docker pull ${IMAGE_TO_DEPLOY}

echo "${IMAGE_TO_DEPLOY}로부터 새 컨테이너를 실행합니다."

docker run -d \
  -p 8090:8090 \
  --name ${CONTAINER_NAME} \
  --network ${NETWORK_NAME} \
  --env-file ${ENV_FILE_PATH} \
  --restart always \
  ${IMAGE_TO_DEPLOY}

# 애플리케이션 시작 확인
echo "애플리케이션 시작 확인 중..."
timeout 60 bash -c 'while ! curl -f http://localhost:8090/actuator/health >/dev/null 2>&1; do sleep 5; done' || echo "헬스체크 타임아웃 - 수동으로 확인하세요"

echo "--- 배포가 성공적으로 완료되었습니다! ---"

echo "컨테이너 상태 확인:"
docker ps --filter "network=${NETWORK_NAME}"

echo "오래된 Docker 이미지를 정리합니다."
docker image prune -f

exit 0