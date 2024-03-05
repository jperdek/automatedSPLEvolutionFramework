# USAGE
# python test_network.py --model aesthetic_not_aesthetic.model --image abc.jpg
# import the necessary packages
from keras.models import load_model
from keras.utils import img_to_array
import numpy as np
import os.path
import cv2


def prepare_model(path_to_model: str) -> any:
    absolute_path_to_model = os.path.abspath(path_to_model)
    model = load_model(absolute_path_to_model)
    return model


def evaluate_image(path_to_image: str, model: any) -> (float, float):
    absolute_path_to_image = os.path.abspath(path_to_image)
    # load the image
    image = cv2.imread(absolute_path_to_image)

    # pre-process the image for classification
    image = cv2.resize(image, (28, 28))
    image = image.astype("float") / 255.0
    image = img_to_array(image)
    image = np.expand_dims(image, axis=0)

    # classify the input image
    (not_aesthetic, aesthetic) = model.predict(image)[0]
    return not_aesthetic, aesthetic
