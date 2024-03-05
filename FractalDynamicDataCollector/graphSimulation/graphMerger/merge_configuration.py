from enum import Enum
from typing import Optional


class StrategyOnIntersection(Enum):
    OVERWRITE = 1
    CONCAT = 2
    NOTHING = 3


class ImageSettings:
    def __init__(
        self,
        max_image_dimensions: (int, int) = (600, 600),
        result_dimensions: (int, int) = (200, 200),
        trim: bool = True,
        color_to_strip=None,
        trim_threshold: int = 55,
    ) -> None:
        if color_to_strip is None:
            color_to_strip = [255, 255, 255]
        self.max_image_dimensions = max_image_dimensions
        self.result_dimensions = result_dimensions
        self.trim = trim
        self.color_to_strip = color_to_strip
        self.trim_threshold = trim_threshold
        self.associated_objects = []
        self.connector_type_name = []


class MergeConfiguration:
    def __init__(
        self,
        strategy_on_intersection: Optional[StrategyOnIntersection],
        image_settings: Optional[ImageSettings],
    ) -> None:
        self.strategy_on_intersection = (
            StrategyOnIntersection.NOTHING
            if not strategy_on_intersection
            else strategy_on_intersection
        )
        self.image_settings = ImageSettings() if not image_settings else image_settings
