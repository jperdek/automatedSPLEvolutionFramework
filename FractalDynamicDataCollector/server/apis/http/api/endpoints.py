import sys
from flask import Flask
import flask_cors
from screenshot.take_screenshot_api import screenshot_api
from tools.single_file_adaptations import singlefile_api as tools_api
from graph_extractor.extract_graph_api import graph_extractor_api

app = Flask(__name__, static_url_path='',
            static_folder='web/static',
            template_folder='web/templates')

flask_cors.CORS(app)
app.register_blueprint(screenshot_api, url_prefix="/api/screenshoter")
app.register_blueprint(tools_api, url_prefix="/api/tools")
app.register_blueprint(graph_extractor_api, url_prefix="/api/graph-extraction")


with app.app_context():
    print('Preparing for requests execution...')

    print('Preparation completed successfully!')


def launch():
    app.run(host="0.0.0.0", debug=False)


if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "production":
        app.run(host="0.0.0.0", debug=False, port=8080)
    else:
        app.run(host="0.0.0.0", debug=True)
