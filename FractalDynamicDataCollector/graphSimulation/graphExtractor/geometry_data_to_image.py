from typing import Optional

from graphSimulation.graphMerger.merge_configuration import ImageSettings
from image_processor import ImageProcessor
from PIL import Image, ImageDraw
import base64
import io


class GeometryDataToImage:
    FUNCTION_NAMES_TO_GENERATORS_MAPPING = {
        "function drawLine": "drawLine"
    }

    @staticmethod
    def convert_to_base64_image_string_url(image_to_save: Image) -> str:
        image_byte_arr = io.BytesIO()
        image_to_save.save(image_byte_arr, format="PNG")
        image_byte_arr = image_byte_arr.getvalue()
        return "data:image/png;base64," + base64.b64encode(image_byte_arr).decode("utf-8")

    @staticmethod
    def draw_geometry_operation(draw: ImageDraw, operation_name: str, associated_object: dict) -> bool:
        if operation_name == "drawLine":
            color = associated_object["color"] if "color" in associated_object.keys() else "black"
            draw.line([associated_object["x1"], associated_object["y1"],
                       associated_object["x2"], associated_object["y2"]], color)
            return True
        else:
            print("Unknown operation: " + operation_name)

    @staticmethod
    def put_geometry_data_to_image(image_settings: Optional[ImageSettings] = None,
                                   connector_type_name: str = "fname",
                                   function_names_to_generators_mapping=None,
                                   ) -> str:
        if function_names_to_generators_mapping is None:
            function_names_to_generators_mapping = GeometryDataToImage.FUNCTION_NAMES_TO_GENERATORS_MAPPING

        image1 = Image.new("RGB", image_settings.max_image_dimensions, tuple(image_settings.color_to_strip))
        draw = ImageDraw.Draw(image1)

        something_has_been_drawn = False
        for associated_object in image_settings.associated_objects:
            if connector_type_name in associated_object.keys() and \
                    associated_object[connector_type_name] in function_names_to_generators_mapping.keys():
                operation_name = function_names_to_generators_mapping[associated_object[connector_type_name]]
                if GeometryDataToImage.draw_geometry_operation(draw, operation_name, associated_object):
                    something_has_been_drawn = True
        if something_has_been_drawn:
            if image_settings.trim:
                image_byte_arr = io.BytesIO()
                image1.save(image_byte_arr, format="PNG")
                image_byte_arr = image_byte_arr.getvalue()
                image1_bytes = ImageProcessor.trim_image_according_observed_boundaries(image_byte_arr,
                                                                                       access="random",
                                                                                       cropped_borders=0,
                                                                                       added_borders=5,
                                                                                       color_to_trip=
                                                                                       image_settings.color_to_strip,
                                                                                       threshold=
                                                                                       image_settings.trim_threshold,
                                                                                       additionally_increase_with=50,
                                                                                       square=True)
                image_file = io.BytesIO(image1_bytes)
                image1 = Image.open(image_file)
                image1 = image1.convert("RGB")
            image1 = image1.resize(image_settings.result_dimensions)
            return GeometryDataToImage.convert_to_base64_image_string_url(image1)
        return ""
