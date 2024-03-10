# USAGE
# python train_network.py --dataset images_native --model aesthetic_not_aesthetic.model
# USED https://github.com/kayveen/pyimagesearch/blob/master/nn/conv/lenet.py
# FROM https://github.com/vatsal-rooprai/Image-Aesthetic-Evaluation


# set the matplotlib backend so figures can be saved in the background
import csv


# import the necessary packages
from keras.applications.densenet import layers
from keras.optimizers import Adam

# import keras.optimizers
from sklearn.model_selection import (
    train_test_split,
)  # randomly split the dataset into training and testing from tensorflow.keras.utils import img_to_array #Convert the image to array
from keras.utils import (
    to_categorical,
    img_to_array,
)  # Converts a class vector (integers) to binary class matrix.
import matplotlib.pyplot as plt
import numpy as np
import random
import cv2
import os

from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Activation, Flatten, Dense
from keras import backend as K, Model, Input

from aestheticAssignment.sources.load_tuples import (
    load_data,
    convert_tuples_to_vectors_with_filtering,
)


class LeNetExtended:
    @staticmethod
    def build_input_parts(
        associated_variable_names: list[str], max_value_array_size: int, max_variables=5
    ) -> (list[layers], list[layers]):
        all_new_end_layers = []
        all_new_start_layers = []
        for associated_variable_name in associated_variable_names:
            new_layer_input = Input(
                batch_shape=(None, max_value_array_size),
                dtype="float32",
                name=associated_variable_name,
            )
            new_layer = layers.Dense(
                32, activation="relu", name=associated_variable_name + "dense"
            )(new_layer_input)

            new_layer = Flatten()(new_layer)
            all_new_end_layers.append(new_layer)
            all_new_start_layers.append(new_layer_input)
        return all_new_start_layers, all_new_end_layers

    # [Conv=>relu ==>pool] * 2 =>fc=>softmax
    @staticmethod
    def build(
        width,
        height,
        depth,
        classes,
        input_layers_start: list[layers],
        input_layers_end: list[layers],
    ):
        inputShape = (height, width, depth)

        if K.image_data_format() == "channels_first":
            inputShape = (depth, height, width)

        input_layer = Input(shape=inputShape, name="base_image_layer")
        main_pipeline = Conv2D(20, kernel_size=(5, 5), padding="same")(input_layer)
        main_pipeline = Activation("relu")(main_pipeline)
        main_pipeline = MaxPooling2D(strides=(2, 2))(main_pipeline)
        main_pipeline = Conv2D(50, kernel_size=(5, 5), padding="same")(main_pipeline)
        main_pipeline = Activation("relu")(main_pipeline)
        main_pipeline = MaxPooling2D(strides=(2, 2))(main_pipeline)
        main_pipeline = Flatten()(main_pipeline)
        main_pipeline = Dense(500)(main_pipeline)
        main_pipeline2 = Activation("relu")(main_pipeline)
        input_layers_end.append(main_pipeline2)
        concatenated = layers.concatenate(input_layers_end, axis=-1)

        merged_pipeline = Dense(classes)(concatenated)

        pipeline_end = Activation("softmax")(merged_pipeline)
        # pipeline_end = Activation("softmax")(main_pipeline2)
        input_layers_start.insert(0, input_layer)
        multiple_input_model = Model(input_layers_start, pipeline_end)
        return multiple_input_model


# initialize the number of epochs to train for, initia learning rate,
# and batch size
EPOCHS = 50  # Feed forward + back propagation
INIT_LR = 1e-3  # alpha : learning rate const
FINAL_IMAGE_SIZE = (600, 600)
MODEL_PATH = "modelMulti.model"
DATASET_CSV_TRAIN_PATH = "../aestheticAssignment/sources/annotated_train.csv"
VARIATION_POINT_DATA_PATH = "../aestheticAssignment/sources/test.json"
DATASET_IMAGES_PATH = "../aestheticAssignment/images"
USE_VALIDATION_SET = False
BS = 32

# initialize the data and labels
print("Now loading images_native...")
data = []
labels = []

# grab the image paths and randomly shuffle them
random.seed(42)
labels = []
image_paths = []


number_classes_dict = {}
with open(DATASET_CSV_TRAIN_PATH, "r", encoding="utf-8-sig") as csvfile:
    reader = csv.DictReader(csvfile, delimiter=";")
    for line in reader:
        image_paths.append(line["Name"])
        aesthetic_assignment = line["perceivedAesthetics"]
        labels.append(line["perceivedAesthetics"])
        number_classes_dict[aesthetic_assignment] = aesthetic_assignment
number_classes = len(number_classes_dict.keys())

# loop over the input images_native
loaded_image_names_in_order = []
for image_name in image_paths:
    loaded_image_names_in_order.append(image_name)
    image_path = os.path.join(DATASET_IMAGES_PATH, image_name)
    image = cv2.imread(
        image_path
    )  # load the image, pre-process it, and store it in the data list
    image = cv2.resize(image, FINAL_IMAGE_SIZE)
    image = img_to_array(image)
    data.append(image)

# scale the raw pixel intensities to the range [0, 1]
data = np.array(data, dtype="float") / 255.0
labels = np.array(labels)
number_classes = 10
MAX_VALUE_ARRAY_SIZE = 100
# convert the labels from integers to vectors

# initialize the model
print("[Now compiling model...")
print(len(loaded_image_names_in_order))
number_samples = len(loaded_image_names_in_order)
input_layers_start, input_layers_end = LeNetExtended.build_input_parts(
    ["centerX", "centerY"], max_value_array_size=MAX_VALUE_ARRAY_SIZE
)
model = LeNetExtended.build(
    width=FINAL_IMAGE_SIZE[0],
    height=FINAL_IMAGE_SIZE[1],
    depth=3,
    classes=number_classes,
    input_layers_start=input_layers_start,
    input_layers_end=input_layers_end,
)
opt = Adam(learning_rate=INIT_LR, decay=INIT_LR / EPOCHS)
# opt = SGD(lr=INIT_LR, decay=INIT_LR / EPOCHS)
model.compile(loss="categorical_crossentropy", optimizer=opt, metrics=["accuracy"])

variation_points_data = load_data(VARIATION_POINT_DATA_PATH)
centerX_vector = convert_tuples_to_vectors_with_filtering(
    loaded_image_names_in_order,
    variation_points_data,
    "centerX",
    "iteration",
    MAX_VALUE_ARRAY_SIZE,
)
centerY_vector = convert_tuples_to_vectors_with_filtering(
    loaded_image_names_in_order,
    variation_points_data,
    "centerY",
    "iteration",
    MAX_VALUE_ARRAY_SIZE,
)
print(centerX_vector)
if USE_VALIDATION_SET:
    # partition the data into training and testing splits using 75% of
    # the data for training and the remaining 25% for testing
    (trainX, testX, trainY, testY) = train_test_split(
        data, labels, test_size=0.25, random_state=42
    )

    # convert the labels from integers to vectors
    trainY = to_categorical(trainY, num_classes=number_classes)
    testY = to_categorical(testY, num_classes=number_classes)
    # A metric function is similar to a loss function, except that the results
    # from evaluating a metric are not used when training the model.
    history = model.fit(
        trainX, trainY, batch_size=BS, validation_data=(testX, testY), epochs=EPOCHS
    )
else:
    trainY = to_categorical(labels, num_classes=number_classes)

    # A metric function is similar to a loss function, except that the results
    # from evaluating a metric are not used when training the model.
    print("FFFFFFFFFFFFFFFITTTTTTTIMNG")
    history = model.fit(
        {
            "base_image_layer": data,
            "centerX": centerX_vector,  # tf.reshape(centerX_vector, shape=(number_samples, 1)),
            "centerY": centerY_vector,  # tf.reshape(centerY_vector, shape=(number_samples, 1))
        },
        trainY,
        batch_size=BS,
        epochs=EPOCHS,
    )

# save the model to disk
print("Now serializing network...")
model.save(MODEL_PATH)

loss = history.history["loss"]
val_loss = history.history["val_loss"]

epochs = range(1, len(loss) + 1)

plt.plot(epochs, loss, "bo", label="Trénovacia strata")
plt.plot(epochs, val_loss, "b", label="Validačná strata")
plt.title("Trénovacia a validačná strata")
plt.xlabel("Epochs")
plt.ylabel("Loss")
plt.legend()
plt.show()
plt.savefig("./model_loss.png")

acc = history.history["accuracy"]
val_acc = history.history["validation_accuracy"]

epochs = range(1, len(acc) + 1)

plt.plot(epochs, acc, "bo", label="Trénovacia správnosť")
plt.plot(epochs, val_acc, "b", label="Validačná správnosť")
plt.title("Trénovacia a validačná správnosť")
plt.xlabel("Epochs")
plt.ylabel("Accuracy")
plt.legend()
plt.savefig("./model_acc.png")
