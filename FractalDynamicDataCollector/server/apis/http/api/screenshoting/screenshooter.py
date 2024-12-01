from playwright.sync_api import sync_playwright, Page
import os


class PlaywrightScreenshooter:
    AVAILABLE_ENGINES = ["chromium", "firefox", "webkit"]

    def __init__(
        self, engine_name: str, resolution: (int, int) = (2560, 1440), **arguments
    ) -> None:
        self.playwright_screenshooter = sync_playwright().start()
        self.engine_name = engine_name
        self.resolution = {"width": resolution[0], "height": resolution[1]}
        self.actual_page = None
        # launching
        self.launched_browser = getattr(
            self.playwright_screenshooter, self.engine_name
        ).launch(**arguments)
        self.browser = None

    def launch(self, arguments: {}) -> None:
        chromium = self.playwright_screenshooter.chromium  # or "firefox" or "webkit".
        self.browser = chromium.launch()

    def new_page(self) -> Page:
        print("Used resolution: " + str(self.resolution))
        self.actual_page = self.launched_browser.new_page(viewport=self.resolution)
        return self.actual_page

    def take_screenshoot_according_locator(
        self, focus_element_selector: str = "body", timeout: int = 30000
    ) -> bytes:
        # arguments = {"type": "png", "quality": 100}
        locator = self.actual_page.locator(focus_element_selector)
        return locator.screenshot(timeout=timeout)

    def close(self) -> None:
        self.playwright_screenshooter.stop()
        if self.launched_browser.is_connected():
            self.launched_browser.close()

    def load_page_from_disc_actual_position(self, path: str) -> None:
        absolute_path = os.path.abspath(path)
        if not self.actual_page:
            self.new_page()
        self.actual_page.goto(absolute_path)
