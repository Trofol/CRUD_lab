FROM openjdk:17-jdk-slim

WORKDIR /app

COPY config-server/target/config-server-1.0-SNAPSHOT.jar app.jar

EXPOSE 8888

CMD ["java", "-jar", "app.jar"]
