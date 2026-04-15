from primitive.primitive import BinaryPrimitive
from typing import Dict, Set
from parser.program_parser import *


class ReturnOutMatchPrimitive(BinaryPrimitive):
    def __init__(self) -> None:
        super().__init__("ReturnOutMatchPrimitive", "symbolic")

    def transform_forward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.forward_cache:
            self.cached_number += 1
            return self.forward_cache[arg]

        self.cached_miss_number += 1
        self.forward_cache[arg] = set([])

        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            return self.forward_cache[arg]

        function = functions[0]

        if function.function_id not in ts_analyzer.callee_caller_map:
            return self.forward_cache[arg]

        # Step 1: Find the caller functions
        caller_ids = ts_analyzer.callee_caller_map[function.function_id]

        for caller_id in caller_ids:
            caller_function = ts_analyzer.environment[caller_id]
            line_numbers = set([])

            # Step 2: Find the call sites invoking the target function with arg as its return value
            for call_site_node in caller_function.call_site_nodes:
                caller_file_id = ts_analyzer.ts_parser.functionToFile[caller_id]
                caller_file_content = ts_analyzer.ts_parser.fileContentDic[
                    caller_file_id
                ]
                callee_ids_at_call_site = ts_analyzer.find_callee(
                    caller_id, caller_file_content, call_site_node
                )
                if function.function_id in callee_ids_at_call_site:
                    line_numbers.add(
                        caller_file_content[: call_site_node.end_byte].count("\n") + 1
                    )

            # Step 3: Find the outputs of the target function at the call sites
            for line_number in line_numbers:
                for output in caller_function.outputs:
                    if output.line_number == line_number:
                        self.forward_cache[arg].add(output)

        print(
            "ret -> out", str(arg), str([str(expr) for expr in self.forward_cache[arg]])
        )
        return self.forward_cache[arg]

    def transform_backward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if arg in self.backward_cache:
            self.cached_number += 1
            return self.backward_cache[arg]

        self.cached_miss_number += 1
        self.backward_cache[arg] = set([])

        functions = ts_analyzer.find_function_by_expr(arg)
        if len(functions) != 1:
            return self.backward_cache[arg]

        function = functions[0]

        for call_site_node in function.call_site_nodes:
            # Step 1: Find the target call site
            file_id = ts_analyzer.ts_parser.functionToFile[function.function_id]
            file_content = ts_analyzer.ts_parser.fileContentDic[file_id]
            line_number = file_content[: call_site_node.end_byte].count("\n") + 1
            if line_number != arg.line_number:
                continue

            function_file_id = ts_analyzer.ts_parser.functionToFile[
                function.function_id
            ]
            function_file_content = ts_analyzer.ts_parser.fileContentDic[
                function_file_id
            ]

            # Step 2: Find callee functions
            callee_ids_at_call_site = ts_analyzer.find_callee(
                function.function_id, function_file_content, call_site_node
            )

            # Step 3: Collect the return values of callee functions
            for callee_id in callee_ids_at_call_site:
                callee_function = ts_analyzer.environment[callee_id]
                for ret in callee_function.rets:
                    self.backward_cache[arg].add(ret)

        print(
            "out -> ret",
            str(arg),
            str([str(expr) for expr in self.backward_cache[arg]]),
        )
        return self.backward_cache[arg]
