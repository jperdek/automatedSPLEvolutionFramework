from typing import Optional, List, Dict

from graphSimulation.graphMerger.merge_configuration import ImageSettings
from processors.image_processor import ImageProcessor
from PIL import Image, ImageDraw
import base64
import io


class GeometryDataToImage:
    FUNCTION_NAMES_TO_GENERATORS_MAPPING = {
        "function drawLine": "drawLine",
        "function  FiveSideFractal": "drawFiveSide",
    }

    @staticmethod
    def convert_to_base64_image_string_url(image_to_save: Image) -> str:
        image_byte_arr = io.BytesIO()
        image_to_save.save(image_byte_arr, format="PNG")
        image_byte_arr = image_byte_arr.getvalue()
        return "data:image/png;base64," + base64.b64encode(image_byte_arr).decode(
            "utf-8"
        )

    @staticmethod
    def __check_consistency_of_keys_in_object(
        associated_object: Dict, key_list: List[str]
    ) -> bool:
        for key in key_list:
            if key not in associated_object.keys():
                return False
        return True

    @staticmethod
    def draw_geometry_operation(
        draw: ImageDraw,
        operation_name: str,
        associated_object: Dict,
        thickness: int = 2,
    ) -> bool:
        if operation_name == "drawFiveSide":
            if not GeometryDataToImage.__check_consistency_of_keys_in_object(
                associated_object,
                ["x1", "y1", "x2", "y2", "x3", "y3", "x4", "y4", "x5", "y5"],
            ):
                return False
            color = (
                associated_object["color"]
                if "color" in associated_object.keys()
                else "black"
            )
            draw.line(
                [
                    associated_object["x1"],
                    associated_object["y1"],
                    associated_object["x2"],
                    associated_object["y2"],
                ],
                color,
                width=thickness,
            )
            draw.line(
                [
                    associated_object["x2"],
                    associated_object["y2"],
                    associated_object["x3"],
                    associated_object["y3"],
                ],
                color,
                width=thickness,
            )
            draw.line(
                [
                    associated_object["x3"],
                    associated_object["y3"],
                    associated_object["x4"],
                    associated_object["y4"],
                ],
                color,
                width=thickness,
            )
            draw.line(
                [
                    associated_object["x4"],
                    associated_object["y4"],
                    associated_object["x5"],
                    associated_object["y5"],
                ],
                color,
                width=thickness,
            )
            draw.line(
                [
                    associated_object["x5"],
                    associated_object["y5"],
                    associated_object["x1"],
                    associated_object["y1"],
                ],
                color,
                width=thickness,
            )
            return True
        elif operation_name == "drawLine":
            if not GeometryDataToImage.__check_consistency_of_keys_in_object(
                associated_object, ["x1", "y1", "x2", "y2"]
            ):
                return False
            color = (
                associated_object["color"]
                if "color" in associated_object.keys()
                else "black"
            )
            draw.line(
                [
                    associated_object["x1"],
                    associated_object["y1"],
                    associated_object["x2"],
                    associated_object["y2"],
                ],
                color,
                width=thickness,
            )
            return True
        else:
            print("Unknown operation: " + operation_name)
        return False

    @staticmethod
    def __is_drawwing_command(
        checked_object: Dict,
        draw: ImageDraw,
        function_names_to_generators_mapping: any,
        connector_type_name: str = "fname",
        thickness: int = 2,
    ):
        if (
            connector_type_name in checked_object.keys()
            and checked_object[connector_type_name]
            in function_names_to_generators_mapping.keys()
        ):
            operation_name = function_names_to_generators_mapping[
                checked_object[connector_type_name]
            ]
            if GeometryDataToImage.draw_geometry_operation(
                draw, operation_name, checked_object, thickness=thickness
            ):
                return True
        return False

    @staticmethod
    def put_geometry_data_to_image(
        graph_json_node: Dict,
        image_settings: Optional[ImageSettings] = None,
        connector_type_name: str = "fname",
        function_names_to_generators_mapping=None,
    ) -> str:
        if function_names_to_generators_mapping is None:
            function_names_to_generators_mapping = (
                GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING
            )
        image1 = Image.new(
            "RGB",
            image_settings.max_image_dimensions,
            tuple(image_settings.color_to_strip),
        )
        draw = ImageDraw.Draw(image1)

        something_has_been_drawn = False
        if GeometryDataToImage.__is_drawwing_command(
            graph_json_node,
            draw,
            function_names_to_generators_mapping,
            connector_type_name,
        ):
            something_has_been_drawn = True
        for associated_object in image_settings.associated_objects:
            if GeometryDataToImage.__is_drawwing_command(
                associated_object,
                draw,
                function_names_to_generators_mapping,
                connector_type_name,
                thickness=image_settings.line_thickness,
            ):
                something_has_been_drawn = True
        if something_has_been_drawn:
            if image_settings.trim:
                image_byte_arr = io.BytesIO()
                image1.save(image_byte_arr, format="PNG")
                image_byte_arr = image_byte_arr.getvalue()
                image1_bytes = ImageProcessor.trim_image_according_observed_boundaries(
                    image_byte_arr,
                    access="random",
                    cropped_borders=0,
                    added_borders=5,
                    color_to_trip=image_settings.color_to_strip,
                    threshold=image_settings.trim_threshold,
                    additionally_increase_with=50,
                    square=True,
                )
                image_file = io.BytesIO(image1_bytes)
                image1 = Image.open(image_file)
                image1 = image1.convert("RGB")
            image1 = image1.resize(image_settings.result_dimensions)
            return GeometryDataToImage.convert_to_base64_image_string_url(image1)
        return ""
