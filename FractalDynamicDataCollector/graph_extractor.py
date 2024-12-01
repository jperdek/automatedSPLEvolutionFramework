from typing import Optional, Dict

from server.apis.http.api.graphSimulation.graphExtractor.graph_scheme import FractalGraphScheme
from server.apis.http.api.graphSimulation.graphExtractor.process_graphs import GraphProcessor
from server.apis.http.api.processors.analyzer import DynamicFractalAnalyzer
import json
import os


class DatasetVariabilityPointGraphDataExtractor:
    def __init__(self) -> None:
        self.dynamic_fractal_analyzer = DynamicFractalAnalyzer()

    def process_dataset(
        self,
        dataset_directory_path: str,
        final_location_path: str = "./generated_dataset_vp_graph_data",
        graph_schema: Optional[Dict] = None,
        connector_list_name: str = "pointsTo",
        connector_type_name: str = "fname",
        drawing: bool = True,
        image_settings: Optional[Dict] = None,
        variable_with_graph: str = "initialGraphRoot",
        file_with_connections_name: str = "connections.csv",
        is_wrapped: bool = False,
    ) -> None:
        if not graph_schema:
            if drawing:
                graph_schema = FractalGraphScheme.create_default_schemes_with_image()
            else:
                graph_schema = FractalGraphScheme.create_default_schemes()
        absolute_dataset_path = os.path.abspath(dataset_directory_path)
        absolute_final_path = os.path.abspath(final_location_path)

        for derivation_name in os.listdir(absolute_dataset_path):
            project_path = os.path.join(absolute_dataset_path, derivation_name)
            script_path = os.path.join(project_path, "js/platnoJS.js")
            graph_root = json.loads(
                self.dynamic_fractal_analyzer.load_data_from_fractal(
                    script_path,
                    variable_with_graph,
                    is_wrapped=is_wrapped
                )
            )
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
            )


if __name__ == "__main__":
    dataset_variability_point_data_extractor = (
        DatasetVariabilityPointGraphDataExtractor()
    )
    dataset_variability_point_data_extractor.process_dataset(
        "E:/aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/evolutionDirectory/evolNum1/conccustom",
        "./generated_dataset_vp_graph_data",
        is_typescript=True,
        is_wrapped=True,
    )
