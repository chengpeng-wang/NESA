from primitive.primitive import UnaryPrimitive
from typing import Dict, Set
from parser.program_parser import *
import json


class CallsiteOutputExprByFunctionPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("CallsiteOutputExprByFunctionPrimitive", "symbolic")
        config_file = (
            Path(__file__).resolve().parent.parent.parent
            / "config"
            / "symbolic"
            / "unary"
            / "source_expr.json"
        )
        with open(config_file, "r") as f:
            self.args = json.load(f)
        self.function_name = self.args["function"]

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        output_set = set([])
        for function_id in ts_analyzer.environment:
            file_id = ts_analyzer.ts_parser.functionToFile[function_id]
            source_code = ts_analyzer.ts_parser.fileContentDic[file_id]

            function = ts_analyzer.environment[function_id]
            root_node = function.parse_tree_root_node

            nodes = TSAnalyzer.find_nodes_by_type(
                root_node, ts_analyzer.node_types.assignment_expression
            )
            nodes.extend(
                TSAnalyzer.find_nodes_by_type(
                    root_node, ts_analyzer.node_types.variable_declarator
                )
            )

            for node in nodes:
                candidate_nodes = TSAnalyzer.find_nodes_by_type(
                    node, ts_analyzer.node_types.method_invocation
                )
                if ts_analyzer.node_types.object_creation_expression is not None:
                    candidate_nodes.extend(
                        TSAnalyzer.find_nodes_by_type(
                            node, ts_analyzer.node_types.object_creation_expression
                        )
                    )
                if (
                    len(candidate_nodes) == 0
                    or self.function_name + "("
                    not in source_code[node.start_byte : node.end_byte]
                ):
                    continue

                name_node = ts_analyzer.find_name_node(node)
                if name_node is None:
                    continue
                line_number = source_code[: name_node.start_byte].count("\n") + 1
                output_set.add(
                    Expr(
                        source_code[name_node.start_byte : name_node.end_byte],
                        line_number,
                        ts_analyzer.ts_parser.functionToFile[function_id],
                    )
                )

        self.cache = output_set
        self.is_transformed = True

        return output_set
