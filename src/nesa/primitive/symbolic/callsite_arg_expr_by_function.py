from primitive.primitive import UnaryPrimitive
from typing import Dict, Set
from parser.program_parser import *
import json


class CallsiteArgExprByFunctionPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("CallsiteArgExprByFunctionPrimitive", "symbolic")
        config_file = (
            Path(__file__).resolve().parent.parent.parent
            / "config"
            / "symbolic"
            / "unary"
            / "sink_expr.json"
        )
        with open(config_file, "r") as f:
            self.args = json.load(f)
        self.function_name = self.args["function"]

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1
        arg_set = set([])
        for function_id in ts_analyzer.environment:
            file_id = ts_analyzer.ts_parser.functionToFile[function_id]
            source_code = ts_analyzer.ts_parser.fileContentDic[file_id]

            function = ts_analyzer.environment[function_id]
            root_node = function.parse_tree_root_node

            nodes = TSAnalyzer.find_nodes_by_type(
                root_node, ts_analyzer.node_types.method_invocation
            )
            for node in nodes:
                if (
                    self.function_name + "("
                    not in source_code[node.start_byte : node.end_byte]
                ):
                    continue
                if ts_analyzer.extract_name_from_node(node, source_code) == "":
                    continue
                for sub_node in node.children:
                    if sub_node.type == ts_analyzer.node_types.argument_list:
                        line_number = source_code[: sub_node.start_byte].count("\n") + 1
                        name = source_code[
                            sub_node.start_byte + 1 : sub_node.end_byte - 1
                        ]
                        arg_set.add(
                            Expr(
                                name,
                                line_number,
                                ts_analyzer.ts_parser.functionToFile[function_id],
                            )
                        )

        self.cache = arg_set
        self.is_transformed = True
        return arg_set
