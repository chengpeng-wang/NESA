import os
import json
from parser.program_parser import *
from typing import List, Set


def print_expr_set(expr_set: Set[Expr]) -> None:
    expr_str_list = [str(expr) for expr in expr_set]
    print(expr_str_list)


# Helper function for printing slice item
def print_slice_item(filename: str, eid: str) -> None:
    # read list from filename
    with open(filename, "r") as file:
        slices = json.load(file)

    slice_item = None
    cnt = 0
    for item in slices:
        print(item["eid"])
        if item["eid"] == eid:
            slice_item = item
            break
        if len(item["backward_slice"]) > 2:
            cnt += 1

    new_lines = []
    line_number = 0
    for line in slice_item["code"].split("\n"):
        line_number += 1
        new_lines.append(str(line_number) + ". " + line)
    slice_item["code"] = "\n".join(new_lines)

    # print(cnt)
    if slice_item is not None:
        print(slice_item["code"])
        print(slice_item["variable"])
        print(slice_item["variable_loc"])
        print(slice_item["line_number"] + 1)
        print(slice_item["backward_slice"])
