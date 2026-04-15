from typing import Dict, Set, Tuple
from copy import deepcopy
from parser.program_parser import *
from nesa.evaluator.utility import *
from primitive.primitive import *
from utility.io import *


class RuleEvaluator:
    def __init__(
        self,
        rule: Rule,
        derived_relations: Set[str],
        symbolic_primitives: Dict[str, Primitive],
        neural_primitives: Dict[str, Primitive],
        eval_rule_mode: str,
        primitive_parallel_workers: int,
        is_small_step: bool = False,
    ) -> None:
        self.rule = rule
        self.derived_relations = derived_relations
        self.symbolic_primitives = symbolic_primitives
        self.neural_primitives = neural_primitives
        self.eval_rule_mode = eval_rule_mode
        self.primitive_parallel_workers = max(1, primitive_parallel_workers)
        self.parallel = self.primitive_parallel_workers > 1
        self.is_small_step = is_small_step

        self.evaluation_plan = []

        # Cache the relation contents in each iteration
        self.relation_term_cache = {}
        for relation_term_id in self.rule.relations_terms:
            self.relation_term_cache[relation_term_id] = set([])

        # Cache the transformation/application instances in each iteration
        self.evaluate_term_cache = {}
        for relation_term_id in self.rule.relations_terms:
            self.evaluate_term_cache[relation_term_id] = set([])

        # generate evaluation plan
        self.generate_evaluation_plan()
        return

    def generate_evaluation_plan(self) -> None:
        derived_term_ids = []
        symbolic_term_ids = []
        neural_unary_term_ids = []
        neural_binary_term_ids = []

        print("start to generate evaluation plan")

        for relation_term_id in self.rule.RHS_relation_term_ids:
            relation_id = self.rule.relations_terms[relation_term_id].relation_id
            relation_name = self.rule.relations_terms[relation_term_id].relation_name
            if relation_name in self.derived_relations:
                derived_term_ids.append(relation_term_id)
            elif relation_name in self.symbolic_primitives:
                symbolic_term_ids.append(relation_term_id)
            elif relation_name in self.neural_primitives:
                if self.rule.relations[relation_id].arity == 1:
                    neural_unary_term_ids.append(relation_term_id)
                else:
                    neural_binary_term_ids.append(relation_term_id)

        term_ids = (
            derived_term_ids
            + symbolic_term_ids
            + neural_unary_term_ids
            + neural_binary_term_ids
        )

        self.evaluation_plan.clear()
        constrained_entry_symbols = set([])
        remained_term_ids = set(term_ids)

        while len(remained_term_ids) > 0:
            for term_id in term_ids:
                if term_id not in remained_term_ids:
                    continue

                relation_term = self.rule.relations_terms[term_id]
                entry_symbols = relation_term.entry_symbols
                relation_name = self.rule.relations[relation_term.relation_id].name

                print(relation_name, entry_symbols, constrained_entry_symbols)

                if set(entry_symbols).issubset(constrained_entry_symbols):
                    self.evaluation_plan.append((term_id, relation_name, 0))
                    remained_term_ids.remove(term_id)
                else:
                    if len(entry_symbols) == 1:
                        self.evaluation_plan.append((term_id, relation_name, 1))
                        constrained_entry_symbols.add(entry_symbols[0])
                        remained_term_ids.remove(term_id)
                    elif len(entry_symbols) == 2:
                        if entry_symbols[0] in constrained_entry_symbols:
                            self.evaluation_plan.append((term_id, relation_name, 1))
                            constrained_entry_symbols.add(entry_symbols[1])
                            remained_term_ids.remove(term_id)
                        elif entry_symbols[1] in constrained_entry_symbols:
                            self.evaluation_plan.append((term_id, relation_name, -1))
                            constrained_entry_symbols.add(entry_symbols[0])
                            remained_term_ids.remove(term_id)
                        elif term_id in derived_term_ids:
                            self.evaluation_plan.append(
                                (term_id, relation_name, 1)
                            )  # 1 is just a placeholder
                            constrained_entry_symbols.add(entry_symbols[0])
                            constrained_entry_symbols.add(entry_symbols[1])
                            remained_term_ids.remove(term_id)
                        else:
                            assert "Please specify syntactically valid rules"

    def get_neural_or_symbolic_primitive(self, relation_name: str) -> Primitive:
        if relation_name in self.symbolic_primitives:
            return self.symbolic_primitives[relation_name]
        elif relation_name in self.neural_primitives:
            return self.neural_primitives[relation_name]
        else:
            assert "Invalid relation name"
            return None

    def collect_common_values_of_entry_symbol(
        self, entry_symbol_index: int, term_id: int, previous_term_ids: Set[int]
    ) -> Set[Tuple[Expr]]:
        values = {}
        entry_symbol = self.rule.relations_terms[term_id].entry_symbols[
            entry_symbol_index
        ]

        for term_id in previous_term_ids:
            relation_term = self.rule.relations_terms[term_id]

            for i in range(len(relation_term.entry_symbols)):
                if relation_term.entry_symbols[i] == entry_symbol:
                    values[(term_id, i)] = set([])
                    for expr_tuple in self.relation_term_cache[term_id]:
                        values[(term_id, i)].add((expr_tuple[i],))

        values_list = list(values.values())
        if len(values_list) == 0:
            return set([])
        common_values = set(values_list[0])
        for i in range(1, len(values_list)):
            common_values = common_values.intersection(values_list[i])
        return common_values

    def batch_join_RHS_relation_terms(self) -> Tuple[List[str], Set[Tuple]]:
        RHS_relation_term_id_list = list(self.rule.RHS_relation_term_ids)
        entry_symbols = self.rule.relations_terms[
            RHS_relation_term_id_list[0]
        ].entry_symbols
        content = self.relation_term_cache[RHS_relation_term_id_list[0]]

        for term_id in RHS_relation_term_id_list[1:]:
            another_entry_symbols = self.rule.relations_terms[term_id].entry_symbols
            another_content = self.relation_term_cache[term_id]
            entry_symbols, content = self.join_two_relation_terms(
                entry_symbols, another_entry_symbols, content, another_content
            )
        return entry_symbols, content

    def join_two_relation_terms(
        self,
        entry_symbols1: List[str],
        entry_symbols2: List[str],
        content1: Set[Tuple],
        content2: Set[Tuple],
    ) -> Tuple[List[str], Set[Tuple]]:
        res = set([])
        entry_symbols = entry_symbols1 + entry_symbols2

        eq_entry_symbol_pairs = set([])
        for i in range(len(entry_symbols)):
            for j in range(i):
                if entry_symbols[i] == entry_symbols[j]:
                    eq_entry_symbol_pairs.add((i, j))

        for tuple1 in content1:
            for tuple2 in content2:
                tuple = tuple1 + tuple2
                is_joinable = True
                for i, j in eq_entry_symbol_pairs:
                    if tuple[i] != tuple[j]:
                        is_joinable = False
                        break
                if is_joinable:
                    res.add(tuple)

        merge_symbols = list(set(entry_symbols))
        trim_res = set([])
        for tuple in res:
            new_tuple = ()
            for entry_symbol in merge_symbols:
                index = entry_symbols.index(entry_symbol)
                new_tuple += (tuple[index],)
            trim_res.add(new_tuple)
        return merge_symbols, trim_res

    def eval(
        self, env: Dict[int, Relation], ts_analyzer: TSAnalyzer
    ) -> Tuple[Dict[int, Relation], bool, int]:
        if self.eval_rule_mode == "full-featured":
            return self.eval_full_featured(env, ts_analyzer)
        elif self.eval_rule_mode == "binary-populate":
            return self.eval_with_binary_populate(env, ts_analyzer)
        elif self.eval_rule_mode == "binary-transform-forward":
            return self.eval_with_binary_transform(env, ts_analyzer, True)
        elif self.eval_rule_mode == "binary-transform-backward":
            return self.eval_with_binary_transform(env, ts_analyzer, False)

    def eval_full_featured(
        self, env: Dict[int, Relation], ts_analyzer: TSAnalyzer
    ) -> Tuple[Dict[int, Relation], bool, int]:
        print("Evaluating rule:", str(self.rule))

        # Step 1: sync relation_term_cache with env
        for term_id in self.relation_term_cache:
            self.relation_term_cache[term_id] = self.relation_term_cache[term_id].union(
                env[self.rule.relations_terms[term_id].relation_id].content
            )

        is_fixed_point = False
        while not is_fixed_point:
            previous_term_ids = set([])

            # Step 2.1: evaluate the first relation in the evaluation plan
            (term_id, relation_name, direction) = self.evaluation_plan[0]
            relation_content = self.relation_term_cache[term_id]
            if len(relation_content) == 0:
                if (
                    relation_name in self.symbolic_primitives
                    or relation_name in self.neural_primitives
                ):
                    primitive = self.get_neural_or_symbolic_primitive(relation_name)
                    if direction == 1:
                        if primitive.type == "unary":
                            exprs = primitive.transform(ts_analyzer)
                            primitive.print_primitive_name()
                            self.relation_term_cache[term_id] = set(
                                [(expr,) for expr in exprs]
                            )
                        else:
                            assert (
                                "Cannot apply binary primitive in the forward direction"
                            )
                    elif direction == -1:
                        assert "Cannot apply binary primitive in the backward direction"
                    else:
                        assert "Invalid direction"
                else:
                    print(
                        "Finish evaluating rule:",
                        str(self.rule),
                        "Is changed:",
                        False,
                        "\n",
                    )
                    return env, False, self.rule.rule_id
            previous_term_ids.add(term_id)

            # Step 2.2: evaluate the remaining relations in the evaluation plan
            for term_id, relation_name, direction in self.evaluation_plan[1:]:
                if direction == 1:
                    if (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 1
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            primitive.print_primitive_name()
                            exprs = primitive.transform(ts_analyzer)
                            self.relation_term_cache[term_id] = set(
                                [(expr,) for expr in exprs]
                            )

                        previous_term_ids.add(term_id)
                    elif (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 2
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            base_exprs = self.collect_common_values_of_entry_symbol(
                                0, term_id, previous_term_ids
                            )
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )

                            # incremental evaluation
                            old_base_exprs = self.evaluate_term_cache[term_id]
                            new_base_exprs = base_exprs - old_base_exprs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_exprs)
                            )

                            unwrapped_base_exprs = set([])
                            # for base_expr in new_base_exprs:
                            #     unwrapped_base_exprs.add(base_expr[0])
                            for base_expr in self.evaluate_term_cache[term_id]:
                                unwrapped_base_exprs.add(base_expr[0])

                            if self.parallel:
                                forward_transform_result = (
                                    primitive.batch_transform_forward(
                                        unwrapped_base_exprs,
                                        ts_analyzer,
                                        self.primitive_parallel_workers,
                                    )
                                )
                            else:
                                forward_transform_result = {}
                                for unwrapped_base_expr in unwrapped_base_exprs:
                                    primitive.print_primitive_name()
                                    forward_transform_result[unwrapped_base_expr] = (
                                        primitive.transform_forward(
                                            unwrapped_base_expr, ts_analyzer
                                        )
                                    )

                            for unwrapped_base_expr in forward_transform_result:
                                for expr in forward_transform_result[
                                    unwrapped_base_expr
                                ]:
                                    self.relation_term_cache[term_id].add(
                                        (unwrapped_base_expr, expr)
                                    )

                        previous_term_ids.add(term_id)
                elif direction == -1:
                    if (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 1
                    ):
                        assert "Cannot apply unary primitive in the backward direction"
                    elif (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 2
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )

                            # Incremental evaluation
                            base_exprs = self.collect_common_values_of_entry_symbol(
                                1, term_id, previous_term_ids
                            )
                            old_base_exprs = self.evaluate_term_cache[term_id]
                            new_base_exprs = base_exprs - old_base_exprs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_exprs)
                            )

                            unwrapped_base_exprs = set([])
                            # for base_expr in new_base_exprs:
                            #     unwrapped_base_exprs.add(base_expr[0])
                            for base_expr in self.evaluate_term_cache[term_id]:
                                unwrapped_base_exprs.add(base_expr[0])

                            if self.parallel:
                                backward_transform_result = (
                                    primitive.batch_transform_backward(
                                        unwrapped_base_exprs,
                                        ts_analyzer,
                                        self.primitive_parallel_workers,
                                    )
                                )
                            else:
                                backward_transform_result = {}
                                for unwrapped_base_expr in unwrapped_base_exprs:
                                    primitive.print_primitive_name()
                                    backward_transform_result[unwrapped_base_expr] = (
                                        primitive.transform_backward(
                                            unwrapped_base_expr, ts_analyzer
                                        )
                                    )

                            for unwrapped_base_expr in backward_transform_result:
                                for expr in backward_transform_result[
                                    unwrapped_base_expr
                                ]:
                                    self.relation_term_cache[term_id].add(
                                        (expr, unwrapped_base_expr)
                                    )

                        previous_term_ids.add(term_id)
                elif direction == 0:
                    if (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 1
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            base_exprs = self.collect_common_values_of_entry_symbol(
                                0, term_id, previous_term_ids
                            )
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )

                            old_base_exprs = self.evaluate_term_cache[term_id]
                            new_base_exprs = base_exprs - old_base_exprs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_exprs)
                            )

                            if self.parallel:
                                primitive.print_primitive_name()

                                # batch_check_result = primitive.batch_check(set([base_expr[0] for base_expr in new_base_exprs]), ts_analyzer)
                                batch_check_result = primitive.batch_check(
                                    set(
                                        [
                                            base_expr[0]
                                            for base_expr in self.evaluate_term_cache[
                                                term_id
                                            ]
                                        ]
                                    ),
                                    ts_analyzer,
                                    self.primitive_parallel_workers,
                                )

                                for expr in batch_check_result:
                                    if batch_check_result[expr]:
                                        self.relation_term_cache[term_id].add((expr,))
                            else:
                                # for base_expr in new_base_exprs:
                                for base_expr in self.evaluate_term_cache[term_id]:
                                    primitive.print_primitive_name()
                                    check_result = primitive.check(
                                        base_expr[0], ts_analyzer
                                    )
                                    if check_result:
                                        self.relation_term_cache[term_id].add(
                                            (base_expr)
                                        )
                        else:
                            pass
                        previous_term_ids.add(term_id)
                    elif (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 2
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            base_exprs_first = (
                                self.collect_common_values_of_entry_symbol(
                                    0, term_id, previous_term_ids
                                )
                            )
                            base_exprs_second = (
                                self.collect_common_values_of_entry_symbol(
                                    1, term_id, previous_term_ids
                                )
                            )
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            old_expr_pairs = self.evaluate_term_cache[term_id]
                            base_expr_pairs = set(
                                [
                                    (base_expr_first[0], base_expr_second[0])
                                    for base_expr_first in base_exprs_first
                                    for base_expr_second in base_exprs_second
                                ]
                            )
                            new_expr_pairs = base_expr_pairs - old_expr_pairs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_expr_pairs)
                            )

                            if self.parallel:
                                primitive.print_primitive_name()
                                # batch_check_result = primitive.batch_check(new_expr_pairs, ts_analyzer)
                                batch_check_result = primitive.batch_check(
                                    base_expr_pairs,
                                    ts_analyzer,
                                    self.primitive_parallel_workers,
                                )
                                for expr_first, expr_second in batch_check_result:
                                    if batch_check_result[(expr_first, expr_second)]:
                                        self.relation_term_cache[term_id].add(
                                            (expr_first, expr_second)
                                        )
                            else:
                                # for base_expr_first, base_expr_second in new_expr_pairs:
                                for (
                                    base_expr_first,
                                    base_expr_second,
                                ) in base_expr_pairs:
                                    primitive.print_primitive_name()
                                    check_result = primitive.check(
                                        base_expr_first, base_expr_second, ts_analyzer
                                    )
                                    if check_result:
                                        self.relation_term_cache[term_id].add(
                                            (base_expr_first, base_expr_second)
                                        )
                        else:
                            pass
                        previous_term_ids.add(term_id)

            # Step 2.3: project the values of entry symbols to target relation in this rule
            entry_symbols, content = self.batch_join_RHS_relation_terms()
            target_content = set([])
            indexes = []
            for entry_symbol in self.rule.relations_terms[
                self.rule.LHS_relation_term_id
            ].entry_symbols:
                if entry_symbol in entry_symbols:
                    indexes.append(entry_symbols.index(entry_symbol))

            for tuple in content:
                t = ()
                for index in indexes:
                    t += (tuple[index],)
                target_content.add(t)

            # Step 2.4: Check whether a fixed point is reached
            is_fixed_point = self.relation_term_cache[
                self.rule.LHS_relation_term_id
            ].issubset(target_content) and target_content.issubset(
                self.relation_term_cache[self.rule.LHS_relation_term_id]
            )
            self.relation_term_cache[self.rule.LHS_relation_term_id] = target_content

            # is_small_step = True to support better synchronization in the parallelization
            if self.is_small_step:
                break

        # Step 3: return updated relations and whether the target relation is changed or not
        new_env = deepcopy(env)
        for term_id in self.relation_term_cache:
            relation_id = self.rule.relations_terms[term_id].relation_id
            new_env[relation_id].content = new_env[relation_id].content.union(
                self.relation_term_cache[term_id]
            )

        is_changed = len(
            new_env[
                self.rule.relations_terms[self.rule.LHS_relation_term_id].relation_id
            ].content
        ) != len(
            env[
                self.rule.relations_terms[self.rule.LHS_relation_term_id].relation_id
            ].content
        )

        print(
            "Finish evaluating rule:", str(self.rule), "Is changed:", is_changed, "\n"
        )
        return new_env, is_changed, self.rule.rule_id

    def eval_with_binary_populate(
        self, env: Dict[int, Relation], ts_analyzer: TSAnalyzer
    ) -> Tuple[Dict[int, Relation], bool, int]:
        print("Evaluating rule:", str(self.rule))

        # Step 1: sync relation_term_cache with env
        for term_id in self.relation_term_cache:
            self.relation_term_cache[term_id] = self.relation_term_cache[term_id].union(
                env[self.rule.relations_terms[term_id].relation_id].content
            )

        is_fixed_point = False
        while not is_fixed_point:
            previous_term_ids = set([])

            # Step 2.1: evaluate the first relation in the evaluation plan
            (term_id, relation_name, direction) = self.evaluation_plan[0]
            relation_content = self.relation_term_cache[term_id]
            if len(relation_content) == 0:
                if (
                    relation_name in self.symbolic_primitives
                    or relation_name in self.neural_primitives
                ):
                    primitive = self.get_neural_or_symbolic_primitive(relation_name)
                    if direction == 1:
                        if primitive.type == "unary":
                            exprs = primitive.transform(ts_analyzer)
                            primitive.print_primitive_name()
                            self.relation_term_cache[term_id] = set(
                                [(expr,) for expr in exprs]
                            )
                        else:
                            assert (
                                "Cannot apply binary primitive in the forward direction"
                            )
                    elif direction == -1:
                        assert "Cannot apply binary primitive in the backward direction"
                    else:
                        assert "Invalid direction"
                else:
                    print(
                        "Finish evaluating rule:",
                        str(self.rule),
                        "Is changed:",
                        False,
                        "\n",
                    )
                    return env, False, self.rule.rule_id
            previous_term_ids.add(term_id)

            # Step 2.2: evaluate the remaining relations in the evaluation plan
            for term_id, relation_name, direction in self.evaluation_plan[1:]:
                if direction == 1:
                    if (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 1
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            primitive.print_primitive_name()
                            exprs = primitive.transform(ts_analyzer)
                            self.relation_term_cache[term_id] = set(
                                [(expr,) for expr in exprs]
                            )

                        previous_term_ids.add(term_id)
                    elif (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 2
                    ):
                        if relation_name in self.symbolic_primitives:
                            base_exprs = self.collect_common_values_of_entry_symbol(
                                0, term_id, previous_term_ids
                            )
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_exprs)
                            )

                            unwrapped_base_exprs = set([])
                            for base_expr in self.evaluate_term_cache[term_id]:
                                unwrapped_base_exprs.add(base_expr[0])

                            if self.parallel:
                                forward_transform_result = (
                                    primitive.batch_transform_forward(
                                        unwrapped_base_exprs,
                                        ts_analyzer,
                                        self.primitive_parallel_workers,
                                    )
                                )
                            else:
                                forward_transform_result = {}
                                for unwrapped_base_expr in unwrapped_base_exprs:
                                    primitive.print_primitive_name()
                                    forward_transform_result[unwrapped_base_expr] = (
                                        primitive.transform_forward(
                                            unwrapped_base_expr, ts_analyzer
                                        )
                                    )

                            for unwrapped_base_expr in forward_transform_result:
                                for expr in forward_transform_result[
                                    unwrapped_base_expr
                                ]:
                                    self.relation_term_cache[term_id].add(
                                        (unwrapped_base_expr, expr)
                                    )

                        elif relation_name in self.neural_primitives:
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            self.relation_term_cache[term_id] = primitive.populate(
                                ts_analyzer
                            )

                        previous_term_ids.add(term_id)

                elif direction == -1:
                    if (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 1
                    ):
                        assert "Cannot apply unary primitive in the backward direction"
                    elif (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 2
                    ):
                        if relation_name in self.symbolic_primitives:
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )

                            # Incremental evaluation
                            base_exprs = self.collect_common_values_of_entry_symbol(
                                1, term_id, previous_term_ids
                            )
                            old_base_exprs = self.evaluate_term_cache[term_id]
                            new_base_exprs = base_exprs - old_base_exprs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_exprs)
                            )

                            unwrapped_base_exprs = set([])
                            # for base_expr in new_base_exprs:
                            #     unwrapped_base_exprs.add(base_expr[0])
                            for base_expr in self.evaluate_term_cache[term_id]:
                                unwrapped_base_exprs.add(base_expr[0])

                            if self.parallel:
                                backward_transform_result = (
                                    primitive.batch_transform_backward(
                                        unwrapped_base_exprs,
                                        ts_analyzer,
                                        self.primitive_parallel_workers,
                                    )
                                )
                            else:
                                backward_transform_result = {}
                                for unwrapped_base_expr in unwrapped_base_exprs:
                                    primitive.print_primitive_name()
                                    backward_transform_result[unwrapped_base_expr] = (
                                        primitive.transform_backward(
                                            unwrapped_base_expr, ts_analyzer
                                        )
                                    )

                            for unwrapped_base_expr in backward_transform_result:
                                for expr in backward_transform_result[
                                    unwrapped_base_expr
                                ]:
                                    self.relation_term_cache[term_id].add(
                                        (expr, unwrapped_base_expr)
                                    )

                        elif relation_name in self.neural_primitives:
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            self.relation_term_cache[term_id] = primitive.populate(
                                ts_analyzer
                            )

                        previous_term_ids.add(term_id)
                elif direction == 0:
                    if (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 1
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            base_exprs = self.collect_common_values_of_entry_symbol(
                                0, term_id, previous_term_ids
                            )
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )

                            old_base_exprs = self.evaluate_term_cache[term_id]
                            new_base_exprs = base_exprs - old_base_exprs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_exprs)
                            )

                            if self.parallel:
                                primitive.print_primitive_name()

                                # batch_check_result = primitive.batch_check(set([base_expr[0] for base_expr in new_base_exprs]), ts_analyzer)
                                batch_check_result = primitive.batch_check(
                                    set(
                                        [
                                            base_expr[0]
                                            for base_expr in self.evaluate_term_cache[
                                                term_id
                                            ]
                                        ]
                                    ),
                                    ts_analyzer,
                                    self.primitive_parallel_workers,
                                )

                                for expr in batch_check_result:
                                    if batch_check_result[expr]:
                                        self.relation_term_cache[term_id].add((expr,))
                            else:
                                # for base_expr in new_base_exprs:
                                for base_expr in self.evaluate_term_cache[term_id]:
                                    primitive.print_primitive_name()
                                    check_result = primitive.check(
                                        base_expr[0], ts_analyzer
                                    )
                                    if check_result:
                                        self.relation_term_cache[term_id].add(
                                            (base_expr)
                                        )
                        else:
                            pass
                        previous_term_ids.add(term_id)
                    elif (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 2
                    ):
                        if relation_name in self.symbolic_primitives:
                            base_exprs_first = (
                                self.collect_common_values_of_entry_symbol(
                                    0, term_id, previous_term_ids
                                )
                            )
                            base_exprs_second = (
                                self.collect_common_values_of_entry_symbol(
                                    1, term_id, previous_term_ids
                                )
                            )
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            old_expr_pairs = self.evaluate_term_cache[term_id]
                            base_expr_pairs = set(
                                [
                                    (base_expr_first[0], base_expr_second[0])
                                    for base_expr_first in base_exprs_first
                                    for base_expr_second in base_exprs_second
                                ]
                            )
                            new_expr_pairs = base_expr_pairs - old_expr_pairs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_expr_pairs)
                            )

                            if self.parallel:
                                primitive.print_primitive_name()
                                # batch_check_result = primitive.batch_check(new_expr_pairs, ts_analyzer)
                                batch_check_result = primitive.batch_check(
                                    base_expr_pairs,
                                    ts_analyzer,
                                    self.primitive_parallel_workers,
                                )
                                for expr_first, expr_second in batch_check_result:
                                    if batch_check_result[(expr_first, expr_second)]:
                                        self.relation_term_cache[term_id].add(
                                            (expr_first, expr_second)
                                        )
                            else:
                                # for base_expr_first, base_expr_second in new_expr_pairs:
                                for (
                                    base_expr_first,
                                    base_expr_second,
                                ) in base_expr_pairs:
                                    primitive.print_primitive_name()
                                    check_result = primitive.check(
                                        base_expr_first, base_expr_second, ts_analyzer
                                    )
                                    if check_result:
                                        self.relation_term_cache[term_id].add(
                                            (base_expr_first, base_expr_second)
                                        )
                        elif relation_name in self.neural_primitives:
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            self.relation_term_cache[term_id] = primitive.populate(
                                ts_analyzer
                            )
                        previous_term_ids.add(term_id)

            # Step 2.3: project the values of entry symbols to target relation in this rule
            entry_symbols, content = self.batch_join_RHS_relation_terms()
            target_content = set([])
            indexes = []
            for entry_symbol in self.rule.relations_terms[
                self.rule.LHS_relation_term_id
            ].entry_symbols:
                if entry_symbol in entry_symbols:
                    indexes.append(entry_symbols.index(entry_symbol))

            for tuple in content:
                t = ()
                for index in indexes:
                    t += (tuple[index],)
                target_content.add(t)

            # Step 2.4: Check whether a fixed point is reached
            is_fixed_point = self.relation_term_cache[
                self.rule.LHS_relation_term_id
            ].issubset(target_content) and target_content.issubset(
                self.relation_term_cache[self.rule.LHS_relation_term_id]
            )
            self.relation_term_cache[self.rule.LHS_relation_term_id] = target_content

            # is_small_step = True to support better synchronization in the parallelization
            if self.is_small_step:
                break

        # Step 3: return updated relations and whether the target relation is changed or not
        new_env = deepcopy(env)
        for term_id in self.relation_term_cache:
            relation_id = self.rule.relations_terms[term_id].relation_id
            new_env[relation_id].content = new_env[relation_id].content.union(
                self.relation_term_cache[term_id]
            )

        is_changed = len(
            new_env[
                self.rule.relations_terms[self.rule.LHS_relation_term_id].relation_id
            ].content
        ) != len(
            env[
                self.rule.relations_terms[self.rule.LHS_relation_term_id].relation_id
            ].content
        )

        print(
            "Finish evaluating rule:", str(self.rule), "Is changed:", is_changed, "\n"
        )
        return new_env, is_changed, self.rule.rule_id

    def eval_with_binary_transform(
        self, env: Dict[int, Relation], ts_analyzer: TSAnalyzer, is_forward: bool
    ) -> Tuple[Dict[int, Relation], bool, int]:
        print("Evaluating rule:", str(self.rule))

        # Step 1: sync relation_term_cache with env
        for term_id in self.relation_term_cache:
            self.relation_term_cache[term_id] = self.relation_term_cache[term_id].union(
                env[self.rule.relations_terms[term_id].relation_id].content
            )

        is_fixed_point = False
        while not is_fixed_point:
            previous_term_ids = set([])

            # Step 2.1: evaluate the first relation in the evaluation plan
            (term_id, relation_name, direction) = self.evaluation_plan[0]
            relation_content = self.relation_term_cache[term_id]
            if len(relation_content) == 0:
                if (
                    relation_name in self.symbolic_primitives
                    or relation_name in self.neural_primitives
                ):
                    primitive = self.get_neural_or_symbolic_primitive(relation_name)
                    if direction == 1:
                        if primitive.type == "unary":
                            exprs = primitive.transform(ts_analyzer)
                            primitive.print_primitive_name()
                            self.relation_term_cache[term_id] = set(
                                [(expr,) for expr in exprs]
                            )
                        else:
                            assert (
                                "Cannot apply binary primitive in the forward direction"
                            )
                    elif direction == -1:
                        assert "Cannot apply binary primitive in the backward direction"
                    else:
                        assert "Invalid direction"
                else:
                    print(
                        "Finish evaluating rule:",
                        str(self.rule),
                        "Is changed:",
                        False,
                        "\n",
                    )
                    return env, False, self.rule.rule_id
            previous_term_ids.add(term_id)

            # Step 2.2: evaluate the remaining relations in the evaluation plan
            for term_id, relation_name, direction in self.evaluation_plan[1:]:
                if direction == 1:
                    if (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 1
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            primitive.print_primitive_name()
                            exprs = primitive.transform(ts_analyzer)
                            self.relation_term_cache[term_id] = set(
                                [(expr,) for expr in exprs]
                            )

                        previous_term_ids.add(term_id)
                    elif (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 2
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            base_exprs = self.collect_common_values_of_entry_symbol(
                                0, term_id, previous_term_ids
                            )
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )

                            # incremental evaluation
                            old_base_exprs = self.evaluate_term_cache[term_id]
                            new_base_exprs = base_exprs - old_base_exprs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_exprs)
                            )

                            unwrapped_base_exprs = set([])
                            # for base_expr in new_base_exprs:
                            #     unwrapped_base_exprs.add(base_expr[0])
                            for base_expr in self.evaluate_term_cache[term_id]:
                                unwrapped_base_exprs.add(base_expr[0])

                            if self.parallel:
                                forward_transform_result = (
                                    primitive.batch_transform_forward(
                                        unwrapped_base_exprs,
                                        ts_analyzer,
                                        self.primitive_parallel_workers,
                                    )
                                )
                            else:
                                forward_transform_result = {}
                                for unwrapped_base_expr in unwrapped_base_exprs:
                                    primitive.print_primitive_name()
                                    forward_transform_result[unwrapped_base_expr] = (
                                        primitive.transform_forward(
                                            unwrapped_base_expr, ts_analyzer
                                        )
                                    )

                            for unwrapped_base_expr in forward_transform_result:
                                for expr in forward_transform_result[
                                    unwrapped_base_expr
                                ]:
                                    self.relation_term_cache[term_id].add(
                                        (unwrapped_base_expr, expr)
                                    )

                        previous_term_ids.add(term_id)
                elif direction == -1:
                    if (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 1
                    ):
                        assert "Cannot apply unary primitive in the backward direction"
                    elif (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 2
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )

                            # Incremental evaluation
                            base_exprs = self.collect_common_values_of_entry_symbol(
                                1, term_id, previous_term_ids
                            )
                            old_base_exprs = self.evaluate_term_cache[term_id]
                            new_base_exprs = base_exprs - old_base_exprs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_exprs)
                            )

                            unwrapped_base_exprs = set([])
                            # for base_expr in new_base_exprs:
                            #     unwrapped_base_exprs.add(base_expr[0])
                            for base_expr in self.evaluate_term_cache[term_id]:
                                unwrapped_base_exprs.add(base_expr[0])

                            if self.parallel:
                                backward_transform_result = (
                                    primitive.batch_transform_backward(
                                        unwrapped_base_exprs,
                                        ts_analyzer,
                                        self.primitive_parallel_workers,
                                    )
                                )
                            else:
                                backward_transform_result = {}
                                for unwrapped_base_expr in unwrapped_base_exprs:
                                    primitive.print_primitive_name()
                                    backward_transform_result[unwrapped_base_expr] = (
                                        primitive.transform_backward(
                                            unwrapped_base_expr, ts_analyzer
                                        )
                                    )

                            for unwrapped_base_expr in backward_transform_result:
                                for expr in backward_transform_result[
                                    unwrapped_base_expr
                                ]:
                                    self.relation_term_cache[term_id].add(
                                        (expr, unwrapped_base_expr)
                                    )

                        previous_term_ids.add(term_id)
                elif direction == 0:
                    if (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 1
                    ):
                        if (
                            relation_name in self.symbolic_primitives
                            or relation_name in self.neural_primitives
                        ):
                            base_exprs = self.collect_common_values_of_entry_symbol(
                                0, term_id, previous_term_ids
                            )
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )

                            old_base_exprs = self.evaluate_term_cache[term_id]
                            new_base_exprs = base_exprs - old_base_exprs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_exprs)
                            )

                            if self.parallel:
                                primitive.print_primitive_name()

                                # batch_check_result = primitive.batch_check(set([base_expr[0] for base_expr in new_base_exprs]), ts_analyzer)
                                batch_check_result = primitive.batch_check(
                                    set(
                                        [
                                            base_expr[0]
                                            for base_expr in self.evaluate_term_cache[
                                                term_id
                                            ]
                                        ]
                                    ),
                                    ts_analyzer,
                                    self.primitive_parallel_workers,
                                )

                                for expr in batch_check_result:
                                    if batch_check_result[expr]:
                                        self.relation_term_cache[term_id].add((expr,))
                            else:
                                # for base_expr in new_base_exprs:
                                for base_expr in self.evaluate_term_cache[term_id]:
                                    primitive.print_primitive_name()
                                    check_result = primitive.check(
                                        base_expr[0], ts_analyzer
                                    )
                                    if check_result:
                                        self.relation_term_cache[term_id].add(
                                            (base_expr)
                                        )
                        else:
                            pass
                        previous_term_ids.add(term_id)
                    elif (
                        self.rule.relations[
                            self.rule.relation_name_id_map[relation_name]
                        ].arity
                        == 2
                    ):
                        if relation_name in self.symbolic_primitives:
                            base_exprs_first = (
                                self.collect_common_values_of_entry_symbol(
                                    0, term_id, previous_term_ids
                                )
                            )
                            base_exprs_second = (
                                self.collect_common_values_of_entry_symbol(
                                    1, term_id, previous_term_ids
                                )
                            )
                            primitive = self.get_neural_or_symbolic_primitive(
                                relation_name
                            )
                            old_expr_pairs = self.evaluate_term_cache[term_id]
                            base_expr_pairs = set(
                                [
                                    (base_expr_first[0], base_expr_second[0])
                                    for base_expr_first in base_exprs_first
                                    for base_expr_second in base_exprs_second
                                ]
                            )
                            new_expr_pairs = base_expr_pairs - old_expr_pairs
                            self.evaluate_term_cache[term_id] = (
                                self.evaluate_term_cache[term_id].union(base_expr_pairs)
                            )

                            if self.parallel:
                                primitive.print_primitive_name()
                                # batch_check_result = primitive.batch_check(new_expr_pairs, ts_analyzer)
                                batch_check_result = primitive.batch_check(
                                    base_expr_pairs,
                                    ts_analyzer,
                                    self.primitive_parallel_workers,
                                )
                                for expr_first, expr_second in batch_check_result:
                                    if batch_check_result[(expr_first, expr_second)]:
                                        self.relation_term_cache[term_id].add(
                                            (expr_first, expr_second)
                                        )
                            else:
                                # for base_expr_first, base_expr_second in new_expr_pairs:
                                for (
                                    base_expr_first,
                                    base_expr_second,
                                ) in base_expr_pairs:
                                    primitive.print_primitive_name()
                                    check_result = primitive.check(
                                        base_expr_first, base_expr_second, ts_analyzer
                                    )
                                    if check_result:
                                        self.relation_term_cache[term_id].add(
                                            (base_expr_first, base_expr_second)
                                        )
                        elif relation_name in self.neural_primitives:
                            if is_forward:
                                # forward transform
                                base_exprs_first = (
                                    self.collect_common_values_of_entry_symbol(
                                        0, term_id, previous_term_ids
                                    )
                                )
                                primitive = self.get_neural_or_symbolic_primitive(
                                    relation_name
                                )

                                # incremental evaluation
                                old_base_exprs = self.evaluate_term_cache[term_id]
                                self.evaluate_term_cache[term_id] = (
                                    self.evaluate_term_cache[term_id].union(
                                        base_exprs_first
                                    )
                                )

                                unwrapped_base_exprs = set([])
                                for base_expr in self.evaluate_term_cache[term_id]:
                                    unwrapped_base_exprs.add(base_expr[0])

                                if self.parallel:
                                    forward_transform_result = (
                                        primitive.batch_transform_forward(
                                            unwrapped_base_exprs,
                                            ts_analyzer,
                                            self.primitive_parallel_workers,
                                        )
                                    )
                                else:
                                    forward_transform_result = {}
                                    for unwrapped_base_expr in unwrapped_base_exprs:
                                        primitive.print_primitive_name()
                                        forward_transform_result[
                                            unwrapped_base_expr
                                        ] = primitive.transform_forward(
                                            unwrapped_base_expr, ts_analyzer
                                        )

                                for unwrapped_base_expr in forward_transform_result:
                                    for expr in forward_transform_result[
                                        unwrapped_base_expr
                                    ]:
                                        self.relation_term_cache[term_id].add(
                                            (unwrapped_base_expr, expr)
                                        )
                            else:
                                # backward transform
                                base_exprs_second = (
                                    self.collect_common_values_of_entry_symbol(
                                        1, term_id, previous_term_ids
                                    )
                                )
                                primitive = self.get_neural_or_symbolic_primitive(
                                    relation_name
                                )

                                # incremental evaluation
                                old_base_exprs = self.evaluate_term_cache[term_id]
                                self.evaluate_term_cache[term_id] = (
                                    self.evaluate_term_cache[term_id].union(
                                        base_exprs_second
                                    )
                                )

                                unwrapped_base_exprs = set([])
                                for base_expr in self.evaluate_term_cache[term_id]:
                                    # print(len(base_expr))
                                    # exit(0)
                                    unwrapped_base_exprs.add(base_expr[0])

                                if self.parallel:
                                    backward_transform_result = (
                                        primitive.batch_transform_backward(
                                            unwrapped_base_exprs,
                                            ts_analyzer,
                                            self.primitive_parallel_workers,
                                        )
                                    )
                                else:
                                    backward_transform_result = {}
                                    for unwrapped_base_expr in unwrapped_base_exprs:
                                        primitive.print_primitive_name()
                                        backward_transform_result[
                                            unwrapped_base_expr
                                        ] = primitive.transform_backward(
                                            unwrapped_base_expr, ts_analyzer
                                        )

                                for unwrapped_base_expr in backward_transform_result:
                                    for expr in backward_transform_result[
                                        unwrapped_base_expr
                                    ]:
                                        self.relation_term_cache[term_id].add(
                                            (expr, unwrapped_base_expr)
                                        )

                        previous_term_ids.add(term_id)

            # Step 2.3: project the values of entry symbols to target relation in this rule
            entry_symbols, content = self.batch_join_RHS_relation_terms()
            target_content = set([])
            indexes = []
            for entry_symbol in self.rule.relations_terms[
                self.rule.LHS_relation_term_id
            ].entry_symbols:
                if entry_symbol in entry_symbols:
                    indexes.append(entry_symbols.index(entry_symbol))

            for tuple in content:
                t = ()
                for index in indexes:
                    t += (tuple[index],)
                target_content.add(t)

            # Step 2.4: Check whether a fixed point is reached
            is_fixed_point = self.relation_term_cache[
                self.rule.LHS_relation_term_id
            ].issubset(target_content) and target_content.issubset(
                self.relation_term_cache[self.rule.LHS_relation_term_id]
            )
            self.relation_term_cache[self.rule.LHS_relation_term_id] = target_content

            # is_small_step = True to support better synchronization in the parallelization
            if self.is_small_step:
                break

        # Step 3: return updated relations and whether the target relation is changed or not
        new_env = deepcopy(env)
        for term_id in self.relation_term_cache:
            relation_id = self.rule.relations_terms[term_id].relation_id
            new_env[relation_id].content = new_env[relation_id].content.union(
                self.relation_term_cache[term_id]
            )

        is_changed = len(
            new_env[
                self.rule.relations_terms[self.rule.LHS_relation_term_id].relation_id
            ].content
        ) != len(
            env[
                self.rule.relations_terms[self.rule.LHS_relation_term_id].relation_id
            ].content
        )

        print(
            "Finish evaluating rule:", str(self.rule), "Is changed:", is_changed, "\n"
        )
        return new_env, is_changed, self.rule.rule_id
