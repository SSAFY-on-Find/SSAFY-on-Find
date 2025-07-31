#!/bin/bash

export BE_DOCKER_IMAGE=$1
export FE_DOCKER_IMAGE=$2

echo "Deploying Backend Image: ${BE_DOCKER_IMAGE}"
echo "Deploying Frontend Image: ${FE_DOCKER_IMAGE}"

docker-compose pull backend frontend

docker-compose up -d --force-recreate backend frontend

echo "Deployment completed."