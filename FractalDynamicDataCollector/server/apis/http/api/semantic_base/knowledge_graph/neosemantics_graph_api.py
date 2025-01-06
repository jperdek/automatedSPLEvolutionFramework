import uuid
from typing import Optional

import neo4j

from server.apis.http.api.semantic_base.knowledge_graph.graph_knowledge_base_api import GraphKnowledgeBaseAPI
from server.apis.http.api.semantic_base.tools.triple_call_neosemantics_factory import TripleCallNeosemanticsFactory
from server.apis.http.api.semantic_base.tools.turtle_constructor import TurtleTriplesConstructor


from server.apis.http.api.semantic_base.knowledge_graph.init_database_drivers import \
    FullyAutomatedProductLinesKnowledgeManager


class NeosemanticsKnowledgeGraphApi:

    def init_graph_neosemantics(self, graph_knowledge_api: GraphKnowledgeBaseAPI):
        prefix_pointing_to_namespace_call = TripleCallNeosemanticsFactory.define_prefix_pointing_to_namespace(
            "faspls", "https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl")
        try:
            graph_knowledge_api.create_knowledge_db()
        except neo4j.exceptions.ClientError:
            pass
        graph_knowledge_api.process_data_transaction_using_commands(TripleCallNeosemanticsFactory.init_semantic_graph())
        graph_knowledge_api.process_data_transaction_using_commands(
            TripleCallNeosemanticsFactory.set_constraint_on_unique_uri())
        graph_knowledge_api.process_data_transaction_using_commands(prefix_pointing_to_namespace_call)

    def init_new_evolution(
            self, graph_knowledge_api: GraphKnowledgeBaseAPI, evolution_id: str, initial_product_line_id: str,
            evolved_script_id: str, evolved_script_path: Optional[str], evolution_configuration_path: Optional[str],
            previous_evolution_id: Optional[str] = None, previous_product_line_id: Optional[str] = None,
            base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> ."):
        new_evolution_knowledge_ttl = TurtleTriplesConstructor.prepare_ttl_of_core_product_line_evolution(
            evolution_id, initial_product_line_id, evolved_script_id, evolved_script_path, evolution_configuration_path,
            previous_evolution_id, previous_product_line_id, base_header)
        knowledge_ttl_call = TripleCallNeosemanticsFactory.import_from_text(new_evolution_knowledge_ttl, "Turtle")
        graph_knowledge_api.process_data_transaction_using_commands(knowledge_ttl_call)

    def register_new_evolution_iteration(
            self, graph_knowledge_api: GraphKnowledgeBaseAPI, evolution_id: str,
            evolved_product_line_id: str, evolution_iteration: str, code_location: Optional[str] = None,
            graph_location: Optional[str] = None, raster_location: Optional[str] = None,
            vector_location: Optional[str] = None, variation_point_data_location: Optional[str] = None,
            evolved_script_id: Optional[str] = None,
            evolved_script_path: Optional[str] = None, previous_product_line_id: Optional[str] = None,
            base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> ."):
        new_evolution_knowledge_ttl = TurtleTriplesConstructor.prepare_ttl_of_diverse_representations(
            evolution_id, evolved_product_line_id, evolution_iteration, code_location, graph_location, raster_location,
            vector_location, variation_point_data_location, evolved_script_id,
            evolved_script_path, previous_product_line_id, base_header)
        knowledge_ttl_call = TripleCallNeosemanticsFactory.import_from_text(new_evolution_knowledge_ttl, "Turtle")
        graph_knowledge_api.process_data_transaction_using_commands(knowledge_ttl_call)

    def register_new_product(
            self, graph_knowledge_api: GraphKnowledgeBaseAPI,
            evolved_product_line_id: str, code_location: Optional[str] = None, graph_location: Optional[str] = None,
            raster_location: Optional[str] = None, vector_location: Optional[str] = None,
            base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> ."):
        new_evolution_knowledge_ttl = TurtleTriplesConstructor.prepare_ttl_of_derived_product(
            evolved_product_line_id, code_location, graph_location, raster_location, vector_location, base_header)
        knowledge_ttl_call = TripleCallNeosemanticsFactory.import_from_text(new_evolution_knowledge_ttl, "Turtle")
        graph_knowledge_api.process_data_transaction_using_commands(knowledge_ttl_call)


if __name__ == "__main__":
    graph_knowl_api = FullyAutomatedProductLinesKnowledgeManager()
    neosemantics_knowledge_graph_api = NeosemanticsKnowledgeGraphApi()
    graph_knowl_api.clear_database()
    neosemantics_knowledge_graph_api.init_graph_neosemantics(graph_knowl_api)

    evolution_id = "evol_" + uuid.uuid4().hex[:8]
    evolved_product_line_id = "prod_line_" + uuid.uuid4().hex[:8]

    evolved_script_id = "evol_script_" + uuid.uuid4().hex[:8]
    evolved_script_path, evolution_configuration_path = "/evolved_script", "/evolved_configuration"
    neosemantics_knowledge_graph_api.init_new_evolution(
        graph_knowl_api, evolution_id, evolved_product_line_id, evolved_script_id, evolved_script_path,
        evolution_configuration_path)

    iteration = "1"
    code_location, graph_location, raster_location, vector_location = "/code", "/graph", "/raster", "/vector"
    neosemantics_knowledge_graph_api.register_new_evolution_iteration(
        graph_knowl_api, evolution_id, evolved_product_line_id, iteration, code_location, graph_location,
        raster_location, vector_location)

    code_product_loc, graph_product_loc = "/code_product", "/graph_product"
    raster_product_loc, vector_product_loc = "/raster_product", "/vector_product"
    neosemantics_knowledge_graph_api.register_new_product(
        graph_knowl_api, evolved_product_line_id, code_product_loc, graph_product_loc,
        raster_product_loc, vector_product_loc)

    code_product_loc2, graph_product_loc2 = "/code_product2", "/graph_product2"
    raster_product_loc2, vector_product_loc2 = "/raster_product2", "/vector_product2"
    neosemantics_knowledge_graph_api.register_new_product(
        graph_knowl_api, evolved_product_line_id, code_product_loc2, graph_product_loc2,
        raster_product_loc2, vector_product_loc2)

    evolved_product_line_id2 = "prod_line_" + uuid.uuid4().hex[:8]
    iteration2 = "2"
    code_location2, graph_location2, raster_location2, vector_location2 = "/code2", "/graph2", "/raster2", "/vector2"
    neosemantics_knowledge_graph_api.register_new_evolution_iteration(
        graph_knowl_api, evolution_id, evolved_product_line_id2, iteration2, code_location2, graph_location2,
        raster_location2, vector_location2, None, evolved_script_id,
        evolved_script_path, evolved_product_line_id)

    evolved_product_line_id3 = "prod_line_" + uuid.uuid4().hex[:8]
    iteration3 = "3"
    code_location3, graph_location3, raster_location3, vector_location3 = "/code3", "/graph3", "/raster3", "/vector3"
    neosemantics_knowledge_graph_api.register_new_evolution_iteration(
        graph_knowl_api, evolution_id, evolved_product_line_id3, iteration3, code_location3, graph_location3,
        raster_location3, vector_location3, None, evolved_script_id,
        evolved_script_path, evolved_product_line_id2)

    new_evolution_id = "evol_" + uuid.uuid4().hex[:8]
    new_evolved_product_line_id = "prod_line_" + uuid.uuid4().hex[:8]
    neosemantics_knowledge_graph_api.init_new_evolution(
        graph_knowl_api, new_evolution_id, new_evolved_product_line_id, evolved_script_id, evolved_script_path,
        evolution_configuration_path=evolution_configuration_path, previous_evolution_id=evolution_id,
        previous_product_line_id=evolved_product_line_id2)

    # network.create_db_for_network()
    graph_knowl_api.close()
