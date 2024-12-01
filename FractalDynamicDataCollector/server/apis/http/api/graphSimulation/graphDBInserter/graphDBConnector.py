import os
from typing import Optional, Dict, List

from graphSimulation.graphExtractor.graph_scheme import FractalGraphScheme
from neo4j import GraphDatabase
from neo4j._sync.work import Session
import re as regexp
import csv
import json


class SchemaConverter:
    @staticmethod
    def convert_according_type(value: any, value_type: str):
        if value_type == "string":
            if value is None or not value:
                value = ""
            return str(value).replace("\"", "").replace("'", "")
        elif value_type == "int":
            if value is None or not value:
                value = 0
            return int(value)
        elif value_type == "float":
            if value is None or not value:
                value = 0.0
            return float(value)
        elif value_type == "str":
            if value is None or not value:
                value = ""
            return str(value)
        elif value_type == "object":
            if value is None or not value:
                return ""
            return json.dumps(value)
        else:
            if value is None or not value:
                return ""
            return str(value)

    @staticmethod
    def copy_node_content_according_schema(
        inserted_graph_node: dict, schema_for_node: dict, remove_dot: bool = True
    ) -> dict:
        node_content = {}
        for schema_name, schema_type in schema_for_node.items():
            if remove_dot:
                schema_name = schema_name.replace(".", "")
            if schema_name in inserted_graph_node.keys():
                converted_checked_value = SchemaConverter.convert_according_type(
                    inserted_graph_node[schema_name], schema_type
                )
                node_content[schema_name] = converted_checked_value
            else:
                node_content[schema_name] = ""
        return node_content


class GraphConnector:
    def __init__(self, uri: str, user: str, password: str) -> None:
        self.driver = GraphDatabase.driver(uri, auth=(user, password))

    def __find_neo4j_schema_for_data(self, data_to_find_schema: csv.DictReader) -> dict:
        object_schema = {}
        for new_object_record in data_to_find_schema:
            for analyzed_key, analyzed_value in new_object_record.items():
                if analyzed_key == "id":
                    object_schema["id"] = "int"
                if regexp.search(
                    r"^((?:-?[123456789]+\d*|0)\.\d+)$", str(analyzed_value).strip()
                ):
                    if (
                        analyzed_key not in object_schema.keys()
                        or object_schema[analyzed_key] != "string"
                    ):
                        object_schema[analyzed_key] = "float"
                        continue
                elif regexp.search(
                    r"^((?:-?[123456789]+\d+|0))$", str(analyzed_value).strip()
                ):
                    if analyzed_key not in object_schema.keys() or (
                        object_schema[analyzed_key] != "float"
                        and object_schema[analyzed_key] != "string"
                    ):
                        object_schema[analyzed_key] = "int"
                        continue
                elif str(analyzed_value).strip() == "[object]":
                    object_schema[analyzed_key] = "string"
                    continue
                object_schema[analyzed_key] = "string"
                continue
        return object_schema

    @staticmethod
    def __construct_non_problematic_schema(object_schema: Optional[dict]) -> dict:
        modified_schema = {}
        for key, value in object_schema.items():
            modified_schema[key.replace(".", "")] = value.replace(".", "")
        return modified_schema

    @staticmethod
    def __convert_schema_dict_to_neo_parameters(object_schema: dict) -> str:
        neo4j_parameters = "{"
        for variable_name in object_schema.keys():
            variable_name = variable_name.replace(".", "")
            neo4j_parameters = (
                neo4j_parameters + " " + variable_name + ": $" + variable_name + ","
            )
        neo4j_parameters = neo4j_parameters[:-1] + " }"
        return neo4j_parameters

    def clear_database(self) -> None:
        with self.driver.session() as session:
            session.run("MATCH (a)-[r]-(b)	DELETE r")
            session.run("MATCH (a) DELETE a")

    def __save_object_schema(
        self, new_schema: dict, new_schema_directory: Optional[str], entity_name: str
    ) -> None:
        if new_schema_directory:
            path_to_serialized_entity = os.path.join(
                new_schema_directory, entity_name + "_SCHEMA.json"
            )
            with open(path_to_serialized_entity, "w", encoding="utf-8") as file:
                file.write(json.dumps(new_schema))

    def insert_objects(
        self,
        path_to_object_csv: str,
        object_schema: Optional[dict],
        object_name: str = "Obj",
        delimiter: str = "$",
        new_schema_directory: str = "../../new_schemas",
    ):
        with open(path_to_object_csv, "r", encoding="utf-8") as file:
            with self.driver.session() as session:
                new_objects = csv.DictReader(file, delimiter=delimiter)
                if not object_schema:
                    object_schema = self.__find_neo4j_schema_for_data(new_objects)
                    self.__save_object_schema(
                        object_schema, new_schema_directory, object_name
                    )
                else:
                    object_schema = self.__construct_non_problematic_schema(
                        object_schema
                    )
                neo4j_parameters = self.__convert_schema_dict_to_neo_parameters(
                    object_schema
                )
                file.seek(0)
                new_objects = csv.DictReader(file, delimiter=delimiter)
                for new_object_record in new_objects:
                    new_object_record_schema = (
                        SchemaConverter.copy_node_content_according_schema(
                            new_object_record, object_schema, remove_dot=True
                        )
                    )
                    session.run(
                        "MERGE (a: " + object_name + " " + neo4j_parameters + ");",
                        new_object_record_schema,
                    )

    def insert_objects_from_ram(
        self,
        new_objects: List[Dict],
        object_schema: Optional[Dict],
        object_name: str = "Obj",
        new_schema_directory: str = "../../new_schemas",
    ):
        with self.driver.session() as session:
            if not object_schema:
                object_schema = self.__find_neo4j_schema_for_data(new_objects)
                self.__save_object_schema(
                    object_schema, new_schema_directory, object_name
                )
            else:
                object_schema = self.__construct_non_problematic_schema(
                    object_schema
                )
            neo4j_parameters = self.__convert_schema_dict_to_neo_parameters(
                object_schema
            )

            for new_object_record in new_objects:
                new_object_record_schema = (
                    SchemaConverter.copy_node_content_according_schema(
                        new_object_record, object_schema, remove_dot=True
                    )
                )

                session.run(
                    "MERGE (a: " + object_name + " " + neo4j_parameters + ");",
                    new_object_record_schema,
                )

    def __insert_base_connection(self, session: Session, node_id_from: str, node_id_to: str) -> None:
        session.run(
            "MATCH (a {id: "
            + node_id_from
            + "} ), (b {id: "
            + node_id_to
            + " }) MERGE (a)-[c: CONNECTS]->(b);"
        )

    def insert_connections_from_ram(self, connections: List[Dict]) -> None:
        with self.driver.session() as session:
            for connection_record in connections:
                self.__insert_base_connection(session, str(connection_record["from"]), str(connection_record["to"]))

    def insert_connections(
        self, path_to_connections_csv: str, delimiter: str = "$"
    ) -> None:
        with self.driver.session() as session:
            with open(path_to_connections_csv, "r", encoding="utf-8") as file:
                connections = csv.DictReader(file, delimiter=delimiter)
                for connection_record in connections:
                    self.__insert_base_connection(session, str(connection_record["from"]), str(connection_record["to"]))

    def close(self) -> None:
        # Don't forget to close the driver connection when you are finished with it
        self.driver.close()

    def insert_graph_without_scheme(
        self,
        project_directory_path: str,
        connections_file_name: str,
        new_schema_directory: str = "../../new_schemas",
        used_schemas: Optional[Dict] = None,
        connections_only: bool = False
    ) -> None:
        if used_schemas is None:
            used_schemas = dict()
        path_to_connections = None
        absolute_project_directory_path = os.path.abspath(project_directory_path)

        for entity_name in os.listdir(absolute_project_directory_path):
            path_to_graph_file = os.path.join(
                absolute_project_directory_path, entity_name
            )
            if not os.path.isdir(path_to_graph_file) and entity_name.endswith(".csv"):
                print("Processing entity: " + entity_name)
                if connections_file_name not in path_to_graph_file and not connections_only:
                    object_type = (
                        entity_name.replace(".csv", "").title().replace(" ", "").replace("\"", "")
                    )
                    used_schema = used_schemas.get(entity_name, {})
                    if not used_schema:
                        used_schemas[entity_name] = used_schema
                    self.insert_objects(
                        path_to_graph_file,
                        object_schema=used_schema,
                        object_name=object_type,
                        new_schema_directory=new_schema_directory,
                    )
                else:
                    path_to_connections = path_to_graph_file
        if path_to_connections:
            print("Inserting connections")
            self.insert_connections(path_to_connections)
        else:
            raise Exception("Path to connections not found!")

    def insert_graph_without_scheme_from_ram(
        self,
        processed_tables_as_graph: Dict,
        new_schema_directory: str = "../../new_schemas",
        used_schemas: Optional[Dict] = None,
        connections_only: bool = False,
        connections_file_name: str = "connectors"
    ) -> None:
        if used_schemas is None:
            used_schemas = dict()
        connections = None

        for table_name, table_data in processed_tables_as_graph.items():
            print("Processing entity: " + table_name)
            if connections_file_name not in table_name and not connections_only:
                object_type = table_name.replace(".csv", "").title().replace(" ", "").replace("\\", "").replace("\"", "").replace("'", "")

                used_schema = used_schemas.get(table_name, {})
                used_schemas[table_name] = used_schema if not used_schema else used_schemas[table_name]
                self.insert_objects_from_ram(
                    table_data["data"],
                    object_schema=used_schema,
                    object_name=object_type,
                    new_schema_directory=new_schema_directory,
                )
            else:
                connections = table_data
        if connections:
            print("Inserting connections")
            self.insert_connections_from_ram(connections["data"])
        else:
            raise Exception("Path to connections not found!")

    @staticmethod
    def insert_graph_without_scheme_transaction(
        project_directory_path: str,
        connections_file_name: str,
        url: str,
        user: str,
        password: str,
        new_schema_directory: str = "../../new_schemas",
        connections_only: bool = False
    ) -> None:
        connection = GraphConnector(url, user, password)
        if not connections_only:
            connection.clear_database()
        connection.insert_graph_without_scheme(
            project_directory_path, connections_file_name, new_schema_directory, connections_only=connections_only
        )
        connection.close()

    @staticmethod
    def insert_graph_without_scheme_for_dataset(
        dataset_directory_path: str,
        url: str,
        user: str,
        password: str,
        connections_file_name: str = "connections.csv",
        new_schema_directory: Optional[str] = "../../new_schemas",
        clear_database: bool = True,
        connections_only: bool = False
    ) -> None:
        connection = GraphConnector(url, user, password)
        if clear_database:
            connection.clear_database()
        absolute_dataset_directory_path = os.path.abspath(dataset_directory_path)

        print("Processing dataset: " + absolute_dataset_directory_path + " started!")
        used_schemas = {}
        for project_name in os.listdir(absolute_dataset_directory_path):
            print("Inserting project: " + project_name)
            project_directory_path = os.path.join(
                absolute_dataset_directory_path, project_name
            )
            connection.insert_graph_without_scheme(
                project_directory_path,
                connections_file_name,
                new_schema_directory=new_schema_directory,
                used_schemas=used_schemas,
                connections_only=connections_only
            )
        connection.close()


class GraphFromFamilyInserter:
    @staticmethod
    def insert_merged_graph(url: str, user: str, password: str) -> None:
        connection = GraphConnector(url, user, password)
        connection.clear_database()
        connection.insert_objects(
            "../../generated_dataset_vp_graph_data_merged/function drawWCurve.csv",
            FractalGraphScheme.create_default_scheme_for_draw_WCurve(),
            "DrawWCurve",
        )
        connection.insert_objects(
            "../../generated_dataset_vp_graph_data_merged/function WCurve.csv",
            FractalGraphScheme.create_default_scheme_for_wcurve(),
            "WCurve",
        )
        # connection.insert_objects("../../generated_dataset_vp_graph_data_merged/function drawLine.csv",
        #                          FractalGraphScheme.create_default_scheme_for_draw_line(), "DrawLine")
        connection.insert_connections(
            "../../generated_dataset_vp_graph_data_merged/connections.csv"
        )
        connection.close()

    @staticmethod
    def insert_small_graph_from_ram(url: str, user: str, password: str, graph_data_in_docs: Dict,
                                    clear_database: bool = False, connections_only: bool = False,
                                    new_schema_directory: Optional[str] = None) -> None:
        connection = GraphConnector(url, user, password)
        if not clear_database:
            connection.clear_database()
        connection.insert_graph_without_scheme_from_ram(graph_data_in_docs, connections_only=connections_only,
                                                        new_schema_directory=new_schema_directory)
        connection.close()

    @staticmethod
    def insert_small_graph(url: str, user: str, password: str) -> None:
        connection = GraphConnector(url, user, password)
        connection.clear_database()
        connection.insert_objects(
            "../../generated_dataset_vp_graph_data/1/function drawWCurve.csv",
            FractalGraphScheme.create_default_scheme_for_draw_WCurve(),
            "DrawWCurve",
        )
        connection.insert_objects(
            "../../generated_dataset_vp_graph_data/1/function WCurve.csv",
            FractalGraphScheme.create_default_scheme_for_wcurve(),
            "WCurve",
        )
        connection.insert_connections(
            "../../generated_dataset_vp_graph_data/1/connections.csv"
        )
        connection.close()


if __name__ == "__main__":
    scheme = "neo4j"
    host_name = "localhost"
    port = 7687
    url1 = f"{scheme}://{host_name}:{port}"
    user1 = "neo4j"
    password1 = "feature"
    # insert_merged_graph(url1, user1, password1)
    # insert_small_graph(url1, user1, password1)
    GraphConnector.insert_graph_without_scheme_for_dataset(
        dataset_directory_path="../../../../../../generated_dataset_vp_graph_data",
        url=url1,
        user=user1,
        password=password1,
        connections_file_name="connections.csv",
        new_schema_directory="../../new_schemas",
        clear_database=True,
        connections_only=False
    )
