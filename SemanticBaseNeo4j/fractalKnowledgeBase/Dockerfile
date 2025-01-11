#FROM neo4j:5.11.0 AS build-stage
#LABEL stage=builder
#ENV NEO4J_AUTH=neo4j/featureNeo4
#COPY ./plugins /plugins
#RUN --mount=target=/dumps,type=bind,source=dumps neo4j-admin load --from=/dumps/probase-dump.dump --database=neo4j --force


FROM neo4j:4.4-community AS final-image
ENV NEO4J_AUTH=neo4j/featureNeo4
#ENV NEO4J_PLUGINS='["apoc", "n10s"]'
#COPY --from=build-stage /data/databases /data/databases
#COPY --from=build-stage /data/transactions /data/transactions
#COPY ./plugins /plugins
#COPY ./plugins /var/lib/neo4j/plugins
#COPY ./labs /labs
COPY ./conf /conf 
#COPY ./conf/neo4j.conf /conf/neo4j.conf
RUN apt-get install wget
CMD [ "neo4j" ]