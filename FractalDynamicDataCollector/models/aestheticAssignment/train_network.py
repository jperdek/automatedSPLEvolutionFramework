# USAGE
# python train_network.py --dataset images_native --model aesthetic_not_aesthetic.model
# USED https://github.com/kayveen/pyimagesearch/blob/master/nn/conv/lenet.py
# FROM https://github.com/vatsal-rooprai/Image-Aesthetic-Evaluation


# set the matplotlib backend so figures can be saved in the background
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


# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-d", "--dataset", required=True,
                help="path to input dataset")
ap.add_argument("-m", "--model", required=True,
                help="path to output model")
ap.add_argument("-p", "--plot", type=str, default="plot.png",
                help="path to output loss/accuracy plot")
args = vars(ap.parse_args())

# initialize the number of epochs to train for, initia learning rate,
# and batch size
EPOCHS = 25  # Feed forward + back propagation
INIT_LR = 1e-3  # alpha : learning rate const
BS = 32

# initialize the data and labels
print("Now loading images_native...")
data = []
labels = []

# grab the image paths and randomly shuffle them
imagePaths = sorted(list(paths.list_images(args["dataset"])))
random.seed(42)
random.shuffle(imagePaths)


# loop over the input images_native
for imagePath in imagePaths:
    image = cv2.imread(imagePath)  # load the image, pre-process it, and store it in the data list
    image = cv2.resize(image, (580, 580))
    image = img_to_array(image)
    data.append(image)

    # extract the class label from the image path and update the
    # labels list
    label = imagePath.split(os.path.sep)[-2]
    label = 1 if label == "aesthetic" else 0
    labels.append(label)

# scale the raw pixel intensities to the range [0, 1]
data = np.array(data, dtype="float") / 255.0
labels = np.array(labels)

# partition the data into training and testing splits using 75% of
# the data for training and the remaining 25% for testing
(trainX, testX, trainY, testY) = train_test_split(data,
                                                  labels, test_size=0.25, random_state=42)

# convert the labels from integers to vectors
trainY = to_categorical(trainY, num_classes=2)
testY = to_categorical(testY, num_classes=2)

# construct the image generator for data augmentation
aug = ImageDataGenerator(rotation_range=30, width_shift_range=0.1,
                         height_shift_range=0.1, shear_range=0.2, zoom_range=0.2,
                         horizontal_flip=True, fill_mode="nearest")

# initialize the model
print("[Now compiling model...")
model = LeNet.build(width=28, height=28, depth=3, classes=2)
opt = Adam(lr=INIT_LR, decay=INIT_LR / EPOCHS)
# opt = SGD(lr=INIT_LR, decay=INIT_LR / EPOCHS)
model.compile(loss="binary_crossentropy", optimizer=opt,
              metrics=["accuracy"])
# A metric function is similar to a loss function, except that the results
# from evaluating a metric are not used when training the model.

# train the network
print("Now training of network started...")
H = model.fit_generator(aug.flow(trainX, trainY, batch_size=BS),
                        validation_data=(testX, testY), steps_per_epoch=len(trainX) // BS,
                        epochs=EPOCHS, verbose=1)  # Verbose : For ---> animation

# save the model to disk
print("Now serializing network...")
model.save(args["model"])

# plot the training loss and accuracy
plt.style.use("ggplot")
plt.figure()
N = EPOCHS
plt.plot(np.arange(0, N), H.history["loss"], label="train_loss")
plt.plot(np.arange(0, N), H.history["val_loss"], label="val_loss")
plt.plot(np.arange(0, N), H.history["acc"], label="train_acc")
plt.plot(np.arange(0, N), H.history["val_acc"], label="val_acc")
plt.title("Training Loss and Accuracy on Aesthetic/Not Aesthetic")
plt.xlabel("Epoch #")
plt.ylabel("Loss/Accuracy")
plt.legend(loc="lower left")
plt.savefig(args["plot"])
