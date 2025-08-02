FROM openjdk:21-slim AS builder

WORKDIR /app

COPY build/libs/chelsea-0.0.1-SNAPSHOT.jar app.jar

FROM openjdk:21-slim

WORKDIR /app

COPY --from=builder /app/app.jar .

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080