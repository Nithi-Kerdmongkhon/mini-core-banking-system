# Stage 1: Build ทั้งหมดจาก Root
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# คัดลอกทุกไฟล์เข้ามา
COPY . .

# เปลี่ยนคำสั่งรันเป็น mvn clean install เพื่อให้ common-entity-demo ถูกติดตั้งลง Local Repo ของ Container ก่อน แล้ว demo ถึงจะเรียกใช้งานได้
RUN mvn clean install -DskipTests

# Stage 2: Run Application เฉพาะโมดูล demo
FROM eclipse-temurin:17-jre
WORKDIR /app

# ดึงไฟล์ .jar ของ demo มาใช้งาน
COPY --from=build /app/demo/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]