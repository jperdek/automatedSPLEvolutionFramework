import os
import shutil
from typing import Optional

import requests
import logging


class DataRepresentationsClient:

    @staticmethod
    def get_destination_dataset_path(evolution_iteration: str, project_unique_id: str) -> str:
        destination_dataset_path = os.getenv("PATH_TO_DATASET_DIRECTORY", "../../temp") + "/evolution" + str(
            evolution_iteration) + "/" + project_unique_id
        if not os.path.exists(destination_dataset_path):
            os.makedirs(destination_dataset_path)
        return destination_dataset_path

    @staticmethod
    def get_server_path() -> str:
        return "http://" + os.getenv("DATA_COLLECTOR_ADDRESS", "localhost") + ":" + os.getenv("DATA_COLLECTOR_PORT", "5000")

    @staticmethod
    def copy_whole_project(evolved_spl_path: str, resulting_spl_project_path: str,
                           logger: Optional[logging.Logger] = None) -> Optional[str]:
        if not os.path.exists(resulting_spl_project_path + "/code"):
            os.makedirs(resulting_spl_project_path, exist_ok=True)
        code_path = resulting_spl_project_path + "/code"
        try:
            shutil.copytree(evolved_spl_path, resulting_spl_project_path + "/code", dirs_exist_ok=True)
        except OSError as error:
            if logger:
                logger.debug("Copying project failed: " + str(error))
            code_path = None
        return code_path

    @staticmethod
    def get_and_save_screenshot(evolved_spl_index_path: str, resulting_spl_project_path: str,
                                logger: Optional[logging.Logger] = None) -> Optional[str]:
        service_url = DataRepresentationsClient.get_server_path() + "/api/screenshoter/screenshoter?url=file:///" + evolved_spl_index_path
        response = requests.get(service_url)

        screenshot_path = None
        if response.status_code == 200:
            screenshot = response.content
            screenshot_path = resulting_spl_project_path + "/screenshot.png"
            with open(screenshot_path, "wb") as file:
                file.write(screenshot)
        else:
            if logger:
                logger.debug(
                    "Error :-> Saving raster screenshot :-> Request: " + service_url + " failed with status code: " + str(
                        response.status_code))
        return screenshot_path

    @staticmethod
    def get_and_save_graph_data(evolved_spl_index_path: str, resulting_spl_project_path: str,
                                logger: Optional[logging.Logger] = None) -> Optional[str]:
        service_url = DataRepresentationsClient.get_server_path() + "/api/graph-extraction/extractObject?max_depth=70&url=file://" + evolved_spl_index_path
        response = requests.get(service_url)

        json_graph_path = None
        if response.status_code == 200:
            spl_graph = response.text
            json_graph_path = resulting_spl_project_path + "/spl_graph.json"
            with open(json_graph_path, "w", encoding="utf-8") as file:
                file.write(spl_graph)
        else:
            if logger:
                logger.debug(
                    "Error :-> Saving single SPL instance graph :-> Request: " + service_url + " failed with status code: " + str(
                        response.status_code))
        return json_graph_path

    @staticmethod
    def get_and_save_svg_data(evolved_spl_index_path: str, resulting_spl_project_path: str,
                              logger: Optional[logging.Logger] = None) -> Optional[str]:
        service_url = DataRepresentationsClient.get_server_path() + "/api/svg_creator/create_svg?time_to_wait=10&generated_svg_id=mySvg&draw_line_in_op=drawLine&replacement_function=function(context, x1, y1, x2, y2, thickness)&url=file://" + evolved_spl_index_path
        response = requests.get(service_url)

        vector_path = None
        if response.status_code == 200:
            spl_raster = response.text
            vector_path = resulting_spl_project_path + "/spl_raster.svg"
            with open(vector_path, "w", encoding="utf-8") as file:
                file.write(spl_raster)
        else:
            if logger:
                logger.debug("Error :-> Saving SVG :-> Request: " + service_url + " failed with status code: " + str(
                    response.status_code))
        return vector_path

    @staticmethod
    def save_knowledge_after_iteration(
            evolution_id: str, evolution_iteration: str, evolved_product_line_id: str, evolved_script_path: str,
            code_path: str, screenshot_path: str, vector_path: str, json_graph_path: str,
            variation_point_data_location: str, previous_product_line_id: str,
            logger: Optional[logging.Logger] = None) -> None:
        service_url = DataRepresentationsClient.get_server_path() + "/api/knowledgeBase/registerNewEvolutionIteration"
        request_data = {"evolution_id": evolution_id, "evolution_iteration": evolution_iteration,
                        "code_path": "file:///" + code_path, "screenshot_path": "file:///" + screenshot_path,
                        "vector_path": "file:///" + vector_path, "json_graph_path": "file:///" + json_graph_path,
                        "evolved_script_path": "file:///" + evolved_script_path,
                        "variation_point_data_location": "file:///" + variation_point_data_location,
                        "evolved_product_line_id": evolved_product_line_id,
                        "previous_product_line_id": previous_product_line_id}
        headers = {"Content-Type": "text/plain"}
        response = requests.post(service_url, data=request_data, headers=headers)

        if response.status_code == 200:
            pass
        else:
            if logger:
                logger.debug("Error :-> Incorporating software product line knowledge :-> Request: " +
                             service_url + " failed with status code: " + str(response.status_code))

    @staticmethod
    def create_all_representations(evolution_id: str, evolution_iteration: str, evolved_product_line_id: str,
                                   evolved_script_path: str, evolved_spl_path: str,
                                   project_id: str, variation_point_data_location: str, previous_product_line_id: str,
                                   logger: Optional[logging.Logger] = None) -> str:
        destination_spl_project_path = DataRepresentationsClient.get_destination_dataset_path(
            evolution_iteration, project_id)
        evolved_spl_path = evolved_spl_path.strip().replace("&#39;", "").strip("'")
        destination_spl_project_path = destination_spl_project_path.strip().replace("&#39;", "").strip("'")
        code_path = DataRepresentationsClient.copy_whole_project(
            evolved_spl_path, destination_spl_project_path, logger)
        screenshot_path = DataRepresentationsClient.get_and_save_screenshot(
            evolved_spl_path + "/index.html", destination_spl_project_path, logger)
        vector_path = DataRepresentationsClient.get_and_save_svg_data(
            evolved_spl_path + "/index.html", destination_spl_project_path, logger)
        json_graph_path = DataRepresentationsClient.get_and_save_graph_data(
            evolved_spl_path + "/index.html", destination_spl_project_path, logger)
        if os.getenv("BUILD_DEFAULT_KNOWLEDGE_BASE", False):
            DataRepresentationsClient.save_knowledge_after_iteration(
                evolution_id=evolution_id, evolution_iteration=evolution_iteration,
                evolved_product_line_id=evolved_product_line_id, evolved_script_path=evolved_script_path,
                code_path=code_path, vector_path=vector_path, screenshot_path=screenshot_path,
                json_graph_path=json_graph_path, variation_point_data_location=variation_point_data_location,
                previous_product_line_id=previous_product_line_id, logger=logger)
        return destination_spl_project_path

    @staticmethod
    def test(logger: Optional[logging.Logger] = None):
        evolved_spl_path = "E:\\aspects\\automatedSPLEvolutionFramework\\FractalDynamicDataCollector\\example\\appcustomevolNum2_2_ecaf78687a1da38_XXX_6"
        evolved_spl_script_path = evolved_spl_path + "/js/platnoJS.js"
        project_id = "my_random_id"
        evolution_iteration = str(-1)

        DataRepresentationsClient.create_all_representations(evolved_spl_path, evolved_spl_script_path,
                                                             evolution_iteration, project_id, logger)


if __name__ == "__main__":
    DataRepresentationsClient.test(logging.getLogger(__name__))
