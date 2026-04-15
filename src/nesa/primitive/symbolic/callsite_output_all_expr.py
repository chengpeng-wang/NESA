from primitive.primitive import UnaryPrimitive
from typing import Dict, Set
from parser.program_parser import *


class CallsiteOutputAllExprPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("CallsiteOutputAllExprPrimitive", "symbolic")

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
                call_nodes = TSAnalyzer.find_nodes_by_type(
                    node, ts_analyzer.node_types.method_invocation
                )
                if len(call_nodes) == 0:
                    continue
                name_node = ts_analyzer.find_name_node(node)
                if name_node is None:
                    continue
                name = source_code[name_node.start_byte : name_node.end_byte]
                line_number = source_code[: name_node.start_byte].count("\n") + 1
                output_set.add(
                    Expr(
                        name,
                        line_number,
                        ts_analyzer.ts_parser.functionToFile[function_id],
                    )
                )
        self.cache = output_set
        self.is_transformed = True
        return output_set
