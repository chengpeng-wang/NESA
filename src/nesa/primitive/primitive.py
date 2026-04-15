from abc import ABC, abstractmethod
from typing import Any, Dict, List, Set, Tuple
from parser.program_parser import *
from concurrent.futures import ThreadPoolExecutor
from typing import Set, Dict


# Base class for all primitives
class Primitive(ABC):
    cached_number = 0
    cached_miss_number = 0

    def __init__(self, name: str, type: str, kind: str) -> None:
        self.name = name
        self.type = type
        self.kind = kind

    def print_primitive_name(self) -> None:
        print(self.name + " is applied.")


# Base class for unary primitives
class UnaryPrimitive(Primitive):
    def __init__(self, name: str, kind: str) -> None:
        super().__init__(name, "unary", kind)
        self.cache = set([])
        self.tuple_bool_cache = {}
        self.is_transformed = False

    @abstractmethod
    def transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        pass

    def check(self, arg: Expr, ts_analyzer: TSAnalyzer) -> bool:
        if self.is_transformed:
            self.cached_number += 1
            return arg in self.cache

        if arg in self.tuple_bool_cache:
            self.cached_number += 1
            return self.tuple_bool_cache[arg]

        self.cached_miss_number += 1
        self.tuple_bool_cache[arg] = arg in self.transform(ts_analyzer)
        self.is_transformed = True
        return self.tuple_bool_cache[arg]

    def batch_transform(self, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        return self.transform(ts_analyzer)

    def batch_check(
        self, args: Set[Expr], ts_analyzer: TSAnalyzer, max_workers: int = 1
    ) -> Dict[Expr, bool]:
        result = {}

        if max_workers <= 1:
            for arg in args:
                result[arg] = self.check(arg, ts_analyzer)
            return result

        def check_and_update(arg: Expr) -> Tuple[Expr, bool]:
            return arg, self.check(arg, ts_analyzer)

        with ThreadPoolExecutor(max_workers=max_workers) as executor:
            futures = executor.map(check_and_update, args)
            for arg, res in futures:
                result[arg] = res

        return result


# Base class for binary primitives
class BinaryPrimitive(Primitive):
    def __init__(self, name: str, kind: str) -> None:
        super().__init__(name, "binary", kind)
        self.forward_cache = {}
        self.backward_cache = {}
        self.tuple_bool_cache = {}

    @abstractmethod
    def transform_forward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        pass

    @abstractmethod
    def transform_backward(self, arg: Expr, ts_analyzer: TSAnalyzer) -> Set[Expr]:
        pass

    def check(self, arg1: Expr, arg2: Expr, ts_analyzer: TSAnalyzer) -> bool:
        if arg1 in self.forward_cache:
            self.cached_number += 1
            return arg2 in self.forward_cache[arg1]

        if arg2 in self.backward_cache:
            self.cached_number += 1
            return arg1 in self.backward_cache[arg2]

        if (arg1, arg2) in self.tuple_bool_cache:
            self.cached_number += 1
            return self.tuple_bool_cache[(arg1, arg2)]

        self.cached_miss_number += 1
        backward_exprs = self.transform_backward(arg2, ts_analyzer)
        self.backward_cache[arg1] = backward_exprs
        for expr in backward_exprs:
            self.tuple_bool_cache[(expr, arg2)] = True
        if arg1 not in backward_exprs:
            self.tuple_bool_cache[(arg1, arg2)] = False
        return self.tuple_bool_cache[(arg1, arg2)]

    def batch_transform_forward(
        self, args: Set[Expr], ts_analyzer: TSAnalyzer, max_workers: int = 1
    ) -> Dict[Expr, Set[Expr]]:
        result = {}

        if max_workers <= 1:
            for arg in args:
                result[arg] = self.transform_forward(arg, ts_analyzer)
            return result

        # Define a helper function to be executed in parallel
        def transform_and_update(arg: Expr) -> Tuple[Expr, Set[Expr]]:
            return arg, self.transform_forward(arg, ts_analyzer)

        with ThreadPoolExecutor(max_workers=max_workers) as executor:
            # Map the transform_and_update function to the args set using ThreadPoolExecutor
            futures = executor.map(transform_and_update, args)

            # Collect the results
            for arg, res in futures:
                result[arg] = res

        return result

    def batch_transform_backward(
        self, args: Set[Expr], ts_analyzer: TSAnalyzer, max_workers: int = 1
    ) -> Dict[Expr, Set[Expr]]:
        result = {}

        if max_workers <= 1:
            for arg in args:
                result[arg] = self.transform_backward(arg, ts_analyzer)
            return result

        # Define a helper function to be executed in parallel
        def transform_and_update(arg: Expr) -> Tuple[Expr, Set[Expr]]:
            return arg, self.transform_backward(arg, ts_analyzer)

        with ThreadPoolExecutor(max_workers=max_workers) as executor:
            # Map the transform_and_update function to the args set using ThreadPoolExecutor
            futures = executor.map(transform_and_update, args)

            # Collect the results
            for arg, res in futures:
                result[arg] = res

        return result

    def batch_check(
        self,
        args: Set[Tuple[Expr, Expr]],
        ts_analyzer: TSAnalyzer,
        max_workers: int = 1,
    ) -> Dict[Tuple[Expr, Expr], bool]:
        result = {}

        # Define a helper function to be executed in parallel
        def check_and_update(args: Tuple[Expr, Expr]) -> Tuple[Tuple[Expr, Expr], bool]:
            return args, self.check(args[0], args[1], ts_analyzer)

        second_arg_set = set([])
        args1 = set([])
        args2 = set([])

        for arg1, arg2 in args:
            if arg2 not in second_arg_set:
                second_arg_set.add(arg2)
                args1.add((arg1, arg2))
            else:
                args2.add((arg1, arg2))

        if max_workers <= 1:
            for args_item in args1:
                result[args_item] = self.check(args_item[0], args_item[1], ts_analyzer)
            for args_item in args2:
                result[args_item] = self.check(args_item[0], args_item[1], ts_analyzer)
            return result

        with ThreadPoolExecutor(max_workers=max_workers) as executor:
            # Map the check_and_update function to the args set using ThreadPoolExecutor
            futures = executor.map(check_and_update, args1)

            # Collect the results
            for args_item, res in futures:
                result[args_item] = res

        with ThreadPoolExecutor(max_workers=max_workers) as executor:
            # Map the check_and_update function to the args set using ThreadPoolExecutor
            futures = executor.map(check_and_update, args2)

            # Collect the results
            for args_item, res in futures:
                result[args_item] = res

        return result
