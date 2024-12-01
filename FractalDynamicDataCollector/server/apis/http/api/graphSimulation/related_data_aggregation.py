import csv
import json
from typing import List, Dict


def evaluate_average(values: str) -> float:
    summ = 0
    for value in json.loads(values.replace("'", '"')):
        summ = summ + float(value.replace(",", "."))
    return summ / len(values)


def evaluate_most_frequent(values: str) -> float:
    mapping = {}
    for value in json.loads(values.replace("'", '"')):
        extracted_value = value.replace(",", ".")
        if extracted_value not in mapping.keys():
            mapping[extracted_value] = 0
        mapping[extracted_value] = mapping[extracted_value] + 1
    maximum = -1
    max_value = -1
    for extracted_value, number_used in mapping.items():
        if int(number_used) > maximum:
            maximum = number_used
            max_value = float(extracted_value)
    return max_value


def prepare_weights(
    base_record: Dict,
    keys_for_weights: List[str],
    analyzed_content: str,
    associated_name: str = "perceivedAesthetics",
) -> None:
    variables = {}
    analyzed_content = json.loads(analyzed_content.replace("'", '"'))
    for variable_name in keys_for_weights:
        variables[variable_name] = 0.0
    for analyzed_variable in analyzed_content:
        variables[analyzed_variable] = variables[analyzed_variable] + 1
    for variable_name in keys_for_weights:
        variables[variable_name] = variables[variable_name] / len(analyzed_content)
        base_record[associated_name + "_" + variable_name] = variables[variable_name]


def aggregate_related_data(
    input_path: str, output_path: str, make_average: bool = True
) -> None:
    data = []
    with open(input_path, "r", encoding="utf-8") as file:
        reader_dict = csv.DictReader(file, delimiter="$")
        for row in reader_dict:
            if make_average:
                row["Aesthetic"] = evaluate_average(row["Aesthetic"])
                row["NotAesthetic"] = evaluate_average(row["NotAesthetic"])
            else:
                row["Aesthetic"] = evaluate_most_frequent(row["Aesthetic"])
                row["NotAesthetic"] = evaluate_most_frequent(row["NotAesthetic"])
            prepare_weights(
                row,
                ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"],
                row["perceivedAesthetics"],
                "perceivedAesthetics",
            )
            prepare_weights(
                row,
                ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"],
                row["perceivedChaos"],
                "perceivedChaos",
            )
            headers = list(row.keys())
            data.append(row)
    with open(output_path, "w", encoding="utf-8", newline="") as file:
        writer_dict = csv.DictWriter(file, fieldnames=headers, delimiter="$")
        writer_dict.writeheader()
        writer_dict.writerows(data)


if __name__ == "__main__":
    aggregate_related_data(
        "../generated_dataset_vp_graph_data_merged/function drawWCurve.csv",
        "drawWCurveProcessed1.csv",
        True,
    )
    aggregate_related_data(
        "../generated_dataset_vp_graph_data_merged/function drawWCurve.csv",
        "drawWCurveProcessed2.csv",
        False,
    )
