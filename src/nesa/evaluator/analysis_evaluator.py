from concurrent.futures import ThreadPoolExecutor
from parser.program_parser import *
from parser.analysis_parser import *
from primitive.primitive import *
from primitive.symbolic import *
from primitive.neural.neural_unary_primitive import *
from primitive.neural.neural_binary_primitive import *
from evaluator.rule_evaluator import RuleEvaluator
from evaluator.utility import *
from copy import deepcopy
from pathlib import Path
from typing import Any, Dict, List, Set, Tuple
from utility.io import *


class AnalysisEvaluator:
    def __init__(
        self,
        analyzer_file: str,
        all_java_files: Dict[str, str],
        language: str,
        inference_model_name: str,
        inference_key_str: str,
        temperature: float,
        additional_context_file: str,
        eval_rule_mode: str,
        parallel_rule_n: int,
        parallel_primitive_n: int,
        semi_naive_evaluation: bool,
        measure_token_cost: bool,
    ) -> None:
        # configuration

        # get the file path of the current python file
        current_directory = os.path.dirname(os.path.realpath(__file__))

        analyzer_path = Path(analyzer_file)
        if not analyzer_path.is_absolute():
            if analyzer_path.exists():
                analyzer_path = analyzer_path.resolve()
            else:
                analyzer_path = (
                    Path(current_directory).parent / "analysis" / analyzer_path
                )

        analyzer_file = analyzer_path
        self.analysis = parse_datalog_program(analyzer_file)
        self.all_java_files = all_java_files
        self.language = language
        self.inference_model_name = inference_model_name
        self.inference_key_str = inference_key_str
        self.temperature = temperature
        self.additional_context_file = additional_context_file

        self.eval_rule_mode = eval_rule_mode
        self.parallel_rule_n = max(1, parallel_rule_n)
        self.parallel_primitive_n = max(1, parallel_primitive_n)
        self.semi_naive_evaluation = semi_naive_evaluation
        self.measure_token_cost = measure_token_cost

        # meta information of rules
        self.derived_relations = self.get_derived_relations()
        self.relation_name_id_map = self.get_relation_name_id_map()
        self.rules = self.get_all_rules(self.relation_name_id_map)

        # analysis components
        self.ts_analyzer = TSAnalyzer(self.all_java_files, self.language)
        self.model = LLM(
            self.inference_model_name,
            self.inference_key_str,
            self.temperature,
            self.measure_token_cost,
        )
        self.available_neural_primitives = self.collect_neural_primitives()
        self.available_symbolic_primitives = self.collect_symbolic_primitives()

        # map each relation to a pair of sets, indicating the in/out sets. Regard the relation as a transfer function.
        self.rule_dependency_graph = {}
        self.final_target_relation_id = None

        # intermediate environment
        self.env = {}
        for rule in self.rules:
            self.env.update(deepcopy(self.rules[rule].relations))

        # final result
        self.result = set([])
        return

    def get_derived_relations(self) -> Set[str]:
        derived_relations = set([])
        for rule_id in self.analysis:
            (_, rule_ast) = self.analysis[rule_id]
            derived_relations.add(rule_ast["left"]["relation"])
        return derived_relations

    def get_relation_name_id_map(self) -> Dict[str, int]:
        name_id_map = {}
        for rule_id in self.analysis:
            (_, rule_ast) = self.analysis[rule_id]
            for relation in rule_ast["right"]:
                if relation["relation"] not in name_id_map:
                    name_id_map[relation["relation"]] = len(name_id_map)
            if rule_ast["left"]["relation"] not in name_id_map:
                name_id_map[rule_ast["left"]["relation"]] = len(name_id_map)
        return name_id_map

    def get_all_rules(self, relation_name_id_map: Dict[str, int]) -> Dict[str, Rule]:
        parsed_rules = {}
        for rule_id in self.analysis:
            (rule_obj, rule_ast) = self.analysis[rule_id]
            rule = Rule(rule_id, rule_obj, rule_ast, relation_name_id_map)
            parsed_rules[rule_id] = rule
        return parsed_rules

    def collect_neural_primitives(self) -> Dict[str, Primitive]:
        current_directory = os.path.dirname(os.path.realpath(__file__))
        prompt_directory = Path(current_directory).parent / "config" / "neural"

        if self.additional_context_file != "" and os.path.exists(
            self.additional_context_file
        ):
            # read the content from the additional context file
            with open(self.additional_context_file, "r") as f:
                additional_context = f.read()
        else:
            additional_context = ""

        neural_primitives = {}
        for file in os.listdir(prompt_directory / "binary"):
            if (
                "_forward.json" in file
                or "_backward.json" in file
                or "_populate" in file
                or "_check" in file
            ):
                primitive_name = (
                    file.replace("_forward.json", "")
                    .replace("_backward.json", "")
                    .replace("_populate.json", "")
                    .replace("_check.json", "")
                )
                if primitive_name not in neural_primitives:
                    neural_primitives[primitive_name] = NeuralBinaryPrimitive(
                        primitive_name,
                        self.model,
                        self.semi_naive_evaluation,
                        additional_context,
                    )

        for file in os.listdir(prompt_directory / "unary"):
            if "_transform.json" in file:
                primitive_name = file.replace("_transform.json", "")
                if primitive_name not in neural_primitives:
                    neural_primitives[primitive_name] = NeuralUnaryPrimitive(
                        primitive_name,
                        self.model,
                        self.semi_naive_evaluation,
                        additional_context,
                    )
        return neural_primitives

    def collect_symbolic_primitives(self) -> Dict[str, Primitive]:
        def convert_file_name_to_primitive_class_name(file_name: str) -> str:
            primitive_class_name = ""
            for word in file_name.split("_"):
                primitive_class_name += word[0:1].capitalize()
                primitive_class_name += word[1:]
            return (
                primitive_class_name[0:1].capitalize()
                + primitive_class_name[1:]
                + "Primitive"
            )

        current_directory = os.path.dirname(os.path.realpath(__file__))
        symbolic_primitive_directory = (
            Path(current_directory).parent / "primitive" / "symbolic"
        )
        symbolic_primitives = {}
        for file in os.listdir(symbolic_primitive_directory):
            if file.endswith(".py") and file != "__init__.py":
                name = file.replace(".py", "")
                module = __import__(
                    "primitive.symbolic." + name,
                    fromlist=[convert_file_name_to_primitive_class_name(name)],
                )
                class_ = getattr(
                    module, convert_file_name_to_primitive_class_name(name)
                )
                symbolic_primitives[name] = class_()
        return symbolic_primitives

    def select_rules_from_worklist_for_parallel_evaluation(
        self, worklist: Set[int]
    ) -> Set[int]:
        selected_rules = set([])
        conflict_dict = {}

        for rule_id_1 in worklist:
            for rule_id_2 in worklist:
                if rule_id_1 == rule_id_2:
                    continue
                common_relation_ids = self.rules[
                    rule_id_1
                ].RHS_relation_ids.intersection(self.rules[rule_id_2].RHS_relation_ids)
                for common_relation_id in common_relation_ids:
                    common_relation_name = next(
                        (
                            k
                            for k, v in self.relation_name_id_map.items()
                            if v == common_relation_id
                        ),
                        None,
                    )
                    if common_relation_name in self.available_neural_primitives:
                        if rule_id_1 not in conflict_dict:
                            conflict_dict[rule_id_1] = set([])
                        conflict_dict[rule_id_1].add(rule_id_2)
                        if rule_id_2 not in conflict_dict:
                            conflict_dict[rule_id_2] = set([])
                        conflict_dict[rule_id_2].add(rule_id_1)

        for rule_id in worklist:
            if rule_id in selected_rules:
                continue
            if rule_id not in conflict_dict:
                selected_rules.add(rule_id)
            else:
                for conflict_rule_id in conflict_dict[rule_id]:
                    if conflict_rule_id not in selected_rules:
                        if rule_id in self.rule_dependency_graph:
                            if conflict_rule_id in self.rule_dependency_graph[rule_id]:
                                if conflict_rule_id not in selected_rules:
                                    selected_rules.add(conflict_rule_id)
        return selected_rules

    def eval(self) -> None:
        if self.parallel_rule_n > 1:
            self.eval_parallel()
        else:
            self.eval_sequential()
        return

    def eval_sequential(self) -> None:
        # Step 1: compute the rule dependency graph
        for rule_id_1 in self.rules:
            for rule_id_2 in self.rules:
                rule_1 = self.rules[rule_id_1]
                rule_2 = self.rules[rule_id_2]
                if rule_2.LHS_relation_id in rule_1.RHS_relation_ids:
                    if rule_id_1 not in self.rule_dependency_graph:
                        self.rule_dependency_graph[rule_id_1] = set([])
                    self.rule_dependency_graph[rule_id_1].add(rule_id_2)

        # worklist algorithm
        worklist = set([])
        for rule_id in self.rules:
            if rule_id not in self.rule_dependency_graph:
                worklist.add(rule_id)

        while len(worklist) > 0:
            print(len(worklist))
            rule_id = worklist.pop()
            rule = self.rules[rule_id]

            rule_evaluator = RuleEvaluator(
                rule,
                self.derived_relations,
                self.available_symbolic_primitives,
                self.available_neural_primitives,
                self.eval_rule_mode,
                self.parallel_primitive_n,
                True,
            )
            env, is_changed, evaluated_rule_id = rule_evaluator.eval(
                self.env, self.ts_analyzer
            )

            for relation_id in self.env:
                self.env[relation_id].content = env[relation_id].content.union(
                    self.env[relation_id].content
                )

            if is_changed:
                for rule_id_parent in self.rule_dependency_graph:
                    if rule_id in self.rule_dependency_graph[rule_id_parent]:
                        worklist.add(rule_id_parent)
        return

    def eval_parallel(self) -> None:
        def eval_rule(
            rule: Rule, self: "AnalysisEvaluator"
        ) -> Tuple[Dict[int, Relation], bool, int]:
            rule_evaluator = RuleEvaluator(
                rule,
                self.derived_relations,
                self.available_symbolic_primitives,
                self.available_neural_primitives,
                self.eval_rule_mode,
                self.parallel_primitive_n,
                True,
            )
            return rule_evaluator.eval(self.env, self.ts_analyzer)

        for rule_id_1 in self.rules:
            for rule_id_2 in self.rules:
                rule_1 = self.rules[rule_id_1]
                rule_2 = self.rules[rule_id_2]
                if rule_2.LHS_relation_id in rule_1.RHS_relation_ids:
                    if rule_id_1 not in self.rule_dependency_graph:
                        self.rule_dependency_graph[rule_id_1] = set([])
                    self.rule_dependency_graph[rule_id_1].add(rule_id_2)

        # debug use
        dep_graph_str = ""
        for rule_id_1 in self.rule_dependency_graph:
            for rule_id_2 in self.rule_dependency_graph[rule_id_1]:
                rule1 = self.rules[rule_id_1]
                rule2 = self.rules[rule_id_2]
                dep_graph_str += "dependency rule pair\n"
                dep_graph_str += str(rule1) + "\n"
                dep_graph_str += str(rule2) + "\n"
                dep_graph_str += "\n"
        print(dep_graph_str)

        # exit(0)

        worklist = set([])
        for rule_id in self.rules:
            if rule_id not in self.rule_dependency_graph:
                worklist.add(rule_id)

        epoch = 0

        while len(worklist) > 0:
            print("========================================")
            print("************ Worklist **************")
            print("epoch ID:", epoch)
            epoch += 1
            print("========================================")
            for rule_id in worklist:
                print(str(self.rules[rule_id]))
            print("========================================\n")

            new_worklist = set([])

            with ThreadPoolExecutor(max_workers=self.parallel_rule_n) as executor:
                future_results = []

                # select rule_ids in worklist to evaluate
                selected_rule_ids = (
                    self.select_rules_from_worklist_for_parallel_evaluation(worklist)
                )

                executed_rule_str = "====Run in parallel====\n"
                for rule_id in selected_rule_ids:
                    executed_rule_str += str(self.rules[rule_id]) + "\n"
                executed_rule_str += "=======================\n\n"
                print(executed_rule_str)

                for rule_id in selected_rule_ids:
                    future_results.append(
                        executor.submit(eval_rule, self.rules[rule_id], self)
                    )

                # delete the elements of selected_rule_ids from worklist
                for rule_id in selected_rule_ids:
                    worklist.discard(rule_id)

                new_worklist = worklist.difference(selected_rule_ids)

                for future in future_results:
                    env, is_changed, rule_id = future.result()
                    if is_changed:
                        for rule_id_parent in self.rule_dependency_graph:
                            if rule_id in self.rule_dependency_graph[rule_id_parent]:
                                new_worklist.add(rule_id_parent)
                    for relation_id in self.env:
                        self.env[relation_id].content = env[relation_id].content.union(
                            self.env[relation_id].content
                        )

            worklist = new_worklist

    def run(self) -> None:
        self.eval()

        print("========================================")
        print("************ All Relations **************")
        print("========================================")
        for relation_id in self.env:
            print("relation:", self.env[relation_id].name)
            for tuple in self.env[relation_id].content:
                print([str(expr) for expr in tuple])
            print("\n")

        print("========================================")
        print("************ Final Result **************")
        print("========================================")

        print("final result:")
        # collect the final result

        if len(self.rule_dependency_graph) != 0:
            for rule_id in self.rule_dependency_graph:
                is_final_target = True
                for other_rule_id in self.rule_dependency_graph:
                    if rule_id == other_rule_id:
                        continue
                    if rule_id in self.rule_dependency_graph[other_rule_id]:
                        if (
                            self.rules[rule_id].LHS_relation_id
                            != self.rules[other_rule_id].LHS_relation_id
                        ):
                            is_final_target = False
                            break
                if is_final_target:
                    final_target_rule_id = rule_id
                    self.final_target_relation_id = self.rules[
                        final_target_rule_id
                    ].LHS_relation_id
                    break
        else:
            final_target_rule_id = list(self.rules.keys())[0]
            self.final_target_relation_id = self.rules[
                final_target_rule_id
            ].LHS_relation_id

        assert self.final_target_relation_id is not None
        self.result = self.env[self.final_target_relation_id].content
        sorted_exprs = sorted(self.result, key=lambda x: x[0].line_number)

        print(self.env[self.final_target_relation_id].name)
        for tuple in sorted_exprs:
            for expr in tuple:
                functions = self.ts_analyzer.find_function_by_expr(expr)
                if len(functions) > 0:
                    print(
                        str(expr), "function:", "{" + functions[0].function_name + "}"
                    )
                else:
                    break

        print("========================================")
        print("******* Performance Statistics *********")
        print("========================================")

        for symbolic_primitive_name in self.available_symbolic_primitives:
            symbolic_primitive = self.available_symbolic_primitives[
                symbolic_primitive_name
            ]
            if (
                symbolic_primitive.cached_number > 0
                or symbolic_primitive.cached_miss_number > 0
            ):
                print(
                    symbolic_primitive_name
                    + " cached: "
                    + str(symbolic_primitive.cached_number)
                    + " missed: "
                    + str(symbolic_primitive.cached_miss_number)
                )

        for neural_primitive_name in self.available_neural_primitives:
            neural_primitive = self.available_neural_primitives[neural_primitive_name]
            if (
                neural_primitive.cached_number > 0
                or neural_primitive.cached_miss_number > 0
            ):
                print(
                    neural_primitive_name
                    + " cached: "
                    + str(neural_primitive.cached_number)
                    + " missed: "
                    + str(neural_primitive.cached_miss_number)
                )

        print("========================================")
        print("************* Token Cost ***************")
        print("========================================")
        for neural_primitive_name in self.available_neural_primitives:
            neural_primitive = self.available_neural_primitives[neural_primitive_name]
            print(
                neural_primitive_name,
                (
                    neural_primitive.total_input_token_cost,
                    neural_primitive.total_output_token_cost,
                ),
            )

        print("========================================")
        print("*************** Program ****************")
        print("========================================")
        for function_id in self.ts_analyzer.environment:
            function = self.ts_analyzer.environment[function_id]
            if function.function_name != "main":
                continue

            file_id = self.ts_analyzer.ts_parser.functionToFile[function_id]
            file_content = self.ts_analyzer.ts_parser.fileContentDic[file_id]

            file_line_number = 1
            file_lines = []
            for file_line in file_content.split("\n"):
                file_lines.append(str(file_line_number) + ". " + file_line)
                file_line_number += 1
            lined_file_content = "\n".join(file_lines)
            print(lined_file_content)

        return self.result
