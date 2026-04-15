from primitive.primitive import UnaryPrimitive
from typing import Set
from parser.program_parser import *


class DbzSourceExprSymPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("DbzSourceExprSymPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        dbz_src_set = set([])
        for function_id in ts_analyzer.environment:
            file_path = ts_analyzer.ts_parser.functionToFile[function_id]
            source_code = ts_analyzer.ts_parser.fileContentDic[file_path]

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
                is_src_node = False
                for number_node in TSAnalyzer.find_nodes_by_type(
                    node, ts_analyzer.node_types.number_literal
                ):
                    if source_code[number_node.start_byte : number_node.end_byte] in {
                        "0",
                        "0.0",
                        "0.0F",
                    }:
                        is_src_node = True
                        break
                for call_node in TSAnalyzer.find_nodes_by_type(
                    node, ts_analyzer.node_types.method_invocation
                ):
                    call_expr = source_code[call_node.start_byte : call_node.end_byte]
                    if (
                        "parseInt(" in call_expr
                        or "nextInt(" in call_expr
                        or "parseFloat(" in call_expr
                        or "nextFloat(" in call_expr
                    ):
                        is_src_node = True
                        break

                if is_src_node:
                    name_node = ts_analyzer.find_name_node(node)
                    if name_node is None:
                        continue
                    name = source_code[name_node.start_byte : name_node.end_byte]
                    line_number = source_code[: name_node.start_byte].count("\n") + 1
                    file_path = ts_analyzer.ts_parser.functionToFile[function_id]
                    dbz_src_set.add(Expr(name, line_number, file_path))
        self.cache = dbz_src_set
        self.transformed = True
        return dbz_src_set
