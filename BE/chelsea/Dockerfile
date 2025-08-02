FROM openjdk:21-jdk-slim as builder
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

COPY src src
COPY config config

RUN chmod +x ./gradlew
RUN ./gradlew build -x test


FROM openjdk:21-jre-slim

COPY --from=builder /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]