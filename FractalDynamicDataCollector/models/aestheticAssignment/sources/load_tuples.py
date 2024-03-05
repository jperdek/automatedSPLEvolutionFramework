import csv
import json

import numpy as np


def load_given_tuples(
    path_to_variation_points: str, observed_variable_name: None
) -> dict:
    file_with_variation_points = {}
    with open(path_to_variation_points, "r", encoding="utf-8-sig") as csvfile:
        reader = csv.DictReader(csvfile, delimiter=",")
        field_names = reader.fieldnames
        print(field_names)
        for line in reader:
            file_name = line[field_names[0]]
            if not observed_variable_name:
                observed_variable_name = field_names[1]
            observed_variable_value = line[observed_variable_name]
            observed_variable_value_str = str(observed_variable_value)

            if file_name not in file_with_variation_points.keys():
                file_with_variation_points[file_name] = {}
            if observed_variable_name not in file_with_variation_points[file_name]:
                file_with_variation_points[file_name][observed_variable_name] = {}
            if (
                observed_variable_value
                not in file_with_variation_points[file_name][observed_variable_name]
            ):
                file_with_variation_points[file_name][observed_variable_name][
                    observed_variable_value_str
                ] = {}

            associated_values = {}
            for field_name in field_names[1:]:
                if field_name != observed_variable_name:
                    if (
                        str(line[field_name])
                        and field_name not in associated_values.keys()
                    ):
                        associated_values[field_name] = line[field_name]

            tuple_length = "tuple__" + "_".join(associated_values.keys())
            if (
                tuple_length
                not in file_with_variation_points[file_name][observed_variable_name][
                    observed_variable_value_str
                ].keys()
            ):
                file_with_variation_points[file_name][observed_variable_name][
                    observed_variable_value_str
                ][tuple_length] = {}
            for field_name, field_value in associated_values.items():
                if (
                    field_name
                    not in file_with_variation_points[file_name][
                        observed_variable_name
                    ][observed_variable_value_str][tuple_length].keys()
                ):
                    file_with_variation_points[file_name][observed_variable_name][
                        observed_variable_value_str
                    ][tuple_length][field_name] = [field_value]
                else:
                    file_with_variation_points[file_name][observed_variable_name][
                        observed_variable_value_str
                    ][tuple_length][field_name].append(field_value)
    return file_with_variation_points


def load_given_tuples(
    path_to_variation_points: str, observed_variable_name: None
) -> dict:
    file_with_variation_points = {}
    with open(path_to_variation_points, "r", encoding="utf-8-sig") as csvfile:
        reader = csv.DictReader(csvfile, delimiter=",")
        field_names = reader.fieldnames
        print(field_names)
        for line in reader:
            file_name = line[field_names[0]]
            if not observed_variable_name:
                try:
                    observed_variable_name = int(field_names[1])
                except:
                    observed_variable_name = field_names[1]
            observed_variable_value = line[observed_variable_name]
            observed_variable_value_str = str(observed_variable_value)

            if file_name not in file_with_variation_points.keys():
                file_with_variation_points[file_name] = {}
            if observed_variable_name not in file_with_variation_points[file_name]:
                file_with_variation_points[file_name][observed_variable_name] = {}
            if (
                observed_variable_value
                not in file_with_variation_points[file_name][observed_variable_name]
            ):
                file_with_variation_points[file_name][observed_variable_name][
                    observed_variable_value_str
                ] = {}

            associated_values = {}
            for field_name in field_names[1:]:
                if field_name != observed_variable_name:
                    if (
                        str(line[field_name])
                        and field_name not in associated_values.keys()
                    ):
                        try:
                            associated_values[field_name] = float(line[field_name])
                        except:
                            associated_values[field_name] = line[field_name]
            tuple_length = "tuple__" + "_".join(associated_values.keys())
            if (
                tuple_length
                not in file_with_variation_points[file_name][observed_variable_name][
                    observed_variable_value_str
                ].keys()
            ):
                file_with_variation_points[file_name][observed_variable_name][
                    observed_variable_value_str
                ][tuple_length] = {}
            for field_name, field_value in associated_values.items():
                if (
                    field_name
                    not in file_with_variation_points[file_name][
                        observed_variable_name
                    ][observed_variable_value_str][tuple_length].keys()
                ):
                    file_with_variation_points[file_name][observed_variable_name][
                        observed_variable_value_str
                    ][tuple_length][field_name] = [field_value]
                else:
                    file_with_variation_points[file_name][observed_variable_name][
                        observed_variable_value_str
                    ][tuple_length][field_name].append(field_value)
    return file_with_variation_points


def convert_tuples_to_vectors(
    file_with_variation_points: dict,
    associated_variable_name: str,
    target_variable_name: str = "iteration",
) -> list:
    prepared_data_per_record = []
    for file_name, data in file_with_variation_points.items():
        for target_variable_value, target_variable_data in data[
            target_variable_name
        ].items():
            key = "tuple__" + associated_variable_name
            record = (
                target_variable_value,
                target_variable_data[key][associated_variable_name],
            )
            prepared_data_per_record.append(record)
    return prepared_data_per_record


def convert_tuples_to_vectors_with_filtering2(
    filtered_file_names: list[str],
    file_with_variation_points: dict,
    associated_variable_name: str,
    target_variable_name: str = "iteration",
    array_size: int = 100,
) -> list:
    prepared_data_per_record = []
    for file_name, data in file_with_variation_points.items():
        file_name = file_name.replace(".json", ".png")
        if file_name in filtered_file_names:
            index_in_array = filtered_file_names.index(file_name)
            for target_variable_value, target_variable_data in data[
                target_variable_name
            ].items():
                if target_variable_value == "0":
                    key = "tuple__" + associated_variable_name
                    # record = (int(target_variable_value), np.asfarray(target_variable_data[key][associated_variable_name]))
                    record_array = np.asfarray(
                        target_variable_data[key][associated_variable_name][:array_size]
                    )
                    record_array.resize(array_size)
                    # float(target_variable_value), validation_split=0.2
                    record = record_array

                    prepared_data_per_record.insert(index_in_array, record)
                    # prepared_data_per_record.insert(index_in_array, record)
    return np.stack(prepared_data_per_record)


def convert_tuples_to_vectors_with_filtering(
    filtered_file_names: list[str],
    file_with_variation_points: dict,
    associated_variable_name: str,
    target_variable_name: str = "iteration",
    array_size: int = 100,
) -> (list, tuple):
    all_values = {}
    for file_name, data in file_with_variation_points.items():
        file_name = file_name.replace(".json", ".png")
        if file_name in filtered_file_names:
            for target_variable_value in data[target_variable_name].keys():
                all_values[target_variable_value] = target_variable_value

    processed_files = 0
    prepared_data_per_record = {}
    for file_name, data in file_with_variation_points.items():
        file_name = file_name.replace(".json", ".png")
        if file_name in filtered_file_names:
            processed_files = processed_files + 1
            index_in_array = filtered_file_names.index(file_name)
            found_values = {}
            for target_variable_value, target_variable_data in data[
                target_variable_name
            ].items():
                if target_variable_value not in prepared_data_per_record:
                    prepared_data_per_record[target_variable_value] = []
                found_values[target_variable_value] = target_variable_value
                key = "tuple__" + associated_variable_name
                record_array = np.asfarray(
                    target_variable_data[key][associated_variable_name][:array_size]
                )
                record_array.resize(array_size)
                record = record_array

                prepared_data_per_record[target_variable_value].insert(
                    index_in_array, record
                )
            for target_value_to_check in all_values.keys():
                if target_value_to_check not in found_values.keys():
                    record_array = np.full(array_size, 0.0)
                    if target_value_to_check not in prepared_data_per_record:
                        prepared_data_per_record[target_value_to_check] = []
                    prepared_data_per_record[target_value_to_check].insert(
                        index_in_array, record_array
                    )

    whole_array = None
    for target_variable_value, values in prepared_data_per_record.items():
        if whole_array is None:
            whole_array = np.stack(values)
        else:
            whole_array = np.concatenate((whole_array, np.stack(values)))
    return (
        whole_array.reshape((processed_files, array_size, len(all_values.keys()))),
        (processed_files, array_size, len(all_values.keys())),
    )


def save_data(data: dict, data_file_path: str) -> None:
    with open(data_file_path, "w", encoding="utf-8") as file:
        file.write(json.dumps(data))


def load_data(data_file_path: str) -> dict:
    with open(data_file_path, "r", encoding="utf-8") as file:
        return json.loads(file.read())


if __name__ == "__main__":
    PATH_TO_VARIATION_POINTS_FILE = "./harvested_data_point.csv"
    variation_points_data = load_given_tuples(PATH_TO_VARIATION_POINTS_FILE, None)
    save_data(variation_points_data, "test.json")
