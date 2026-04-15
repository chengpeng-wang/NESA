from primitive.primitive import UnaryPrimitive
from typing import Set
from parser.program_parser import *


class XssSourceExprSymPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("XssSourceExprSymPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        xss_src_set = set([])
        for function_id in ts_analyzer.environment:
            file_id = ts_analyzer.ts_parser.functionToFile[function_id]
            source_code = ts_analyzer.ts_parser.fileContentDic[file_id]

            function = ts_analyzer.environment[function_id]
            root_node = function.parse_tree_root_node

            nodes = TSAnalyzer.find_nodes_by_type(
                root_node, ts_analyzer.node_types.assignment_expression
            )

            # Find local_variable_declaration
            nodes.extend(
                TSAnalyzer.find_nodes_by_type(
                    root_node, ts_analyzer.node_types.variable_declarator
                )
            )

            # Extract the name info and line number
            for node in nodes:
                is_src_node = False
                for call_node in TSAnalyzer.find_nodes_by_type(
                    node, ts_analyzer.node_types.method_invocation
                ):
                    call_expr = source_code[call_node.start_byte : call_node.end_byte]
                    if (
                        "recv" in call_expr
                        or "getProperty" in call_expr
                        or "getCookies" in call_expr
                        or "getString" in call_expr
                        or "nextToken" in call_expr
                        or "getParameter" in call_expr
                        or "readLine" in call_expr
                    ):
                        is_src_node = True
                        break
                if is_src_node:
                    name_node = ts_analyzer.find_name_node(node)
                    if name_node is None:
                        continue
                    name = source_code[name_node.start_byte : name_node.end_byte]
                    line_number = source_code[: name_node.start_byte].count("\n") + 1
                    xss_src_set.add(
                        Expr(
                            name,
                            line_number,
                            ts_analyzer.ts_parser.functionToFile[function_id],
                        )
                    )
        self.cache = xss_src_set
        self.is_transformed = True
        return xss_src_set
