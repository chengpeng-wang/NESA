import json
from pathlib import Path
from typing import List, Tuple
import argparse
from llm import LLM
from utils import *
import tree_sitter
import time

# JAVA_LANGUAGE = tree_sitter.Language("build/my-languages.so", "java")

# parser = tree_sitter.Parser()
# parser.set_language(JAVA_LANGUAGE)


def find_nodes_by_type(
    root: tree_sitter.Node, node_type: str
) -> List[tree_sitter.Node]:
    """
    Find all the nodes with node_type type underlying the root node.
    :param root: root node
    :return the list of the nodes with node_type type
    """
    nodes = []
    if root.type == node_type:
        nodes.append(root)

    for child_node in root.children:
        nodes.extend(find_nodes_by_type(child_node, node_type))
    return nodes


def clean_comment_name(source_code: str) -> str:
    cwd = Path(__file__).resolve().parent.parent.absolute()
    TSPATH = cwd / "../lib/build/"
    language_path = TSPATH / "my-languages.so"
    java_lang = tree_sitter.Language(str(language_path), "java")

    parser = tree_sitter.Parser()
    parser.set_language(java_lang)

    t = parser.parse(bytes(source_code, "utf8"))
    root_node = t.root_node
    nodes = find_nodes_by_type(root_node, "line_comment")
    nodes.extend(find_nodes_by_type(root_node, "block_comment"))
    nodes.extend(find_nodes_by_type(root_node, "javadoc_comment"))

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


def start_run_model(
    code_content: str,
    prompt_file_path: str,
    online_model_name: str,
    key: str,
    temperature: float,
) -> Tuple[str, str, int, int]:
    model = LLM(online_model_name, key, temperature, True)
    with open(prompt_file_path, "r") as read_file:
        spec = json.load(read_file)

    message = spec["task"] + "\n"
    message += "\n".join(spec["analysis_rules"]) + "\n"
    message += "\n".join(spec["output_constraints"]) + "\n"
    message += "\n".join(spec["analysis_examples"]) + "\n"

    lined_code_lines = []
    for idx, line in enumerate(code_content.split("\n")):
        lined_code_lines.append(f"{idx + 1} {line}")
    code_content = "\n".join(lined_code_lines)

    program = "```\n" + code_content + "\n```\n\n"
    message += "\n".join(spec["meta_prompts_without_reflection"]) + "\n"
    message = message.replace("<PROGRAM>", program)

    response, input_token_cost, output_token_cost = model.infer(message)
    print(message)

    return message, response, input_token_cost, output_token_cost


def detect_bugs(
    bug_type: str,
    model_name: str,
    temperature: float,
    source_file: str = "",
    code_directory: str = "",
) -> None:
    cwd = Path(__file__).parent

    prompt_file_path = cwd / "prompt" / (bug_type + ".json")
    default_code_directory = cwd.parent.parent / "data" / "juliet" / (bug_type + "data")

    if source_file:
        java_files = [Path(source_file).resolve()]
    else:
        target_directory = (
            Path(code_directory).resolve() if code_directory else default_code_directory
        )
        java_files = sorted(target_directory.glob("*.java"))

    log_path = (
        cwd.parent.parent
        / "log"
        / "juliet"
        / (bug_type + "_detector_cot" + "_" + model_name)
    )

    if not log_path.exists():
        log_path.mkdir(parents=True)

    for java_file in java_files:
        if not java_file.exists():
            raise FileNotFoundError(f"Source file not found: {java_file}")
        with open(java_file, "r") as read_file:
            name = java_file.stem
            log_file = log_path / f"{name}.txt"

            if log_file.exists():
                print(f"Skip {name}")
                continue

            source_code = read_file.read()
            code_content = clean_comment_name(source_code)
            time1 = time.time()
            message, response, input_token_cost, output_token_cost = start_run_model(
                code_content,
                prompt_file_path,
                model_name,
                standard_keys[0],
                temperature,
            )
            time2 = time.time()
            print(response)

            with open(log_file, "a") as write_file:
                write_file.write("=====================================\n")
                write_file.write("Prompt: \n")
                write_file.write(message + "\n")
                write_file.write("=====================================\n")
                write_file.write("Repsonse: \n")
                write_file.write(response + "\n")
                write_file.write("=====================================\n")
                write_file.write("Input Token Cost: \n")
                write_file.write(str(input_token_cost) + "\n")
                write_file.write("=====================================\n")
                write_file.write("Output Token Cost: \n")
                write_file.write(str(output_token_cost) + "\n")
                write_file.write("=====================================\n")
                write_file.write("Time: \n")
                write_file.write(str(time2 - time1) + "\n")
                write_file.write("=====================================\n")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--bug-type",
        type=str,
        required=True,
        help="Specify the bug type prompt to run",
    )
    parser.add_argument(
        "--inference-model",
        type=str,
        default="gpt-4o-mini",
        help="Specify the LLM model for inference. Defaults to gpt-4o-mini.",
    )
    parser.add_argument(
        "--temperature",
        type=float,
        default=0.0,
        help="Specify the sampling temperature for the LLM. Defaults to 0.0.",
    )
    parser.add_argument(
        "--source-file",
        type=str,
        default="",
        help="Run the baseline on exactly one Java source file",
    )
    parser.add_argument(
        "--code-directory",
        type=str,
        default="",
        help="Run the baseline on all Java files under the specified directory",
    )

    args = parser.parse_args()
    detect_bugs(
        args.bug_type,
        args.inference_model,
        args.temperature,
        args.source_file,
        args.code_directory,
    )
