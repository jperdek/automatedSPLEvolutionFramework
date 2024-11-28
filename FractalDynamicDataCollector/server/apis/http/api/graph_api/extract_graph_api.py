import os
import time
from typing import Dict

from flask import Blueprint, request
import json

from screenshoting.screenshooter import PlaywrightScreenshooter
from processors.analyzer import DynamicFractalAnalyzer


def json_response(payload: Dict, status: int = 200):
    return json.dumps(payload), status, {"content-type": "application/json" }


def json_response_str(payload: str, status: int = 200):
    return payload, status, {"content-type": "application/json" }


graph_extractor_api = Blueprint("graph_extractor_api", __name__, template_folder="templates")


@graph_extractor_api.route("/extract", methods=["GET"])
def extract_graph_from_browser():
    web_page_location = request.args.get("url")
    time_to_wait = int(request.args.get("time_to_wait", 10))

    screenshooter = PlaywrightScreenshooter("chromium")
    page = screenshooter.new_page()
    page.goto(web_page_location, wait_until="networkidle")
    time.sleep(time_to_wait)
    variability_point_data = page.evaluate("() => JSON.stringify(initialGraphRoot);")
    try:
        screenshooter.close()
    except:
        pass
    return json_response(variability_point_data)


@graph_extractor_api.route("/extractFromScript", methods=["GET"])
def extract_graph_from_script():
    web_page_location = request.args.get("url")
    is_wrapped = request.args.get("is_wrapped", True)
    processed_script_extension = request.args.get("processed_script_extension", "js/platnoJS.js")

    web_page_location = web_page_location.replace("file:///", "")
    if "/" in web_page_location and not web_page_location.endswith("/"):
        web_page_location = web_page_location[:web_page_location.rfind("/") + 1]

    script_path = os.path.join(web_page_location, processed_script_extension)
    dynamic_fractal_analyzer = DynamicFractalAnalyzer()
    variability_point_data = dynamic_fractal_analyzer.load_fractal(
        script_path, is_wrapped=is_wrapped
    )

    return json_response_str(json.loads(variability_point_data))


@graph_extractor_api.route("/extractObject", methods=["GET"])
def extract_graph_from_browser_object():
    web_page_location = request.args.get("url")
    time_to_wait = int(request.args.get("time_to_wait", 10))
    extension_key = request.args.get("extension_key", "pointsTo")
    max_depth = int(request.args.get("max_depth", -1))
    into_array = request.args.get("into_array", True)

    screenshooter = PlaywrightScreenshooter("chromium")
    page = screenshooter.new_page()
    page.goto(web_page_location, wait_until="networkidle")
    time.sleep(time_to_wait)
    variability_point_data = {}
    DynamicFractalAnalyzer.get_graph(0, max_depth, variability_point_data, page, "", extension_key, into_array)
    try:
        screenshooter.close()
    except:
        pass
    return json_response(variability_point_data)
