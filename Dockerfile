FROM alpine:latest


ARG project_path=/EvolutionSPLFramework
ENV PROJECT_PATH=$project_path

#ARG path_to_evolution_directory=/evolution
#ENV PATH_TO_EVOLUTION_DIRECTORY=$path_to_evolution_directory
#ENV canto_script_resource_location=/EvolutionSPLFramework/resources/canto/canto-0.15.js
#ENV CANTO_SCRIPT_RESOURCE_LOCATION=$canto_script_resource_location

COPY ./EvolutionSPLFramework/splsToMerge /localFiles/splsToMerge
COPY ./EvolutionSPLFramework/resources /localFiles/resources