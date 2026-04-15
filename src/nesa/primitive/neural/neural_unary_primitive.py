from nesa.parser.program_parser import Expr, TSAnalyzer
from primitive.primitive import *
from parser.program_parser import *
from parser.response_parser import *
from parser.prompt_parser import *
from model.llm import *
from model.utils import *
from typing import Set
from concurrent.futures import ThreadPoolExecutor


"""
NeuralUnaryPrimitive: Intra-procedural unary semantic relations derived from LLMs
"""


class NeuralUnaryPrimitive(UnaryPrimitive):
    """
    DBZ source
    etc
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
            Path(__file__).parent.parent.parent / "config" / "neural" / "unary"
        )
        self.transform_prompt_file = prompt_path / (
            neural_relation_name + "_transform.json"
        )
        self.model = model
        self.semi_naive_evaluation = semi_naive_evaluation
        self.additional_context = additional_context

        self.total_input_token_cost = 0
        self.total_output_token_cost = 0

        # Whether feed the whole file to LLMs when fetching function-level semantic facts via prompting
        self.is_infile = "infile" in neural_relation_name

        # check the existence of the two files
        if not os.path.exists(self.transform_prompt_file):
            raise FileNotFoundError(
                "transform prompt file not found for the relation "
                + neural_relation_name
            )

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        return self.transform_by_function(ts_analyzer)

    def transform_by_file(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.semi_naive_evaluation:
            if self.is_transformed:
                self.cached_number += 1
                return self.cache

        self.cached_miss_number += 1

        for file_id in ts_analyzer.ts_parser.fileContentDic:
            source_code = ts_analyzer.ts_parser.fileContentDic[file_id]

            lines = []
            line_number = 1
            for line in source_code.split("\n"):
                lines.append(str(line_number) + ". " + line)
                line_number += 1
            lined_source_code = "\n".join(lines)

            prompt = construct_transform_prompt_from_unary_relation(
                self.transform_prompt_file, lined_source_code, ts_analyzer.language
            )
            output, input_token_cost, output_token_cost = self.model.infer(prompt)
            self.total_input_token_cost += input_token_cost
            self.total_output_token_cost += output_token_cost
            eval_exprs, _ = parse_transform_prompt_response(output)
            self.cache = self.cache.union(eval_exprs)
        self.is_transformed = True
        return self.cache

    def transform_by_function(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.semi_naive_evaluation:
            if self.is_transformed:
                self.cached_number += 1
                return self.cache

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
                    "Here is the source code you need to analyze: "
                    + lined_source_code
                    + "\n"
                )
            else:
                final_lined_source_code = lined_source_code

            prompt = construct_transform_prompt_from_unary_relation(
                self.transform_prompt_file,
                final_lined_source_code,
                ts_analyzer.language,
            )

            output, input_token_cost, output_token_cost = self.model.infer(prompt)
            self.total_input_token_cost += input_token_cost
            self.total_output_token_cost += output_token_cost
            eval_exprs, _ = parse_transform_prompt_response(
                output, ts_analyzer.ts_parser.functionToFile[function_id]
            )

            print("unary prompt:", prompt)
            print("unary output:", output)

            eval_exprs_with_updated_line_number = set([])
            for eval_expr in eval_exprs:
                eval_expr.line_number = (
                    eval_expr.line_number
                    if self.is_infile
                    else eval_expr.line_number + function.start_line_number - 1
                )
                eval_exprs_with_updated_line_number.add(eval_expr)
            self.cache = self.cache.union(eval_exprs_with_updated_line_number)

        self.is_transformed = True
        return self.cache
