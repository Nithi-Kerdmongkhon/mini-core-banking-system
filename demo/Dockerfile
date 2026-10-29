# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY common-entity-demo ./common-entity-demo
COPY demo ./demo

# build และติดตั้ง common-entity-demo ลง local repo ก่อน
WORKDIR /app/common-entity-demo
RUN mvn clean install -DskipTests

# แล้วค่อย build demo
WORKDIR /app/demo
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/demo/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]