from aestheticAssignment.test_network_script import prepare_model, evaluate_image
import csv
import os


class Evaluator:
    @staticmethod
    def evaluate_dataset(path_to_dataset: str, path_to_model: str) -> list[dict]:
        absolute_path_to_dataset = os.path.abspath(path_to_dataset)
        model = prepare_model(path_to_model)

        results = []
        for image_name in os.listdir(path_to_dataset):
            path_to_image = os.path.join(absolute_path_to_dataset, image_name)
            not_aesthetic, aesthetic = evaluate_image(path_to_image, model)
            result = {"Name": image_name, "Not Aesthetic": not_aesthetic, "Aesthetic": aesthetic}
            results.append(result)
        return results

    @staticmethod
    def create_csv_from_results(file_csv_path: str, data: list[dict], field_names: list[str]) -> None:
        with open(file_csv_path, 'w', encoding='UTF8') as f:
            writer = csv.DictWriter(f, fieldnames=field_names)
            writer.writeheader()
            writer.writerows(data)


if __name__ == "__main__":
    evaluator = Evaluator()
    results = evaluator.evaluate_dataset("E://aspects/spaProductLine/fractalDynamicDataCollector/generated_dataset",
                                         "./aestheticAssignment/aesthetic_not_aesthetic.model")
    evaluator.create_csv_from_results("evaluated.csv", results, ["Name", "Not Aesthetic", "Aesthetic"])
