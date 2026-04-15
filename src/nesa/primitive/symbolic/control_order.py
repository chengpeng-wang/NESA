from nesa.parser.program_parser import Expr, TSAnalyzer
from primitive.primitive import BinaryPrimitive
from primitive.symbolic.callsite_arg_expr import CallsiteArgExprPrimitive
from primitive.symbolic.callsite_output_expr import CallsiteOutputExprPrimitive
from primitive.symbolic.function_parameter_expr import FunctionParameterExprPrimitive
from primitive.symbolic.function_return_expr import FunctionReturnExprPrimitive
from typing import Dict, Set
from parser.program_parser import *


class ControlOrderPrimitive(BinaryPrimitive):
    def __init__(self) -> None:
        super().__init__("ControlOrderPrimitive", "symbolic")

    # Attention: Huge overhead
    # Only focus on specific forms of expressions: para/arg, output/return, etc
    def transform_forward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.forward_cache:
            self.cached_number += 1
            return self.forward_cache[arg]

        self.cached_miss_number += 1

        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            self.forward_cache[arg] = set([])
            return set([])

        function_parameter_expr_primitive = FunctionParameterExprPrimitive()
        function_return_expr_primitive = FunctionReturnExprPrimitive()
        callsite_arg_expr_primitive = CallsiteArgExprPrimitive()
        callsite_output_expr_primitive = CallsiteOutputExprPrimitive()

        expr_set = set([])
        expr_set = expr_set.union(function_parameter_expr_primitive.apply(ts_analyzer))
        expr_set = expr_set.union(function_return_expr_primitive.apply(ts_analyzer))
        expr_set = expr_set.union(callsite_arg_expr_primitive.apply(ts_analyzer))
        expr_set = expr_set.union(callsite_output_expr_primitive.apply(ts_analyzer))

        forward_set = set([])
        for expr in expr_set:
            if self.check(arg, expr, ts_analyzer):
                forward_set.add(expr)
        self.forward_cache[arg] = forward_set
        return forward_set

    # Attention: Huge overhead
    # Only focus on specific forms of expressions: para/arg, output/return, etc
    def transform_backward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.forward_cache:
            self.cached_number += 1
            return self.forward_cache[arg]

        self.cached_miss_number += 1

        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            self.forward_cache[arg] = set([])
            return set([])

        function_parameter_expr_primitive = FunctionParameterExprPrimitive()
        function_return_expr_primitive = FunctionReturnExprPrimitive()
        callsite_arg_expr_primitive = CallsiteArgExprPrimitive()
        callsite_output_expr_primitive = CallsiteOutputExprPrimitive()

        expr_set = set([])
        expr_set = expr_set.union(function_parameter_expr_primitive.apply(ts_analyzer))
        expr_set = expr_set.union(function_return_expr_primitive.apply(ts_analyzer))
        expr_set = expr_set.union(callsite_arg_expr_primitive.apply(ts_analyzer))
        expr_set = expr_set.union(callsite_output_expr_primitive.apply(ts_analyzer))

        backward_set = set([])
        for expr in expr_set:
            if self.check(expr, arg, ts_analyzer):
                backward_set.add(expr)
        self.backward_cache[arg] = backward_set
        return backward_set

    # Overwritten to avoid heavy overhead in the sanitization
    def check(self, arg1: Expr, arg2: Expr, ts_analyzer: TSAnalyzer) -> bool:
        arg1_functions = ts_analyzer.find_function_by_expr(arg1)
        arg2_functions = ts_analyzer.find_function_by_expr(arg2)
        if len(arg1_functions) != 1 or len(arg2_functions) != 1:
            self.cached_number += 1
            return False
        if arg1_functions[0] != arg2_functions[0]:
            self.cached_number += 1
            return False

        self.cached_miss_number += 1

        function = arg1_functions[0]
        src_line_number = arg1.line_number
        sink_line_number = arg2.line_number

        src_line_number_in_function = src_line_number - function.start_line_number + 1
        sink_line_number_in_function = sink_line_number - function.start_line_number + 1

        for if_statement_start_line, if_statement_end_line in function.if_statements:
            (
                _,
                _,
                _,
                (true_branch_start_line, true_branch_end_line),
                (else_branch_start_line, else_branch_end_line),
            ) = function.if_statements[(if_statement_start_line, if_statement_end_line)]
            if (
                true_branch_start_line
                <= src_line_number_in_function
                <= true_branch_end_line
                and else_branch_start_line
                <= sink_line_number_in_function
                <= else_branch_end_line
                and else_branch_start_line != 0
                and else_branch_end_line != 0
            ):
                return False

        if src_line_number_in_function >= sink_line_number_in_function:
            for loop_start_line, loop_end_line in function.loop_statements:
                (
                    _,
                    _,
                    loop_body_start_line,
                    loop_body_end_line,
                ) = function.loop_statements[(loop_start_line, loop_end_line)]
                if (
                    loop_body_start_line
                    <= src_line_number_in_function
                    <= loop_body_end_line
                    and loop_body_start_line
                    <= sink_line_number_in_function
                    <= loop_body_end_line
                ):
                    return True
            return False
        return True
