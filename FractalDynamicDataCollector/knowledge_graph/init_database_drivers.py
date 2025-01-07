import os

from server.apis.http.api.semantic_base.knowledge_graph.graph_knowledge_base_api import GraphKnowledgeBaseAPI


class FullyAutomatedProductLinesKnowledgeManager(GraphKnowledgeBaseAPI):
    def __init__(self):
        self.database_name = os.environ.get("KNOWLEDGE_DB_NAME", "evolutionKnowledgeBase")
        self.database_user = os.environ.get("KNOWLEDGE_BASE_USERNAME", "neo4j")
        self.database_password = os.environ.get("KNOWLEDGE_BASE_PASSWORD", "feature")
        self.database_bolt = os.environ.get("CO_OCCURRENCE_DB_BOLT", "bolt://localhost:7688")

        super().__init__(self.database_bolt, self.database_user, self.database_password, self.database_name)
