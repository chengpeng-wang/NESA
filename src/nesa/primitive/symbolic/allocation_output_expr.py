import re

from primitive.primitive import UnaryPrimitive
from typing import Optional, Set
from parser.program_parser import *


class AllocationOutputExprPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("AllocationOutputExprPrimitive", "symbolic")

    @staticmethod
    def _extract_lhs_expr(
        node: tree_sitter.Node, source_code: str, ts_analyzer: TSAnalyzer, file_id: str
    ) -> Optional[Expr]:
        name_node = ts_analyzer.find_name_node(node)
        if name_node is None:
            return None

        name = source_code[name_node.start_byte : name_node.end_byte]
        line_number = source_code[: name_node.start_byte].count("\n") + 1
        return Expr(name, line_number, file_id)

    @staticmethod
    def _extract_cpp_callee_name(call_node: tree_sitter.Node, source_code: str) -> str:
        function_node = get_child_by_field_name(call_node, "function")
        if function_node is None:
            return ""

        function_text = source_code[function_node.start_byte : function_node.end_byte]
        last_segment = re.split(r"::|->|\.", function_text)[-1].strip()
        last_segment = re.sub(r"<.*>$", "", last_segment).strip()
        match = re.search(r"([A-Za-z_][A-Za-z0-9_]*)\s*$", last_segment)
        if match is None:
            return ""
        return match.group(1)

    @staticmethod
    def _extract_value_node(node: tree_sitter.Node) -> Optional[tree_sitter.Node]:
        for field_name in ("right", "value"):
            value_node = get_child_by_field_name(node, field_name)
            if value_node is not None:
                return value_node
        return None

    @staticmethod
    def _unwrap_parenthesized_value_node(
        value_node: tree_sitter.Node,
    ) -> tree_sitter.Node:
        current_node = value_node
        while (
            current_node is not None and current_node.type == "parenthesized_expression"
        ):
            named_children = [
                child_node
                for child_node in current_node.children
                if getattr(child_node, "is_named", False)
            ]
            if len(named_children) != 1:
                break
            current_node = named_children[0]
        return current_node

    def _is_cpp_allocation_output(
        self, value_node: tree_sitter.Node, source_code: str, ts_analyzer: TSAnalyzer
    ) -> bool:
        normalized_value_node = self._unwrap_parenthesized_value_node(value_node)
        if normalized_value_node.type != ts_analyzer.node_types.method_invocation:
            return False

        allocation_prefixes = ("malloc", "alloc", "calloc", "create")
        callee_name = self._extract_cpp_callee_name(
            normalized_value_node, source_code
        ).lower()
        return any(callee_name.startswith(prefix) for prefix in allocation_prefixes)

    def _is_java_allocation_output(
        self, value_node: tree_sitter.Node, ts_analyzer: TSAnalyzer
    ) -> bool:
        object_creation_type = ts_analyzer.node_types.object_creation_expression
        if object_creation_type is None:
            return False
        normalized_value_node = self._unwrap_parenthesized_value_node(value_node)
        return normalized_value_node.type == object_creation_type

    def _is_allocation_output(
        self, node: tree_sitter.Node, source_code: str, ts_analyzer: TSAnalyzer
    ) -> bool:
        value_node = self._extract_value_node(node)
        if value_node is None:
            return False

        if ts_analyzer.language == "cpp":
            return self._is_cpp_allocation_output(value_node, source_code, ts_analyzer)
        if ts_analyzer.language == "java":
            return self._is_java_allocation_output(value_node, ts_analyzer)
        return False

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
                if not self._is_allocation_output(node, source_code, ts_analyzer):
                    continue

                expr = self._extract_lhs_expr(node, source_code, ts_analyzer, file_id)
                if expr is None:
                    continue
                output_set.add(expr)

        self.cache = output_set
        self.is_transformed = True
        return output_set
