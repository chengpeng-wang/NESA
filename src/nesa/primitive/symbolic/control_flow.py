from primitive.primitive import BinaryPrimitive
from typing import Dict, Set
from parser.program_parser import *


class ControlFlowPrimitive(BinaryPrimitive):
    def __init__(self) -> None:
        super().__init__("ControlFlowPrimitive", "symbolic")

    def transform_forward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.forward_cache:
            self.cached_number += 1
            return self.forward_cache[arg]

        self.cached_miss_number += 1

        defined_exprs = set([])
        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            self.forward_cache[arg] = set([])
            return set([])

        function = functions[0]
        controlled_lines = ts_analyzer.extract_controlled_program_lines(
            function, arg.line_number
        )
        for controlled_line in controlled_lines:
            for identifier_str, identifier_line_number in function.local_RW_vars:
                if identifier_line_number == controlled_line:

                    file_path = ts_analyzer.ts_parser.functionToFile[
                        function.function_id
                    ]
                    defined_exprs.add(
                        Expr(identifier_str, identifier_line_number, file_path)
                    )
                    print(
                        "control flow value: ",
                        arg,
                        Expr(identifier_str, identifier_line_number, file_path),
                    )
        self.forward_cache[arg] = defined_exprs

        print(
            "forward of control_flow: ",
            str(arg),
            str([str(expr) for expr in defined_exprs]),
        )
        return defined_exprs

    def transform_backward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.backward_cache:
            self.cached_number += 1
            return self.backward_cache[arg]

        self.cached_miss_number += 1

        guarded_exprs = set([])
        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            self.backward_cache[arg] = set([])
            return set([])

        function = functions[0]
        control_dependency_lines = ts_analyzer.extract_control_program_lines(
            function, arg.line_number
        )
        for control_dependency_line in control_dependency_lines:
            for identifier_str, identifier_line_number in function.local_RW_vars:
                if identifier_line_number == control_dependency_line:
                    file_path = ts_analyzer.ts_parser.functionToFile[
                        function.function_id
                    ]
                    guarded_exprs.add(
                        Expr(identifier_str, identifier_line_number, file_path)
                    )
        self.backward_cache[arg] = guarded_exprs

        print(
            "backward of control_flow: ",
            str(arg),
            str([str(expr) for expr in guarded_exprs]),
        )
        return guarded_exprs
