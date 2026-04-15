from typing import Any, Dict, List, Set, Tuple
from parser.program_parser import *
from nesa.evaluator.utility import *
from utility.io import *


class Relation:
    def __init__(self, name: str, arity: int) -> None:
        self.name = name
        self.arity = arity
        self.content = set([])
        pass

    def get_specific_entry_content(self, index: int) -> Set[Expr]:
        specific_entry_content = set([])
        for expr in self.content:
            specific_entry_content.add(expr[index])
        return specific_entry_content


class RelationTerm:
    def __init__(
        self, id: int, relation_id: int, relation_name: str, entry_symbols: List[str]
    ) -> None:
        self.id = id
        self.relation_id = relation_id
        self.relation_name = relation_name
        self.entry_symbols = entry_symbols
        return


class Rule:
    def __init__(
        self,
        rule_id: int,
        rule_content: Any,
        raw_rule: Dict[str, Any],
        relation_name_id_map: Dict[str, int],
    ) -> None:
        self.rule_id = rule_id
        self.rule_content = rule_content
        self.raw_rule = raw_rule
        self.relation_name_id_map = relation_name_id_map

        self.relations = {}  # relation_id -> Relation
        self.LHS_relation_id = None
        self.RHS_relation_ids = set([])

        self.relations_terms = {}  # relation_term_id -> RelationTerm
        self.LHS_relation_term_id = None
        self.RHS_relation_term_ids = set([])
        self.rule_str = None

        self.initialize(raw_rule, relation_name_id_map)

        self.entry_symbols = set([])
        for relation_term_id in self.relations_terms:
            for entry_symbol in self.relations_terms[relation_term_id].entry_symbols:
                self.entry_symbols.add(entry_symbol)
        return

    def initialize(
        self, raw_rule: Dict[str, Any], relation_name_id_map: Dict[str, int]
    ) -> None:
        # Step 1: Initialize the relations
        relation_term_id = 0
        self.rule_str = ""

        raw_relation = raw_rule["left"]
        relation_id = relation_name_id_map[raw_relation["relation"]]
        if relation_id not in self.relations:
            self.relations[relation_id] = Relation(
                raw_relation["relation"], len(raw_relation["entity"])
            )
            self.LHS_relation_id = relation_id

        self.relations_terms[relation_term_id] = RelationTerm(
            relation_term_id,
            relation_id,
            raw_relation["relation"],
            raw_relation["entity"],
        )
        self.LHS_relation_term_id = relation_term_id
        relation_term_id += 1

        self.rule_str += raw_relation["relation"] + "("
        for i in range(len(raw_relation["entity"])):
            self.rule_str += raw_relation["entity"][i]
            if i != len(raw_relation["entity"]) - 1:
                self.rule_str += ", "
        self.rule_str += ") :- "

        for i in range(len(raw_rule["right"])):
            raw_relation = raw_rule["right"][i]
            raw_relation_name = raw_relation["relation"]
            relation_id = relation_name_id_map[raw_relation_name]
            self.RHS_relation_ids.add(relation_id)

            if relation_id not in self.relations:
                self.relations[relation_id] = Relation(
                    raw_relation_name, len(raw_relation["entity"])
                )
            self.relations_terms[relation_term_id] = RelationTerm(
                relation_term_id,
                relation_id,
                raw_relation["relation"],
                raw_relation["entity"],
            )
            self.RHS_relation_term_ids.add(relation_term_id)
            relation_term_id += 1

            self.rule_str += raw_relation_name + "("
            for j in range(len(raw_relation["entity"])):
                self.rule_str += raw_relation["entity"][j]
                if j != len(raw_relation["entity"]) - 1:
                    self.rule_str += ", "
            self.rule_str += ")"
            if i != len(raw_rule["right"]) - 1:
                self.rule_str += ", "
        return

    def __str__(self) -> str:
        return self.rule_str
