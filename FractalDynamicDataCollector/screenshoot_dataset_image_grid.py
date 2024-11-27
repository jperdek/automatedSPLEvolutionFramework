from server.apis.http.api.screenshoting.screenshooter import PlaywrightScreenshooter
from server.apis.http.api.processors.image_processor import ImageProcessor
from typing import Optional
from PIL import Image
import io
import os


class DatasetScreenshooterToImageGrid:
    def __init__(self, resolution: (int, int) = (2560, 1440)) -> None:
        self.screenshooter = PlaywrightScreenshooter("chromium", resolution)
        self.page = self.screenshooter.new_page()

    def process_dataset(
        self,
        dataset_directory_path: str,
        final_location_path: str = "./generated_dataset",
        max_size: Optional[tuple] = (3000, 3000),
        result_image_size: Optional[tuple] = (300, 300),
    ) -> None:
        absolute_dataset_path = os.path.abspath(dataset_directory_path)
        absolute_final_path = os.path.abspath(final_location_path)

        final_image_path = os.path.join(absolute_final_path, "final_grid.png")
        sources_to_screenshot = []
        for derivation_name in os.listdir(absolute_dataset_path):
            project_path = os.path.join(absolute_dataset_path, derivation_name)
            project_page_path = os.path.join(project_path, "index.html")
            sources_to_screenshot.append(project_page_path)

        new_im = Image.new("RGB", max_size)

        index = 0
        for i in range(0, max_size[0], result_image_size[0]):
            for j in range(0, max_size[1], result_image_size[1]):
                if index >= len(sources_to_screenshot):
                    break
                project_page_path = sources_to_screenshot[index]
                self.page.goto(project_page_path)
                image_bytes = self.screenshooter.take_screenshoot_according_locator(
                    "#game"
                )
                image_bytes = ImageProcessor.trim_image_according_observed_boundaries(
                    image_bytes
                )
                im = Image.open(io.BytesIO(image_bytes))
                im.thumbnail(result_image_size)
                new_im.paste(im, (i, j))
                index += 1
            if index >= len(sources_to_screenshot):
                break

        new_im.save(final_image_path)

    def test(self) -> None:
        screenshooter_test = self.screenshooter
        page_test = self.page
        screenshooter_test.load_page_from_disc_actual_position("./example/1/index.html")

        image_test = self.screenshooter.take_screenshoot_according_locator("#game")
        image_test = ImageProcessor.trim_image_according_observed_boundaries(image_test)
        ImageProcessor.save_image_using_PIL(image_test, "image.png", (600, 600))
        page_test.close()
        # screenshooter_test.close()

    def close(self) -> None:
        self.page.close()
        self.screenshooter.close()


if __name__ == "__main__":
    test = False
    dataset_screenshooter = DatasetScreenshooterToImageGrid()
    if test:
        dataset_screenshooter.test()
    else:
        dataset_screenshooter.process_dataset(
            "E://aspects/src/derived",
            max_size=(1500, 2500),
            result_image_size=(100, 100),
        )
    # dataset_screenshooter.close()
