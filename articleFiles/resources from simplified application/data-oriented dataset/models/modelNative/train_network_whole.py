# USAGE
# python train_network.py --dataset images_native --model aesthetic_not_aesthetic.model
# USED https://github.com/kayveen/pyimagesearch/blob/master/nn/conv/lenet.py
# FROM https://github.com/vatsal-rooprai/Image-Aesthetic-Evaluation


# set the matplotlib backend so figures can be saved in the background
import csv

import matplotlib

matplotlib.use("Agg")  # "Agg "Will save the plot as a png, and will not pop up on the screen
import keras
# import the necessary packages
from keras.preprocessing.image import \
    ImageDataGenerator  # Generate batches of tensor image data with real-time data augmentation
from keras.optimizers import Adam
# import keras.optimizers
from keras.optimizers import \
    SGD  # schotastic gradient desecnt : Optimizer for deep neural net --> SGD gives a constant learning rate
from sklearn.model_selection import \
    train_test_split  # randomly split the dataset into training and testing from tensorflow.keras.utils import img_to_array #Convert the image to array
from keras.utils import to_categorical, img_to_array  # Converts a class vector (integers) to binary class matrix.
from imutils import paths
import matplotlib.pyplot as plt
import numpy as np
import argparse
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

        model.add(Conv2D(
            20,
            kernel_size=(5, 5),
            padding="same",
            input_shape=inputShape
        ))
        model.add(Activation("relu"))
        model.add(MaxPooling2D(strides=(2, 2)))
        model.add(Conv2D(
            50,
            kernel_size=(5, 5),
            padding="same"
        ))
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
EPOCHS = 25  # Feed forward + back propagation
INIT_LR = 1e-3  # alpha : learning rate const
FINAL_IMAGE_SIZE = (600, 600)
MODEL_PATH = "./modelNative.model"
DATASET_CSV_PATH = "../aestheticAssignment/sources/annotated.csv"
DATASET_IMAGES_PATH = "../aestheticAssignment/images"
EPOCHS = 1
BS = 32

# initialize the data and labels
print("Now loading images_native...")
data = []
labels = []

# grab the image paths and randomly shuffle them
random.seed(42)
#random.shuffle(imagePaths)
labels = []
image_paths = []


number_classes_dict = {}
with open(DATASET_CSV_PATH, "r", encoding="utf-8-sig") as csvfile:
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
    image = cv2.imread(image_path)  # load the image, pre-process it, and store it in the data list
    image = cv2.resize(image, FINAL_IMAGE_SIZE)
    image = img_to_array(image)
    data.append(image)

# scale the raw pixel intensities to the range [0, 1]
data = np.array(data, dtype="float") / 255.0
labels = np.array(labels)

number_classes = 10
# convert the labels from integers to vectors
trainY = to_categorical(trainY, num_classes=number_classes)
testY = to_categorical(testY, num_classes=number_classes)


# initialize the model
print("[Now compiling model...")
model = LeNet.build(width=FINAL_IMAGE_SIZE[0], height=FINAL_IMAGE_SIZE[1], depth=3, classes=number_classes)
opt = Adam(learning_rate=INIT_LR, decay=INIT_LR / EPOCHS)
# opt = SGD(lr=INIT_LR, decay=INIT_LR / EPOCHS)
model.compile(loss="categorical_crossentropy", optimizer=opt,
              metrics=["accuracy"])
# A metric function is similar to a loss function, except that the results
# from evaluating a metric are not used when training the model.
history = model.fit(trainX, trainY, epochs=EPOCHS)

# save the model to disk
print("Now serializing network...")
model.save(MODEL_PATH)

results = model.evaluate(testX, testY)
print('Training loss: ', results[0])
print('Test accuracy: ', results[1])

print(history.history.keys())
print(history.history)
loss = history.history['loss']
val_loss = history.history['validation_loss']

epochs = range(1, len(loss) + 1)

plt.plot(epochs, loss, 'bo', label='Trénovacia strata')
plt.plot(epochs, val_loss, 'b', label='Validačná strata')
plt.title('Trénovacia a validačná strata')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()
plt.show()

acc = history.history['acc']
val_acc = history.history['validation_acc']

epochs = range(1, len(acc) + 1)

plt.plot(epochs, acc, 'bo', label='Trénovacia správnosť')
plt.plot(epochs, val_acc, 'b', label="Validačná správnosť")
plt.title('Trénovacia a validačná správnosť')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.legend()

