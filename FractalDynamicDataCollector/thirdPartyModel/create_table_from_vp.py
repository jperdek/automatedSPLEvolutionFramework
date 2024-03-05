import csv
import json


class VariationPointTableCreator:
    @staticmethod
    def load_json(path_to_json: str):
        with open(path_to_json, "r", encoding="utf-8") as file:
            return json.loads(file.read())

    @staticmethod
    def transform_variation_point_data(
        analyzed_json: dict, analyzed_variable: str, related_variables: list[str]
    ) -> (list[dict], list[str]):
        harvested_values = []
        field_names = {"name": "name"}
        for file_name, file_content in analyzed_json.items():
            file_name = file_name.replace(".json", ".png")
            content = {"name": file_name}
            for analyzed_value_name, value_variable_config in file_content[
                analyzed_variable
            ].items():
                for (
                    tuple_related_variable_name,
                    tuple_related_variable_config,
                ) in value_variable_config.items():
                    for (
                        related_variable_name,
                        related_variable_list,
                    ) in tuple_related_variable_config.items():
                        if related_variable_name in related_variables:
                            for index, value in enumerate(related_variable_list):
                                whole_variable_name = (
                                    analyzed_variable
                                    + "_"
                                    + analyzed_value_name
                                    + "_"
                                    + related_variable_name
                                    + "_"
                                    + str(index)
                                )
                                content.update({whole_variable_name: value})
                                field_names[whole_variable_name] = whole_variable_name
            harvested_values.append(content)
        return harvested_values, list(field_names.keys())

    @staticmethod
    def write_data_to_csv(file_csv_path: str, data: list[dict], field_names: list[str]):
        with open(file_csv_path, "w", encoding="UTF8", newline="") as f:
            writer = csv.DictWriter(f, fieldnames=field_names, delimiter=";")
            writer.writeheader()
            writer.writerows(data)

    @staticmethod
    def process_json(
        path_to_analyzed_json: str,
        path_to_result_csv: str,
        analyzed_variable: str,
        related_variables: list[str],
    ) -> None:
        loaded_data = VariationPointTableCreator.load_json(path_to_analyzed_json)
        (
            transformed_data,
            field_names,
        ) = VariationPointTableCreator.transform_variation_point_data(
            loaded_data, analyzed_variable, related_variables
        )
        VariationPointTableCreator.write_data_to_csv(
            path_to_result_csv, transformed_data, field_names
        )


if __name__ == "__main__":
    table_creator = VariationPointTableCreator()
    table_creator.process_json(
        "./aestheticAssignment/sources/test.json",
        "./aestheticAssignment/sources/vp_multi.csv",
        "iteration",
        ["centerX", "centerY"],
    )
