from server.apis.http.api.graphSimulation.graphExtractor.geometry_data_to_image import GeometryDataToImage
from typing import Optional
import json


class NodeVerifier:
    SKIP_VERIFICATION = ["image_base64_url", "pointsTo"]
    DISAGREE_ON_VERIFICATION = []

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
    def verify_actual_objects_according_schema(
        graph_json_node: dict,
        graph_tree_json_node: dict,
        schema_for_node: dict,
        connector_type_name: str = "fname",
    ):
        if (
            graph_json_node[connector_type_name]
            != graph_tree_json_node[connector_type_name]
        ):
            return False

        for schema_name, schema_type in schema_for_node.items():
            if (
                schema_name == connector_type_name
                or schema_name in NodeVerifier.DISAGREE_ON_VERIFICATION
            ):
                return False
            if schema_name == "id" or schema_name in NodeVerifier.SKIP_VERIFICATION:
                continue  # id is omitted from the check
            elif schema_name in graph_json_node.keys():
                converted_checked_value1 = NodeVerifier.convert_according_type(
                    graph_json_node[schema_name], schema_type
                )

                converted_checked_value2 = NodeVerifier.convert_according_type(
                    graph_tree_json_node[schema_name], schema_type
                )
                if converted_checked_value1 != converted_checked_value2:
                    return False
            else:
                if "." in schema_name:
                    schema_names = schema_name.split(".")
                    iterator_variable1 = graph_json_node
                    skip1 = False
                    for actual_name1 in schema_names:
                        if actual_name1 not in iterator_variable1.keys():
                            skip1 = False
                            break
                        iterator_variable1 = iterator_variable1[actual_name1]

                    if skip1:
                        converted_checked_value1 = None
                    else:
                        try:
                            converted_checked_value1 = (
                                NodeVerifier.convert_according_type(
                                    iterator_variable1, schema_type
                                )
                            )
                        except Exception:
                            converted_checked_value1 = (
                                NodeVerifier.convert_according_type(
                                    graph_tree_json_node[schema_name], schema_type
                                )
                            )
                    iterator_variable2 = graph_tree_json_node
                    skip = False
                    for actual_name2 in schema_names:
                        if skip1 and actual_name2 not in iterator_variable2.keys():
                            return False
                        elif actual_name2 not in iterator_variable2.keys():
                            continue
                        iterator_variable2 = iterator_variable2[actual_name2]

                    if skip:
                        converted_checked_value2 = None
                    else:
                        try:
                            converted_checked_value2 = (
                                NodeVerifier.convert_according_type(
                                    iterator_variable2, schema_type
                                )
                            )
                        except Exception:
                            converted_checked_value2 = (
                                NodeVerifier.convert_according_type(
                                    graph_tree_json_node[schema_name], schema_type
                                )
                            )
                    if converted_checked_value1 != converted_checked_value2:
                        return False
                else:
                    continue  # checking should be according schema only
        return True

    @staticmethod
    def verify_neighbour_graphic_nodes(
        graph_json_node: dict,
        tree_graph_json: Optional[dict],
        graph_schema: dict,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
    ):
        if connector_list_name in graph_json_node.keys():
            for connected_node in graph_json_node[connector_list_name]:
                graphic_object_name = graph_json_node[connector_type_name]
                graphic_object_schema = graph_schema[graphic_object_name]
                if (
                    graphic_object_name
                    in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING.keys()
                ):
                    for connected_node_tree in tree_graph_json[connector_list_name]:
                        if (
                            tree_graph_json[connector_list_name]
                            in GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING.keys()
                        ):
                            if not NodeVerifier.verify_actual_objects_according_schema(
                                connected_node,
                                connected_node_tree,
                                graphic_object_schema,
                            ):
                                return False
        return True

    @staticmethod
    def verify_actual_node_as_new_node(
        graph_json_node: dict,
        tree_graph_json: Optional[dict],
        graph_schema: dict,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
    ) -> bool:
        if (
            tree_graph_json is None
        ):  # or graph_json_node[connector_type_name] != tree_graph_json[connector_type_name]:
            return True
        schema_for_node = graph_schema[graph_json_node[connector_type_name]]
        if not NodeVerifier.verify_actual_objects_according_schema(
            graph_json_node, tree_graph_json, schema_for_node
        ):
            return True
        if not NodeVerifier.verify_neighbour_graphic_nodes(
            graph_json_node,
            tree_graph_json,
            graph_schema,
            connector_list_name,
            connector_type_name,
        ):
            return True
        return False
