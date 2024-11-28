import json

from graphSimulation.graphExtractor.process_graphs import GraphProcessor
from graphSimulation.graphMerger.merge_configuration import ImageSettings
from server.apis.http.api.screenshoting.screenshooter import PlaywrightScreenshooter
from server.apis.http.api.processors.analyzer import DynamicFractalAnalyzer
from typing import Dict, Optional
import os


class DatasetVariabilityPointGraphDataExtractor:
    def __init__(self) -> None:
        self.dynamic_fractal_analyzer = DynamicFractalAnalyzer()
        self.screenshooter = PlaywrightScreenshooter("chromium")
        self.page = self.screenshooter.new_page()

    def __get_hierarchy_of_called_instances(self) -> Dict:
        ordered_calls = self.page.evaluate("() => JSON.stringify(harvestStackData());")

        ordered_calls = json.loads(ordered_calls)
        graph_root = {"pointsTo": ordered_calls[0]["pointsTo"]}
        helper_stack = [graph_root]
        while len(helper_stack) > 0:
            processed_entity = helper_stack.pop()
            points_to_merge_id_indexes = processed_entity["pointsTo"]
            del processed_entity["pointsTo"]
            if "mergeId" in processed_entity.keys():
                del processed_entity["mergeId"]

            processed_entity["pointsTo"] = []
            for child_merge_id in points_to_merge_id_indexes:
                # push child to stack to be later extended
                helper_stack.append(ordered_calls[int(child_merge_id)])
                # extends parent with its child
                processed_entity["pointsTo"].append(ordered_calls[int(child_merge_id)])
        return graph_root

    def process_dataset(
        self,
        dataset_directory_path: str,
        final_location_path: str = "./generated_dataset_vp_graph_data",
        graph_schema: dict = None,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        image_settings: Optional[ImageSettings] = None,
        variable_with_graph: str = "initialGraphRoot",
        file_with_connections_name: str = "connections.csv",
        skip_nodes_used_to_draw_image: bool = True,
        browser_timeout=30000,
    ) -> None:
        absolute_dataset_path = os.path.abspath(dataset_directory_path)
        absolute_final_path = os.path.abspath(final_location_path)

        for derivation_name in os.listdir(absolute_dataset_path):
            project_path = os.path.join(absolute_dataset_path, derivation_name)
            if os.path.isdir(project_path):
                project_page_path = os.path.join(project_path, "index.html")
                self.page.goto(
                    project_page_path, timeout=browser_timeout, wait_until="networkidle"
                )
                self.page.wait_for_timeout(60000)

                graph_root = None
                try:
                    graph_root = self.page.evaluate(
                        "() => Object.entries(" + variable_with_graph + ");"
                    )
                except Exception:
                    pass
                finally:
                    if not graph_root:  # due to deeply nested calls - graphs
                        print(
                            "Cannot get graph data based on instance calls. Trying to build calls without recursion!"
                            "Helper functionality is required for this purpose."
                        )
                        graph_root = self.__get_hierarchy_of_called_instances()
                absolute_derivation_path = os.path.join(
                    absolute_final_path, derivation_name
                )
                os.makedirs(absolute_derivation_path, exist_ok=True)
                absolute_connection_file_path = os.path.join(
                    absolute_derivation_path, file_with_connections_name
                )
                GraphProcessor.process_graph(
                    graph_root,
                    graph_schema,
                    absolute_connection_file_path,
                    absolute_derivation_path,
                    connector_list_name,
                    connector_type_name,
                    drawing,
                    image_settings,
                    skip_nodes_used_to_draw_image,
                )


if __name__ == "__main__":
    dataset_variability_point_data_extractor = (
        DatasetVariabilityPointGraphDataExtractor()
    )
    # sys.setrecursionlimit(500000)
    dataset_variability_point_data_extractor.process_dataset(
        "E:/aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/evolutionDirectory/evolNum1/conccustom",
        "./generated_dataset_vp_graph_data",
        # whole hierarchy under given node will be skipped (if primitive drawing commands are mapped only otherwise
        # should be disabled)
        skip_nodes_used_to_draw_image=False,
        image_settings=ImageSettings(
            (1200, 1200), (400, 400), disable_taking_images=True
        ),
        browser_timeout=180000,
    )
