from parser.program_parser import Expr
from typing import Set, Tuple
import re


def parse_transform_prompt_response(
    response: str, file_name: str
) -> Tuple[Set[Expr], bool]:
    """
    Extracts line numbers and variable names from the formatted output text.

    Parameters:
    - output_text (str): The output text from which to extract data.

    Returns:
    - list of expressions: A list of Expr objects
    """
    print("response: ", response)

    # Use regular expressions to find all occurrences matching the expected format
    extracted_data = set([])
    is_error = False

    for response_line in response.split("\n"):
        pattern = re.compile(r"- Program Line: Line (\d+), Expression: (.*)")
        if response_line.startswith("- Program Line:"):
            match = pattern.match(response_line)
            if match:
                line_number = int(match.group(1))
                variable_name = match.group(2)
                extracted_data.add(Expr(variable_name, line_number, file_name))
            else:
                is_error = True
                return set([]), is_error
    return extracted_data, is_error


def parse_check_prompt_response(response: str) -> Tuple[bool, bool]:
    """
    Extracts the boolean result from the formatted output text.

    Parameters:
    - output_text (str): The output text from which to extract data.

    Returns:
    - bool: The boolean result extracted from the text.
    """
    return (
        "yes" in response.strip().lower(),
        "yes" not in response.strip().lower() and "no" not in response.strip().lower(),
    )


def parse_populate_prompt_response(
    response: str, file_name: str
) -> Tuple[Set[Tuple[Expr]], bool]:
    """
    Extracts line numbers and variable names from the formatted output text.

    Parameters:
    - output_text (str): The output text from which to extract data.

    Returns:
    - list of expressions: A list of Expr objects
    """
    # Use regular expressions to find all occurrences matching the expected format
    extracted_data = set([])
    is_error = False  # currently decrepated
    for response_line in response.split("\n"):
        pattern = re.compile(
            r"- \(Program Line 1: Line (?P<line_number_1>\d+), Expression 1: (?P<variable_name_1>\w+)\), "
            r"\(Program Line 2: Line (?P<line_number_2>\d+), Expression 2: (?P<variable_name_2>\w+)\)"
        )

        # parse response_line. If it matches the format, extract the line numbers and variable names
        if response_line.startswith("- (Program Line 1:"):
            match = pattern.match(response_line)
            if match:
                line_number_1 = int(match.group("line_number_1"))
                variable_name_1 = match.group("variable_name_1")
                line_number_2 = int(match.group("line_number_2"))
                variable_name_2 = match.group("variable_name_2")
                extracted_data.add(
                    (
                        Expr(variable_name_1, line_number_1, file_name),
                        Expr(variable_name_2, line_number_2, file_name),
                    )
                )
            else:
                is_error = True
                return set([]), is_error
    return extracted_data, is_error


def parseLineNumberFromCoTPromptingResponse(response: str, file_name: str) -> Set[int]:
    line_numbers = []
    for line in response.split("\n"):
        if line.startswith("Slice: "):
            token_lines = line[line.find("[") + 1 : line.find("]")].split(", ")
            line_numbers = [
                int(token_line.replace("Line", "").strip())
                for token_line in token_lines
            ]
    return line_numbers
