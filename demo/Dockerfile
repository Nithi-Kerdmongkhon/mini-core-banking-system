# Stage 1: Build โปรเจกต์
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# คัดลอกทุกโฟลเดอร์ในโปรเจกต์เข้ามาทั้งหมด (รวมถึง common-entity-demo และ demo)
COPY . .

# สั่งเข้าไปบิลด์เฉพาะในโฟลเดอร์ demo โดยตรง (ข้ามการใช้ Root POM)
WORKDIR /app/demo
RUN mvn clean package -DskipTests

# Stage 2: Run Application
FROM eclipse-temurin:17-jre
WORKDIR /app

# ดึงไฟล์ jar จากโฟลเดอร์ demo/target มาใช้งาน
COPY --from=build /app/demo/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]