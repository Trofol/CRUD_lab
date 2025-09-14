FROM openjdk:17-jdk-slim

WORKDIR /app

COPY eureka-server/target/eureka-server-1.0-SNAPSHOT.jar app.jar

EXPOSE 8761

CMD ["java", "-jar", "app.jar"]
