FROM openjdk:17-jdk

# RUN addgroup -S spring && adduser -S spring -G spring
# USER spring:spring

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} main.jar
RUN mkdir uploads static
ENTRYPOINT ["java", "-jar", "main.jar"]