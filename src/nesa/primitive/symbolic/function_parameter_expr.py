from primitive.primitive import UnaryPrimitive
from typing import Set
from parser.program_parser import *


class FunctionParameterExprPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("FunctionParameterExprPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1
        para_set = set([])
        for function_id in ts_analyzer.environment:
            function = ts_analyzer.environment[function_id]
            for para, _ in function.paras:
                para_set.add(para)
        self.cache = para_set
        return para_set
