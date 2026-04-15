import sys
import os
from os import path
import tree_sitter
from tree_sitter import Language

sys.path.append(path.dirname(path.dirname(path.dirname(path.abspath(__file__)))))

from typing import List, Tuple, Dict, Set
from enum import Enum
from pathlib import Path
from parser.program_parser import *
from parser.language_config import get_language_node_types


def beautify_source_code(source_code: str) -> str:
    lines = source_code.split("\n")
    beautified_lines = []
    indent_level = 0
    indent_size = 2  # Number of spaces for each indentation level

    for line in lines:
        stripped_line = line.strip()

        # Decrease indent level if the line contains a closing brace
        if stripped_line.startswith("}"):
            indent_level -= 1

        # Add the appropriate amount of indentation
        beautified_lines.append(" " * (indent_level * indent_size) + stripped_line)

        # Increase indent level if the line contains an opening brace
        if stripped_line.endswith("{"):
            indent_level += 1

    return "\n".join(beautified_lines)


def beautify_java_code(java_code: str) -> str:
    return beautify_source_code(java_code)


def clean_comment_name(source_code: str, language: str = "java") -> str:
    cwd = Path(__file__).resolve().parent.parent.absolute()
    TSPATH = cwd / "../../lib/build/"
    language_path = TSPATH / "my-languages.so"
    node_types = get_language_node_types(language)
    ts_language = Language(str(language_path), language)

    parser = tree_sitter.Parser()
    parser.set_language(ts_language)

    t = parser.parse(bytes(source_code, "utf8"))
    root_node = t.root_node
    nodes = []
    for comment_node_type in node_types.comment_nodes:
        nodes.extend(TSAnalyzer.find_nodes_by_type(root_node, comment_node_type))

    new_code = source_code
    for node in nodes:
        comment = source_code[node.start_byte : node.end_byte]
        new_code = new_code.replace(comment, "\n" * comment.count("\n"))

    new_code = (
        new_code.replace("good", "foo")
        .replace("bad", "hoo")
        .replace("G2B", "xx")
        .replace("B2G", "yy")
    )
    return new_code
