import csv
import os
from keras.utils import img_to_array
from keras.models import load_model
import numpy as np
import cv2

# construct the argument parse and parse the arguments
FINAL_IMAGE_SIZE = (600, 600)
MODEL_PATH = "./modelMulti.model"
DATASET_CSV_TEST_PATH = "../aestheticAssignment/sources/annotated_test.csv"
DATASET_IMAGES_PATH = "../aestheticAssignment/images"
RESULTS_CSV_PATH = "./results.csv"
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


model = load_model(MODEL_PATH)

records = []
for image_name in image_paths:
    image_path = os.path.join(DATASET_IMAGES_PATH, image_name)
    image = cv2.imread(image_path)  # load the image, pre-process it, and store it in the data list
    image = cv2.resize(image, FINAL_IMAGE_SIZE)
    image = image.astype("float") / 255.0
    image = img_to_array(image)
    image = np.expand_dims(image, axis=0)

    # load the trained convolutional neural network
    print("[Loading the network model...")

    record_results = model.predict(image)[0]
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


with open(RESULTS_CSV_PATH, "w", encoding="utf-8-sig", newline='') as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=records[0].keys(), delimiter=";")
    writer.writeheader()
    writer.writerows(records)

