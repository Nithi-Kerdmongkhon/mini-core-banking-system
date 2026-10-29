# Stage 1: Build ทั้งหมดจาก Root เพื่อให้ Maven อ่าน Dependency ข้ามโมดูลได้
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# คัดลอกทุกโฟลเดอร์และไฟล์ทั้งหมดเข้า Container
COPY . .

# สั่งรัน Maven จากระดับ Root (มันจะจัดการคอมไพล์ common-entity-demo ให้ก่อน แล้วค่อยมา demo)
RUN mvn clean package -DskipTests

# Stage 2: Run Application เฉพาะโมดูล demo
FROM eclipse-temurin:17-jre
WORKDIR /app

# ดึงไฟล์ .jar ที่บิลด์เสร็จแล้วจากโฟลเดอร์ demo/target/ มาใช้งาน
COPY --from=build /app/demo/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]