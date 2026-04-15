from datalog.reader import *
import json
from typing import Any, Dict


def parse_datalog_program(datalog_program_path: str) -> Dict[str, Any]:
    try:
        with open(datalog_program_path, "r") as file:
            datalog_program = file.read()
    except FileNotFoundError:
        raise FileNotFoundError(f"Analyzer file not found: {datalog_program_path}")

    tree = read_dataset(datalog_program)
    print(datalog_program)

    if tree is None:
        raise ValueError(
            f"Failed to parse datalog program from: {datalog_program_path}. Please check if the file format is correct."
        )

    rule_data = {}
    rule_id = 1
    for rule in tree.rules():
        rule_ast = {}
        rule_ast["left"] = {
            "relation": rule.pattern[0].value,
            "entity": [atom.value for atom in rule.pattern[1:]],
        }

        rule_ast["right"] = []
        for clause in rule.clauses:
            clause_ast = {
                "relation": clause[0].value,
                "entity": [atom.value for atom in clause[1:]],
            }
            rule_ast["right"].append(clause_ast)

        rule_data["rule_" + str(rule_id)] = (rule, rule_ast)
        rule_id += 1

    return rule_data
