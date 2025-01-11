from typing import Dict, Tuple

from flask import Blueprint, request
import json

from graphSimulation.graphExtractor.process_graphs import GraphProcessor


def json_response(payload: Dict, status: int = 200) -> Tuple[str, int, Dict]:
    return json.dumps(payload), status, {"content-type": "application/json"}


nodes_and_connectors_api = Blueprint(
    "nodes_and_connectors_api", __name__, template_folder="templates"
)


@nodes_and_connectors_api.route("/process_into_nodes_connectors", methods=["POST"])
def process_extracted_graph_into_nodes_and_connectors():
    graph_root = json.loads(request.get_data().decode("utf-8", "ignore"))
    nodes_and_connectors = GraphProcessor.process_graph_to_ram(graph_root=graph_root)
    return json_response(nodes_and_connectors)
