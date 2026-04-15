from primitive.primitive import BinaryPrimitive
from typing import Dict, Set
from parser.program_parser import *


class StmtDefUsePrimitive(BinaryPrimitive):
    def __init__(self) -> None:
        super().__init__("StmtDefUsePrimitive", "symbolic")

    def transform_forward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.forward_cache:
            self.cached_number += 1
            return self.forward_cache[arg]

        self.cached_miss_number += 1

        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            self.forward_cache[arg] = set([])
            return set([])

        function = functions[0]
        use_set = set([])

        for variable, line_number in function.local_RW_vars:
            if line_number == arg.line_number:
                if "R" in function.local_RW_vars[(variable, line_number)]:
                    expr = Expr(
                        variable,
                        line_number,
                        ts_analyzer.ts_parser.functionToFile[function.function_id],
                    )
                    if expr not in use_set:
                        use_set.add(expr)

        self.forward_cache[arg] = use_set
        print(
            "forward of stmt_def_use: ", str(arg), str([str(expr) for expr in use_set])
        )
        return use_set

    def transform_backward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.backward_cache:
            self.cached_number += 1
            return self.backward_cache[arg]

        self.cached_miss_number += 1

        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            self.backward_cache[arg] = set([])
            return set([])

        function = functions[0]
        defined_set = set([])

        for variable, line_number in function.local_RW_vars:
            if line_number == arg.line_number:
                if "W" in function.local_RW_vars[(variable, line_number)]:
                    file_path = ts_analyzer.ts_parser.functionToFile[
                        function.function_id
                    ]
                    expr = Expr(variable, line_number, file_path)
                    if expr not in defined_set:
                        defined_set.add(expr)

        self.backward_cache[arg] = defined_set
        print(
            "backward of stmt_def_use: ",
            str(arg),
            str([str(expr) for expr in defined_set]),
        )
        return defined_set
