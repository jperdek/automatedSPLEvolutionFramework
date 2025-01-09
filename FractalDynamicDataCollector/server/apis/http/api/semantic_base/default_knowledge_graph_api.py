import json
from typing import Dict, Tuple

from flask import Blueprint, request, g

from semantic_base.knowledge_graph.neosemantics_graph import NeosemanticsKnowledgeGraphApi
from semantic_base.tools.triple_call_neosemantics_factory import TripleCallNeosemanticsFactory

from semantic_base.knowledge_graph.init_database_drivers import FullyAutomatedProductLinesKnowledgeManager

from semantic_base.knowledge_graph.neosemantics_graph import test


def html_text_response(payload: str, status: int = 200) -> Tuple[str, int, Dict]:
    return payload, status, {"content-type": "text/plain"}


default_knowledge_graph_api = Blueprint("default_knowledge_graph_api", __name__, template_folder="templates")


@default_knowledge_graph_api.route("/registerNewEvolutionIteration", methods=["POST"])
def register_new_evolution_iteration():
    request_data = json.loads(request.get_data().decode("utf-8", "ignore"))
    evolution_id = request_data.get("evolution_id")
    if not evolution_id:
        return "No evolution id provided.", 500, {"content-type": "text/plain"}
    evolution_iteration = request_data.get("evolution_iteration")
    evolved_product_line_id = request_data.get("evolved_product_line_id")
    if not evolved_product_line_id:
        return "No evolved software product line id provided.", 500, {"content-type": "text/plain"}
    previous_product_line_id = request_data.get("previous_product_line_id")

    # diverse representations
    code_path = request_data.get("code_path")
    screenshot_path = request_data.get("screenshot_path")
    vector_path = request_data.get("vector_path")
    json_graph_path = request_data.get("json_graph_path")

    # optional extensions
    evolved_script_path = request_data.get("evolved_script_path")
    variation_point_data_location = request_data.get("variation_point_data_location")
    evolved_script_id = evolved_script_path[evolved_script_path.rfind("/") + 1:].split(".")[0]

    base_header = request_data.get(
        "base_header", "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .")

    if "fully_automated_product_lines_knowledge_manager" not in g:
        g.fully_automated_product_lines_knowledge_manager = FullyAutomatedProductLinesKnowledgeManager()
    NeosemanticsKnowledgeGraphApi.register_new_evolution_iteration(
        g.fully_automated_product_lines_knowledge_manager, evolution_id, evolved_product_line_id, evolution_iteration,
        code_path, json_graph_path, screenshot_path, vector_path, variation_point_data_location,
        evolved_script_id, evolved_script_path, previous_product_line_id, True, base_header)
    return html_text_response("OK")


@default_knowledge_graph_api.route("/registerNewProduct", methods=["POST"])
def register_new_product():
    request_data = json.loads(request.get_data().decode("utf-8", "ignore"))
    evolved_product_line_id = request_data.get("evolved_product_line_id")
    if not evolved_product_line_id:
        return "No product line id provided.", 500, {"content-type": "text/plain"}

    # diverse representations
    code_path = request_data.get("code_path")
    screenshot_path = request_data.get("screenshot_path")
    vector_path = request_data.get("vector_path")
    json_graph_path = request_data.get("json_graph_path")

    base_header = request_data.get(
        "base_header", "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .")
    if "fully_automated_product_lines_knowledge_manager" not in g:
        g.fully_automated_product_lines_knowledge_manager = FullyAutomatedProductLinesKnowledgeManager()
    NeosemanticsKnowledgeGraphApi.register_new_product(
        g.fully_automated_product_lines_knowledge_manager, evolved_product_line_id,
        code_path, json_graph_path, screenshot_path, vector_path, base_header)
    return html_text_response("OK")


@default_knowledge_graph_api.route("/init", methods=["GET"])
def init_default_neosemantics_knowledge_graph():
    prefix_name = request.args.get("prefix_name", "faspls")
    prefixed_namespace = request.args.get(
        "prefixed_namespace", "https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl")
    if "fully_automated_product_lines_knowledge_manager" not in g:
        g.fully_automated_product_lines_knowledge_manager = FullyAutomatedProductLinesKnowledgeManager()
    NeosemanticsKnowledgeGraphApi.init_graph_neosemantics(
        g.fully_automated_product_lines_knowledge_manager, prefix_name, prefixed_namespace)
    return html_text_response("OK")


@default_knowledge_graph_api.route("/clear", methods=["GET"])
def clear_neosemantics_knowledge_graph():
    if "fully_automated_product_lines_knowledge_manager" not in g:
        g.fully_automated_product_lines_knowledge_manager = FullyAutomatedProductLinesKnowledgeManager()
    g.fully_automated_product_lines_knowledge_manager.clear_database()
    return html_text_response("OK")


@default_knowledge_graph_api.route("/registerNewEvolution", methods=["POST"])
def register_new_evolution():
    request_data = json.loads(request.get_data().decode("utf-8", "ignore"))
    evolution_id = request_data.get("evolution_id")
    if not evolution_id:
        return "No evolution id provided.", 500, {"content-type": "text/plain"}
    initial_product_line_id = request_data.get("initial_product_line_id")
    if not initial_product_line_id:
        return "No initial product line id provided.", 500, {"content-type": "text/plain"}

    # optional extensions
    evolved_script_path = request_data.get("evolved_script_path")
    previous_product_line_id = request_data.get("previous_product_line_id")

    previous_evolution_id = request_data.get("previous_evolution_id")
    evolution_configuration_path = request_data.get("evolution_configuration_path")
    evolved_script_id = evolved_script_path[evolved_script_path.rfind("/") + 1:].split(".")[0]

    base_header = request_data.get(
        "base_header", "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .")
    if "fully_automated_product_lines_knowledge_manager" not in g:
        g.fully_automated_product_lines_knowledge_manager = FullyAutomatedProductLinesKnowledgeManager()
    NeosemanticsKnowledgeGraphApi.init_new_evolution(
        g.fully_automated_product_lines_knowledge_manager, evolution_id, initial_product_line_id, evolved_script_id,
        evolved_script_path, evolution_configuration_path, previous_evolution_id, previous_product_line_id, base_header)
    return html_text_response("OK")


@default_knowledge_graph_api.route("/addTriples", methods=["POST"])
def add_triples():
    new_evolution_knowledge_ttl = request.get_data().decode("utf-8", "ignore")
    if "fully_automated_product_lines_knowledge_manager" not in g:
        g.fully_automated_product_lines_knowledge_manager = FullyAutomatedProductLinesKnowledgeManager()
    triples_ttl = new_evolution_knowledge_ttl.replace("\r\n", " ").replace("\n", " ")
    knowledge_ttl_call = TripleCallNeosemanticsFactory.import_from_text(triples_ttl, "Turtle")
    g.fully_automated_product_lines_knowledge_manager.process_data_transaction_using_commands_block(knowledge_ttl_call)

    return html_text_response("OK")


@default_knowledge_graph_api.route("/test", methods=["GET"])
def insert_default_knowledge_and_make_graph():
    import_variation_points = request.args.get("importVariationPoints", "true")
    import_variation_points = True if import_variation_points == "true" else False
    print(import_variation_points)
    test(local=False, import_variation_points=import_variation_points)
    return html_text_response("OK")
