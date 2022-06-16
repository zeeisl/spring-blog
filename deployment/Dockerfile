FROM openjdk:17-jdk

# RUN addgroup -S spring && adduser -S spring -G spring
# USER spring:spring

WORKDIR /app
RUN mkdir uploads static
COPY ./main.jar main.jar
ENTRYPOINT ["java", "-jar", "main.jar"]