import re
from typing import List, Tuple


def parse_dep_query_responose(response: str) -> List[Tuple[str, int]]:
    """
    Extracts line numbers and variable names from the formatted output text.

    Parameters:
    - output_text (str): The output text from which to extract data.

    Returns:
    - list of tuples: A list of (line_number, variable_name) tuples.
    """
    # Use regular expressions to find all occurrences matching the expected format

    extracted_data = []
    for response_line in response.split("\n"):
        if response_line.startswith("- Program Line:"):
            line_number = int(response_line.split(": Line ")[1].split(",")[0])
            var_name = (
                response_line.split(",")[1]
                .strip()
                .replace("Variable Name:", "")
                .strip()
            )
            extracted_data.append((var_name, line_number))
    return extracted_data


def parseLineNumberFromCoTPromptingResponseForSlicing(response: str) -> List[int]:
    line_numbers = []
    for line in response.split("\n"):
        if line.startswith("Slice: "):
            token_lines = line[line.find("[") + 1 : line.find("]")].split(", ")
            line_numbers = [
                int(token_line.replace("Line", "").strip())
                for token_line in token_lines
            ]
    return line_numbers


def parseLineNumberFromCoTPromptingResponseForDataDep(response: str) -> List[int]:
    line_numbers = []
    for line in response.split("\n"):
        if line.startswith("Definition Lines:"):
            token_lines = line[line.find("[") + 1 : line.find("]")].split(", ")
            line_numbers = [
                int(token_line.replace("Line", "").strip())
                for token_line in token_lines
            ]

    return line_numbers
