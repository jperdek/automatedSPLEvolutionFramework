from typing import Dict

from flask import Blueprint, request
import json

from graphSimulation.graphDBInserter.graphDBConnector import GraphFromFamilyInserter


def json_response(payload: Dict, status: int = 200):
    return json.dumps(payload), status, {"content-type": "application/json" }


def response_str(payload: str, status: int = 200):
    return payload, status, {"content-type": "text/plain"}


graph_database_entry_api = Blueprint("graph_database_entry_api", __name__, template_folder="templates")


@graph_database_entry_api.route("/insertInstanceGraph", methods=["POST"])
def insert_instance_graph():
    graph_data_in_docs = json.loads(request.get_data().decode("utf-8", "ignore"))
    scheme = request.args.get("scheme", "neo4j")
    host_name = request.args.get("host_name", "localhost")
    port = request.args.get("port", 7687)
    url1 = f"{scheme}://{host_name}:{port}"
    user1 = request.args.get("user", "neo4j")
    password1 = request.args.get("password", "featureNeo4")

    clear_database = request.args.get("clear_database", False)
    connections_only = request.args.get("connections_only", False)
    GraphFromFamilyInserter.insert_small_graph_from_ram(
        url=url1,
        user=user1,
        password=password1,
        graph_data_in_docs=graph_data_in_docs,
        clear_database=clear_database, connections_only=connections_only
    )
    return response_str("OK")
