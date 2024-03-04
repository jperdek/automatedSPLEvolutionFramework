from typing import Optional

from graphSimulation.graphExtractor.geometry_data_to_image import GeometryDataToImage
from graphSimulation.graphMerger.merge_configuration import ImageSettings
import csv
import os
import json


class GraphProcessor:
    @staticmethod
    def convert_according_type(value: any, value_type: str):
        if value_type == "string":
            return str(value)
        elif value_type == "int":
            return int(value)
        elif value_type == "float":
            return float(value)
        elif value_type == "str":
            return str(value)
        elif value_type == "object":
            return json.dumps(value)
        else:
            return str(value)

    @staticmethod
    def copy_node_content_according_schema(graph_json_node: dict, node_id: int,
                                           schema_for_node: dict, connector_list_name: str = "pointsTo",
                                           connector_type_name: str = "fname",
                                           drawing: bool = True,
                                           image_settings: Optional[ImageSettings] = None) -> dict:
        if image_settings is None:
            image_settings = ImageSettings()
        node_content = {}
        for schema_name, schema_type in schema_for_node.items():
            if schema_name == "id":
                node_content[schema_name] = node_id
            elif drawing and schema_name == "image_base64_url":
                image_settings.associated_objects = graph_json_node[
                    connector_list_name] if connector_list_name in graph_json_node.keys() else []
                image_settings.connector_type_name = connector_type_name
                node_content[schema_name] = GeometryDataToImage.put_geometry_data_to_image(image_settings)
            elif schema_name in graph_json_node.keys():
                converted_checked_value = GraphProcessor.convert_according_type(
                    graph_json_node[schema_name], schema_type)
                node_content[schema_name] = converted_checked_value
            else:
                if "." in schema_name:
                    schema_names = schema_name.split(".")
                    iterator_variable = graph_json_node
                    for actual_name in schema_names:
                        iterator_variable = iterator_variable[actual_name]
                    converted_checked_value = GraphProcessor.convert_according_type(iterator_variable, schema_type)
                    node_content[schema_name] = converted_checked_value
                else:
                    node_content[schema_name] = None
        return node_content

    @staticmethod
    def create_connection(result_node1: dict, result_node2: dict,
                          node_identifier_key: str = "id",
                          connection_name1: str = "from", connection_name2: str = "to") -> dict:
        return {
            connection_name1: result_node1[node_identifier_key],
            connection_name2: result_node2[node_identifier_key]
        }

    @staticmethod
    def process_node(graph_json_node: dict, node_number_id: dict, connections: list[dict],
                     result_nodes: dict, used_schema: dict, connector_list_name: str = "pointsTo",
                     connector_type_name: str = "fname",
                     drawing: bool = True, image_settings: Optional[ImageSettings] = None) -> dict:
        node_type = graph_json_node[connector_type_name]
        if node_type not in result_nodes.keys() or not result_nodes[node_type]:
            result_nodes[node_type] = []
        node_content = GraphProcessor.copy_node_content_according_schema(graph_json_node,
                                                                         node_number_id["id"], used_schema[node_type],
                                                                         connector_list_name, connector_type_name,
                                                                         drawing, image_settings)
        node_number_id["id"] = node_number_id["id"] + 1
        result_nodes[node_type].append(node_content)

        if connector_list_name in graph_json_node.keys():
            for connected_node in graph_json_node[connector_list_name]:
                # skip nodes which are used to draw image
                if drawing and connected_node[connector_type_name] in \
                        GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING.keys():
                    continue
                neighbour_node = GraphProcessor.process_node(connected_node, node_number_id,
                                                             connections, result_nodes, used_schema,
                                                             connector_list_name, connector_type_name,
                                                             drawing, image_settings)
                connections.append(GraphProcessor.create_connection(node_content, neighbour_node))
        return node_content

    @staticmethod
    def get_nodes_and_connectors_from_graph_in_JSON(graph_json: dict,
                                                    used_schema: dict, connectors_list_name: str = "pointsTo",
                                                    connector_type_name: str = "fname",
                                                    drawing: bool = True, image_settings=None) -> (list[dict], dict):
        connections = []
        node_number_id = {"id": 1}
        result_nodes = {}
        init_node = {"id": 0}

        if connectors_list_name in graph_json:
            for graph_json_node in graph_json[connectors_list_name]:
                neighbour_node = GraphProcessor.process_node(graph_json_node, node_number_id,
                                                             connections, result_nodes, used_schema,
                                                             connectors_list_name, connector_type_name, drawing,
                                                             image_settings)
                connections.append(GraphProcessor.create_connection(init_node, neighbour_node))
        result_nodes[list(result_nodes.keys())[0]].append(init_node)
        return connections, result_nodes

    @staticmethod
    def create_csv_from_results(file_csv_path: str, data: list[dict], field_names: list[str]) -> None:
        with open(file_csv_path, 'w', encoding='UTF8', newline="") as f:
            writer = csv.DictWriter(f, fieldnames=field_names, delimiter="$")
            writer.writeheader()
            writer.writerows(data)

    @staticmethod
    def process_graph(graph_root: dict, graph_schema: dict,
                      connector_result_file_path: str,
                      result_file_path: str,
                      connector_list_name: str = "pointsTo", connector_type_name: str = "fname",
                      drawing: bool = True, image_settings: Optional[ImageSettings] = None):
        connections, result_nodes = GraphProcessor.get_nodes_and_connectors_from_graph_in_JSON(
            graph_root, graph_schema, connector_list_name, connector_type_name, drawing, image_settings)
        for node_type_name, result_nodes_type in result_nodes.items():
            node_type_absolute_path = os.path.join(result_file_path, node_type_name + ".csv")
            GraphProcessor.create_csv_from_results(node_type_absolute_path, result_nodes_type,
                                                   result_nodes_type[0].keys())
        GraphProcessor.create_csv_from_results(connector_result_file_path, connections, list(connections[0].keys()))
