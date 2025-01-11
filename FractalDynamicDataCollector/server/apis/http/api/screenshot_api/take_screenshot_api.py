import time
from typing import Tuple, Dict

from flask import Blueprint, request

from processors.image_processor import ImageProcessor
from screenshoting.screenshooter import PlaywrightScreenshooter


def image_png_response(payload: bytes, status: int = 200) -> Tuple[str, int, Dict]:
    return payload, status, {"content-type": "image/png"}


screenshot_api = Blueprint("screenshoter_api", __name__, template_folder="templates")


@screenshot_api.route("/mhtml", methods=["POST"])
def take_screenshot_from_mhtml():
    mhtml = request.get_data().decode("utf-8", "ignore")
    focus_element_selector = request.args.get("screenshot_element_selector", "body")
    browser_timeout = request.args.get("browser_timeout", 10000)
    image_borders = request.args.get("image_borders", 35)

    screenshooter = PlaywrightScreenshooter("chromium")
    page = screenshooter.new_page()
    page.set_content(mhtml, timeout=browser_timeout)
    image_content = screenshooter.take_screenshoot_according_locator(
        focus_element_selector=focus_element_selector, timeout=browser_timeout
    )
    resulting_image = ImageProcessor.automatically_trim_and_get_image(
        image_content, added_borders=image_borders
    )
    try:
        screenshooter.close()
    except:
        pass
    return image_png_response(resulting_image)


@screenshot_api.route("/screenshoter", methods=["GET"])
def take_screenshot_from_page():
    web_page_location = request.args.get("url")
    focus_element_selector = request.args.get("screenshot_element_selector", "body")
    browser_timeout = request.args.get("browser_timeout", 10000)
    time_to_wait = int(request.args.get("time_to_wait", 10))
    image_borders = request.args.get("image_borders", 35)

    screenshooter = PlaywrightScreenshooter("chromium")
    page = screenshooter.new_page()
    page.goto(web_page_location, wait_until="networkidle")
    time.sleep(time_to_wait)
    image_content = screenshooter.take_screenshoot_according_locator(
        focus_element_selector=focus_element_selector, timeout=browser_timeout
    )
    resulting_image = ImageProcessor.automatically_trim_and_get_image(
        image_content, added_borders=image_borders
    )
    try:
        screenshooter.close()
    except:
        pass
    return image_png_response(resulting_image)
