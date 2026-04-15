from primitive.primitive import UnaryPrimitive
from typing import Set
from parser.program_parser import *


class XssSinkExprSymPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("XssSinkExprSymPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        xss_sink_set = set([])
        for function_id in ts_analyzer.environment:
            file_id = ts_analyzer.ts_parser.functionToFile[function_id]
            source_code = ts_analyzer.ts_parser.fileContentDic[file_id]

            function = ts_analyzer.environment[function_id]
            root_node = function.parse_tree_root_node

            nodes = TSAnalyzer.find_nodes_by_type(
                root_node, ts_analyzer.node_types.method_invocation
            )
            for node in nodes:
                if ts_analyzer.extract_name_from_node(node, source_code) != "println":
                    continue
                for sub_node in node.children:
                    if sub_node.type == ts_analyzer.node_types.argument_list:
                        line_number = source_code[: sub_node.start_byte].count("\n") + 1
                        name = source_code[
                            sub_node.start_byte + 1 : sub_node.end_byte - 1
                        ]
                        xss_sink_set.add(
                            Expr(
                                name,
                                line_number,
                                ts_analyzer.ts_parser.functionToFile[function_id],
                            )
                        )
        self.cache = xss_sink_set
        self.is_transformed = True
        return xss_sink_set
