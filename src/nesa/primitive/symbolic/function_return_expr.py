from primitive.primitive import UnaryPrimitive
from typing import Set
from parser.program_parser import *


class FunctionReturnExprPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("FunctionReturnExprPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        return_set = set([])
        for function_id in ts_analyzer.environment:
            function = ts_analyzer.environment[function_id]
            return_set = return_set.union(function.rets)
        self.cache = return_set
        self.is_transformed = True
        return return_set
