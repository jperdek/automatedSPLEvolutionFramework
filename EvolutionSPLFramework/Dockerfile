FROM openjdk:18-jdk-alpine
MAINTAINER perdek.jakub@gmail.com


COPY target/deploy.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]