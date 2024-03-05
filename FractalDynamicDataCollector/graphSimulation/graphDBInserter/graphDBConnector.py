from graphSimulation.graphExtractor.graph_scheme import FractalGraphScheme
from neo4j import GraphDatabase
import csv
import json


class SchemaConverter:
    @staticmethod
    def convert_according_type(value: any, value_type: str):
        if value_type == "string":
            if value is None or not value:
                value = ""
            return str(value)
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

    @staticmethod
    def construct_non_problematic_schema(object_schema: dict) -> dict:
        modified_schema = {}
        for key, value in object_schema.items():
            modified_schema[key.replace(".", "")] = value.replace(".", "")
        return modified_schema

    @staticmethod
    def convert_schema_dict_to_neo_parameters(object_schema: dict) -> str:
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

    def insert_objects(
        self,
        path_to_object_csv: str,
        object_schema: dict,
        object_name: str = "Obj",
        delimiter: str = "$",
    ):
        object_schema = self.construct_non_problematic_schema(object_schema)
        neo4j_parameters = self.convert_schema_dict_to_neo_parameters(object_schema)
        with self.driver.session() as session:
            with open(path_to_object_csv, "r", encoding="utf-8") as file:
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

    def insert_connections(
        self, path_to_connections_csv: str, delimiter: str = "$"
    ) -> None:
        with self.driver.session() as session:
            with open(path_to_connections_csv, "r", encoding="utf-8") as file:
                connections = csv.DictReader(file, delimiter=delimiter)
                for connection_record in connections:
                    session.run(
                        "MATCH (a {id: "
                        + connection_record["from"]
                        + "} ), (b {id: "
                        + connection_record["to"]
                        + " }) MERGE (a)-[c: CONNECTS]->(b);"
                    )

    def close(self) -> None:
        # Don't forget to close the driver connection when you are finished with it
        self.driver.close()


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
    insert_merged_graph(url1, user1, password1)
    # insert_small_graph(url1, user1, password1)
