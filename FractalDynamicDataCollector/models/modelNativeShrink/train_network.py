# USAGE
# python train_network.py --dataset images_native --model aesthetic_not_aesthetic.model
# USED https://github.com/kayveen/pyimagesearch/blob/master/nn/conv/lenet.py
# FROM https://github.com/vatsal-rooprai/Image-Aesthetic-Evaluation


# set the matplotlib backend so figures can be saved in the background
import csv
import os

os.environ["PATH"] += os.pathsep + "C:/Program Files/Graphviz/bin/"
from keras.optimizers import Adam
from sklearn.model_selection import (
    train_test_split,
)  # randomly split the dataset into training and testing from tensorflow.keras.utils import img_to_array #Convert the image to array
from keras.utils import (
    to_categorical,
    img_to_array,
    plot_model,
)  # Converts a class vector (integers) to binary class matrix.
import matplotlib.pyplot as plt
import numpy as np
import random
import cv2
import os

from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Activation, Flatten, Dense
from keras import backend as K


class LeNet:
    # [Conv=>relu ==>pool] * 2 =>fc=>softmax
    @staticmethod
    def build(width, height, depth, classes):
        model = Sequential()
        inputShape = (height, width, depth)

        if K.image_data_format() == "channels_first":
            inputShape = (depth, height, width)

        model.add(
            Conv2D(20, kernel_size=(5, 5), padding="same", input_shape=inputShape)
        )
        model.add(Activation("relu"))
        model.add(MaxPooling2D(strides=(2, 2)))
        model.add(Conv2D(50, kernel_size=(5, 5), padding="same"))
        model.add(Activation("relu"))
        model.add(MaxPooling2D(strides=(2, 2)))
        model.add(Flatten())
        model.add(Dense(500))
        model.add(Activation("relu"))
        model.add(Dense(classes))
        model.add(Activation("softmax"))

        return model


# initialize the number of epochs to train for, initia learning rate,
# and batch size
EPOCHS = 50  # Feed forward + back propagation
INIT_LR = 1e-3  # alpha : learning rate const
FINAL_IMAGE_SIZE = (28, 28)
MODEL_PATH = "./modelNative.model"
DATASET_CSV_TRAIN_PATH = "../aestheticAssignment/sources/annotated_train.csv"
DATASET_IMAGES_PATH = "../aestheticAssignment/images"
USE_VALIDATION_SET = True
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
for image_name in image_paths:
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

# convert the labels from integers to vectors

# initialize the model
print("[Now compiling model...")
model = LeNet.build(
    width=FINAL_IMAGE_SIZE[0],
    height=FINAL_IMAGE_SIZE[1],
    depth=3,
    classes=number_classes,
)
opt = Adam(learning_rate=INIT_LR, decay=INIT_LR / EPOCHS)
# opt = SGD(lr=INIT_LR, decay=INIT_LR / EPOCHS)
model.compile(loss="categorical_crossentropy", optimizer=opt, metrics=["accuracy"])
plot_model(
    model, to_file="modelNativeShrink.png", show_shapes=True, show_layer_names=True
)

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
    history = model.fit(
        data, trainY, batch_size=BS, epochs=EPOCHS, validation_split=0.2
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
val_acc = history.history["val_accuracy"]

epochs = range(1, len(acc) + 1)

plt.plot(epochs, acc, "bo", label="Trénovacia správnosť")
plt.plot(epochs, val_acc, "b", label="Validačná správnosť")
plt.title("Trénovacia a validačná správnosť")
plt.xlabel("Epochs")
plt.ylabel("Accuracy")
plt.legend()
plt.savefig("./model_acc.png")
