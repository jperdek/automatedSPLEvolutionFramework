import csv
from sklearn.model_selection import train_test_split


DATASET_CSV_PATH = "./annotated.csv"
DATASET_CSV_TRAIN_PATH = "./annotated_train.csv"
DATASET_CSV_TEST_PATH = "./annotated_test.csv"
# initialize the data and labels
print("Now loading images_native...")

data = []
labels = []

number_classes_dict = {}
with open(DATASET_CSV_PATH, "r", encoding="utf-8-sig") as csvfile:
    reader = csv.DictReader(csvfile, delimiter=";")
    for line in reader:
        data.append(line)
        labels.append(line["perceivedAesthetics"])

(trainX, testX, trainY, testY) = train_test_split(
    data, labels, test_size=0.40, random_state=42
)

with open(DATASET_CSV_TRAIN_PATH, "w", encoding="utf-8-sig", newline="") as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=trainX[0].keys(), delimiter=";")
    writer.writeheader()
    writer.writerows(trainX)

with open(DATASET_CSV_TEST_PATH, "w", encoding="utf-8-sig", newline="") as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=testX[0].keys(), delimiter=";")
    writer.writeheader()
    writer.writerows(testX)
