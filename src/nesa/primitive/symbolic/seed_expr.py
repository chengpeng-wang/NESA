from primitive.primitive import UnaryPrimitive
from typing import Set
from parser.program_parser import *
import json


class SeedExprPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        config_file = (
            Path(__file__).resolve().parent.parent.parent
            / "config"
            / "symbolic"
            / "unary"
            / "seed_expr.json"
        )
        with open(config_file, "r") as f:
            self.args = json.load(f)
        super().__init__("SeedExprPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        seed_expr_set = set([])
        for function_id in ts_analyzer.environment:
            function = ts_analyzer.environment[function_id]
            file_id = ts_analyzer.ts_parser.functionToFile[function_id]
            source_code = ts_analyzer.ts_parser.fileContentDic[file_id]

            nodes = TSAnalyzer.find_nodes_by_type(
                function.parse_tree_root_node, ts_analyzer.node_types.identifier
            )
            for node in nodes:
                if source_code[node.start_byte : node.end_byte] == self.args["name"]:
                    line_number = source_code[: node.start_byte].count("\n") + 1
                    file_path = ts_analyzer.ts_parser.functionToFile[function_id]
                    if line_number == int(self.args["loc"]):
                        seed_expr_set.add(
                            Expr(self.args["name"], line_number, file_path)
                        )
        self.cache = seed_expr_set
        self.is_transformed = True
        return seed_expr_set
