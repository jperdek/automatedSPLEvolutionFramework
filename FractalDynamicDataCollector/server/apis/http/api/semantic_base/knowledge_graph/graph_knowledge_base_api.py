import types
from typing import Optional, Tuple, List

import neo4j


class GraphKnowledgeBaseAPI:
    def __init__(
        self, uri: str, user: str, password: str, database_name: str = "neo4j"
    ) -> None:
        self.driver = neo4j.GraphDatabase.driver(uri, auth=(user, password))
        self.database_name = database_name

    def close(self) -> None:
        self.driver.close()

    def create_knowledge_db(self, db_name: Optional[str] = None) -> None:
        if not db_name:
            db_name = self.database_name
        with self.driver.session() as session:
            creation = session.write_transaction(self._create_db, db_name)
            print(creation)

    def process_data_transaction(
        self, data: Tuple, used_function: any, db_name: Optional[str] = None
    ):
        if not db_name:
            db_name = self.database_name
        with self.driver.session() as session:
            creation = session.write_transaction(used_function, *data, db_name)
        return creation

    def process_data_transaction_using_commands(self, command: str):
        with self.driver.session() as session:
            creation = session.write_transaction(self.__run_transaction, command)
            print(creation)
        return creation

    def process_data_transaction_using_commands_block(self, command: str):
        with self.driver.session() as session:
            creation = session.write_transaction(self.__run_transaction_block, command)
        return creation

    def process_data_transaction_without_arguments(
        self, used_function: any, db_name: Optional[str] = None
    ):
        if not db_name:
            db_name = self.database_name
        with self.driver.session() as session:
            creation = session.read_transaction(used_function, db_name)
        return creation

    def process_data_transactions(
        self, data: List, used_function: any, db_name: Optional[str] = None
    ):
        if not db_name:
            db_name = self.database_name
        with self.driver.session() as session:
            for data_part in data:
                creation = session.write_transaction(used_function, *data_part, db_name)
        return creation

    @staticmethod
    def _create_db(tx, db_name: str):
        result = tx.run("CREATE DATABASE {name} IF NOT EXISTS".format(name=db_name))
        return result.single()[0]

    def clear_database(self):
        self.process_data_transaction_using_commands("MATCH (a)-[r]->() DELETE a, r")
        self.process_data_transaction_using_commands("MATCH (a) DELETE a")

    @staticmethod
    def __run_transaction(tx, command: str):
        print(command.replace("\n", "' + \n'").strip("+'\" ").strip().strip("+' "))
        result = tx.run(
            command.replace("\n", "' + \n'").strip("+'\" ").strip().strip("+' ")
        )
        return result

    @staticmethod
    def __run_transaction_block(tx, command: str):
        print(command)
        result = tx.run(command)
        return result

    def get_data(self, db_name: Optional[str] = None):
        if not db_name:
            db_name = self.database_name
        with self.driver.session() as session:
            relations = session.write_transaction(self._list_relations, db_name)
            for relation in relations:
                print(relation)

    @staticmethod
    def _list_relations(tx, db_name: Optional[str] = None):
        result = tx.run("USE {name} MATCH (n) RETURN n LIMIT 5".format(name=db_name))
        return result


def get_properties(self):
    return self._properties


def set_properties(node):
    if node:
        node.get_properties = types.MethodType(get_properties, node)
    return node


if __name__ == "__main__":
    network = GraphKnowledgeBaseAPI(
        "bolt://localhost:7687", "neo4j", "perdekj", "neo4j"
    )
    # network = NetworkManager("bolt://localhost:7688", "neo4j", "neo4j1", "neo4j")
    # network.create_db_for_network()
    network.get_data()
    network.close()
