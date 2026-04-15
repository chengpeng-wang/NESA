from primitive.primitive import *
from parser.program_parser import *
from parser.response_parser import *
from parser.prompt_parser import *
from primitive.symbolic.mem_read_expr import *
from primitive.symbolic.mem_write_expr import *
from primitive.symbolic.control_order import *
from model.llm import *
from model.utils import *
from typing import Set
from copy import deepcopy
import json

"""
NeuralBinaryPrimitive: Intra-procedural binary semantic relations derived from LLMs
"""


class NeuralBinaryPrimitive(BinaryPrimitive):
    """
    Inherient neural relations include:
    - data_flow_edge
    - data_flow_path
    - eq_flow_edge
    - eq_flow_path
    - etc
    """

    def __init__(
        self,
        neural_relation_name: str,
        model: LLM,
        semi_naive_evaluation: bool,
        additional_context: str,
    ) -> None:
        super().__init__(neural_relation_name, "neural")
        self.neural_relation_name = neural_relation_name

        prompt_path = (
            Path(__file__).parent.parent.parent / "config" / "neural" / "binary"
        )
        self.forward_prompt_file = str(
            prompt_path / (neural_relation_name + "_forward.json")
        )
        self.backward_prompt_file = str(
            prompt_path / (neural_relation_name + "_backward.json")
        )
        self.check_prompt_file = str(
            prompt_path / (neural_relation_name + "_check.json")
        )
        self.populate_prompt_file = str(
            prompt_path / (neural_relation_name + "_populate.json")
        )

        self.total_input_token_cost = 0
        self.total_output_token_cost = 0
        self.model = model
        self.semi_naive_evaluation = semi_naive_evaluation
        self.additional_context = additional_context

        self.is_populated = False
        self.populated_tuples = set([])

        # Whether feed the whole file to LLMs when fetching function-level semantic facts via prompting
        self.is_infile = "infile" in neural_relation_name

        # check the existence of the three files
        if not os.path.exists(self.forward_prompt_file):
            raise FileNotFoundError(
                "forward prompt file not found for the relation " + neural_relation_name
            )
        if not os.path.exists(self.backward_prompt_file):
            raise FileNotFoundError(
                "backward prompt file not found for the relation "
                + neural_relation_name
            )
        if not os.path.exists(self.check_prompt_file):
            raise FileNotFoundError(
                "check prompt file not found for the relation " + neural_relation_name
            )
        if not os.path.exists(self.populate_prompt_file):
            raise FileNotFoundError(
                "populate prompt file not found for the relation "
                + neural_relation_name
            )

    def apply(
        self, prompt_template_file: str, expr: Expr, ts_analyzer: TSAnalyzer
    ) -> Set[Expr]:
        functions = ts_analyzer.find_function_by_expr(expr)
        if len(functions) != 1:
            if prompt_template_file == self.forward_prompt_file:
                self.forward_cache[expr] = set([])
            else:
                self.backward_cache[expr] = set([])
            return set([])

        self.cached_miss_number += 1

        function = functions[0]
        file_id = ts_analyzer.ts_parser.functionToFile[function.function_id]
        source_code = ts_analyzer.ts_parser.fileContentDic[file_id]

        if not self.is_infile:
            source = source_code[
                function.parse_tree_root_node.start_byte : function.parse_tree_root_node.end_byte
            ]
            expr = deepcopy(expr)
            expr.line_number = expr.line_number - function.start_line_number + 1
        else:
            source = source_code

        lines = []
        line_number = 1
        for line in source.split("\n"):
            lines.append(str(line_number) + ". " + line)
            line_number += 1
        lined_source_code = "\n".join(lines)

        if self.is_infile:
            final_lined_source_code = (
                "Here are several related source files:\n\n"
                + self.additional_context
                + "\n\n"
            )
            final_lined_source_code += (
                "Here is the source code you need to analyze: "
                + lined_source_code
                + "\n"
            )
        else:
            final_lined_source_code = lined_source_code

        cnt = 0
        while cnt < iterative_count_bound:
            cnt += 1
            prompt = construct_transform_prompt_from_binary_relation(
                prompt_template_file,
                final_lined_source_code,
                expr,
                ts_analyzer.language,
            )
            output, input_token_cost, output_token_cost = self.model.infer(prompt)
            self.total_input_token_cost += input_token_cost
            self.total_output_token_cost += output_token_cost
            eval_exprs, is_error = parse_transform_prompt_response(
                output, ts_analyzer.ts_parser.functionToFile[function.function_id]
            )

            if not is_error:
                break

        eval_exprs_with_updated_line_number = set([])
        for eval_expr in eval_exprs:
            eval_expr.line_number = (
                eval_expr.line_number
                if self.is_infile
                else eval_expr.line_number + function.start_line_number - 1
            )
            eval_exprs_with_updated_line_number.add(eval_expr)

        if prompt_template_file == self.forward_prompt_file:
            self.forward_cache[expr] = eval_exprs_with_updated_line_number
        else:
            self.backward_cache[expr] = eval_exprs_with_updated_line_number
        return eval_exprs_with_updated_line_number

    def transform_forward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.semi_naive_evaluation:
            if arg in self.forward_cache:
                self.cached_number += 1
                return self.forward_cache[arg]

        transform_result = self.apply(self.forward_prompt_file, arg, ts_analyzer)
        if self.neural_relation_name in {
            "data_flow_edge",
            "data_flow_path",
            "eq_flow_edge",
            "eq_flow_path",
        }:
            exprs = set([])
            control_order_primitive = ControlOrderPrimitive()
            mem_read_expr_primitive = MemReadExprPrimitive()
            mem_write_expr_primitive = MemWriteExprPrimitive()
            for expr in transform_result:
                # if control_order_primitive.check(arg, expr, ts_analyzer) and \
                #     (mem_read_expr_primitive.check(expr, ts_analyzer) or mem_write_expr_primitive.check(expr, ts_analyzer)):
                if control_order_primitive.check(arg, expr, ts_analyzer):
                    exprs.add(expr)
                    print("forward: ", str(arg), str(expr))
                else:
                    print("pruned in forward: ", str(arg), str(expr))
            print(
                "forward of neural primitive: ",
                str(arg),
                str([str(expr) for expr in exprs]),
            )
            self.forward_cache[arg] = exprs
            return exprs
        else:
            return transform_result

    def transform_backward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.semi_naive_evaluation:
            if arg in self.backward_cache:
                self.cached_number += 1
                return self.backward_cache[arg]

        transform_result = self.apply(self.backward_prompt_file, arg, ts_analyzer)
        if self.neural_relation_name in {
            "data_flow_edge",
            "data_flow_path",
            "eq_flow_edge",
            "eq_flow_path",
        }:
            exprs = set([])
            control_order_primitive = ControlOrderPrimitive()
            mem_read_expr_primitive = MemReadExprPrimitive()
            mem_write_expr_primitive = MemWriteExprPrimitive()

            for expr in transform_result:
                # if control_order_primitive.check(expr, arg, ts_analyzer) and \
                #     (mem_read_expr_primitive.check(expr, ts_analyzer) or mem_write_expr_primitive.check(expr, ts_analyzer)):
                if control_order_primitive.check(expr, arg, ts_analyzer):
                    exprs.add(expr)
                else:
                    print("pruned in backward: ", str(arg), str(expr))
            print(
                "backward of neural primitive: ",
                str(arg),
                str([str(expr) for expr in exprs]),
            )
            self.backward_cache[arg] = exprs
            return exprs
        else:
            return transform_result

    def check(self, arg1: Expr, arg2: Expr, ts_analyzer: TSAnalyzer) -> bool:
        if self.semi_naive_evaluation:
            if arg1 in self.forward_cache:
                self.cached_number += 1
                return arg2 in self.forward_cache[arg1]
            if arg2 in self.backward_cache:
                self.cached_number += 1
                return arg1 in self.backward_cache[arg2]
            if (arg1, arg2) in self.tuple_bool_cache:
                self.cached_number += 1
                return self.tuple_bool_cache[(arg1, arg2)]

        arg1_functions = ts_analyzer.find_function_by_expr(arg1)
        arg2_functions = ts_analyzer.find_function_by_expr(arg2)
        if len(arg1_functions) != 1:
            self.forward_cache[arg1] = set([])
            self.tuple_bool_cache[(arg1, arg2)] = False
            return False
        if len(arg2_functions) != 1:
            self.backward_cache[arg2] = set([])
            self.tuple_bool_cache[(arg1, arg2)] = False
            return False

        function1 = arg1_functions[0]
        function2 = arg2_functions[0]
        file_id_1 = ts_analyzer.ts_parser.functionToFile[function1.function_id]
        file_id_2 = ts_analyzer.ts_parser.functionToFile[function2.function_id]

        if file_id_1 != file_id_2 or function1.function_id != function2.function_id:
            return False

        self.cached_miss_number += 1

        source_code = ts_analyzer.ts_parser.fileContentDic[file_id_1]
        if not self.is_infile:
            arg1 = deepcopy(arg1)
            arg1.line_number = arg1.line_number - function1.start_line_number + 1
            arg2 = deepcopy(arg2)
            arg2.line_number = arg2.line_number - function2.start_line_number + 1
            code = source_code[
                function1.parse_tree_root_node.start_byte : function1.parse_tree_root_node.end_byte
            ]
        else:
            code = source_code

        lines = []
        line_number = 1
        for line in code.split("\n"):
            lines.append(str(line_number) + ". " + line)
            line_number += 1
        lined_source_code = "\n".join(lines)

        if self.is_infile:
            final_lined_source_code = (
                "Here are several related source files:\n\n"
                + self.additional_context
                + "\n\n"
            )
            final_lined_source_code += (
                "Here is the source code you need to analyze: "
                + lined_source_code
                + "\n"
            )
        else:
            final_lined_source_code = lined_source_code

        cnt = 0
        is_satisfied = False
        while cnt < iterative_count_bound:
            cnt += 1
            prompt = construct_check_prompt_from_binary_relation(
                self.check_prompt_file,
                final_lined_source_code,
                arg1,
                arg2,
                ts_analyzer.language,
            )
            output, input_token_cost, output_token_cost = self.model.infer(prompt)
            self.total_input_token_cost += input_token_cost
            self.total_output_token_cost += output_token_cost
            is_satisfied, is_error = parse_check_prompt_response(output)
            if not is_error:
                break
        print("check of neural primitive: ", str(arg1), str(arg2), is_satisfied)
        self.tuple_bool_cache[(arg1, arg2)] = is_satisfied
        return is_satisfied

    def populate(self, ts_analyzer: TSAnalyzer) -> Set[Tuple[Expr]]:
        if self.is_populated:
            self.cached_number += 1
            return self.populated_tuples

        self.cached_miss_number += 1
        for function_id in ts_analyzer.environment:
            function = ts_analyzer.environment[function_id]
            file_id = ts_analyzer.ts_parser.functionToFile[function.function_id]
            source_code = ts_analyzer.ts_parser.fileContentDic[file_id]

            if not self.is_infile:
                code = source_code[
                    function.parse_tree_root_node.start_byte : function.parse_tree_root_node.end_byte
                ]
            else:
                code = source_code

            lines = []
            line_number = 1
            for line in code.split("\n"):
                lines.append(str(line_number) + ". " + line)
                line_number += 1
            lined_source_code = "\n".join(lines)

            if self.is_infile:
                final_lined_source_code = (
                    "Here are several related source files:\n\n"
                    + self.additional_context
                    + "\n\n"
                )
                final_lined_source_code += (
                    "Here is the source code you need to analyze: \n"
                    + lined_source_code
                    + "\n"
                )
            else:
                final_lined_source_code = lined_source_code

            cnt = 0
            while cnt < iterative_count_bound:
                cnt += 1
                prompt = construct_populate_prompt_from_binary_relation(
                    self.populate_prompt_file,
                    final_lined_source_code,
                    ts_analyzer.language,
                )
                output, input_token_cost, output_token_cost = self.model.infer(prompt)
                self.total_input_token_cost += input_token_cost
                self.total_output_token_cost += output_token_cost
                eval_expr_tuples, is_error = parse_populate_prompt_response(
                    output, ts_analyzer.ts_parser.functionToFile[function.function_id]
                )

                if not is_error:
                    break

            eval_exprs_with_updated_line_number = set([])
            for eval_expr_tuple in eval_expr_tuples:
                new_eval_expr_tuple = ()
                for eval_expr in eval_expr_tuple:
                    eval_expr.line_number = (
                        eval_expr.line_number
                        if self.is_infile
                        else eval_expr.line_number + function.start_line_number - 1
                    )
                    new_eval_expr_tuple += (eval_expr,)
                eval_exprs_with_updated_line_number.add(new_eval_expr_tuple)

            self.populated_tuples = self.populated_tuples.union(
                eval_exprs_with_updated_line_number
            )
        self.is_populated = True
        return self.populated_tuples
