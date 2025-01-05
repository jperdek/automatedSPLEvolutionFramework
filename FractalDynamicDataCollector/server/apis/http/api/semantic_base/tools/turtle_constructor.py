import hashlib
import json
import uuid
from typing import Optional, Dict


class TurtleTriplesConstructor:

    @staticmethod
    def get_default_headers_in_ttl(base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .") -> str:
        return base_header + """
            @prefix dbp: <http://dbpedia.org/ontology/> .
            @prefix owl: <http://www.w3.org/2002/07/owl#> .
            @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
            @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
            @prefix skos: <http://www.w3.org/2004/02/skos/core#> .
            @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
    
        """

    @staticmethod
    def get_rdfs_headers_in_ttl(base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .") -> str:
        return base_header + """
            @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
            @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
        
        """

    @staticmethod
    def prepare_ttl_to_import_with_headers(base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .") -> str:
        prepared_ttl = TurtleTriplesConstructor.get_rdfs_headers_in_ttl(base_header=base_header)

        return prepared_ttl

    @staticmethod
    def prepare_ttl_of_core_product_line_evolution(
            evolution_id: str, initial_product_line_id: str, evolved_script_id: str,
            evolved_script_path: Optional[str], evolution_configuration_path: Optional[str],
            base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .") -> str:
        prepared_ttl = TurtleTriplesConstructor.get_rdfs_headers_in_ttl(base_header=base_header)
        return prepared_ttl + f"""
            <{evolution_id}> a faspls:Evolution .
            <{initial_product_line_id}> a faspls:ProductLine .
            <{evolved_script_id}> a faspls:EvolvedScript .
            
            
            <{evolution_id}> faspls:startFromSPL <{initial_product_line_id}> .
            <{evolution_id}> faspls:evolving <{evolved_script_id}> .
            <{evolution_id}> faspls:confAssoc "{evolution_configuration_path}" .
            <{evolved_script_id}> faspls:location "{evolved_script_path}" .
        """

    @staticmethod
    def prepare_ttl_of_derived_product(evolved_product_line_id: str,
                                       code_location: Optional[str] = None,
                                       graph_location: Optional[str] = None,
                                       raster_location: Optional[str] = None,
                                       vector_location: Optional[str] = None,
                                       base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .") -> str:
        prepared_ttl = TurtleTriplesConstructor.get_rdfs_headers_in_ttl(base_header=base_header)
        new_product_id = uuid.uuid4()
        prepared_ttl += f"""
            <{new_product_id}> a faspls:Product .
            
            <{evolved_product_line_id}> faspls:derivedFrom <{new_product_id}> .
        """
        prepared_ttl += TurtleTriplesConstructor.__prepare_code_ttl(code_location)
        prepared_ttl += TurtleTriplesConstructor.__prepare_raster_ttl(raster_location)
        prepared_ttl += TurtleTriplesConstructor.__prepare_vector_ttl(vector_location)
        prepared_ttl += TurtleTriplesConstructor.__prepare_graph_ttl(graph_location)
        return prepared_ttl

    @staticmethod
    def prepare_ttl_of_diverse_representations(evolution_id: str,
                                               evolved_product_line_id: str,
                                               evolution_iteration: str,
                                               code_location: Optional[str] = None,
                                               graph_location: Optional[str] = None,
                                               raster_location: Optional[str] = None,
                                               vector_location: Optional[str] = None,
                                               variation_point_data_location: Optional[str] = None,
                                               base_header: str = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .") -> str:
        prepared_ttl = TurtleTriplesConstructor.get_rdfs_headers_in_ttl(base_header=base_header)
        prepared_ttl += f"""
            <{evolution_id}> a faspls:Evolution .
            <{evolved_product_line_id}> a faspls:ProductLine .

            <{evolved_product_line_id}> faspls:evolvedIn <{evolution_id}> .
            <{evolved_product_line_id}> faspls:evolSerie "{evolution_iteration}" .
        """
        prepared_ttl += TurtleTriplesConstructor.__prepare_code_ttl(code_location)
        prepared_ttl += TurtleTriplesConstructor.__prepare_raster_ttl(raster_location)
        prepared_ttl += TurtleTriplesConstructor.__prepare_vector_ttl(vector_location)
        prepared_ttl += TurtleTriplesConstructor.__prepare_graph_ttl(graph_location)
        prepared_ttl += TurtleTriplesConstructor.__prepare_variation_points_ttl(
            evolved_product_line_id, variation_point_data_location)
        return prepared_ttl

    @staticmethod
    def normalize_configuration_expression(configuration_expression: str) -> str:
        return configuration_expression.replace("\"", "").replace("'", "").replace(
            ":", "-").replace("{", "I_").replace("}", "_I").replace(" ", "")

    @staticmethod
    def __prepare_vp_annotation_with_expression_in_ttl(
            variation_point_configuration: Dict, unique_variation_point_id: str) -> str:
        prepared_ttl = ""
        for variability_annot_config in variation_point_configuration.get("variabilitySelections", []):
            variability_annotation_id = uuid.uuid4()
            is_user_annotation = variability_annot_config.get("isNegativeVariabilityUserAnnotation", True)
            guarded_entity_name = variability_annot_config.get("name", "Undefined")
            fully_annot_name = variability_annot_config.get("fullName", guarded_entity_name)
            is_illegal_decorator = variability_annot_config.get("isMarkedAsIllegal", False)
            configuration_expression_str = variability_annot_config.get("configurationExpressionStr", "{}")
            configuration_expression_id = TurtleTriplesConstructor.normalize_configuration_expression(
                configuration_expression_str)
            annotated_entity_type = variability_annot_config.get("annotationVPType", "Unknown")
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
                configuration_expression, configuration_expression_id)
        return prepared_ttl

    @staticmethod
    def __prepare_variation_point_in_ttl(variation_point_configuration: Dict, evolved_product_line_id: str) -> str:
        unique_variation_point_id_str = variation_point_configuration["hierarchicIdentifier"]
        if variation_point_configuration.get("newVariationPoint"):
            for callable_object in variation_point_configuration.get("allAvailableCalls", []):
                unique_variation_point_id_str += callable_object
            variation_point_type = "faspls:PositiveVP"
        else:
            unique_variation_point_id_str += variation_point_configuration.get("affectedCode", "")
            variation_point_type = "faspls:NegativeVP"
        hash_object = hashlib.sha256(unique_variation_point_id_str.encode("utf-8"))
        unique_variation_point_id = hash_object.hexdigest()
        is_class_related = variation_point_configuration.get("classRelated", False)
        hierarchic_identifier = variation_point_configuration.get("hierarchicIdentifier", ".")
        is_inside_recursion = variation_point_configuration.get("isInsideRecursion", False)
        prepared_ttl = f"""
            <{unique_variation_point_id}> a {variation_point_type} .

            <{evolved_product_line_id}> faspls:hasVP <{unique_variation_point_id}> .
            <{unique_variation_point_id}> faspls:isClassRelated {is_class_related} .
            <{unique_variation_point_id}> faspls:isInsideRecursion {is_inside_recursion} .
            <{unique_variation_point_id}> faspls:hierarchicIdentifier {hierarchic_identifier} .
        """
        if variation_point_configuration.get("newVariationPoint"):
            for callable_object in variation_point_configuration.get("allAvailableCalls", []):
                prepared_ttl += f"""
                   <{unique_variation_point_id}> faspls:canCall "{callable_object}" . 
                """
        else:
            prepared_ttl += TurtleTriplesConstructor.__prepare_vp_annotation_with_expression_in_ttl(
                variation_point_configuration, unique_variation_point_id)
        return prepared_ttl

    @staticmethod
    def __prepare_variation_points_ttl(evolved_product_line_id: str, variation_point_data_location: Optional[str]) -> str:
        prepared_ttl = ""
        if variation_point_data_location:
            with open(variation_point_data_location, "r", encoding="utf-8") as file:
                variation_points_configuration = json.loads(file.read())
                for variation_point_configuration in variation_points_configuration:
                    prepared_ttl += TurtleTriplesConstructor.__prepare_variation_point_in_ttl(
                        variation_point_configuration, evolved_product_line_id)
        return prepared_ttl

    @staticmethod
    def __process_configuration_expression(
            configuration_expression: Dict, parent_configuration_expression_id: str) -> str:
        prepared_ttl = ""
        for name, configuration_json in configuration_expression.items():
            prepared_ttl += TurtleTriplesConstructor.__process_configuration_expression_rec(
                name, configuration_json, parent_configuration_expression_id)
        return prepared_ttl

    @staticmethod
    def __is_operator(object_type: str) -> bool:
        if object_type.upper() == "AND" or object_type.upper() == "OR":
            return True
        return False

    @staticmethod
    def __process_configuration_expression_rec(object_type: str, configuration_expression: Dict,
                                               parent_configuration_expression_id: str) -> str:
        prepared_ttl = ""
        if isinstance(configuration_expression, Dict):
            configuration_expression_id = TurtleTriplesConstructor.normalize_configuration_expression(
                json.dumps(configuration_expression))
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
                prepared_ttl += TurtleTriplesConstructor.__process_configuration_expression_rec(
                    name, configuration_json, configuration_expression_id)
        elif TurtleTriplesConstructor.__is_operator(object_type):
            prepared_ttl += f"""
                <{parent_configuration_expression_id}> faspls:belongsToFeature "{object_type}" .
            """
        return prepared_ttl

    @staticmethod
    def __prepare_code_ttl(code_location: Optional[str]) -> str:
        prepared_ttl = ""
        if code_location:
            code_uuid = uuid.uuid4()[:5]
            code_representation_id = "code_" + code_uuid
            prepared_ttl += f"""
                <{code_representation_id}> a faspls:Code .

                <{code_representation_id}> faspls:location "{code_location}" .
            """
        return prepared_ttl

    @staticmethod
    def __prepare_raster_ttl(raster_location: Optional[str]) -> str:
        prepared_ttl = ""
        if raster_location:
            raster_uuid = uuid.uuid4()[:5]
            raster_representation_id = "raster_" + raster_uuid
            prepared_ttl += f"""
                <{raster_representation_id}> a faspls:Screenshot .

                <{raster_representation_id}> faspls:location "{raster_location}" .
            """
        return prepared_ttl

    @staticmethod
    def __prepare_vector_ttl(vector_location: Optional[str]) -> str:
        prepared_ttl = ""
        if vector_location:
            vector_uuid = uuid.uuid4()[:5]
            vector_representation_id = "svg_" + vector_uuid
            prepared_ttl += f"""
                <{vector_representation_id}> a faspls:SVG .

                <{vector_representation_id}> faspls:location "{vector_location}" .
            """
        return prepared_ttl

    @staticmethod
    def __prepare_graph_ttl(graph_location: Optional[str]) -> str:
        prepared_ttl = ""
        if graph_location:
            graph_uuid = uuid.uuid4()[:5]
            graph_representation_id = "graph_" + graph_uuid
            prepared_ttl = f"""
                <{graph_representation_id}> a faspls:InstanceGraph .

                <{graph_representation_id}> faspls:location "{graph_location}" .
            """
        return prepared_ttl
