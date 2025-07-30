#!/bin/bash
set -e

IMAGE_TO_DEPLOY=$1

CONTAINER_NAME="chelsea-be-app"

if [ -z "$IMAGE_TO_DEPLOY" ]; then
  echo "오류: 배포할 Docker 이미지가 지정되지 않았습니다. 사용법: ./deploy.sh <이미지이름:태그>"
  exit 1
fi

echo "--- ${IMAGE_TO_DEPLOY} 배포를 시작합니다 ---"

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

docker run -d -p 8080:8090 --name ${CONTAINER_NAME} -e SPRING_PROFILES_ACTIVE=prod ${IMAGE_TO_DEPLOY}

echo "--- 배포가 성공적으로 완료되었습니다! ---"

echo "오래된 Docker 이미지를 정리합니다."
docker image prune -f

exit 0