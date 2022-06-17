FROM openjdk:17-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app
COPY ./build/libs/blog-0.0.1-SNAPSHOT.jar main.jar
ENTRYPOINT ["java", "-jar", "main.jar"]