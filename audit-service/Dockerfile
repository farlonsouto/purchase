FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the already-built JAR from the host
COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]