from primitive.primitive import UnaryPrimitive
from typing import Set
from parser.program_parser import *


class MemWriteExprPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("MemWriteExprPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        read_expr_set = set([])
        for function_id in ts_analyzer.environment:
            function = ts_analyzer.environment[function_id]
            for identifier_str, identifier_line_number in function.local_RW_vars:
                if (
                    "W"
                    in function.local_RW_vars[(identifier_str, identifier_line_number)]
                ):
                    file_path = ts_analyzer.ts_parser.functionToFile[function_id]
                    read_expr_set.add(
                        Expr(identifier_str, identifier_line_number, file_path)
                    )
        self.cache = read_expr_set
        return read_expr_set
