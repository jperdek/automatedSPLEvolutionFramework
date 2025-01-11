import hashlib
import json
import uuid
from typing import Optional, Dict

import requests
from semantic_base.knowledge_graph.graph_knowledge_base_api import GraphKnowledgeBaseAPI
from semantic_base.tools.triple_call_neosemantics_factory import (
    TripleCallNeosemanticsFactory,
)


class TurtleTriplesConstructor:
    @staticmethod
    def get_default_headers_in_ttl(
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        return (
            base_header
            + """
            @prefix faspls: <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .
            @prefix dbp: <http://dbpedia.org/ontology/> .
            @prefix owl: <http://www.w3.org/2002/07/owl#> .
            @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
            @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
            @prefix skos: <http://www.w3.org/2004/02/skos/core#> .
            @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
    
        """
        )

    @staticmethod
    def get_rdfs_headers_in_ttl(
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        return (
            base_header
            + """
            @prefix faspls: <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .
            @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
            @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
        
        """
        )

    @staticmethod
    def prepare_ttl_to_import_with_headers(
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        prepared_ttl = TurtleTriplesConstructor.get_rdfs_headers_in_ttl(
            base_header=base_header
        )

        return prepared_ttl

    @staticmethod
    def prepare_ttl_of_new_product_line_to_exiting_evolution(
        evolution_id: str,
        product_line_id: str,
        evolved_script_id: Optional[str],
        evolved_script_path: Optional[str],
        previous_product_line_id: Optional[str],
    ) -> str:
        prepared_ttl = f"""
            <{product_line_id}> a faspls:ProductLine .
        """
        if evolved_script_id:
            prepared_ttl += f"""
                <{evolved_script_id}> a faspls:EvolvedScript .
                <{evolution_id}> faspls:evolving <{evolved_script_id}> .
            """

        if evolved_script_id and evolved_script_path:
            prepared_ttl += f"""
                <{evolved_script_id}> faspls:location "{evolved_script_path}" .
            """
        if previous_product_line_id:
            prepared_ttl += f"""
                <{previous_product_line_id}> faspls:nextEvolved <{product_line_id}> .
            """
        return prepared_ttl

    @staticmethod
    def prepare_ttl_of_core_product_line_evolution(
        evolution_id: str,
        initial_product_line_id: str,
        evolved_script_id: str,
        evolved_script_path: Optional[str],
        evolution_configuration_path: Optional[str],
        previous_evolution_id: Optional[str] = None,
        previous_product_line_id: Optional[str] = None,
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        prepared_ttl = TurtleTriplesConstructor.get_rdfs_headers_in_ttl(
            base_header=base_header
        )
        prepared_ttl += f"""
            <{evolution_id}> a faspls:Evolution .
        """
        prepared_ttl += TurtleTriplesConstructor.prepare_ttl_of_new_product_line_to_exiting_evolution(
            evolution_id,
            initial_product_line_id,
            evolved_script_id,
            evolved_script_path,
            previous_product_line_id,
        )
        if previous_evolution_id:
            prepared_ttl += f"""
                <{previous_evolution_id}> faspls:nextEvol <{evolution_id}> .
            """
        if evolution_configuration_path:
            prepared_ttl += f"""
                <{evolution_id}> faspls:confAssoc "{evolution_configuration_path}" .
            """
        prepared_ttl += f"""
            <{evolution_id}> faspls:startFromSPL <{initial_product_line_id}> .
        """
        return prepared_ttl

    @staticmethod
    def prepare_ttl_of_derived_product(
        evolved_product_line_id: str,
        code_location: Optional[str] = None,
        graph_location: Optional[str] = None,
        raster_location: Optional[str] = None,
        vector_location: Optional[str] = None,
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        prepared_ttl = TurtleTriplesConstructor.get_rdfs_headers_in_ttl(
            base_header=base_header
        )
        new_product_id = "product_" + uuid.uuid4().hex[:8]
        prepared_ttl += f"""
            <{new_product_id}> a faspls:Product .
            
            <{evolved_product_line_id}> faspls:derivedFrom <{new_product_id}> .
        """
        prepared_ttl += TurtleTriplesConstructor.__prepare_code_ttl(
            new_product_id, code_location
        )
        prepared_ttl += TurtleTriplesConstructor.__prepare_raster_ttl(
            new_product_id, raster_location
        )
        prepared_ttl += TurtleTriplesConstructor.__prepare_vector_ttl(
            new_product_id, vector_location
        )
        prepared_ttl += TurtleTriplesConstructor.__prepare_graph_ttl(
            new_product_id, graph_location
        )
        return prepared_ttl

    @staticmethod
    def prepare_ttl_of_diverse_representations(
        evolution_id: str,
        evolved_product_line_id: str,
        evolution_iteration: str,
        code_location: Optional[str] = None,
        graph_location: Optional[str] = None,
        raster_location: Optional[str] = None,
        vector_location: Optional[str] = None,
        variation_point_data_location: Optional[str] = None,
        evolved_script_id: Optional[str] = None,
        evolved_script_path: Optional[str] = None,
        previous_product_line_id: Optional[str] = None,
        immediately_save_ttls: bool = False,
        graph_knowledge_api: Optional[GraphKnowledgeBaseAPI] = None,
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        prepared_ttl = TurtleTriplesConstructor.get_rdfs_headers_in_ttl(
            base_header=base_header
        )
        prepared_ttl += TurtleTriplesConstructor.prepare_ttl_of_new_product_line_to_exiting_evolution(
            evolution_id,
            evolved_product_line_id,
            evolved_script_id,
            evolved_script_path,
            previous_product_line_id,
        )
        prepared_ttl += f"""
            <{evolved_product_line_id}> faspls:evolvedIn <{evolution_id}> .
            <{evolved_product_line_id}> faspls:evolSerie "{evolution_iteration}" .
        """
        prepared_ttl += TurtleTriplesConstructor.__prepare_code_ttl(
            evolved_product_line_id, code_location
        )
        prepared_ttl += TurtleTriplesConstructor.__prepare_raster_ttl(
            evolved_product_line_id, raster_location
        )
        prepared_ttl += TurtleTriplesConstructor.__prepare_vector_ttl(
            evolved_product_line_id, vector_location
        )
        prepared_ttl += TurtleTriplesConstructor.__prepare_graph_ttl(
            evolved_product_line_id, graph_location
        )

        # overwrites or lefts untouched - only = operator is used
        prepared_ttl = (
            TurtleTriplesConstructor.__check_and_optionally_save_prepared_ttl(
                prepared_ttl, immediately_save_ttls, graph_knowledge_api, base_header
            )
        )
        prepared_ttl += TurtleTriplesConstructor.__prepare_variation_points_ttl(
            evolved_product_line_id,
            variation_point_data_location,
            immediately_save_ttls,
            graph_knowledge_api,
            base_header,
        )
        return prepared_ttl

    @staticmethod
    def normalize_configuration_expression(configuration_expression: str) -> str:
        return (
            configuration_expression.replace('"', "")
            .replace("'", "")
            .replace(":", "-")
            .replace("{", "I_")
            .replace("}", "_I")
            .replace(" ", "")
        )

    @staticmethod
    def __prepare_vp_annotation_with_expression_in_ttl(
        variation_point_configuration: Dict,
        unique_variation_point_id: str,
        immediately_save_ttls: bool = False,
        graph_knowledge_api: Optional[GraphKnowledgeBaseAPI] = None,
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        prepared_ttl = ""
        for variability_annot_config in variation_point_configuration.get(
            "variabilitySelections", []
        ):
            variability_annotation_id = "var_annot_" + uuid.uuid4().hex[:8]
            is_user_annotation = variability_annot_config.get(
                "isNegativeVariabilityUserAnnotation", True
            )
            guarded_entity_name = variability_annot_config.get("name", "Undefined")
            fully_annot_name = variability_annot_config.get(
                "fullName", guarded_entity_name
            )
            is_illegal_decorator = variability_annot_config.get(
                "isMarkedAsIllegal", False
            )
            configuration_expression_str = variability_annot_config.get(
                "configurationExpressionStr", "{}"
            )
            configuration_expression_id = (
                TurtleTriplesConstructor.normalize_configuration_expression(
                    configuration_expression_str
                )
            )
            annotated_entity_type = variability_annot_config.get(
                "annotationVPType", "Unknown"
            )
            prepared_ttl += f"""
                <{unique_variation_point_id}> faspls:hasVariabilityAnnotation <{variability_annotation_id}> .
                <{variability_annotation_id}> faspls:isUserAnnotation "{is_user_annotation}" .
                <{variability_annotation_id}> faspls:fullName "{fully_annot_name}" .
                <{variability_annotation_id}> faspls:isIllegalDecorator "{is_illegal_decorator}" .
                <{variability_annotation_id}> faspls:entityType "{annotated_entity_type}" .

                <{configuration_expression_id}> a faspls:ConfigurationExpression .
                <{variability_annotation_id}> faspls:hasConfiguration <{configuration_expression_id}> .
                <{configuration_expression_id}> faspls:asString "{configuration_expression_str}" .
                <{configuration_expression_id}> faspls:guardedFeature "{guarded_entity_name}" .  
            """
            configuration_expression = json.loads(configuration_expression_str)
            prepared_ttl += TurtleTriplesConstructor.__process_configuration_expression(
                configuration_expression, configuration_expression_id
            )
            # overwrites or lefts untouched - only = operator is used
            prepared_ttl = (
                TurtleTriplesConstructor.__check_and_optionally_save_prepared_ttl(
                    prepared_ttl,
                    immediately_save_ttls,
                    graph_knowledge_api,
                    base_header,
                )
            )
        return prepared_ttl

    @staticmethod
    def __check_and_optionally_save_prepared_ttl(
        prepared_ttl: str,
        immediately_save_ttls: bool = False,
        graph_knowledge_api: Optional[GraphKnowledgeBaseAPI] = None,
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        if immediately_save_ttls and graph_knowledge_api:
            headers_ttl = TurtleTriplesConstructor.get_rdfs_headers_in_ttl(base_header)
            if headers_ttl not in prepared_ttl:
                prepared_ttl = headers_ttl + prepared_ttl
            command_to_store_ttls = TripleCallNeosemanticsFactory.import_from_text(
                prepared_ttl, "Turtle"
            )
            graph_knowledge_api.process_data_transaction_using_commands(
                command_to_store_ttls
            )
            return ""
        return prepared_ttl

    @staticmethod
    def __prepare_variation_point_in_ttl(
        variation_point_configuration: Dict,
        evolved_product_line_id: str,
        immediately_save_ttls: bool = False,
        graph_knowledge_api: Optional[GraphKnowledgeBaseAPI] = None,
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        unique_variation_point_id_str = variation_point_configuration[
            "hierarchicIdentifier"
        ]
        if variation_point_configuration.get("newVariationPoint"):
            for callable_object in variation_point_configuration.get(
                "allAvailableCalls", []
            ):
                unique_variation_point_id_str += callable_object
            variation_point_type = "faspls:PositiveVP"
        else:
            unique_variation_point_id_str += variation_point_configuration.get(
                "affectedCode", ""
            )
            variation_point_type = "faspls:NegativeVP"
        hash_object = hashlib.sha256(unique_variation_point_id_str.encode("utf-8"))
        unique_variation_point_id = "var_point_" + hash_object.hexdigest()[:8]
        is_class_related = variation_point_configuration.get("classRelated", False)
        hierarchic_identifier = variation_point_configuration.get(
            "hierarchicIdentifier", "."
        )
        is_inside_recursion = variation_point_configuration.get(
            "isInsideRecursion", False
        )
        prepared_ttl = f"""
            <{unique_variation_point_id}> a {variation_point_type} .
            
            <{evolved_product_line_id}> faspls:hasVP <{unique_variation_point_id}> .
            <{unique_variation_point_id}> faspls:isClassRelated "{is_class_related}" .
            <{unique_variation_point_id}> faspls:isInsideRecursion "{is_inside_recursion}" .
            <{unique_variation_point_id}> faspls:hierarchicIdentifier "{hierarchic_identifier}" .
        """
        if variation_point_configuration.get("newVariationPoint"):
            for callable_object in variation_point_configuration.get(
                "allAvailableCalls", []
            ):
                prepared_ttl += f"""
                   <{unique_variation_point_id}> faspls:canCall "{callable_object}" . 
                """
            # overwrites or lefts untouched - only = operator is used
            prepared_ttl = (
                TurtleTriplesConstructor.__check_and_optionally_save_prepared_ttl(
                    prepared_ttl,
                    immediately_save_ttls,
                    graph_knowledge_api,
                    base_header,
                )
            )
        else:
            # overwrites or lefts untouched - only = operator is used
            prepared_ttl = (
                TurtleTriplesConstructor.__check_and_optionally_save_prepared_ttl(
                    prepared_ttl,
                    immediately_save_ttls,
                    graph_knowledge_api,
                    base_header,
                )
            )
            prepared_ttl += (
                TurtleTriplesConstructor.__prepare_vp_annotation_with_expression_in_ttl(
                    variation_point_configuration, unique_variation_point_id
                )
            )
        return prepared_ttl

    @staticmethod
    def __prepare_variation_points_ttl(
        evolved_product_line_id: str,
        variation_point_data_location: Optional[str],
        immediately_save_ttls: bool = False,
        graph_knowledge_api: Optional[GraphKnowledgeBaseAPI] = None,
        base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .",
    ) -> str:
        prepared_ttl = ""
        if variation_point_data_location:
            if "http" in variation_point_data_location:
                variation_points_configuration = json.loads(
                    requests.get(variation_point_data_location).content
                )
            else:
                with open(
                    variation_point_data_location.replace("file:///", ""),
                    "r",
                    encoding="utf-8",
                ) as file:
                    variation_points_configuration = json.loads(file.read())
            if variation_points_configuration:
                for variation_point_configuration in variation_points_configuration:
                    prepared_ttl += (
                        TurtleTriplesConstructor.__prepare_variation_point_in_ttl(
                            variation_point_configuration,
                            evolved_product_line_id,
                            immediately_save_ttls,
                            graph_knowledge_api,
                            base_header,
                        )
                    )
        return prepared_ttl

    @staticmethod
    def __process_configuration_expression(
        configuration_expression: Dict, parent_configuration_expression_id: str
    ) -> str:
        prepared_ttl = ""
        for name, configuration_json in configuration_expression.items():
            prepared_ttl += (
                TurtleTriplesConstructor.__process_configuration_expression_rec(
                    name, configuration_json, parent_configuration_expression_id
                )
            )
        return prepared_ttl

    @staticmethod
    def __is_operator(object_type: str) -> bool:
        if object_type.upper() == "AND" or object_type.upper() == "OR":
            return True
        return False

    @staticmethod
    def __process_configuration_expression_rec(
        object_type: str,
        configuration_expression: Dict,
        parent_configuration_expression_id: str,
    ) -> str:
        prepared_ttl = ""
        if isinstance(configuration_expression, Dict):
            configuration_expression_id = (
                TurtleTriplesConstructor.normalize_configuration_expression(
                    json.dumps(configuration_expression)
                )
            )
            prepared_ttl = f"""
                <{configuration_expression_id}> a faspls:JSONExpression .
                <{configuration_expression_id}> faspls:hasValue "{json.dumps(configuration_expression)}" .
            """
            if TurtleTriplesConstructor.__is_operator(object_type):
                prepared_ttl += f"""
                    <{configuration_expression_id}> faspls:belongsToFeature "{object_type}" .
                """
            else:
                prepared_ttl += f"""
                    <{configuration_expression_id}> faspls:hasValue "{object_type}" .
                """
            for name, configuration_json in configuration_expression.items():
                prepared_ttl += (
                    TurtleTriplesConstructor.__process_configuration_expression_rec(
                        name, configuration_json, configuration_expression_id
                    )
                )
        elif TurtleTriplesConstructor.__is_operator(object_type):
            prepared_ttl += f"""
                <{parent_configuration_expression_id}> faspls:belongsToFeature "{object_type}" .
            """
        return prepared_ttl

    @staticmethod
    def __prepare_code_ttl(source_id: str, code_location: Optional[str]) -> str:
        prepared_ttl = ""
        if code_location:
            code_uuid = uuid.uuid4().hex[:8]
            code_representation_id = "code_" + code_uuid[:8]
            prepared_ttl += f"""
                <{code_representation_id}> a faspls:Code .

                <{source_id}> faspls:codeRepr <{code_representation_id}> .
                <{code_representation_id}> faspls:location "{code_location}" .
            """
        return prepared_ttl

    @staticmethod
    def __prepare_raster_ttl(source_id: str, raster_location: Optional[str]) -> str:
        prepared_ttl = ""
        if raster_location:
            raster_uuid = uuid.uuid4().hex[:8]
            raster_representation_id = "raster_" + raster_uuid
            prepared_ttl += f"""
                <{raster_representation_id}> a faspls:Screenshot .

                <{source_id}> faspls:rasterRepr <{raster_representation_id}> .
                <{raster_representation_id}> faspls:location "{raster_location}" .
            """
        return prepared_ttl

    @staticmethod
    def __prepare_vector_ttl(source_id: str, vector_location: Optional[str]) -> str:
        prepared_ttl = ""
        if vector_location:
            vector_uuid = uuid.uuid4().hex[:8]
            vector_representation_id = "svg_" + vector_uuid
            prepared_ttl += f"""
                <{vector_representation_id}> a faspls:SVG .

                <{source_id}> faspls:vectorRepr <{vector_representation_id}> .
                <{vector_representation_id}> faspls:location "{vector_location}" .
            """
        return prepared_ttl

    @staticmethod
    def __prepare_graph_ttl(source_id: str, graph_location: Optional[str]) -> str:
        prepared_ttl = ""
        if graph_location:
            graph_uuid = uuid.uuid4().hex[:8]
            graph_representation_id = "graph_" + graph_uuid
            prepared_ttl = f"""
                <{graph_representation_id}> a faspls:InstanceGraph .

                <{source_id}> faspls:graphRepr <{graph_representation_id}> .
                <{graph_representation_id}> faspls:location "{graph_location}" .
            """
        return prepared_ttl
