FROM openjdk:18-jdk-alpine
MAINTAINER perdek.jakub@gmail.com

ARG shared_evolution_volume=evolution-volume
ARG project_path=/EvolutionSPLFramework
ENV PROJECT_PATH=$project_path
ARG path_to_evolution_directory=/evolution
ENV PATH_TO_EVOLUTION_DIRECTORY=$path_to_evolution_directory
ENV canto_script_resource_location=/EvolutionSPLFramework/resources/canto/canto-0.15.js
ENV CANTO_SCRIPT_RESOURCE_LOCATION=$canto_script_resource_location
ENV DOCKER_HOST=host.docker.internal
ENV IS_LINUX=true


COPY deploy/deploy.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]