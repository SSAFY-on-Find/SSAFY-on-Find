FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY build/libs/chelsea-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]