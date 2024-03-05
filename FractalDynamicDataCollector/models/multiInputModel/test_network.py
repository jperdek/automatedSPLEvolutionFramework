import csv
import os
from keras.utils import img_to_array
from keras.models import load_model
import numpy as np
import cv2

# construct the argument parse and parse the arguments
from aestheticAssignment.sources.load_tuples import (
    convert_tuples_to_vectors_with_filtering,
    load_data,
)

FINAL_IMAGE_SIZE = (600, 600)
MODEL_PATH = "./modelMulti.model"
DATASET_CSV_TEST_PATH = "../aestheticAssignment/sources/annotated_test.csv"
DATASET_IMAGES_PATH = "../aestheticAssignment/images"
VARIATION_POINT_DATA_PATH = "../aestheticAssignment/sources/test.json"
RESULTS_CSV_PATH = "./results.csv"
MAX_VALUE_ARRAY_SIZE = 100
BS = 32
data = []
labels = []
image_paths = []


number_classes_dict = {}
with open(DATASET_CSV_TEST_PATH, "r", encoding="utf-8-sig") as csvfile:
    reader = csv.DictReader(csvfile, delimiter=";")
    for line in reader:
        image_paths.append(line["Name"])
number_classes = len(number_classes_dict.keys())

# loop over the input images_native
loaded_image_names_in_order = []
for image_name in image_paths:
    loaded_image_names_in_order.append(image_name)

variation_points_data = load_data(VARIATION_POINT_DATA_PATH)
centerX_vector, dimension = convert_tuples_to_vectors_with_filtering(
    loaded_image_names_in_order,
    variation_points_data,
    "centerX",
    "iteration",
    MAX_VALUE_ARRAY_SIZE,
)
centerY_vector, dimension2 = convert_tuples_to_vectors_with_filtering(
    loaded_image_names_in_order,
    variation_points_data,
    "centerY",
    "iteration",
    MAX_VALUE_ARRAY_SIZE,
)


model = load_model(MODEL_PATH)

records = []
for index, image_name in enumerate(image_paths):
    image_path = os.path.join(DATASET_IMAGES_PATH, image_name)
    image = cv2.imread(
        image_path
    )  # load the image, pre-process it, and store it in the data list
    image = cv2.resize(image, FINAL_IMAGE_SIZE)
    image = image.astype("float") / 255.0
    image = img_to_array(image)
    image = np.expand_dims(image, axis=0)

    record_results = model.predict(
        {
            "base_image_layer": image,
            "centerX": np.reshape(
                centerX_vector[index], (1, dimension[1], dimension[2])
            ),  # tf.reshape(centerX_vector, shape=(number_samples, 2)),
            "centerY": np.reshape(
                centerY_vector[index], (1, dimension[1], dimension[2])
            ),  # tf.reshape(centerY_vector, shape=(number_samples, 2))
        }
    )[0]
    # classify the input image
    record = {"name": image_name}
    highest = -1
    highest_category = "-1"
    for category_identifier, category_probability in enumerate(record_results):
        record[str(category_identifier)] = category_probability
        if highest < category_probability:
            highest = category_probability
            highest_category = str(category_identifier)
    record["category"] = highest_category
    record["category_value"] = highest
    records.append(record)


with open(RESULTS_CSV_PATH, "w", encoding="utf-8-sig", newline="") as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=records[0].keys(), delimiter=";")
    writer.writeheader()
    writer.writerows(records)
