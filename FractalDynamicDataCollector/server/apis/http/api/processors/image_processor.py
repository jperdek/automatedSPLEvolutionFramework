import io
import os
import sys
from typing import Optional, Tuple

from PIL import Image
from io import BytesIO

# for Windows only
if hasattr(sys, "getwindowsversion"):
    os.environ["PATH"] += r";" + os.getcwd() + "\\vips-dev-8.12\\bin"
import pyvips


class ImageProcessor:
    @staticmethod
    def automatically_trim_and_save_image(
        image_content: bytes,
        image_location: str,
        max_size: Optional[tuple] = None,
        added_borders: int = 15,
        color_to_trip=None,
    ) -> None:
        if color_to_trip is None:
            color_to_trip = [51, 51, 51]
        image_content = ImageProcessor.trim_image_according_observed_boundaries(
            image_content, added_borders=added_borders, color_to_trip=color_to_trip
        )
        ImageProcessor.save_image_using_PIL(image_content, image_location, max_size)

    @staticmethod
    def automatically_trim_and_get_image(
            image_content: bytes,
            added_borders: int = 15,
            color_to_trip=None,
    ) -> bytes:
        if color_to_trip is None:
            color_to_trip = [51, 51, 51]
        image_content = ImageProcessor.trim_image_according_observed_boundaries(
            image_content, added_borders=added_borders, color_to_trip=color_to_trip
        )
        return image_content

    @staticmethod
    def convert_from_svg(image_content: bytes) -> bytes:
        u_image_file = pyvips.Image.new_from_buffer(
            image_content, "", access="sequential"
        )
        image_file = u_image_file.write_to_buffer(".png")
        return image_file

    @staticmethod
    def __clip_borders(image_content: bytes, number_pixels: int = 15) -> bytes:
        image_file = BytesIO(image_content)
        image = Image.open(image_file)
        width, height = image.size

        new_image = image.crop(
            (
                number_pixels,
                number_pixels,
                width - number_pixels,
                height - number_pixels,
            )
        )

        image.close()
        del image
        image_file.close()
        del image_file
        img_byte_arr = io.BytesIO()
        new_image.save(img_byte_arr, format="PNG")
        return img_byte_arr.getvalue()

    @staticmethod
    def trim_image_according_observed_boundaries(
        image_content: bytes,
        access: str = "random",
        cropped_borders: int = 15,
        added_borders: int = 15,
        color_to_trip=None,
        threshold: int = 5,
        additionally_increase_with: int = 50,
        square: bool = False,
    ) -> bytes:
        if color_to_trip is None:
            color_to_trip = [51, 51, 51]
        if cropped_borders > 0:
            image_content = ImageProcessor.__clip_borders(
                image_content, cropped_borders
            )
        u_image_file = pyvips.Image.new_from_buffer(
            image_content, "", access=access
        )  # access="sequential"
        original_height = u_image_file.height

        original_width = u_image_file.width
        left, top, width, height = u_image_file[0].find_trim(
            background=color_to_trip, threshold=threshold
        )

        if left != 0 and left - added_borders >= 0:
            left = left - added_borders
            width = width + added_borders
        if top != 0 and top - added_borders >= 0:
            top = top - added_borders
            height = height + added_borders
        if width != original_width and width + added_borders <= original_width:
            width = width + added_borders
        if height != original_height and height + added_borders <= original_height:
            height = height + added_borders

        width = width + additionally_increase_with
        height = height + additionally_increase_with
        if square and width > height:
            height = width
        else:
            width = height
        if left + width > original_width:
            left = original_width - width

        if top + height > original_height:
            top = original_height - height

        left = 0 if left <= 0 else left
        top = 0 if top <= 0 else top
        width = original_width if left + width > original_width else width
        height = original_height if top + height > original_height else height

        u_image_file = u_image_file.crop(left, top, width, height)
        image_file = u_image_file.write_to_buffer(".png")
        return image_file

    @staticmethod
    def increase_image_size(
        image_file: BytesIO, max_size: (int, int), color: Optional[Tuple[int, int, int]] = None
    ) -> Image:
        if color is None:
            color = (51, 51, 51)
        image = Image.open(image_file)
        image = image.convert("RGB")
        width, height = image.size

        if width > max_size[0]:
            raise Exception(
                "Image width: "
                + str(width)
                + " is greater then is expected given by: "
                + str(max_size[0])
            )
        if height > max_size[1]:
            raise Exception(
                "Image height: "
                + str(height)
                + " is greater then is expected given by: "
                + str(max_size[1])
            )

        left = int((max_size[0] - width) / 2.0)
        top = int((max_size[1] - height) / 2.0)
        result = Image.new("RGB", max_size, color)

        result.paste(image, (left, top))
        image.close()
        del image
        return result

    @staticmethod
    def save_image_using_PIL(
        image_content: bytes, image_location: str, max_size: Optional[tuple] = None
    ) -> None:
        image_file = BytesIO(image_content)
        if max_size:
            image = ImageProcessor.increase_image_size(image_file, max_size)
        else:
            image = Image.open(image_file)
            image = image.convert("RGB")

        image.save(image_location, quality=100)

        image.close()
        del image
        image_file.close()
        del image_file
