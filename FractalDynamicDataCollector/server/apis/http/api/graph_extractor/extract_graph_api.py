from typing import Dict

from flask import Blueprint, request
import json


def json_response(payload: Dict, status: int = 200):
    return json.dumps(payload), status, {"content-type": "application/json" }


graph_extractor_api = Blueprint("graph_extractor_api", __name__, template_folder="templates")


@graph_extractor_api.route("/api/graphExtractor", methods=["POST"])
def extract_graph_api():
    mhtml_file = request.get_data().decode("utf-8", "ignore")
    k = int(request.args.get("k"))


    return json_response(result)