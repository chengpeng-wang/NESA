from dataclasses import dataclass
from typing import Dict, Optional, Tuple


@dataclass(frozen=True)
class TSNodeTypes:
    language: str
    method_declaration: str
    method_invocation: str
    return_statement: str
    argument_list: str
    assignment_expression: str
    variable_declarator: str
    formal_parameter: str
    local_variable_declaration: str
    if_statement: str
    condition_expression: str
    while_statement: str
    for_statement: str
    enhanced_for_statement: Optional[str]
    block: str
    expression_statement: str
    identifier: str
    number_literal: str
    object_creation_expression: Optional[str]
    comment_nodes: Tuple[str, ...]
    assignment_operator_nodes: Tuple[str, ...]


LANGUAGE_NODE_TYPES: Dict[str, TSNodeTypes] = {
    "java": TSNodeTypes(
        language="java",
        method_declaration="method_declaration",
        method_invocation="method_invocation",
        return_statement="return_statement",
        argument_list="argument_list",
        assignment_expression="assignment_expression",
        variable_declarator="variable_declarator",
        formal_parameter="formal_parameter",
        local_variable_declaration="local_variable_declaration",
        if_statement="if_statement",
        condition_expression="parenthesized_expression",
        while_statement="while_statement",
        for_statement="for_statement",
        enhanced_for_statement="enhanced_for_statement",
        block="block",
        expression_statement="expression_statement",
        identifier="identifier",
        number_literal="number_literal",
        object_creation_expression="object_creation_expression",
        comment_nodes=("line_comment", "block_comment", "javadoc_comment"),
        assignment_operator_nodes=("+=", "-=", "*=", "/="),
    ),
    "cpp": TSNodeTypes(
        language="cpp",
        method_declaration="function_definition",
        method_invocation="call_expression",
        return_statement="return_statement",
        argument_list="argument_list",
        assignment_expression="assignment_expression",
        variable_declarator="init_declarator",
        formal_parameter="parameter_declaration",
        local_variable_declaration="declaration",
        if_statement="if_statement",
        condition_expression="condition_clause",
        while_statement="while_statement",
        for_statement="for_statement",
        enhanced_for_statement=None,
        block="compound_statement",
        expression_statement="expression_statement",
        identifier="identifier",
        number_literal="number_literal",
        object_creation_expression=None,
        comment_nodes=("comment",),
        assignment_operator_nodes=("+=", "-=", "*=", "/="),
    ),
}


def get_language_node_types(language: str) -> TSNodeTypes:
    normalized_language = language.lower()
    if normalized_language not in LANGUAGE_NODE_TYPES:
        supported_languages = ", ".join(sorted(LANGUAGE_NODE_TYPES))
        raise ValueError(
            f"Unsupported language '{language}'. Supported languages: {supported_languages}"
        )
    return LANGUAGE_NODE_TYPES[normalized_language]
