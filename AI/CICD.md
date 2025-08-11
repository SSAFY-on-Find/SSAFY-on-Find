# AI 서버(FastAPI) Jenkins CI/CD 파이프라인 통합 가이드

이 문서는 기존 Jenkins 파이프라인에 새로운 FastAPI 기반 AI 서버를 마이크로서비스로 추가하는 방법을 설명합니다.

## 1\. 개요

`dev` 브랜치에 Push 이벤트가 발생했을 때, 변경된 파일이 어느 폴더(`FE`, `BE`, `AI`)에 속하는지 감지하여 해당 서비스만 선택적으로 빌드하고 배포하는 효율적인 파이프라인을 구성합니다.

## 2\. `Jenkinsfile` 수정

기존 `Jenkinsfile`에 AI 서버를 위한 설정과 스테이지를 추가하고, 변경된 파일에 따라 특정 스테이지를 실행할지 결정하는 `when` 조건을 추가합니다.

```groovy
pipeline {
    agent any

    environment {
        PROJECT_DIR = '.'
        FRONTEND_DIR = "${PROJECT_DIR}/FE"
        BACKEND_DIR = "${PROJECT_DIR}/BE/chelsea"
        AI_SERVER_DIR = "${PROJECT_DIR}/AI" // << AI 서버 폴더 경로 추가

        DOCKER_FRONTEND_IMAGE = 'pug9483/chelsea-fe'
        DOCKER_BACKEND_IMAGE = 'pug9483/chelsea-be'
        DOCKER_AI_IMAGE = 'pug9483/chelsea-ai' // << AI 서버 이미지 이름 추가
        DOCKER_HUB_CREDENTIAL_ID = 'dockerhub-jenkins'
        IMAGE_TAG = 'latest'

        VITE_APP_BASE_URL = 'https://i13a704.p.ssafy.io/api/v1'
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                git url: 'https://lab.ssafy.com/s13-webmobile1-sub1/S13P11A704.git',
                    branch: 'dev',
                    credentialsId: 'gitlab-jenkins'
            }
        }

        stage('Detect Changes') {
            steps {
                script {
                    // 변경된 파일 목록을 가져와서 각 서비스별로 변경 여부를 변수에 저장
                    def changedFiles = sh(script: "git diff --name-only HEAD~1 HEAD", returnStdout: true).trim().split('\n')
                    env.FE_CHANGED = changedFiles.any { it.startsWith("FE/") }.toString()
                    env.BE_CHANGED = changedFiles.any { it.startsWith("BE/") }.toString()
                    env.AI_CHANGED = changedFiles.any { it.startsWith("AI/") }.toString()

                    echo "FE Changed: ${env.FE_CHANGED}"
                    echo "BE Changed: ${env.BE_CHANGED}"
                    echo "AI Changed: ${env.AI_CHANGED}"
                }
            }
        }

        stage('Build & Push Images') {
            parallel {
                stage('Build & Push Backend') {
                    // BE/ 폴더에 변경이 있을 때만 이 스테이지를 실행
                    when { expression { env.BE_CHANGED == 'true' } }
                    steps {
                        echo '1. Backend 애플리케이션을 빌드하고 이미지를 푸시합니다.'
                        dir(BACKEND_DIR) {
                            sh 'chmod +x ./gradlew'
                            sh './gradlew clean build -x test'
                            sh 'cp `find ./build/libs -name "*.jar" | grep -v "plain.jar"` ./app.jar'
                            script {
                                withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIAL_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                                }
                                sh "docker build -t ${DOCKER_BACKEND_IMAGE}:${IMAGE_TAG} ."
                                sh "docker push ${DOCKER_BACKEND_IMAGE}:${IMAGE_TAG}"
                            }
                        }
                    }
                }
                stage('Build & Push Frontend') {
                    // FE/ 폴더에 변경이 있을 때만 이 스테이지를 실행
                    when { expression { env.FE_CHANGED == 'true' } }
                    steps {
                        echo '2. Frontend 도커 이미지를 빌드하고 푸시합니다.'
                        dir(FRONTEND_DIR) {
                             script {
                                withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIAL_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                                }
                                sh "docker build --build-arg VITE_APP_BASE_URL=${VITE_APP_BASE_URL} -t ${DOCKER_FRONTEND_IMAGE}:${IMAGE_TAG} ."
                                sh "docker push ${DOCKER_FRONTEND_IMAGE}:${IMAGE_TAG}"
                            }
                        }
                    }
                }
                stage('Build & Push AI Server') {
                    // AI/ 폴더에 변경이 있을 때만 이 스테이지를 실행
                    when { expression { env.AI_CHANGED == 'true' } }
                    steps {
                        echo '3. AI Server 도커 이미지를 빌드하고 푸시합니다.'
                        dir(AI_SERVER_DIR) {
                             script {
                                withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIAL_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                                }
                                sh "docker build -t ${DOCKER_AI_IMAGE}:${IMAGE_TAG} ."
                                sh "docker push ${DOCKER_AI_IMAGE}:${IMAGE_TAG}"
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy on EC2') {
            // FE, BE, AI 중 하나라도 변경사항이 있을 때만 배포 실행
            when { expression { env.FE_CHANGED == 'true' || env.BE_CHANGED == 'true' || env.AI_CHANGED == 'true' } }
            steps {
                echo 'SSH Agent를 사용하여 EC2에 재배포합니다.'
                sshagent(credentials: ['ssh-credentials']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ubuntu@<YOUR_EC2_IP> '''
                            # docker-compose.yml 파일이 있는 경로로 이동합니다.
                            cd /home/ubuntu/app

                            echo "새 이미지를 pull 합니다."
                            docker compose pull

                            echo "변경된 서비스만 재시작합니다."
                            docker compose up -d --force-recreate --no-deps nginx-proxy spring-app ai-app

                            echo "배포가 완료되었습니다."
                        '''
                    """
                }
            }
        }
    }

    post {
        // ... (기존과 동일) ...
    }
}
```

---

## 3\. 주요 변경사항 설명

### `Detect Changes` 스테이지 추가

- `git diff` 명령어를 사용하여 마지막 커밋에서 변경된 파일들의 목록을 가져옵니다.
- 변경된 파일의 경로를 확인하여 `FE_CHANGED`, `BE_CHANGED`, `AI_CHANGED` 라는 환경 변수에 변경 여부를 `true`/`false`로 저장합니다.

### `Build & Push Images` 스테이지 변경

- 기존의 순차적인 빌드 방식을 `parallel` 블록으로 감싸 **세 개의 서비스를 동시에 빌드**하도록 변경하여 파이프라인 실행 시간을 단축합니다.
- 각 서비스의 빌드 스테이지(`Build & Push Backend` 등)에 `when` 조건을 추가합니다.
  - `when { expression { env.AI_CHANGED == 'true' } }`: `Detect Changes` 단계에서 설정한 변수 값을 확인하여, `AI/` 폴더에 변경이 있었을 경우에만 AI 서버를 빌드하고 푸시합니다. FE, BE도 마찬가지입니다.

### `Deploy on EC2` 스테이지 변경

- `when` 조건을 추가하여 세 서비스 중 어느 하나라도 변경사항이 감지되었을 경우에만 배포를 진행하도록 합니다.
- `docker compose up ...` 명령어에 재시작할 서비스 이름(`nginx-proxy spring-app ai-app`)을 명시하여, DB처럼 변경되지 않은 서비스는 건드리지 않고 필요한 서비스만 안전하게 재시작하도록 수정했습니다.

## 4\. EC2 서버 준비사항

배포 서버(EC2)에는 이전에 작업했던 `docker-compose.yml` 파일에 `ai-app` 서비스가 추가된 최종 버전이 준비되어 있어야 합니다.

## 5\. 실행 흐름

1.  개발자가 `AI/` 폴더의 코드만 수정하고 `dev` 브랜치에 푸시합니다.
2.  Jenkins 파이프라인이 실행됩니다.
3.  `Detect Changes` 스테이지에서 `AI_CHANGED` 변수만 `true`가 됩니다.
4.  `Build & Push Images` 스테이지에서는 `Build & Push AI Server` 단계만 실행되고, FE와 BE 빌드는 건너뜁니다.
5.  `Deploy on EC2` 스테이지가 실행되어 EC2 서버의 서비스들을 최신 버전으로 업데이트합니다.
