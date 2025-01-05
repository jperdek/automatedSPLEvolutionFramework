
class TripleCallNeosemanticsFactory:

    @staticmethod
    def define_prefix_pointing_to_namespace(prefix_name: str, namespace: str = "https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl") -> str:
        return f"""
            CALL n10s.nsprefixes.add("{prefix_name}", "{namespace}");
        """

    @staticmethod
    def import_from_file(url: str, triple_format: str = "Turtle") -> str:
        return f"""
            CALL n10s.rdf.import.fetch("{url}","{triple_format}");
        """

    @staticmethod
    def import_from_text(triples: str, triple_format: str = "Turtle") -> str:
        return f"""
            CALL n10s.rdf.import.inline("{triples}", "{triple_format}")
        """

