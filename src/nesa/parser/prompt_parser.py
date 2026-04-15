from parser.program_parser import Expr, Function
from typing import Set
import json


def render_code_block(code: str, language: str) -> str:
    _ = language
    return f"```\n{code}\n```\n"


def construct_transform_prompt_from_binary_relation(
    prompt_template_file: str,
    lined_source_code: str,
    expr: Expr,
    language: str = "java",
) -> str:
    """
    Load a JSON file from the specified path and construct a prompt from the data.
    """
    prompt = []  # Collection of all prompt sections as a list of strings
    try:
        # Open and load the JSON file
        with open(prompt_template_file, "r", encoding="utf-8") as file:
            data = json.load(file)

        system_description = data.get("system", "")
        task_description = data.get("task", "")
        examples = data.get("examples", [])
        final_task = data.get("final_task", {})

        # Append the System Description to the prompt list
        prompt.append("## System Description\n")
        prompt.append(system_description + "\n")

        # Append the Task Description to the prompt list
        prompt.append("## Task\n")
        prompt.append(task_description + "\n")

        # Append Examples Section to the prompt list
        prompt.append("## Examples\n")
        for idx, example in enumerate(examples, 1):
            prompt.append(f"**Example {idx}:** {example['description']}\n")
            prompt.append(render_code_block(example["code"], language))
            prompt.append("**Question:** " + example["question"] + "\n")
            prompt.append("**Explanation:** " + example["output"]["explanation"] + "\n")
            prompt.append("**Answer:**\n")
            for dep in example["output"]["answer"]:
                prompt.append(
                    f"- Program Line: Line {dep['Line']}, Expression: {dep['Variable']}\n"
                )
            prompt.append("\n")

        # Append the Final Task if exists
        if final_task:
            prompt.append("**Final Task**\n")
            prompt.append(final_task["description"] + "\n")
            prompt.append(render_code_block(final_task["code"], language))
            prompt.append("**Question:** " + final_task["question"] + "\n")
            prompt.append("**Format:** " + "\n".join(final_task["format"]) + "\n")

    except FileNotFoundError:
        print(f"The file at {prompt_template_file} was not found.")
        exit(0)
        return ""
    except Exception as e:
        print(f"An error occurred: {e}")
        exit(0)
        return ""

    return (
        "".join(prompt)
        .replace("{P_CODE}", lined_source_code)
        .replace("{P_VARIABLE_NAME}", expr.name)
        .replace("{P_LINE_NUMBER}", str(expr.line_number))
    )


def construct_check_prompt_from_binary_relation(
    prompt_template_file: str,
    lined_source_code: str,
    expr1: Expr,
    expr2: Expr,
    language: str = "java",
) -> str:
    """
    Load a JSON file from the specified path and construct a prompt from the data.
    """
    prompt = []  # Collection of all prompt sections as a list of strings
    try:
        # Open and load the JSON file
        with open(prompt_template_file, "r", encoding="utf-8") as file:
            data = json.load(file)

        system_description = data.get("system", "")
        task_description = data.get("task", "")
        examples = data.get("examples", [])
        final_task = data.get("final_task", {})

        # Append the System Description to the prompt list
        prompt.append("## System Description\n")
        prompt.append(system_description + "\n")

        # Append the Task Description to the prompt list
        prompt.append("## Task\n")
        prompt.append(task_description + "\n")

        # Append Examples Section to the prompt list
        prompt.append("## Examples\n")
        for idx, example in enumerate(examples, 1):
            prompt.append(f"**Example {idx}:** {example['description']}\n")
            prompt.append(render_code_block(example["code"], language))
            prompt.append("**Question:** " + example["question"] + "\n")
            prompt.append("**Explanation:** " + example["output"]["explanation"] + "\n")
            prompt.append("**Answer:**\n")
            prompt.append(example["output"]["answer"] + "\n")
            prompt.append("\n")

        # Append the Final Task if exists
        if final_task:
            prompt.append("**Final Task**\n")
            prompt.append(final_task["description"] + "\n")
            prompt.append(render_code_block(final_task["code"], language))
            prompt.append("**Question:** " + final_task["question"] + "\n")
            prompt.append("**Format:** " + "\n".join(final_task["format"]) + "\n")

    except FileNotFoundError:
        print(f"The file at {prompt_template_file} was not found.")
        return ""
    except Exception as e:
        print(f"An error occurred: {e}")
        return ""

    return (
        "".join(prompt)
        .replace("{P_CODE}", lined_source_code)
        .replace("{P_VARIABLE_NAME_1}", expr1.name)
        .replace("{P_LINE_NUMBER_1}", str(expr1.line_number))
        .replace("{P_VARIABLE_NAME_2}", expr2.name)
        .replace("{P_LINE_NUMBER_2}", str(expr2.line_number))
    )


def construct_populate_prompt_from_binary_relation(
    prompt_template_file: str, lined_source_code: str, language: str = "java"
) -> str:
    """
    Load a JSON file from the specified path and construct a prompt from the data.
    """
    prompt = []  # Collection of all prompt sections as a list of strings
    try:
        # Open and load the JSON file
        with open(prompt_template_file, "r", encoding="utf-8") as file:
            data = json.load(file)

        system_description = data.get("system", "")
        task_description = data.get("task", "")
        examples = data.get("examples", [])
        final_task = data.get("final_task", {})

        # Append the System Description to the prompt list
        prompt.append("## System Description\n")
        prompt.append(system_description + "\n")

        # Append the Task Description to the prompt list
        prompt.append("## Task\n")
        prompt.append(task_description + "\n")

        # Append Examples Section to the prompt list
        prompt.append("## Examples\n")
        for idx, example in enumerate(examples, 1):
            prompt.append(f"**Example {idx}:** {example['description']}\n")
            prompt.append(render_code_block(example["code"], language))
            prompt.append("**Question:** " + example["question"] + "\n")
            prompt.append("**Explanation:** " + example["output"]["explanation"] + "\n")
            prompt.append("**Answer:**\n")

            format_str = "- (Program Line 1: Line {LINE_NUMBER_1}, Expression 1: {VARIABLE_NAME_1}), (Program Line 2: Line {LINE_NUMBER_2}, Expression 2: {VARIABLE_NAME_2})"

            for [
                (line_number_1, variable_name_1),
                (line_number_2, variable_name_2),
            ] in example["output"]["answer"]:
                if line_number_1.isdigit() and line_number_2.isdigit():
                    prompt.append(
                        format_str.format(
                            LINE_NUMBER_1=line_number_1,
                            VARIABLE_NAME_1=variable_name_1,
                            LINE_NUMBER_2=line_number_2,
                            VARIABLE_NAME_2=variable_name_2,
                        )
                        + "\n"
                    )
            prompt.append("\n")

        # Append the Final Task if exists
        if final_task:
            prompt.append("**Final Task**\n")
            prompt.append(final_task["description"] + "\n")
            prompt.append(render_code_block(final_task["code"], language))
            prompt.append("**Question:** " + final_task["question"] + "\n")
            prompt.append("**Format:** " + "\n".join(final_task["format"]) + "\n")

    except FileNotFoundError:
        print(f"The file at {prompt_template_file} was not found.")
        exit(0)
        return ""
    except Exception as e:
        print(f"An error occurred: {e}")
        exit(0)
        return ""

    return "".join(prompt).replace("{P_CODE}", lined_source_code)


def construct_transform_prompt_from_unary_relation(
    prompt_template_file: str, lined_source_code: str, language: str = "java"
) -> str:
    """
    Load a JSON file from the specified path and construct a prompt from the data.
    """
    prompt = []  # Collection of all prompt sections as a list of strings
    try:
        # Open and load the JSON file
        with open(prompt_template_file, "r", encoding="utf-8") as file:
            data = json.load(file)

        system_description = data.get("system", "")
        task_description = data.get("task", "")
        examples = data.get("examples", [])
        final_task = data.get("final_task", {})

        # Append the System Description to the prompt list
        prompt.append("## System Description\n")
        prompt.append(system_description + "\n")

        # Append the Task Description to the prompt list
        prompt.append("## Task\n")
        prompt.append(task_description + "\n")

        # Append Examples Section to the prompt list
        prompt.append("## Examples\n")
        for idx, example in enumerate(examples, 1):
            prompt.append(f"**Example {idx}:** {example['description']}\n")
            prompt.append(render_code_block(example["code"], language))
            prompt.append("**Question:** " + example["question"] + "\n")
            prompt.append("**Explanation:** " + example["output"]["explanation"] + "\n")
            prompt.append("**Answer:**\n")
            for dep in example["output"]["answer"]:
                prompt.append(
                    f"- Program Line: Line {dep['Line']}, Expression: {dep['Expression']}\n"
                )
            prompt.append("\n")

        # Append the Final Task if exists
        if final_task:
            prompt.append("**Final Task**\n")
            prompt.append(final_task["description"] + "\n")
            prompt.append(render_code_block(final_task["code"], language))
            prompt.append("**Question:** " + final_task["question"] + "\n")
            prompt.append("**Format:** " + "\n".join(final_task["format"]) + "\n")

    except FileNotFoundError:
        print(f"The file at {prompt_template_file} was not found.")
        return ""
    except Exception as e:
        print(f"An error occurred: {e}")
        return ""

    return "".join(prompt).replace("{P_CODE}", lined_source_code)
