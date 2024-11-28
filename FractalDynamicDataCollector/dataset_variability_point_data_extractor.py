from server.apis.http.api.processors.analyzer import DynamicFractalAnalyzer
import os


class DatasetVariabilityPointDataExtractor:
    def __init__(self) -> None:
        self.dynamic_fractal_analyzer = DynamicFractalAnalyzer()

    def process_dataset(
        self,
        dataset_directory_path: str,
        final_location_path: str = "./generated_dataset_vp_data",
        is_wrapped: bool = False,
    ) -> None:
        absolute_dataset_path = os.path.abspath(dataset_directory_path)
        absolute_final_path = os.path.abspath(final_location_path)

        for derivation_name in os.listdir(absolute_dataset_path):
            project_path = os.path.join(absolute_dataset_path, derivation_name)
            if os.path.isdir(project_path):
                script_path = os.path.join(project_path, "js/platnoJS.js")
                vp_data_file_name = derivation_name + ".json"
                final_image_path = os.path.join(absolute_final_path, vp_data_file_name)
                variability_point_data = self.dynamic_fractal_analyzer.load_fractal(
                    script_path, is_wrapped=is_wrapped
                )
                self.__save_json_data_to_file(variability_point_data, final_image_path)

    def __save_json_data_to_file(
        self, variability_point_data: str, final_image_path: str
    ) -> None:
        with open(final_image_path, "w", encoding="utf-8") as file:
            file.write(variability_point_data)


if __name__ == "__main__":
    dataset_variability_point_data_extractor = DatasetVariabilityPointDataExtractor()
    dataset_variability_point_data_extractor.process_dataset(
        "E:/aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/evolutionDirectory/evolNum1/conccustom",
        final_location_path="./generated_dataset_vp_data",
        is_wrapped=True,
    )
