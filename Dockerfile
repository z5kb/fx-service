# Build the application
FROM maven:3.9.16-eclipse-temurin-26 AS build
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

# Build the image
FROM eclipse-temurin:26
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
