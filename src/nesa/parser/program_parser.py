import sys
import os
import re
from os import path
import tree_sitter
from tree_sitter import Language

sys.path.append(path.dirname(path.dirname(path.dirname(path.abspath(__file__)))))

from typing import Any, Dict, List, Optional, Set, Tuple
from enum import Enum
from pathlib import Path
from parser.language_config import TSNodeTypes, get_language_node_types


TEST_NAME_TOKENS = {"test", "tests", "testcase", "testcases"}


def split_identifier_tokens(identifier: str) -> List[str]:
    if not identifier:
        return []
    return [
        token.lower()
        for token in re.findall(
            r"[A-Z]+(?=[A-Z][a-z]|[0-9]|$)|[A-Z]?[a-z]+|[0-9]+", identifier
        )
    ]


def looks_like_test_name(name: str) -> bool:
    if not name:
        return False
    return any(token in TEST_NAME_TOKENS for token in split_identifier_tokens(name))


def should_skip_test_file(file_path: str) -> bool:
    file_name = Path(file_path).stem
    return looks_like_test_name(file_name)


def should_skip_test_function(function_name: str) -> bool:
    return looks_like_test_name(function_name)


def get_child_by_field_name(
    node: tree_sitter.Node, field_name: str
) -> Optional[tree_sitter.Node]:
    if node is None:
        return None
    try:
        return node.child_by_field_name(field_name)
    except AttributeError:
        return None


def find_name_node(
    node: tree_sitter.Node, node_types: TSNodeTypes
) -> Optional[tree_sitter.Node]:
    if node is None:
        return None

    for field_name in ("name", "field", "declarator", "function", "left"):
        child_node = get_child_by_field_name(node, field_name)
        if child_node is None:
            continue
        name_node = find_name_node(child_node, node_types)
        if name_node is not None:
            return name_node

    for child_node in node.children:
        if any(
            get_child_by_field_name(child_node, field_name) is not None
            for field_name in ("name", "field", "declarator", "function", "left")
        ):
            name_node = find_name_node(child_node, node_types)
            if name_node is not None:
                return name_node

    if node.type == node_types.identifier:
        return node

    for child_node in node.children:
        if child_node.type == node_types.identifier:
            return child_node

    for child_node in node.children:
        name_node = find_name_node(child_node, node_types)
        if name_node is not None:
            return name_node

    return None


def extract_name_from_node(
    node: tree_sitter.Node, source_code: str, node_types: TSNodeTypes
) -> str:
    name_node = find_name_node(node, node_types)
    if name_node is None:
        return ""
    return source_code[name_node.start_byte : name_node.end_byte]


def get_statement_line_span(
    node: tree_sitter.Node, source_code: str, node_types: TSNodeTypes
) -> Tuple[int, int]:
    if node is None:
        return (0, 0)

    if node.type == node_types.expression_statement:
        return (
            source_code[: node.start_byte].count("\n") + 1,
            source_code[: node.end_byte].count("\n") + 1,
        )

    if node.type == node_types.block:
        lower_lines = []
        upper_lines = []
        for child_node in node.children:
            if child_node.type in {"{", "}"}:
                continue
            start_line, end_line = get_statement_line_span(
                child_node, source_code, node_types
            )
            if start_line == end_line == 0:
                continue
            lower_lines.append(start_line)
            upper_lines.append(end_line)
        if lower_lines and upper_lines:
            return (min(lower_lines), max(upper_lines))
        return (0, 0)

    named_children = [
        child_node
        for child_node in node.children
        if getattr(child_node, "is_named", False)
    ]
    if named_children:
        lower_lines = []
        upper_lines = []
        for child_node in named_children:
            start_line, end_line = get_statement_line_span(
                child_node, source_code, node_types
            )
            if start_line == end_line == 0:
                start_line = source_code[: child_node.start_byte].count("\n") + 1
                end_line = source_code[: child_node.end_byte].count("\n") + 1
            lower_lines.append(start_line)
            upper_lines.append(end_line)
        return (min(lower_lines), max(upper_lines))

    return (
        source_code[: node.start_byte].count("\n") + 1,
        source_code[: node.end_byte].count("\n") + 1,
    )


class Expr:
    def __init__(self, name: str, line_number: int, file_path: str) -> None:
        self.name = name
        self.line_number = line_number
        self.file_path = file_path

    def leq(self, other: Any) -> bool:
        return (
            self.name in other.name
            and self.line_number == other.line_number
            and self.file_path == other.file_path
        )

    def __str__(self) -> str:
        return (
            "(" + self.name + ", " + str(self.line_number) + ", " + self.file_path + ")"
        )

    def __eq__(self, other: Any) -> bool:
        return (
            self.name == other.name
            and self.line_number == other.line_number
            and self.file_path == other.file_path
        )

    def __hash__(self) -> int:
        return hash((self.name, self.line_number, self.file_path))


class Function:
    def __init__(
        self,
        function_id: int,
        function_name: str,
        function_code: str,
        start_line_number: int,
        end_line_number: int,
        function_node: tree_sitter.Node,
    ) -> None:
        """
        Record basic facts of the function
        """

        ## Preliminary function facts obtained by parsers
        self.function_id = function_id
        self.function_name = function_name
        self.function_code = function_code
        self.start_line_number = start_line_number
        self.end_line_number = end_line_number
        self.parse_tree_root_node = function_node
        self.call_site_nodes = (
            []
        )  # call site nodes and line numbers (conform to control flow order)

        ## Specific values
        self.rets = set([])  # A set of Expr objects
        self.args = set(
            []
        )  # A set of (Expr, int) tuples, where int indicates the index of the argument
        self.outputs = set([])  # A set of Expr objects
        self.paras = set(
            []
        )  # A set of (Expr, int) tuples, where int indicates the index of the parameter

        ## Advanced function facts obtained by analyzers
        self.localvars = []  # local variables, including parameters
        self.local_RW_vars = {}  # Read/Write local variables

        self.if_statements = {}  # if statement info
        self.loop_statements = {}  # loop statement info


class TSParser:
    """
    TSParser class for extracting information from source files using tree-sitter.
    """

    def __init__(
        self, code_in_projects: Dict[str, str], language: str = "java"
    ) -> None:
        """
        Initialize TSParser with a collection of source files.
        """
        self.code_in_projects = code_in_projects
        self.language = language
        self.node_types = get_language_node_types(language)
        self.functionRawDataDic = {}
        self.functionNameToId = {}
        self.functionToFile = {}
        self.fileContentDic = {}

        cwd = Path(__file__).resolve().parent.parent.absolute()
        TSPATH = cwd / "../../lib/build/"
        language_path = TSPATH / "my-languages.so"
        self.ts_language = Language(str(language_path), self.language)

        # Initialize the parser
        self.parser = tree_sitter.Parser()
        self.parser.set_language(self.ts_language)

    def parse_project(self) -> None:
        cnt = 0
        for source_file_path in self.code_in_projects:
            print("Parsing file: ", cnt, "/", len(self.code_in_projects))
            cnt += 1
            if should_skip_test_file(source_file_path):
                continue
            source_code = self.code_in_projects[source_file_path]
            tree = self.parser.parse(bytes(source_code, "utf8"))
            self.parse_function_info(source_file_path, source_code, tree)
            self.fileContentDic[source_file_path] = source_code
        return

    def parse_function_info(
        self,
        file_path: str,
        source_code: str,
        tree: tree_sitter.Tree,
    ) -> None:
        """
        Extract class declaration info: class name, fields, and functions
        :param file_path: The path of the Java file.
        :param source_code: The content of the source code
        :param package_name: The package name
        :param root_node: The root node the parse tree
        """
        all_function_nodes = TSAnalyzer.find_nodes_by_type(
            tree.root_node, self.node_types.method_declaration
        )

        for node in all_function_nodes:
            function_name = extract_name_from_node(node, source_code, self.node_types)
            if function_name == "":
                continue
            if should_skip_test_file(file_path) or should_skip_test_function(
                function_name
            ):
                continue

            # Initialize the raw data of a function
            start_line_number = source_code[: node.start_byte].count("\n") + 1
            end_line_number = source_code[: node.end_byte].count("\n") + 1
            function_id = len(self.functionRawDataDic) + 1

            self.functionRawDataDic[function_id] = (
                function_name,
                start_line_number,
                end_line_number,
                node,
            )
            self.functionToFile[function_id] = file_path

            if function_name not in self.functionNameToId:
                self.functionNameToId[function_name] = set([])
            self.functionNameToId[function_name].add(function_id)
        return


class TSAnalyzer:
    """
    TSAnalyzer class for retrieving necessary facts or functions for LMAgent
    """

    def __init__(
        self,
        code_in_projects: Dict[str, str],
        language: str = "java",
    ) -> None:
        """
        Initialize TSParser with the project path.
        Currently we only analyze a single source file
        """
        self.language = language
        self.node_types = get_language_node_types(language)
        self.ts_parser = TSParser(code_in_projects, language)
        self.ts_parser.parse_project()

        self.environment = {}
        self.caller_callee_map = {}
        self.callee_caller_map = {}

        cnt = 0

        # collect the function environment: Function and Call Graph
        for function_id in self.ts_parser.functionRawDataDic:
            print(
                "Analyzing functions:", cnt, "/", len(self.ts_parser.functionRawDataDic)
            )
            cnt += 1
            (name, start_line_number, end_line_number, function_node) = (
                self.ts_parser.functionRawDataDic[function_id]
            )
            file_content = self.ts_parser.fileContentDic[
                self.ts_parser.functionToFile[function_id]
            ]
            function_code = file_content[
                function_node.start_byte : function_node.end_byte
            ]
            current_function = Function(
                function_id,
                name,
                function_code,
                start_line_number,
                end_line_number,
                function_node,
            )
            self.environment[function_id] = current_function

        # Extract meta data in each function
        for function_id in self.environment:
            current_function = self.environment[function_id]
            file_content = self.ts_parser.fileContentDic[
                self.ts_parser.functionToFile[function_id]
            ]
            self.environment[function_id] = self.extract_meta_data_in_single_function(
                current_function, file_content
            )
        return

    def find_name_node(self, node: tree_sitter.Node) -> Optional[tree_sitter.Node]:
        return find_name_node(node, self.node_types)

    def extract_name_from_node(self, node: tree_sitter.Node, source_code: str) -> str:
        return extract_name_from_node(node, source_code, self.node_types)

    def get_statement_line_span(
        self, node: tree_sitter.Node, source_code: str
    ) -> Tuple[int, int]:
        return get_statement_line_span(node, source_code, self.node_types)

    def extract_meta_data_in_single_function(
        self, current_function: Function, file_content: str
    ) -> Function:
        """
        :param current_function: Function object
        :return: Function object with updated parse tree and call info
        """

        # Analysis I: Identify call site info and maintain the environment (Necessary)
        for call_site_node in self.find_nodes_by_type(
            current_function.parse_tree_root_node, self.node_types.method_invocation
        ):
            callee_ids = self.find_callee(
                current_function.function_id, file_content, call_site_node
            )
            if len(callee_ids) > 0:
                # Update the call graph
                for callee_id in callee_ids:
                    caller_id = current_function.function_id
                    if caller_id not in self.caller_callee_map:
                        self.caller_callee_map[caller_id] = set([])
                    self.caller_callee_map[caller_id].add(callee_id)
                    if callee_id not in self.callee_caller_map:
                        self.callee_caller_map[callee_id] = set([])
                    self.callee_caller_map[callee_id].add(caller_id)
                current_function.call_site_nodes.append(call_site_node)

        # Analysis II: Identify specific values, including return values, arguments, output values, and parameters
        current_function.rets = self.extract_rets(current_function, file_content)
        current_function.args = self.extract_args(current_function, file_content)
        current_function.outputs = self.extract_outputs(current_function, file_content)
        current_function.paras = self.extract_paras(current_function, file_content)

        # Analysis III: Identify declared variables
        current_function.localvars = self.extract_local_variables(
            current_function, file_content
        )

        # Analysis IV: Identify Read/Write variables
        current_function.local_RW_vars = self.extract_local_RW_variables(
            current_function, file_content
        )

        # Analysis V: compute the scope of the if-statements to guide the further reachability sanitization
        current_function.if_statements = self.find_if_statements(
            current_function.function_code,
            current_function.parse_tree_root_node,
        )

        # Analysis VI: compute the scope of the loops to guide the further reachability sanitization
        current_function.loop_statements = self.find_loop_statements(
            current_function.function_code,
            current_function.parse_tree_root_node,
        )
        return current_function

    def extract_rets(self, current_function: Function, file_content: str) -> Set[Expr]:
        return_set = set([])
        ret_nodes = self.find_nodes_by_type(
            current_function.parse_tree_root_node, self.node_types.return_statement
        )
        for ret_node in ret_nodes:
            ret_stmt = file_content[ret_node.start_byte : ret_node.end_byte]
            return_var = ret_stmt.replace("return ", "").replace(";", "")
            line_number = file_content[: ret_node.start_byte].count("\n") + 1

            file_path = self.ts_parser.functionToFile[current_function.function_id]
            return_set.add(Expr(return_var, line_number, file_path))
        return return_set

    def extract_args(
        self, current_function: Function, file_content: str
    ) -> Set[Tuple[Expr, int]]:
        arg_set = set([])

        for call_site_node in current_function.call_site_nodes:
            # Ignore library function calls
            callee_name = self.extract_name_from_node(call_site_node, file_content)
            if callee_name == "":
                continue
            is_library_call = True
            for fid in self.environment:
                if self.environment[fid].function_name == callee_name:
                    is_library_call = False
                    break
            if is_library_call:
                continue

            index = 0
            for node in call_site_node.children:
                print(node.type)
                if node.type == self.node_types.argument_list:
                    for child in node.children:
                        if child.type in {"(", ")", ","}:
                            continue
                        arg_str = file_content[child.start_byte : child.end_byte]
                        line_number = file_content[: child.start_byte].count("\n") + 1

                        file_path = self.ts_parser.functionToFile[
                            current_function.function_id
                        ]
                        arg_set.add((Expr(arg_str, line_number, file_path), index))
                        index += 1
        return arg_set

    def extract_outputs(
        self, current_function: Function, file_content: str
    ) -> Set[Expr]:
        output_set = set([])

        print("extract_outputs in the function", current_function.function_name)

        for call_site_node in current_function.call_site_nodes:
            # Ignore library function calls
            callee_name = self.extract_name_from_node(call_site_node, file_content)
            if callee_name == "":
                continue
            is_library_call = True
            for fid in self.environment:
                if self.environment[fid].function_name == callee_name:
                    is_library_call = False
                    break
            if is_library_call:
                continue

            # Find assignment_expression
            nodes = self.find_nodes_by_type(
                current_function.parse_tree_root_node,
                self.node_types.assignment_expression,
            )

            # Find local_variable_declaration
            nodes.extend(
                self.find_nodes_by_type(
                    current_function.parse_tree_root_node,
                    self.node_types.variable_declarator,
                )
            )

            # Extract the name info and line number
            for node in nodes:
                if file_content[: node.start_byte].count("\n") == file_content[
                    : call_site_node.start_byte
                ].count("\n"):
                    name_node = self.find_name_node(node)
                    if name_node is None:
                        continue
                    name = file_content[name_node.start_byte : name_node.end_byte]
                    line_number = file_content[: name_node.start_byte].count("\n") + 1
                    file_path = self.ts_parser.functionToFile[
                        current_function.function_id
                    ]
                    output_set.add(Expr(name, line_number, file_path))
        return output_set

    def extract_paras(
        self, current_function: Function, file_content: str
    ) -> Set[Tuple[Expr, int]]:
        paras = set([])
        parameters = self.find_nodes_by_type(
            current_function.parse_tree_root_node, self.node_types.formal_parameter
        )
        index = 0
        for parameter_node in parameters:
            parameter_node_name = self.find_name_node(parameter_node)
            if parameter_node_name is None:
                continue
            parameter_name = file_content[
                parameter_node_name.start_byte : parameter_node_name.end_byte
            ]
            line_number = file_content[: parameter_node_name.start_byte].count("\n") + 1
            file_path = self.ts_parser.functionToFile[current_function.function_id]
            paras.add((Expr(parameter_name, line_number, file_path), index))
            index += 1
        return paras

    def extract_local_variables(
        self, current_function: Function, file_content: str
    ) -> List[str]:
        localvars = []
        all_local_variables = self.find_nodes_by_type(
            current_function.parse_tree_root_node,
            self.node_types.local_variable_declaration,
        )
        for local_variable_node in all_local_variables:
            declarator_nodes = self.find_nodes_by_type(
                local_variable_node, self.node_types.variable_declarator
            )
            if len(declarator_nodes) == 0:
                declarator_nodes = [local_variable_node]

            for declarator_node in declarator_nodes:
                name_node = self.find_name_node(declarator_node)
                if name_node is None:
                    continue
                variable_name = file_content[name_node.start_byte : name_node.end_byte]
                localvars.append(variable_name)

        all_formal_parameters = self.find_nodes_by_type(
            current_function.parse_tree_root_node, self.node_types.formal_parameter
        )
        for formal_parameter_node in all_formal_parameters:
            name_node = self.find_name_node(formal_parameter_node)
            if name_node is None:
                continue
            parameter_name = file_content[name_node.start_byte : name_node.end_byte]
            localvars.append(parameter_name)
        return localvars

    def extract_local_RW_variables(
        self, current_function: Function, file_content: str
    ) -> Dict[Tuple[str, int], str]:
        local_RW_vars = {}
        all_identifier_nodes = self.find_nodes_by_type(
            current_function.parse_tree_root_node, self.node_types.identifier
        )
        for identifier_node in all_identifier_nodes:
            identifier_str = file_content[
                identifier_node.start_byte : identifier_node.end_byte
            ]
            identifier_line_number = (
                file_content[: identifier_node.end_byte].count("\n") + 1
            )
            if identifier_str in current_function.localvars:
                local_RW_vars[(identifier_str, identifier_line_number)] = "R"

        all_assignment_opnodes = []
        for assignment_operator in self.node_types.assignment_operator_nodes:
            all_assignment_opnodes.extend(
                self.find_nodes_by_type(
                    current_function.parse_tree_root_node, assignment_operator
                )
            )

        for opnode in self.find_nodes_by_type(
            current_function.parse_tree_root_node, "="
        ):
            identifier_line_number = file_content[: opnode.end_byte].count("\n") + 1
            identifier_str = file_content[
                opnode.prev_sibling.start_byte : opnode.prev_sibling.end_byte
            ]
            local_RW_vars[(identifier_str, identifier_line_number)] = "W"

        for opnode in all_assignment_opnodes:
            identifier_line_number = file_content[: opnode.end_byte].count("\n") + 1
            identifier_str = file_content[
                opnode.prev_sibling.start_byte : opnode.prev_sibling.end_byte
            ]
            local_RW_vars[(identifier_str, identifier_line_number)] = "WR"
        return local_RW_vars

    # Collect the control dependencies of a specific program line
    @staticmethod
    def extract_control_program_lines(
        current_function: Function, line_number: int
    ) -> List[int]:
        control_program_lines = []
        for (
            if_statement_start_line,
            if_statement_end_line,
        ) in current_function.if_statements:
            if if_statement_start_line <= line_number <= if_statement_end_line:
                (condition_start_line, condition_end_line, _, _, _) = (
                    current_function.if_statements[
                        (if_statement_start_line, if_statement_end_line)
                    ]
                )
                if condition_start_line <= line_number <= condition_end_line:
                    continue
                control_program_lines.extend(
                    range(condition_start_line, condition_end_line + 1)
                )
        for (
            for_statement_start_line,
            for_statement_end_line,
        ) in current_function.loop_statements:
            if for_statement_start_line <= line_number <= for_statement_end_line:
                (condition_start_line, condition_end_line, _, _) = (
                    current_function.loop_statements[
                        (for_statement_start_line, for_statement_end_line)
                    ]
                )
                if condition_start_line <= line_number <= condition_end_line:
                    continue
                control_program_lines.extend(
                    range(condition_start_line, condition_end_line + 1)
                )

        # Error prone
        for (
            if_statement_start_line,
            if_statement_end_line,
        ) in current_function.if_statements:
            (
                condition_start_line,
                condition_end_line,
                _,
                (true_branch_start_line, true_branch_end_line),
                (else_branch_start_line, else_branch_end_line),
            ) = current_function.if_statements[
                (if_statement_start_line, if_statement_end_line)
            ]
            for (
                for_statement_start_line,
                for_statement_end_line,
            ) in current_function.loop_statements:
                if (
                    for_statement_start_line
                    <= if_statement_start_line
                    < if_statement_end_line
                    <= for_statement_end_line
                ):
                    if line_number in range(
                        condition_start_line, condition_end_line + 1
                    ):
                        control_program_lines.extend(
                            range(condition_start_line, condition_end_line + 1)
                        )
        return control_program_lines

    # Collect the controlled program lines (explict flows) of a specific program line
    # TOBE refactored
    @staticmethod
    def extract_controlled_program_lines(
        current_function: Function, line_number: int
    ) -> List[int]:
        controlled_program_lines = []
        for (
            if_statement_start_line,
            if_statement_end_line,
        ) in current_function.if_statements:
            (
                condition_start_line,
                condition_end_line,
                _,
                (true_branch_start_line, true_branch_end_line),
                (else_branch_start_line, else_branch_end_line),
            ) = current_function.if_statements[
                (if_statement_start_line, if_statement_end_line)
            ]
            if condition_start_line <= line_number <= condition_end_line:
                controlled_program_lines.extend(
                    range(true_branch_start_line, true_branch_end_line + 1)
                )
                if else_branch_start_line != 0:
                    controlled_program_lines.extend(
                        range(else_branch_start_line, else_branch_end_line + 1)
                    )
        for (
            for_statement_start_line,
            for_statement_end_line,
        ) in current_function.loop_statements:
            (
                condition_start_line,
                condition_end_line,
                loop_body_start_line,
                loop_body_end_line,
            ) = current_function.loop_statements[
                (for_statement_start_line, for_statement_end_line)
            ]
            if condition_start_line <= line_number <= condition_end_line:
                controlled_program_lines.extend(
                    range(loop_body_start_line, loop_body_end_line + 1)
                )
        return controlled_program_lines

    # Utility functions
    def find_all_top_functions(self) -> List[int]:
        """
        Collect all the main functions, which are ready for analysis
        :return: a list of ids indicating main functions
        """
        main_ids = []
        for function_id in self.ts_parser.functionRawDataDic:
            (name, code, start_line_number, end_line_number) = (
                self.ts_parser.functionRawDataDic[function_id]
            )
            if code.count("\n") < 2:
                continue
            if name in {"main"}:
                main_ids.append(function_id)
        return main_ids

    @staticmethod
    def find_all_nodes(root_node: tree_sitter.Node) -> List[tree_sitter.Node]:
        if root_node is None:
            return []
        nodes = [root_node]
        for child_node in root_node.children:
            nodes.extend(TSAnalyzer.find_all_nodes(child_node))
        return nodes

    @staticmethod
    def find_nodes_by_type(
        root_node: tree_sitter.Node, node_type: str
    ) -> List[tree_sitter.Node]:
        if root_node is None or node_type is None:
            return []
        nodes = []
        if root_node.type == node_type:
            nodes.append(root_node)
        for child_node in root_node.children:
            nodes.extend(TSAnalyzer.find_nodes_by_type(child_node, node_type))
        return nodes

    def find_callee(
        self, function_id: int, source_code: str, call_expr_node: tree_sitter.Node
    ) -> List[int]:
        """
        Find callees that invoked by a specific function.
        Attention: call_site_node should be derived from source_code directly
        :param function_id: caller function id
        :param file_path: the path of the file containing the caller function
        :param source_code: the content of the source file
        :param call_site_node: the node of the call site. The type is 'call_expression'
        :return the list of the ids of called functions
        """
        assert call_expr_node.type == self.node_types.method_invocation
        function_name = self.extract_name_from_node(call_expr_node, source_code)

        if function_name not in self.ts_parser.functionNameToId:
            return []
        else:
            return self.ts_parser.functionNameToId[function_name]

    def find_if_statements(
        self, source_code: str, root_node: tree_sitter.Node
    ) -> Dict[Tuple, Tuple]:
        targets = self.find_nodes_by_type(root_node, self.node_types.if_statement)
        if_statements = {}
        for target in targets:
            condition_str = ""
            condition_start_line = 0
            condition_end_line = 0
            true_branch_start_line = 0
            true_branch_end_line = 0
            else_branch_start_line = 0
            else_branch_end_line = 0

            condition_node = get_child_by_field_name(target, "condition")
            if condition_node is not None:
                condition_start_line = (
                    source_code[: condition_node.start_byte].count("\n") + 1
                )
                condition_end_line = (
                    source_code[: condition_node.end_byte].count("\n") + 1
                )
                condition_str = source_code[
                    condition_node.start_byte : condition_node.end_byte
                ]

            consequence_node = get_child_by_field_name(target, "consequence")
            if consequence_node is not None:
                true_branch_start_line, true_branch_end_line = (
                    self.get_statement_line_span(consequence_node, source_code)
                )

            alternative_node = get_child_by_field_name(target, "alternative")
            if alternative_node is not None:
                else_branch_start_line, else_branch_end_line = (
                    self.get_statement_line_span(alternative_node, source_code)
                )

            if_statement_start_line = source_code[: target.start_byte].count("\n") + 1
            if_statement_end_line = source_code[: target.end_byte].count("\n") + 1
            if_statements[(if_statement_start_line, if_statement_end_line)] = (
                condition_start_line,
                condition_end_line,
                condition_str,
                (true_branch_start_line, true_branch_end_line),
                (else_branch_start_line, else_branch_end_line),
            )
        return if_statements

    def find_loop_statements(
        self, source_code: str, root_node: tree_sitter.Node
    ) -> Dict[Tuple, Tuple]:
        loop_statements = {}
        for_statement_nodes = self.find_nodes_by_type(
            root_node, self.node_types.for_statement
        )
        if self.node_types.enhanced_for_statement is not None:
            for_statement_nodes.extend(
                self.find_nodes_by_type(
                    root_node, self.node_types.enhanced_for_statement
                )
            )
        while_statement_nodes = self.find_nodes_by_type(
            root_node, self.node_types.while_statement
        )

        for loop_node in for_statement_nodes:
            loop_start_line = source_code[: loop_node.start_byte].count("\n") + 1
            loop_end_line = source_code[: loop_node.end_byte].count("\n") + 1

            condition_line_start = 0
            condition_line_end = 0
            loop_body_start_line = 0
            loop_body_end_line = 0

            condition_node = get_child_by_field_name(loop_node, "condition")
            if condition_node is not None:
                condition_line_start = (
                    source_code[: condition_node.start_byte].count("\n") + 1
                )
                condition_line_end = (
                    source_code[: condition_node.end_byte].count("\n") + 1
                )

            for loop_child_node in loop_node.children:
                if condition_line_start == 0 and loop_child_node.type == "(":
                    condition_line_start = (
                        source_code[: loop_child_node.start_byte].count("\n") + 1
                    )
                if condition_line_end == 0 and loop_child_node.type == ")":
                    condition_line_end = (
                        source_code[: loop_child_node.end_byte].count("\n") + 1
                    )
            body_node = get_child_by_field_name(loop_node, "body")
            if body_node is not None:
                loop_body_start_line, loop_body_end_line = self.get_statement_line_span(
                    body_node, source_code
                )
            loop_statements[(loop_start_line, loop_end_line)] = (
                condition_line_start,
                condition_line_end,
                loop_body_start_line,
                loop_body_end_line,
            )

        for loop_node in while_statement_nodes:
            loop_start_line = source_code[: loop_node.start_byte].count("\n") + 1
            loop_end_line = source_code[: loop_node.end_byte].count("\n") + 1

            condition_line_start = 0
            condition_line_end = 0
            loop_body_start_line = 0
            loop_body_end_line = 0

            condition_node = get_child_by_field_name(loop_node, "condition")
            if condition_node is not None:
                condition_line_start = (
                    source_code[: condition_node.start_byte].count("\n") + 1
                )
                condition_line_end = (
                    source_code[: condition_node.end_byte].count("\n") + 1
                )

            body_node = get_child_by_field_name(loop_node, "body")
            if body_node is not None:
                loop_body_start_line, loop_body_end_line = self.get_statement_line_span(
                    body_node, source_code
                )
            loop_statements[(loop_start_line, loop_end_line)] = (
                condition_line_start,
                condition_line_end,
                loop_body_start_line,
                loop_body_end_line,
            )
        return loop_statements

    def find_function_by_line_number(self, line_number: int) -> List[Function]:
        for function_id in self.environment:
            function = self.environment[function_id]
            if function.start_line_number <= line_number <= function.end_line_number:
                return [function]
        return []

    def find_node_by_line_number(
        self, line_number: int
    ) -> List[Tuple[str, tree_sitter.Node]]:
        code_node_list = []
        for function_id in self.environment:
            function = self.environment[function_id]
            if (
                not function.start_line_number
                <= line_number
                <= function.end_line_number
            ):
                continue
            all_nodes = TSAnalyzer.find_all_nodes(function.parse_tree_root_node)
            for node in all_nodes:
                start_line = (
                    function.function_code[: node.start_byte].count("\n")
                    + function.start_line_number
                )
                end_line = (
                    function.function_code[: node.end_byte].count("\n")
                    + function.start_line_number
                )
                if start_line == end_line == line_number:
                    code_node_list.append((function.function_code, node))
        return code_node_list

    def find_function_by_expr(self, expr: Expr) -> List[Function]:
        line_number = expr.line_number
        return self.find_function_by_line_number(line_number)

    # Debug use
    def dump_call_graph(self) -> None:
        print("Caller-Callee Edge:")
        for caller_id in self.caller_callee_map:
            for callee_id in self.ts_analyzer.caller_callee_map[caller_id]:
                caller_name = self.ts_analyzer.environment[caller_id].function_name
                callee_name = self.ts_analyzer.environment[callee_id].function_name
                print(caller_name + " -> " + callee_name)
        return

    def dump_function_para_ret_arg_out(self) -> None:
        for function_id in self.environment:
            function = self.environment[function_id]
            print("========Function Name========")
            print("Function Name:", function.function_name)
            print("========RETs========")
            for ret in function.rets:
                print(str(ret))
            print("========ARGS========")
            for arg, index in function.args:
                print(str(arg), index)
            print("========OUTPUTS========")
            for output in function.outputs:
                print(str(output))
            print("========PARAS========")
            for para, index in function.paras:
                print(str(para), index)
            print("\n")
        return
