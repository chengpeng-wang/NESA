import sys
import os
from os import path
import json
import argparse
import time

import tree_sitter
from tree_sitter import Language

# import tree_sitter_c as tsc
# import tree_sitter_java as tsjava
# import tree_sitter_cpp as tscpp

from typing import List, Tuple, Dict, Set
from enum import Enum
from pathlib import Path
from model.llm import *
from model.utils import *
from parser.response_parser import *
from parser.program_parser import *
from evaluator.analysis_evaluator import *
from utility.io import *
from utility.metrics import *
from utility.obfuscate import *


def get_project_source_file_extensions(language: str) -> Set[str]:
    if language == "java":
        return {".java"}
    if language == "cpp":
        return {".c", ".cc", ".cpp", ".cxx", ".h", ".hh", ".hpp", ".hxx"}
    raise ValueError(f"Unsupported language for project analysis: {language}")


def load_project_source_files(src_project: str, language: str) -> Dict[str, str]:
    project_path = Path(src_project).resolve()
    if not project_path.is_dir():
        raise ValueError(f"src-project must be a directory: {src_project}")

    valid_suffixes = get_project_source_file_extensions(language)
    source_files = {}
    for file_path in sorted(project_path.rglob("*")):
        if not file_path.is_file():
            continue
        if file_path.suffix.lower() not in valid_suffixes:
            continue

        with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
            source_files[str(file_path)] = f.read()

    if len(source_files) == 0:
        raise ValueError(
            f"No supported source files found in project {src_project} for language {language}"
        )
    return source_files


def run(
    source_file: str,
    src_project: str,
    analyzer_file: str,
    seed_file: str,
    language: str,
    online_model_name: str,
    key: str,
    temperature: float,
    additional_context_file: str,
    eval_rule_mode: str,
    parallel_rule_n: int,
    parallel_primitive_n: int,
    semi_naive_evaluation: bool,
    measure_token_cost: bool,
) -> None:
    if source_file != "":
        with open(source_file, "r") as f:
            code = f.read()
        all_source_files = {source_file: code}
    else:
        all_source_files = load_project_source_files(src_project, language)

    if source_file == "" and seed_file != "config/symbolic/unary/seed_expr.json":
        raise ValueError("seed-file is only supported when --source-file is used")

    if source_file != "" and seed_file != "config/symbolic/unary/seed_expr.json":
        with open(seed_file, "r") as f:
            seed = json.load(f)
        var_name = seed["name"]
        line_number = seed["loc"]

        config_file = (
            Path(__file__).resolve().parent
            / "config"
            / "symbolic"
            / "unary"
            / "seed_expr.json"
        )
        with open(config_file, "w") as f:
            json.dump({"loc": line_number, "name": var_name}, f, indent=4)

        all_source_files[source_file] = beautify_source_code(code)

    for file_path in list(all_source_files.keys()):
        all_source_files[file_path] = clean_comment_name(
            all_source_files[file_path], language
        )

    evaluator = AnalysisEvaluator(
        analyzer_file,
        all_source_files,
        language,
        online_model_name,
        key,
        temperature,
        additional_context_file,
        eval_rule_mode,
        parallel_rule_n,
        parallel_primitive_n,
        semi_naive_evaluation,
        measure_token_cost,
    )
    evaluator.run()
    return


if __name__ == "__main__":
    parser = argparse.ArgumentParser()

    source_group = parser.add_mutually_exclusive_group(required=True)

    source_group.add_argument(
        "--source-file",
        type=str,
        help="Specify the source file",
    )
    source_group.add_argument(
        "--src-project",
        type=str,
        help="Specify the source project directory",
    )

    # analysis specification
    parser.add_argument(
        "--analyzer-file",
        type=str,
        required=True,
        help="Specify the analyzer file",
    )

    parser.add_argument(
        "--language",
        type=str,
        default="java",
        choices=["java", "cpp"],
        help="Specify the source language",
    )

    # define an optional argument that specifies the seed file. This argument can be absent.
    parser.add_argument(
        "--seed-file",
        type=str,
        default="config/symbolic/unary/seed_expr.json",
        help="Specify the path to the slicing seed file",
    )

    # LLM configuration
    parser.add_argument(
        "--inference-model",
        type=str,
        default="gpt-4o-mini",
        help="Specify LLM model for inference. Defaults to gpt-4o-mini.",
    )
    parser.add_argument(
        "--temperature",
        type=float,
        default=0.0,
        help="Specify temperature for LLM inference. Defaults to 0.0.",
    )

    # add an argument to specify a string as an additional context, default value is ""
    parser.add_argument(
        "--additional-context-file",
        type=str,
        default="",
        help="Specify the additional context for the analysis",
    )

    # add the evaluation mode: full-featured, binary-populate-only, binary-transform-only
    parser.add_argument(
        "--eval-rule-mode",
        choices=[
            "full-featured",
            "binary-populate",
            "binary-transform-forward",
            "binary-transform-backward",
        ],
        help="Specify the evaluation rule mode",
    )

    # add arguments to configure rule-level and primitive-level parallelism
    parser.add_argument(
        "--parallel-rule-n",
        type=int,
        default=8,
        help="Maximum number of rules to evaluate concurrently; use 1 to disable rule-level parallelism",
    )
    parser.add_argument(
        "--parallel-primitive-n",
        type=int,
        default=8,
        help="Maximum number of primitive batch tasks to execute concurrently; use 1 to disable primitive-level parallelism",
    )

    # add an argument to determine whether to use naive evaluation or semi-naive evaluation
    parser.add_argument(
        "-semi-naive-evaluation",
        action="store_true",
        help="Specify whether to use semi-naive evaluation or not",
    )

    # add an argument to indicate whether to measure the token cost
    parser.add_argument(
        "-measure-token-cost",
        action="store_true",
        help="Specify whether to measure the token cost or not",
    )

    args = parser.parse_args()

    source_file = args.source_file or ""
    src_project = args.src_project or ""
    analyzer_file = args.analyzer_file
    seed_file = args.seed_file
    language = args.language
    inference_model = args.inference_model
    temperature = args.temperature
    additional_context_file = args.additional_context_file

    eval_rule_mode = args.eval_rule_mode
    parallel_rule_n = args.parallel_rule_n
    parallel_primitive_n = args.parallel_primitive_n
    semi_naive_evaluation = args.semi_naive_evaluation
    measure_token_cost = args.measure_token_cost

    if parallel_rule_n < 1:
        raise ValueError("--parallel-rule-n must be at least 1")
    if parallel_primitive_n < 1:
        raise ValueError("--parallel-primitive-n must be at least 1")

    start_time = time.time()

    run(
        source_file,
        src_project,
        analyzer_file,
        seed_file,
        language,
        inference_model,
        standard_keys[0],
        temperature,
        additional_context_file,
        eval_rule_mode,
        parallel_rule_n,
        parallel_primitive_n,
        semi_naive_evaluation,
        measure_token_cost,
    )

    end_time = time.time()

    print("Total time: ", end_time - start_time)
