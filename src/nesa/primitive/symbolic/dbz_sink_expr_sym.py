from primitive.primitive import UnaryPrimitive
from typing import Set
from parser.program_parser import *


class DbzSinkExprSymPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("DbzSinkExprSymPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        dbz_sink_set = set([])
        for function_id in ts_analyzer.environment:
            file_path = ts_analyzer.ts_parser.functionToFile[function_id]
            source_code = ts_analyzer.ts_parser.fileContentDic[file_path]

            function = ts_analyzer.environment[function_id]
            root_node = function.parse_tree_root_node

            nodes = TSAnalyzer.find_nodes_by_type(root_node, "binary_expression")
            for node in nodes:
                is_sink_node = False
                for child in node.children:
                    if child.type in {"/", "%"}:
                        is_sink_node = True
                        continue
                    if is_sink_node and child.type == ts_analyzer.node_types.identifier:
                        name = source_code[child.start_byte : child.end_byte]
                        line_number = source_code[: child.start_byte].count("\n") + 1
                        dbz_sink_set.add(Expr(name, line_number, file_path))
        self.cache = dbz_sink_set
        self.transformed = True
        return dbz_sink_set
