import time
from typing import Dict, Tuple

from flask import Blueprint, request
import json

from screenshoting.screenshooter import PlaywrightScreenshooter


def json_response(payload: Dict, status: int = 200) -> Tuple[str, int, Dict]:
    return json.dumps(payload), status, {"content-type": "application/json" }


def text_response(payload: str, status: int = 200) -> Tuple[str, int, Dict]:
    return payload, status, {"content-type": "text/plain"}


singlefile_api = Blueprint("singlefile_api", __name__, template_folder="templates")


@singlefile_api.route("/url", methods=["GET"])
def prepare_content_from_page():
    path_or_url = request.args.get("url")
    time_to_wait = int(request.args.get("time_to_wait", 10))
    screenshooter = PlaywrightScreenshooter("chromium")

    page = screenshooter.new_page()
    page.goto(path_or_url, wait_until="networkidle")
    time.sleep(time_to_wait)
    mhtml = page.content()
    try:
        screenshooter.close()
    except:
        pass
    return text_response(str(mhtml))


@singlefile_api.route("/mhtml", methods=["GET"])
def prepare_mhtml_from_page():
    path_or_url = request.args.get("url")
    time_to_wait = int(request.args.get("time_to_wait", 10))
    screenshooter = PlaywrightScreenshooter("chromium")
    page = screenshooter.new_page()

    page.goto(path_or_url, wait_until="networkidle")
    time.sleep(time_to_wait)
    client = page.context.new_cdp_session(page)
    mhtml = client.send("Page.captureSnapshot", {"format": "mhtml"})["data"]
    try:
        screenshooter.close()
    except:
        pass
    return text_response(str(mhtml))


@singlefile_api.route("/mhtml-single-file", methods=["GET"])
def prepare_mhtml_from_single_file():
    path_or_url = request.args.get("url")
    time_to_wait = int(request.args.get("time_to_wait", 10))
    screenshooter = PlaywrightScreenshooter("chromium")
    page = screenshooter.new_page()

    page.goto(path_or_url, wait_until="networkidle")
    with open("r", "single-file.txt", encoding="utf-8") as file:
        page.evaluate(file.read())
    content = page.evaluate("""(async () => {
        const { content, title, filename } = await singlefile.getPageData({
            removeImports: false,
            removeScripts: false,
            removeAudioSrc: false,
            removeVideoSrc: false 
        });
        console.log(content);
        const url = "data:application/x-mimearchive;base64," + btoa(content);
        return content
    })();""")
    time.sleep(time_to_wait)
    try:
        screenshooter.close()
    except:
        pass
    return text_response(str(content))