# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY common-entity-demo ./common-entity-demo
COPY demo ./demo

WORKDIR /app/common-entity-demo
RUN mvn clean install -DskipTests

WORKDIR /app/demo
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/demo/target/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]