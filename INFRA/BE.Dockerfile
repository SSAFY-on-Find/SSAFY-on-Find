FROM gradle:jdk21 AS builder

WORKDIR /home/gradle

COPY BE/build.gradle BE/settings.gradle ./
COPY BE/gradlew ./
COPY BE/gradle ./gradle

COPY BE/src ./src

RUN chmod +x ./gradlew && ./gradlew build --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /home/gradle/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]