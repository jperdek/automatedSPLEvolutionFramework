import sys
import os

# for Windows only
from graphSimulation.graphMerger.merge_configuration import (
    MergeConfiguration,
    ImageSettings,
    StrategyOnIntersection,
)
from graphSimulation.graphMerger.related_data_manager import RelatedDataManager

if hasattr(sys, "getwindowsversion"):
    os.environ["PATH"] += r";" + os.getcwd() + "\\..\\..\\vips-dev-8.12\\bin"

from graphSimulation.graphExtractor.geometry_data_to_image import GeometryDataToImage
from graphSimulation.graphExtractor.graph_scheme import FractalGraphScheme
from graphSimulation.graphMerger.node_verifier import NodeVerifier
from merge_aggregations import MergeAggregations
from analyzer import DynamicFractalAnalyzer
from typing import Optional
import csv
import os
import json


class GraphMerger:
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
    def copy_node_content_according_schema(
        graph_json_node: dict,
        node_id: int,
        schema_for_node: dict,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        merge_configuration: MergeConfiguration = None,
        instance_related_data: dict = None,
    ) -> dict:
        image_settings = None
        if merge_configuration:
            image_settings = merge_configuration.image_settings
        if image_settings is None:
            image_settings = ImageSettings()
        node_content = {"id": node_id, "fname": graph_json_node[connector_type_name]}
        for schema_name, schema_type in schema_for_node.items():
            if schema_name == "id":
                node_content[schema_name] = node_id
            if connector_type_name == schema_name:
                continue
            elif drawing and schema_name == "image_base64_url":
                image_settings.associated_objects = (
                    graph_json_node[connector_list_name]
                    if connector_list_name in graph_json_node.keys()
                    else []
                )
                image_settings.connector_type_name = connector_type_name
                node_content[
                    schema_name
                ] = GeometryDataToImage.put_geometry_data_to_image(image_settings)
            elif schema_name in graph_json_node.keys():
                converted_checked_value = GraphMerger.convert_according_type(
                    graph_json_node[schema_name], schema_type
                )
                node_content[schema_name] = converted_checked_value
            else:
                if "." in schema_name:
                    schema_names = schema_name.split(".")
                    iterator_variable = graph_json_node
                    for actual_name in schema_names:
                        iterator_variable = iterator_variable[actual_name]
                    converted_checked_value = GraphMerger.convert_according_type(
                        iterator_variable, schema_type
                    )
                    node_content[schema_name] = converted_checked_value
                else:
                    node_content[schema_name] = None
        node_content.update({"id": node_id})
        RelatedDataManager.process_instance_related_data(
            instance_related_data, node_content, merge_configuration
        )
        return node_content

    @staticmethod
    def create_connection(
        result_node1: dict,
        result_node2: dict,
        node_identifier_key: str = "id",
        connection_name1: str = "from",
        connection_name2: str = "to",
    ) -> dict:
        return {
            connection_name1: result_node1[node_identifier_key],
            connection_name2: result_node2[node_identifier_key],
        }

    @staticmethod
    def process_node(
        graph_json_node: dict,
        tree_graph_json: Optional[dict],
        node_number_id: dict,
        connections: list[dict],
        result_nodes: dict,
        graph_schema: dict,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        merge_configuration: Optional[MergeConfiguration] = None,
        with_graphic_nodes: bool = False,
        instance_related_data: dict = None,
    ) -> Optional[dict]:
        node_type = graph_json_node[connector_type_name]
        if node_type not in result_nodes.keys() or not result_nodes[node_type]:
            result_nodes[node_type] = []
        if tree_graph_json is not None:
            is_new_node = NodeVerifier.verify_actual_node_as_new_node(
                graph_json_node,
                tree_graph_json,
                graph_schema,
                connector_list_name,
                connector_type_name,
            )
        else:
            is_new_node = True

        # CREATING NEW NODE
        if is_new_node:
            node_content = GraphMerger.copy_node_content_according_schema(
                graph_json_node,
                node_number_id["id"],
                graph_schema[node_type],
                connector_list_name,
                connector_type_name,
                drawing,
                merge_configuration,
                instance_related_data=instance_related_data,
            )
            if (
                node_content[connector_type_name]
                not in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING
            ):
                node_number_id["id"] = node_number_id["id"] + 1
                node_content1 = node_content  # json.loads(json.dumps(node_content))
                if "pointTo" in node_content1.keys():
                    del node_content1["pointTo"]
                result_nodes[node_type].append(node_content1)

            if connector_list_name in graph_json_node.keys():
                for connected_node in graph_json_node[connector_list_name]:
                    neighbour_node = GraphMerger.process_node(
                        connected_node,
                        None,
                        node_number_id,
                        connections,
                        result_nodes,
                        graph_schema,
                        connector_list_name,
                        connector_type_name,
                        drawing,
                        merge_configuration,
                        instance_related_data=instance_related_data,
                    )
                    if connector_list_name not in node_content.keys():
                        node_content[connector_list_name] = []

                    if "id" not in connected_node.keys():
                        connected_node["id"] = neighbour_node["id"]
                    # neighbour_node ~ connected_node, but neighbour is already created
                    if neighbour_node is not None and (
                        with_graphic_nodes
                        or neighbour_node[connector_type_name]
                        not in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING
                    ):
                        node_content[connector_list_name].append(neighbour_node)
                        connections.append(
                            GraphMerger.create_connection(node_content, neighbour_node)
                        )

            MergeAggregations.mark_the_original_method(node_content)
            return node_content
        # SEARCHING OLD ONES
        else:
            node_content = tree_graph_json
            RelatedDataManager.process_instance_related_data(
                instance_related_data, tree_graph_json, merge_configuration
            )

            if connector_list_name in graph_json_node.keys():
                # FOR EACH NODE OF NEW TREE - FIND NODE IN OLD TREE OR START NEW ONE
                for connected_node in graph_json_node[connector_list_name]:
                    found = False
                    for connected_node_tree in tree_graph_json[connector_list_name]:
                        if NodeVerifier.verify_actual_objects_according_schema(
                            connected_node,
                            connected_node_tree,
                            graph_schema,
                            connector_type_name,
                        ):
                            neighbour_node = GraphMerger.process_node(
                                connected_node,
                                connected_node_tree,
                                node_number_id,
                                connections,
                                result_nodes,
                                graph_schema,
                                connector_list_name,
                                connector_type_name,
                                drawing,
                                merge_configuration,
                                with_graphic_nodes,
                                instance_related_data=instance_related_data,
                            )
                            # if "id" not in connected_node.keys():
                            #    connected_node["id"] = neighbour_node["id"]
                            if neighbour_node is not None and (
                                with_graphic_nodes
                                or neighbour_node[connector_type_name]
                                not in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING
                            ):
                                if neighbour_node["id"] != connected_node_tree["id"]:
                                    tree_graph_json[connector_list_name].append(
                                        neighbour_node
                                    )
                                    connections.append(
                                        GraphMerger.create_connection(
                                            connected_node_tree, neighbour_node
                                        )
                                    )
                            # ALREADY CONNECTED
                            found = True
                            break

                    # CREATE NEW CONNECTION - UNCONNECTED SHAPES TO FRACTAL INTO ARRAY
                    if not found:
                        neighbour_node = GraphMerger.process_node(
                            connected_node,
                            None,
                            node_number_id,
                            connections,
                            result_nodes,
                            graph_schema,
                            connector_list_name,
                            connector_type_name,
                            drawing,
                            merge_configuration,
                            with_graphic_nodes,
                            instance_related_data=instance_related_data,
                        )
                        if neighbour_node is not None and (
                            with_graphic_nodes
                            or neighbour_node[connector_type_name]
                            not in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING
                        ):
                            if neighbour_node["id"] != tree_graph_json["id"]:
                                tree_graph_json[connector_list_name].append(
                                    neighbour_node
                                )
                                connections.append(
                                    GraphMerger.create_connection(
                                        tree_graph_json, neighbour_node
                                    )
                                )

            MergeAggregations.mark_the_same_method(node_content)
        return node_content

    @staticmethod
    def get_nodes_and_connectors_from_graph_in_JSON(
        graph_json: dict,
        tree_graph_json: Optional[dict],
        used_schema: dict,
        connections: list[dict],
        node_number_id: dict,
        result_nodes: dict,
        init_node: dict,
        connectors_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        with_graphic_nodes: bool = False,
        merge_configuration: Optional[MergeConfiguration] = None,
        instance_related_data: dict = None,
    ) -> list[dict]:
        # without checking which node is correct
        nodes_to_iterate = graph_json[connectors_list_name]
        if tree_graph_json:
            nodes_to_iterate_tree = tree_graph_json[connectors_list_name]
        else:
            nodes_to_iterate_tree = nodes_to_iterate
        newly_added_nodes = []
        if connectors_list_name in graph_json:
            for index, graph_json_node in enumerate(nodes_to_iterate):
                if tree_graph_json:
                    found = False
                    for connected_node_tree in tree_graph_json[connectors_list_name]:
                        if NodeVerifier.verify_actual_objects_according_schema(
                            graph_json_node,
                            connected_node_tree,
                            used_schema[graph_json_node[connector_type_name]],
                            connector_type_name,
                        ):
                            neighbour_node = GraphMerger.process_node(
                                graph_json_node,
                                connected_node_tree,
                                node_number_id,
                                connections,
                                result_nodes,
                                used_schema,
                                connectors_list_name,
                                connector_type_name,
                                drawing,
                                merge_configuration,
                                with_graphic_nodes,
                                instance_related_data,
                            )

                            found = True
                            break
                    if not found:
                        neighbour_node = GraphMerger.process_node(
                            graph_json_node,
                            None,
                            node_number_id,
                            connections,
                            result_nodes,
                            used_schema,
                            connectors_list_name,
                            connector_type_name,
                            drawing,
                            merge_configuration,
                            with_graphic_nodes,
                            instance_related_data,
                        )
                        if "id" not in graph_json_node.keys():
                            graph_json_node["id"] = neighbour_node["id"]

                        if neighbour_node is not None and (
                            with_graphic_nodes
                            or graph_json_node[connector_type_name]
                            not in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING
                        ):
                            tree_graph_json[connectors_list_name].append(neighbour_node)
                            connections.append(
                                GraphMerger.create_connection(init_node, neighbour_node)
                            )
                else:
                    graph_json_node["id"] = node_number_id["id"]
                    node_number_id["id"] = node_number_id["id"] + 1
                    neighbour_node = GraphMerger.process_node(
                        graph_json_node,
                        None,
                        node_number_id,
                        connections,
                        result_nodes,
                        used_schema,
                        connectors_list_name,
                        connector_type_name,
                        drawing,
                        merge_configuration,
                        with_graphic_nodes,
                        instance_related_data,
                    )
                    newly_added_nodes.append(neighbour_node)
                    if neighbour_node is not None and (
                        with_graphic_nodes
                        or graph_json_node[connector_type_name]
                        not in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING
                    ):
                        connections.append(
                            GraphMerger.create_connection(init_node, neighbour_node)
                        )
        if tree_graph_json is None:
            graph_json[connectors_list_name] = (
                graph_json[connectors_list_name] + newly_added_nodes
            )

        return nodes_to_iterate_tree

    @staticmethod
    def create_csv_from_results(
        file_csv_path: str, data: list[dict], field_names: list[str]
    ) -> None:
        with open(file_csv_path, "w", encoding="UTF8", newline="") as f:
            writer = csv.DictWriter(f, fieldnames=field_names, delimiter="$")
            writer.writeheader()
            writer.writerows(data)

    @staticmethod
    def merge_dataset(
        dataset_directory_path: str,
        final_location_path: str = "../../generated_dataset_vp_graph_data_merged",
        graph_schema: dict = None,
        connectors_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        with_graphic_nodes: bool = False,
        merge_configuration: Optional[MergeConfiguration] = None,
        instances_related_data: dict = None,
    ) -> None:
        dynamic_fractal_analyzer = DynamicFractalAnalyzer()
        if not graph_schema:
            graph_schema = FractalGraphScheme.create_default_schemes(drawing)
        absolute_dataset_path = os.path.abspath(dataset_directory_path)
        absolute_final_path = os.path.abspath(final_location_path)
        absolute_connection_file_path = os.path.join(
            absolute_final_path, "connections.csv"
        )
        connections = []
        node_number_id = {"id": 1}
        result_nodes = {}
        global_root = None
        init_node = {"id": 0}

        # PROCESS ALL DERIVATIONS INTO ONE TREE
        for index, derivation_name in enumerate(os.listdir(absolute_dataset_path)):
            print("Processing derivation: " + str(index))
            if instances_related_data is not None:
                instance_related_data_class = instances_related_data[
                    derivation_name + ".png"
                ]
            else:
                instance_related_data_class = None
            project_path = os.path.join(absolute_dataset_path, derivation_name)
            script_path = os.path.join(project_path, "js/platnoJS.js")
            graph_root = json.loads(
                dynamic_fractal_analyzer.load_data_from_fractal(
                    script_path, "initialGraphRoot"
                )
            )
            GraphMerger.get_nodes_and_connectors_from_graph_in_JSON(
                graph_root,
                global_root,
                graph_schema,
                connections,
                node_number_id,
                result_nodes,
                init_node,
                connectors_list_name,
                connector_type_name,
                drawing,
                with_graphic_nodes,
                merge_configuration=merge_configuration,
                instance_related_data=instance_related_data_class,
            )
            if global_root is None:  # Get first instance as tree reference - root
                global_root = graph_root
        result_nodes[list(result_nodes.keys())[0]].append(init_node)

        all_keys_dict = {}
        for node_type_name, result_nodes_type in result_nodes.items():
            for result_node_type_part in result_nodes_type:
                for name in result_node_type_part.keys():
                    if name != "pointsTo":
                        all_keys_dict[name] = name
        for node_type_name, result_nodes_type in result_nodes.items():
            node_type_absolute_path = os.path.join(
                absolute_final_path, node_type_name + ".csv"
            )
            if (
                len(result_nodes_type) > 0
            ):  # create file only if will contain any data/ entities
                for i, result_type in enumerate(result_nodes_type):
                    if "pointsTo" in result_type.keys():
                        del result_nodes_type[i]["pointsTo"]
                GraphMerger.create_csv_from_results(
                    node_type_absolute_path,
                    result_nodes_type,
                    list(all_keys_dict.values()),
                )
        GraphMerger.create_csv_from_results(
            absolute_connection_file_path, connections, list(connections[0].keys())
        )

    @staticmethod
    def load_instance_related_data(
        path_to_object_csv: str, delimiter: str = ";"
    ) -> dict:
        whole_data = {}
        with open(path_to_object_csv, "r", encoding="utf-8-sig") as file:
            new_objects = csv.DictReader(file, delimiter=delimiter)
            for row in new_objects:
                whole_data[row["Name"]] = row
        return whole_data


if __name__ == "__main__":
    instance_related_data = GraphMerger.load_instance_related_data("./annotated.csv")
    merge_configuration = MergeConfiguration(StrategyOnIntersection.CONCAT, None)
    GraphMerger.merge_dataset(
        "E://aspects/src/derived2",
        with_graphic_nodes=False,
        merge_configuration=merge_configuration,
        instances_related_data=instance_related_data,
    )
