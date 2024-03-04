
class MergeAggregations:
    SAME_COUNTER = "same_counter"

    @staticmethod
    def mark_the_same_method(node_content: dict) -> None:
        if MergeAggregations.SAME_COUNTER not in node_content.keys():
            node_content[MergeAggregations.SAME_COUNTER] = 1
        node_content[MergeAggregations.SAME_COUNTER] = node_content[MergeAggregations.SAME_COUNTER] + 1

    @staticmethod
    def mark_the_original_method(node_content: dict) -> None:
        if MergeAggregations.SAME_COUNTER not in node_content.keys():
            node_content[MergeAggregations.SAME_COUNTER] = 1
