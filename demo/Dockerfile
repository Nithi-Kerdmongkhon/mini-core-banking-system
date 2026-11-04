# ขั้นตอนที่ 1: Build JAR
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn -f common-entity-demo/pom.xml clean install -DskipTests
RUN mvn -f demo/pom.xml clean package -DskipTests

# ขั้นตอนที่ 2: Run Application
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/demo/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]