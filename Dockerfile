FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} jobrunr.jar
ENTRYPOINT ["java", "-jar", "/jobrunr.jar"]