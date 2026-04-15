from primitive.primitive import UnaryPrimitive
from typing import Dict, Set
from parser.program_parser import *


class CallsiteArgExprPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("CallsiteArgExprPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        arg_set = set([])
        for function_id in ts_analyzer.environment:
            function = ts_analyzer.environment[function_id]
            for arg, _ in function.args:
                arg_set.add(arg)

        self.cache = arg_set
        self.is_transformed = True
        return arg_set
