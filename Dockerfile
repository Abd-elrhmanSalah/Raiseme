# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY target/*.jar app.jar


EXPOSE  9090
CMD ["java", "-jar", "app.jar"]
