from primitive.primitive import BinaryPrimitive
from typing import Dict, Set
from parser.program_parser import *


class ArgParaMatchPrimitive(BinaryPrimitive):
    def __init__(self) -> None:
        super().__init__("ArgParaMatchPrimitive", "symbolic")

    def transform_forward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.forward_cache:
            self.cached_number += 1
            return self.forward_cache[arg]

        self.cached_miss_number += 1
        self.forward_cache[arg] = set([])

        # Step 1: Find the argument index
        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            return self.forward_cache[arg]

        function = functions[0]
        if function.function_id not in ts_analyzer.caller_callee_map:
            return self.forward_cache[arg]

        args = function.args
        index = -1
        for expr, i in args:
            if expr == arg:
                index = i
                break

        if index == -1:
            return self.forward_cache[arg]

        # Step 2: Find the callee functions
        for call_site_node in function.call_site_nodes:
            file_id = ts_analyzer.ts_parser.functionToFile[function.function_id]
            file_content = ts_analyzer.ts_parser.fileContentDic[file_id]
            line_number = file_content[: call_site_node.end_byte].count("\n") + 1
            if line_number != arg.line_number:
                continue

            caller_file_id = ts_analyzer.ts_parser.functionToFile[function.function_id]
            caller_file_content = ts_analyzer.ts_parser.fileContentDic[caller_file_id]
            callee_ids_at_call_site = ts_analyzer.find_callee(
                function.function_id, caller_file_content, call_site_node
            )
            for callee_id in callee_ids_at_call_site:
                callee_function = ts_analyzer.environment[callee_id]
                for expr, i in callee_function.paras:
                    if i == index:
                        self.forward_cache[arg].add(expr)
        print(
            "arg -> para",
            str(arg),
            str([str(expr) for expr in self.forward_cache[arg]]),
        )
        return self.forward_cache[arg]

    def transform_backward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.backward_cache:
            self.cached_number += 1
            return self.backward_cache[arg]

        self.cached_miss_number += 1
        self.backward_cache[arg] = set([])

        # Step 1: Find the parameter index
        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            return self.backward_cache[arg]

        function = functions[0]
        if function.function_id not in ts_analyzer.callee_caller_map:
            return self.backward_cache[arg]

        paras = function.paras
        index = -1
        for para, i in paras:
            if para == arg:
                index = i
                break

        if index == -1:
            return self.backward_cache[arg]

        # Step 2: Find the caller functions
        caller_ids = ts_analyzer.callee_caller_map[function.function_id]

        for caller_id in caller_ids:
            line_numbers = set([])
            caller_function = ts_analyzer.environment[caller_id]
            caller_file_id = ts_analyzer.ts_parser.functionToFile[caller_id]
            caller_file_content = ts_analyzer.ts_parser.fileContentDic[caller_file_id]

            # Step 3: Find the call sites invoking the function with the target parameter and collect the line numbers
            for call_site_node in caller_function.call_site_nodes:
                callee_ids = ts_analyzer.find_callee(
                    caller_function.function_id, caller_file_content, call_site_node
                )
                if function.function_id in callee_ids:
                    line_number = (
                        caller_file_content[: call_site_node.end_byte].count("\n") + 1
                    )
                    line_numbers.add(line_number)

            # Step 4: Find the arguments in the call sites with the same line number and index
            for line_number in line_numbers:
                for expr, i in caller_function.args:
                    if line_number == expr.line_number and i == index:
                        self.backward_cache[arg].add(expr)
        print(
            "para -> arg",
            str(arg),
            str([str(expr) for expr in self.backward_cache[arg]]),
        )
        return self.backward_cache[arg]
