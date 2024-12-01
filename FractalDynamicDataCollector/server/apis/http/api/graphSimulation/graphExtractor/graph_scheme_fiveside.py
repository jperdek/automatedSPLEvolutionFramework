from typing import Dict

from graphSimulation.graphExtractor.graph_scheme import FractalGraphScheme


class FiveSideGraphScheme:
    @staticmethod
    def add_wcurve_instance_to_scheme(wcurve_scheme: Dict) -> None:
        wcurve_scheme.update(
            {
                "wcurve.diagonalLength": "int",
                "wcurve.distanceWidthRadius": "int",
                "wcurve.lineLength": "int",
                "wcurve.lineLengthHalf": "int",
                "wcurve.moveRatio": "int",
                "wcurve.size": "int",
                "wcurve.thickness": "int",
            }
        )

    @staticmethod
    def create_default_scheme_for_draw_anklet_mod_main() -> Dict:
        wcurve_scheme = {
            "id": "int",
            "centerX": "int",
            "centerY": "int",
            "direction": "int",
            "inheritedOperation": "bool",
            "iteration": "bool",
            "moveRatioIteration": "float",
        }
        FiveSideGraphScheme.add_wcurve_instance_to_scheme(wcurve_scheme)
        return wcurve_scheme

    @staticmethod
    def additionally_add_image_to_scheme(wcurve_scheme: Dict) -> None:
        wcurve_scheme.update({"image_base64_url": "string"})

    @staticmethod
    def create_default_scheme_for_draw_line() -> Dict:
        line_scheme = {
            "id": "int",
            "x1": "float",
            "x2": "float",
            "y1": "float",
            "y2": "float",
            "thickness": "int",
        }
        return line_scheme

    @staticmethod
    def create_default_scheme_for_wcurve() -> Dict:
        wcurve_scheme = {
            "id": "int",
            "distanceWidthRadius": "int",
            "lineLength": "int",
            "size": "int",
            "thickness": "int",
        }
        return wcurve_scheme

    @staticmethod
    def create_default_schemes(allow_image: True) -> Dict:
        wcurve_scheme = FractalGraphScheme.create_default_scheme_for_draw_WCurve()
        if allow_image:
            FractalGraphScheme.additionally_add_image_to_scheme(wcurve_scheme)
        return {
            "function drawWCurve": wcurve_scheme,
            "function drawLine": FractalGraphScheme.create_default_scheme_for_draw_line(),
            "function Wcurve": FractalGraphScheme.create_default_scheme_for_wcurve(),
        }

    @staticmethod
    def create_default_schemes_with_image() -> Dict:
        wcurve_scheme = FractalGraphScheme.create_default_scheme_for_draw_WCurve()
        FractalGraphScheme.additionally_add_image_to_scheme(wcurve_scheme)
        return {
            "function drawWCurve": wcurve_scheme,
            "function Wcurve": FractalGraphScheme.create_default_scheme_for_wcurve(),
        }
