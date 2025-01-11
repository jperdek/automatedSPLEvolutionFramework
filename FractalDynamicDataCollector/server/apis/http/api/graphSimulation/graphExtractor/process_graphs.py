import io
from typing import Optional, Dict, List

from typing_extensions import deprecated

from graphSimulation.graphExtractor.geometry_data_to_image import GeometryDataToImage
from graphSimulation.graphMerger.merge_configuration import ImageSettings
import csv
import os
import json


class GraphProcessor:
    @staticmethod
    def convert_according_type(value: any, value_type: str) -> any:
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
    def copy_node_content_according_schema(
        graph_json_node: Dict,
        node_id: int,
        schema_for_node: Dict,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        image_settings: Optional[ImageSettings] = None,
    ) -> Dict:
        if image_settings is None:
            image_settings = ImageSettings()
        node_content = {}
        for schema_name, schema_type in schema_for_node.items():
            if schema_name == "id":
                node_content[schema_name] = node_id
            elif drawing and schema_name == "image_base64_url":
                image_settings.associated_objects = (
                    graph_json_node[connector_list_name]
                    if connector_list_name in graph_json_node.keys()
                    else []
                )
                image_settings.connector_type_name = connector_type_name
                node_content[
                    schema_name
                ] = GeometryDataToImage.put_geometry_data_to_image(
                    graph_json_node, image_settings
                )
            elif schema_name in graph_json_node.keys():
                converted_checked_value = GraphProcessor.convert_according_type(
                    graph_json_node[schema_name], schema_type
                )
                node_content[schema_name] = converted_checked_value
            else:
                if "." in schema_name:
                    schema_names = schema_name.split(".")
                    iterator_variable = graph_json_node
                    for actual_name in schema_names:
                        iterator_variable = iterator_variable[actual_name]
                    converted_checked_value = GraphProcessor.convert_according_type(
                        iterator_variable, schema_type
                    )
                    node_content[schema_name] = converted_checked_value
                else:
                    node_content[schema_name] = None
        return node_content

    @staticmethod
    def copy_node_content_without_schema(
        graph_json_node: Dict,
        node_id: int,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        image_settings: Optional[ImageSettings] = None,
    ) -> Dict:
        node_content = {"id": node_id}
        if drawing:
            if image_settings is None:
                image_settings = ImageSettings(disable_taking_images=False)
            if not image_settings.disable_taking_images:
                image_settings.associated_objects = (
                    graph_json_node[connector_list_name]
                    if connector_list_name in graph_json_node.keys()
                    else []
                ).copy()

                image_settings.connector_type_name = connector_type_name
                node_content[
                    "image_base64_url"
                ] = GeometryDataToImage.put_geometry_data_to_image(
                    graph_json_node, image_settings
                )
                image_settings.associated_objects.clear()
        for key, value in graph_json_node.items():
            if (
                connector_list_name != key
                and "previously_processed_node_content" != key
            ):
                node_content[key] = value
        return node_content

    @staticmethod
    def create_connection(
        result_node1: Dict,
        result_node2: Dict,
        node_identifier_key: str = "id",
        connection_name1: str = "from",
        connection_name2: str = "to",
    ) -> Dict:
        return {
            connection_name1: result_node1[node_identifier_key],
            connection_name2: result_node2[node_identifier_key],
        }

    @staticmethod
    @deprecated
    def process_node_recursive(
        graph_json_node: Dict,
        node_number_id: Dict,
        connections: List[Dict],
        result_nodes: Dict,
        used_schema: Optional[Dict],
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        image_settings: Optional[ImageSettings] = None,
        skip_nodes_used_to_draw_image: bool = True,
    ) -> Dict:
        node_type = graph_json_node[connector_type_name]
        if node_type not in result_nodes.keys() or not result_nodes[node_type]:
            result_nodes[node_type] = []
        if used_schema and node_type in used_schema:
            node_content = GraphProcessor.copy_node_content_according_schema(
                graph_json_node,
                node_number_id["id"],
                used_schema[node_type],
                connector_list_name,
                connector_type_name,
                drawing,
                image_settings,
            )
        else:
            node_content = GraphProcessor.copy_node_content_without_schema(
                graph_json_node,
                node_number_id["id"],
                connector_list_name,
                connector_type_name,
                drawing,
                image_settings,
            )
        node_number_id["id"] = node_number_id["id"] + 1
        result_nodes[node_type].append(node_content)

        if connector_list_name in graph_json_node.keys():
            for connected_node in graph_json_node[connector_list_name]:
                # skip nodes which are used to draw image
                if (
                    skip_nodes_used_to_draw_image
                    and drawing
                    and connected_node[connector_type_name]
                    in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING.keys()
                ):
                    continue
                neighbour_node = GraphProcessor.process_node(
                    connected_node,
                    node_number_id,
                    connections,
                    result_nodes,
                    used_schema,
                    connector_list_name,
                    connector_type_name,
                    drawing,
                    image_settings,
                )
                connections.append(
                    GraphProcessor.create_connection(node_content, neighbour_node)
                )
        return node_content

    @staticmethod
    def process_node(
        graph_json_node: Dict,
        node_number_id: Dict,
        connections: List[Dict],
        result_nodes: Dict,
        used_schema: Optional[Dict],
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        image_settings: Optional[ImageSettings] = None,
        skip_nodes_used_to_draw_image: bool = True,
    ) -> Dict:
        node_content = None
        graph_json_node["previously_processed_node_content"] = None
        process_node_stack = [graph_json_node]
        while len(process_node_stack) > 0:
            processed_node = process_node_stack.pop()
            node_type = processed_node[connector_type_name]
            if node_type not in result_nodes.keys() or not result_nodes[node_type]:
                result_nodes[node_type] = []

            if used_schema and node_type in used_schema:
                node_content = GraphProcessor.copy_node_content_according_schema(
                    processed_node,
                    node_number_id["id"],
                    used_schema[node_type],
                    connector_list_name,
                    connector_type_name,
                    drawing,
                    image_settings,
                )
            else:
                node_content = GraphProcessor.copy_node_content_without_schema(
                    processed_node,
                    node_number_id["id"],
                    connector_list_name,
                    connector_type_name,
                    drawing,
                    image_settings,
                )
            node_number_id["id"] = node_number_id["id"] + 1
            result_nodes[node_type].append(node_content)
            # manage connections
            parent_node_content = processed_node["previously_processed_node_content"]
            if parent_node_content:
                connections.append(
                    GraphProcessor.create_connection(parent_node_content, node_content)
                )
            del processed_node["previously_processed_node_content"]

            if connector_list_name in processed_node.keys():
                for connected_node in processed_node[connector_list_name]:
                    # skip nodes which are used to draw image
                    if (
                        skip_nodes_used_to_draw_image
                        and drawing
                        and connected_node[connector_type_name]
                        in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING.keys()
                    ):
                        continue
                    connected_node["previously_processed_node_content"] = node_content
                    process_node_stack.append(connected_node)
        return node_content

    @staticmethod
    def get_nodes_and_connectors_from_graph_in_JSON(
        graph_json: Dict,
        used_schema: Optional[Dict],
        connectors_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        image_settings: Optional[ImageSettings] = None,
        skip_nodes_used_to_draw_image: bool = True,
    ) -> (List[Dict], Dict):
        connections = []
        node_number_id = {"id": 1}
        result_nodes = {}
        init_node = {"id": 0}

        if connectors_list_name in graph_json:
            for graph_json_node in graph_json[connectors_list_name]:
                neighbour_node = GraphProcessor.process_node(
                    graph_json_node,
                    node_number_id,
                    connections,
                    result_nodes,
                    used_schema,
                    connectors_list_name,
                    connector_type_name,
                    drawing,
                    image_settings,
                    skip_nodes_used_to_draw_image,
                )
                connections.append(
                    GraphProcessor.create_connection(init_node, neighbour_node)
                )
        result_nodes[list(result_nodes.keys())[0]].append(init_node)
        return connections, result_nodes

    @staticmethod
    def create_csv_from_results(
        file_csv_path: str, data: List[Dict], field_names: List[str]
    ) -> None:
        with open(file_csv_path, "w", encoding="UTF8", newline="") as f:
            writer = csv.DictWriter(f, fieldnames=field_names, delimiter="$")
            writer.writeheader()
            writer.writerows(data)

    @staticmethod
    def process_graph(
        graph_root: Dict,
        graph_schema: Optional[Dict],
        connector_result_file_path: str,
        result_file_path: str,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        image_settings: Optional[ImageSettings] = None,
        skip_nodes_used_to_draw_image: bool = True,
    ):
        (
            connections,
            result_nodes,
        ) = GraphProcessor.get_nodes_and_connectors_from_graph_in_JSON(
            graph_root,
            graph_schema,
            connector_list_name,
            connector_type_name,
            drawing,
            image_settings,
            skip_nodes_used_to_draw_image,
        )
        for node_type_name, result_nodes_type in result_nodes.items():
            node_type_absolute_path = os.path.join(
                result_file_path, node_type_name + ".csv"
            )
            result_nodes_keys = list(result_nodes_type[0].keys())
            if not graph_schema:
                for result_nodes_instance in result_nodes_type:
                    for result_nodes_instance_key in result_nodes_instance.keys():
                        if result_nodes_instance_key not in result_nodes_keys:
                            result_nodes_keys.append(result_nodes_instance_key)
            GraphProcessor.create_csv_from_results(
                node_type_absolute_path, result_nodes_type, result_nodes_keys
            )
        GraphProcessor.create_csv_from_results(
            connector_result_file_path, connections, list(connections[0].keys())
        )

    @staticmethod
    def create_csv_from_results_to_ram(data: List[Dict], field_names: List[str]) -> str:
        buffer = io.BytesIO()
        with io.TextIOWrapper(buffer, encoding="utf-8") as wrapper:
            dict_writer = csv.DictWriter(wrapper, fieldnames=field_names, delimiter="$")
            dict_writer.writeheader()
            dict_writer.writerows(data)
            return buffer.read().decode("UTF-8")

    @staticmethod
    def process_graph_to_ram(
        graph_root: Dict,
        graph_schema: Optional[Dict] = None,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        image_settings: Optional[ImageSettings] = None,
        skip_nodes_used_to_draw_image: bool = True,
    ) -> Dict:
        (
            connections,
            result_nodes,
        ) = GraphProcessor.get_nodes_and_connectors_from_graph_in_JSON(
            graph_root,
            graph_schema,
            connector_list_name,
            connector_type_name,
            drawing,
            image_settings,
            skip_nodes_used_to_draw_image,
        )
        resulting_csvs = {}
        for node_type_name, result_nodes_type in result_nodes.items():
            result_nodes_keys = list(result_nodes_type[0].keys())
            if not graph_schema:
                for result_nodes_instance in result_nodes_type:
                    for result_nodes_instance_key in result_nodes_instance.keys():
                        if result_nodes_instance_key not in result_nodes_keys:
                            result_nodes_keys.append(result_nodes_instance_key)
            resulting_csvs[node_type_name] = {
                "data": result_nodes_type,
                "field_names": result_nodes_keys,
            }
        if len(connections) > 0:
            resulting_csvs["connectors"] = {
                "data": connections,
                "field_names": list(connections[0].keys()),
            }
        return resulting_csvs
