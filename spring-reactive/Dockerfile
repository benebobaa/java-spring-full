# Build stage
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Run stage
FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY src/main/resources/application-local.properties /app/application.properties

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]