from screenshooter import PlaywrightScreenshooter
from image_processor import ImageProcessor
from typing import Optional
import os


class DatasetScreenshooter:
    def __init__(self, resolution: (int, int) = (2560, 1440)) -> None:
        self.screenshooter = PlaywrightScreenshooter("chromium", resolution)
        self.page = self.screenshooter.new_page()

    def process_dataset(self, dataset_directory_path: str,
                        final_location_path: str = "./generated_dataset",
                        max_size: Optional[tuple] = (600, 600), added_borders: int = 35) -> None:
        absolute_dataset_path = os.path.abspath(dataset_directory_path)
        absolute_final_path = os.path.abspath(final_location_path)

        for derivation_name in os.listdir(absolute_dataset_path):
            project_path = os.path.join(absolute_dataset_path, derivation_name)
            project_page_path = os.path.join(project_path, "index.html")
            self.page.goto(project_page_path)
            image = self.screenshooter.take_screenshoot_according_locator("#game")
            image_name = derivation_name + ".png"
            final_image_path = os.path.join(absolute_final_path, image_name)
            ImageProcessor.automatically_trim_and_save_image(image, final_image_path, max_size,
                                                             added_borders=added_borders)

    def test(self) -> None:
        screenshooter_test = self.screenshooter
        page_test = self.page
        screenshooter_test.load_page_from_disc_actual_position("./example/1/index.html")

        image_test = self.screenshooter.take_screenshoot_according_locator("#game")
        image_test = ImageProcessor.trim_image_according_observed_boundaries(image_test)
        ImageProcessor.save_image_using_PIL(image_test, "image.png", (600, 600))
        page_test.close()
        #screenshooter_test.close()

    def close(self) -> None:
        self.page.close()
        self.screenshooter.close()


if __name__ == "__main__":
    test = False
    dataset_screenshooter = DatasetScreenshooter()
    if test:
        dataset_screenshooter.test()
    else:
        dataset_screenshooter.process_dataset("E://aspects/src/derived", max_size=None)
    # dataset_screenshooter.close()
