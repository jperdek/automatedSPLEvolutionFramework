FROM openjdk:18-jdk-alpine
MAINTAINER perdek.jakub@gmail.com


COPY deploy/deploy.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]