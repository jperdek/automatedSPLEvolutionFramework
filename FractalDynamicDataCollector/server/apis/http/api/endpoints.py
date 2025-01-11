import sys
from flask import Flask
import flask_cors

from screenshot_api.take_screenshot_api import screenshot_api
from semantic_base.default_knowledge_graph_api import default_knowledge_graph_api
from tools.single_file_adaptations import singlefile_api as tools_api
from graph_api.extract_graph_api import graph_extractor_api
from graph_api.nodes_and_connectors_api import nodes_and_connectors_api
from graph_database_api.graph_database_entry import graph_database_entry_api
from svg_transformation_api.svg_creator_api import svg_creator_api

app = Flask(
    __name__,
    static_url_path="",
    static_folder="web/static",
    template_folder="web/templates",
)

flask_cors.CORS(app)
app.register_blueprint(screenshot_api, url_prefix="/api/screenshoter")
app.register_blueprint(tools_api, url_prefix="/api/tools")
app.register_blueprint(graph_extractor_api, url_prefix="/api/graph-extraction")
app.register_blueprint(nodes_and_connectors_api, url_prefix="/api/nodes-and-connectors")
app.register_blueprint(graph_database_entry_api, url_prefix="/api/graph_db_entry")
app.register_blueprint(svg_creator_api, url_prefix="/api/svg_creator")
app.register_blueprint(default_knowledge_graph_api, url_prefix="/api/knowledge-base")


with app.app_context():
    print("Preparing for requests execution...")
    # g.fully_automated_product_lines_knowledge_manager = FullyAutomatedProductLinesKnowledgeManager()
    print("Preparation completed successfully!")


def launch():
    app.run(host="0.0.0.0", debug=False)


if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "production":
        app.run(host="0.0.0.0", debug=False, port=8080)
    else:
        app.run(host="0.0.0.0", debug=True)
