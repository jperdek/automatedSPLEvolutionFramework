from typing import Dict, Tuple

from flask import Blueprint, request

from svg_transformation_api.svg_transformation import SVGTransform


def html_text_response(payload: str, status: int = 200) -> Tuple[str, int, Dict]:
    return payload, status, {"content-type": "text/html"}


svg_creator_api = Blueprint("svg_creator_api", __name__, template_folder="templates")


@svg_creator_api.route("/create_svg", methods=["GET"])
def create_svg_and_extract_it_from_browser():
    web_page_location = request.args.get("url")
    time_to_wait = int(request.args.get("time_to_wait", 10))
    generated_svg_id = request.args.get("generated_svg_id", "svgId")
    draw_line_in_op = request.args.get("draw_line_in_op", "drawLine")
    replacement_function = request.args.get(
        "replacement_function", "function(context, x1, y1, x2, y2, thickness)"
    )

    if not web_page_location:
        return "No product/fractal url provided.", 500, {"content-type": "text/plain"}
    resulting_svg = SVGTransform.transform_into_svg(
        web_page_location,
        generated_svg_id,
        draw_line_in_op,
        replacement_function,
        time_to_wait,
    )
    return html_text_response(resulting_svg)
