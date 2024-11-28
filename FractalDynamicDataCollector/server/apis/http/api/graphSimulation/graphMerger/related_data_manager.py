from server.apis.http.api.graphSimulation.graphMerger.merge_configuration import (
    MergeConfiguration,
    StrategyOnIntersection,
)
from typing import Optional


class RelatedDataManager:
    @staticmethod
    def process_instance_related_data(
        instance_related_data: dict,
        node_content: dict,
        merge_configuration: Optional[MergeConfiguration],
        counter_name: str = "__counter__",
    ) -> None:
        if not instance_related_data:
            return
        if (
            merge_configuration.strategy_on_intersection
            == StrategyOnIntersection.NOTHING
        ):
            return
        if counter_name not in node_content.keys():
            node_content[counter_name] = 1
        else:
            node_content[counter_name] = node_content[counter_name] + 1
        for key, value in instance_related_data.items():
            if key == "pointsTo":
                continue
            if key not in node_content.keys():
                if (
                    merge_configuration.strategy_on_intersection is None
                    or merge_configuration.strategy_on_intersection
                    == StrategyOnIntersection.CONCAT
                ):
                    if key not in node_content.keys():
                        node_content[key] = []
                        node_content[key].append(value)
                else:
                    node_content[key] = value
            elif (
                merge_configuration.strategy_on_intersection
                == StrategyOnIntersection.CONCAT
            ):
                node_content[key].append(value)
            elif (
                merge_configuration.strategy_on_intersection
                == StrategyOnIntersection.OVERWRITE
            ):
                node_content[key] = value
