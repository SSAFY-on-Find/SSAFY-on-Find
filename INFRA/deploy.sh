#!/bin/bash
set -e 

cd "$(dirname "$0")"

export BE_DOCKER_IMAGE=$1
export FE_DOCKER_IMAGE=$2

ENV_FILE="./.env"
COMPOSE_FILE="docker-compose.prod.yml"

echo "Deploying with compose file: ${COMPOSE_FILE}"
echo "Using environment file: ${ENV_FILE}"
echo "Deploying Backend Image: ${BE_DOCKER_IMAGE}"
echo "Deploying Frontend Image: ${FE_DOCKER_IMAGE}"

docker compose --env-file ${ENV_FILE} -f ${COMPOSE_FILE} pull backend frontend
docker compose --env-file ${ENV_FILE} -f ${COMPOSE_FILE} up -d --force-recreate backend frontend

echo "Deployment completed."