from primitive.primitive import UnaryPrimitive
from typing import Dict, Set
from parser.program_parser import *


class CallsiteOutputExprPrimitive(UnaryPrimitive):
    def __init__(self) -> None:
        super().__init__("CallsiteOutputExprPrimitive", "symbolic")

    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        if self.is_transformed:
            self.cached_number += 1
            return self.cache

        self.cached_miss_number += 1

        output_set = set([])
        for function_id in ts_analyzer.environment:
            function = ts_analyzer.environment[function_id]
            output_set = output_set.union(function.outputs)

        self.cache = output_set
        self.is_transformed = True
        return output_set
