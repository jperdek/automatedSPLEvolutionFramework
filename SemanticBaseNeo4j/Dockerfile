FROM neo4j:5.25.1 AS build-stage
LABEL stage=builder
ENV NEO4J_AUTH=neo4j/featureNeo4
COPY ./plugins /plugins
RUN --mount=target=/dumps,type=bind,source=dumps neo4j-admin load --from=/dumps/probase-dump.dump --database=neo4j --force


FROM neo4j:5.25.1 AS final-image
ENV NEO4J_AUTH=neo4j/featureNeo4
RUN apk add --no-cache wget
COPY --from=build-stage /data/databases /data/databases
COPY --from=build-stage /data/transactions /data/transactions
COPY ./plugins /plugins
COPY ./conf/neo4j.conf /conf/neo4j.conf
CMD [ "neo4j" ]