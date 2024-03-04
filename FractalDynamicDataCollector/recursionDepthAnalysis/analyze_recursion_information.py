from typing import Union
import re as regexp
import json
import csv
import os


class RecursionCollector:
    TYPES_DEF = {"double": r"\d+\.?\d+", "integer": r"\d+", "string": "[^_]+", "boolean": "(?:false|true)"}

    @staticmethod
    def __construct_type_search_expression(used_type: str) -> str:
        variable_search_formula = "_("
        for type, type_search_value in RecursionCollector.TYPES_DEF.items():
            if used_type == "all" or not used_type or type in used_type:
                variable_search_formula = variable_search_formula + type_search_value + "|"
        if variable_search_formula.endswith("|"):
            variable_search_formula = variable_search_formula[:-1]
        variable_search_formula = variable_search_formula + ")_"
        return variable_search_formula

    @staticmethod
    def extract_associated_variable_from_pair_in_key(program_identifier: str, variability_point_data: dict,
                                                     variable_name: str, variable_type: str = "all",
                                                     allowed_other_types: str = "all") -> (list[dict], list[str]):
        variable_search_expression = "_" + variable_name + \
                                     RecursionCollector.__construct_type_search_expression(variable_type)
        analyzed_third_dependency = r"([^_]+)_" + \
                                    RecursionCollector.__construct_type_search_expression(allowed_other_types)

        results = []
        result_names = ["Name", variable_name]
        for variable_combination in variability_point_data.keys():
            dependent_variables_part = "_" + variable_combination.split("_____")[1]
            search_expression = variable_search_expression + "__" + analyzed_third_dependency
            variable_value, other_variable_name, other_variable_value = regexp.search(
                search_expression, dependent_variables_part).groups()
            variable_template = RecursionCollector.__generate_variable_template(
                program_identifier, variable_name, variable_value)
            variable_template[other_variable_name] = other_variable_value
            if other_variable_name not in result_names:
                result_names.append(other_variable_name)
            results.append(variable_template)
        return results, result_names

    @staticmethod
    def __generate_variable_template(program_identifier: str, variable_name: str,
                                     variable_value: Union[int, str]) -> dict:
        variable_template = {"Name": program_identifier, variable_name: variable_value}
        return variable_template

    @staticmethod
    def extract_variable_values_from_combination_of_key(program_identifier: str, variable_name: str,
                                                        collected_data: dict, variable_type: str = "all",
                                                        allowed_other_types: str = "all",
                                                        number_related: int = 1) -> (list[dict], list[str]):
        variable_search_expression = "_" + variable_name + \
                                     RecursionCollector.__construct_type_search_expression(variable_type)
        analyzed_third_dependency = r"([^_]+)" + \
                                    RecursionCollector.__construct_type_search_expression(allowed_other_types)

        results = []
        result_names = ["Name", variable_name]
        search_expression = "____" + variable_search_expression
        while number_related > 0:
            search_expression = search_expression + "_" + analyzed_third_dependency
            number_related = number_related - 1
        if search_expression.endswith("_"):
            search_expression = search_expression[:-1] + "$"
        for variable_combination in collected_data.keys():
            dependencies_regexp = regexp.search(r"" + search_expression, variable_combination)
            if not dependencies_regexp:
                continue
            variables_data = dependencies_regexp.groups()
            if len(variables_data) == 0:
                continue
            variable_value = variables_data[0]
            variable_template = RecursionCollector.__generate_variable_template(
                program_identifier, variable_name, variable_value)
            for other_variable_name, other_variable_value in zip(variables_data[1::2], variables_data[2::2]):
                variable_template[other_variable_name] = other_variable_value
                if other_variable_name not in result_names:
                    result_names.append(other_variable_name)
                results.append(variable_template)

        return results, result_names

    @staticmethod
    def __load_variability_point_data(filename: str) -> dict:
        with open(filename, "r", encoding="utf-8") as file:
            file_data = json.loads(file.read())
        return file_data

    @staticmethod
    def evaluate_dataset(path_to_dataset: str, analyzed_variable_name: str = "iteration",
                         number_related_ones: int = 1, #8
                         variability_point_name: str = "1-1-1") -> (list[dict], list[str]):
        absolute_path_to_dataset = os.path.abspath(path_to_dataset)

        results = []
        column_names = []
        for file_name in os.listdir(path_to_dataset):
            path_to_program_json = os.path.join(absolute_path_to_dataset, file_name)
            print(path_to_program_json)
            program_variability_point_data = RecursionCollector.__load_variability_point_data(path_to_program_json)

            result, column_names = RecursionCollector.extract_variable_values_from_combination_of_key(
                file_name, analyzed_variable_name, program_variability_point_data[variability_point_name],
                "all", "all", number_related_ones)
            results = results + result
        return results, column_names

    @staticmethod
    def create_csv_from_results(file_csv_path: str, data: list[dict], field_names: list[str]) -> None:
        with open(file_csv_path, 'w', encoding='UTF8') as f:
            writer = csv.DictWriter(f, fieldnames=field_names)
            writer.writeheader()
            writer.writerows(data)


if __name__ == "__main__":
    evaluator = RecursionCollector()
    results_all, result_column_names = evaluator.evaluate_dataset(
        "E://aspects/spaProductLine/fractalDynamicDataCollector/generated_dataset_vp_data")
    evaluator.create_csv_from_results("harvested_data_point.csv", results_all, result_column_names)
