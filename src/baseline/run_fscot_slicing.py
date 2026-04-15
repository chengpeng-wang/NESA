import os
import json
import time
import argparse
from pathlib import Path
from typing import Optional
from llm import LLM
from utils import *
from response_parser import *


def run_all_data(
    online_model_name: str,
    key: str,
    temperature: float,
    source_file: str = "",
) -> None:
    model = LLM(online_model_name, key, temperature, True)

    # Obtain the root path of this project
    root_path = Path(__file__).parent.parent.parent

    # Define paths for benchmark files and source directories
    backward_slices_benchmark_file = os.path.join(
        root_path, "data/backwardslice/backward_benchmark.json"
    )
    forward_slices_benchmark_file = os.path.join(
        root_path, "data/forwardslice/forward_benchmark.json"
    )

    backward_slices_source_directory = os.path.join(
        root_path, "data/backwardslice/source_files"
    )
    forward_slices_source_directory = os.path.join(
        root_path, "data/forwardslice/source_files"
    )

    source_directories = {
        "backslice": backward_slices_source_directory,
        "forwardslice": forward_slices_source_directory,
    }

    benchmark_files = {
        "backslice": backward_slices_benchmark_file,
        # "forwardslice": forward_slices_benchmark_file
    }

    log_paths = {
        "backslice": os.path.join(
            root_path, "log/backslice_cot" + "_" + online_model_name
        ),
        "forwardslice": os.path.join(
            root_path, "log/forwardslice_cot" + "_" + online_model_name
        ),
    }

    if source_file:
        source_path = Path(source_file).resolve()
        if not source_path.exists():
            raise FileNotFoundError(f"Source file not found: {source_path}")

        file = source_path.name
        log_path = log_paths["backslice"]
        if not os.path.exists(log_path):
            os.makedirs(log_path)

        log_file = os.path.join(log_path, file + ".txt")
        if os.path.exists(log_file):
            print(log_file)
            return

        with open(source_path, "r") as f:
            content = f.read()

        name = file.replace(".java", "")
        varname = name.split("_")[-1]
        if not name.split("_")[-2].isdigit():
            raise ValueError(
                "Single-file slicing mode expects a file name that encodes the seed "
                "line number, e.g. *_<line>_<var>.java"
            )
        line_number = int(name.split("_")[-2])

        t1 = time.time()
        prompt = construct_cot_backward_slicing_prompt(
            content, varname, str(line_number)
        )
        output, input_token_cost, output_token_cost = model.infer(prompt)
        t2 = time.time()

        with open(log_file, "w") as f:
            f.write(prompt)
            f.write("\n=====================================\n")
            f.write("output\n")
            f.write(output)
            f.write("\ninput_token_cost:\n" + str(input_token_cost))
            f.write("\noutput_token_cost\n" + str(output_token_cost))
            f.write("\ntime:\n" + str(t2 - t1))
        print(log_file)
        return

    for slice_type in benchmark_files:
        # Load the list from benchmark_files[slice_type]
        with open(benchmark_files[slice_type], "r") as file:
            slice_items = set(json.load(file))

        log_path = log_paths[slice_type]
        if not os.path.exists(log_path):
            os.makedirs(log_path)

        # Collect all the Java files in source_directories[slice_type]
        for root, dirs, files in os.walk(source_directories[slice_type]):
            for file in files:
                if file.endswith(".java"):
                    print(file)
                    log_file = os.path.join(log_path, file + ".txt")

                    if os.path.exists(log_file):
                        continue

                    if file.replace(".java", "") not in slice_items:
                        continue

                    # Read the content of the file
                    with open(os.path.join(root, file), "r") as f:
                        content = f.read()

                    name = file.replace(".java", "")
                    varname = name.split("_")[-1]
                    if not name.split("_")[-2].isdigit():
                        continue
                    line_number = int(name.split("_")[-2])

                    t1 = time.time()
                    if slice_type == "backslice":
                        prompt = construct_cot_backward_slicing_prompt(
                            content, varname, str(line_number)
                        )
                    else:
                        prompt = construct_cot_forward_slicing_prompt(
                            content, varname, str(line_number)
                        )

                    output, input_token_cost, output_token_cost = model.infer(prompt)
                    t2 = time.time()

                    # Dump output, input_token_cost, output_token_cost to log_path
                    with open(log_file, "w") as f:
                        f.write(prompt)
                        f.write("\n=====================================\n")
                        f.write("output\n")
                        f.write(output)
                        f.write("\ninput_token_cost:\n" + str(input_token_cost))
                        f.write("\noutput_token_cost\n" + str(output_token_cost))
                        f.write("\ntime:\n" + str(t2 - t1))
                    print(os.path.join(log_path, file + ".txt"))
    return


def construct_cot_backward_slicing_prompt(code: str, var: str, line_number: str) -> str:
    inference_prompt = """Now I want you to generate a slice for a given variable, denoted by `var`, at a specific line, denoted by Line x, in the code.
The slice is a sequence of line numbers indicating the program lines that affect the value of the variable `var` at Line x. 
Specifically, Line y belongs to the slice if and only if it satisfies at least one of the following conditions:
(1) The value of the variable `var` at Line x is defined at Line y.
(2) The value of the branch condition of Line x is assigned at Line y.
(3) Line z belongs to the slice and a variable used at Line z is defined at Line y.

Here are several examples:

Example 1:

Code:
1: a = 5
2: b = 10
3: c = a + b
4: x = 20  
5: if c > 10:
6:     d = c * 2
7: e = d + 1
8: f = 100  
9: g = f + 1  
10: h = 50  
11: i = h + 2  

Slicing seed: d at line 7

Slice: [1, 2, 3, 5, 6]
Explanation: 
- Line 6: d is defined here.
- Line 5: The branch condition c > 10 affects whether Line 6 is executed.
- Line 3: c is defined here, which is used in the branch condition at Line 5.
- Line 1 and Line 2: a and b are defined here, which are used to compute c at Line 3.

Example 2:

Code:
1: x = 1
2: y = 2
3: z = x + y
4: w = 50  
5: if z < 5:
6:     v = z * 2
7: u = v + 3
8: t = 200  
9: s = t - 1  
10: r = 300  
11: q = r + 4  

Slicing seed: v at line 7

Slice: [1, 2, 3, 5, 6]
Explanation: 
- Line 6: v is defined here.
- Line 5: The branch condition z < 5 affects whether Line 6 is executed.
- Line 3: z is defined here, which is used in the branch condition at Line 5.
- Line 1 and Line 2: x and y are defined here, which are used to compute z at Line 3.

Example 3:

Code:
1: m = 3
2: n = 4
3: p = m * n
4: o = 60  
5: q = p + 2
6: if q > 10:
7:     r = q - 1
8: s = r / 2
9: t = 500  
10: u = t + 5  
11: v = 700  
12: w = v - 3  

Slicing seed: r at line 8

Slice: [1, 2, 3, 5, 6, 7]
Explanation: 
- Line 7: r is defined here.
- Line 6: The branch condition q > 10 affects whether Line 7 is executed.
- Line 5: q is defined here, which is used in the branch condition at Line 6.
- Line 3: p is defined here, which is used to compute q at Line 5.
- Line 1 and Line 2: m and n are defined here, which are used to compute p at Line 3.

Given the following code snippet, please generate the slice for the variable {SEED_VAR} at Line {SEED_LINE}.
{CODE_WITH_LINE_NUMBERS}

Please think step by step and output your answer in the following format:

Slice: The above program has the slice as follows: [Line l1, Line l2, Line l3]
Explanation: [Your explanation here]
"""
    prompt = (
        inference_prompt.replace("{SEED_VAR}", var)
        .replace("{CODE_WITH_LINE_NUMBERS}", code)
        .replace("{SEED_LINE}", str(line_number))
    )
    return prompt


def construct_cot_forward_slicing_prompt(code: str, var: str, line_number: str) -> str:
    inference_prompt = """Now I want you to generate a forward slice for a given variable, denoted by `var`, at a specific line, denoted by Line x, in the code.
The slice is a sequence of line numbers indicating the program lines that are affected by the value of the variable `var` at Line x. 
Specifically, Line y belongs to the slice if and only if it satisfies at least one of the following conditions:
(1) The value of the variable `var` at Line x is used at Line y.
(2) The value of the branch condition of Line y is assigned at Line x.
(3) Line z belongs to the slice and a variable defined at Line z is used at Line y.

Here are several examples:

Example 1:

Code:
1: a = 5
2: b = 10
3: c = a + b
4: x = 20  
5: if c > 10:
6:     d = c * 2
7: e = d + 1
8: f = 100  
9: g = f + 1  
10: h = 50  
11: i = h + 2  

Slicing seed: c at line 3

Slice: [3, 5, 6, 7]
Explanation: 
- Line 3: c is defined here.
- Line 5: The branch condition c > 10 uses the value of c.
- Line 6: d is defined here using the value of c.
- Line 7: e is defined here using the value of d, which is derived from c.

Example 2:

Code:
1: x = 1
2: y = 2
3: z = x + y
4: w = 50  
5: if z < 5:
6:     v = z * 2
7: u = v + 3
8: t = 200  
9: s = t - 1  
10: r = 300  
11: q = r + 4  

Slicing seed: z at line 3

Slice: [3, 5, 6, 7]
Explanation: 
- Line 3: z is defined here.
- Line 5: The branch condition z < 5 uses the value of z.
- Line 6: v is defined here using the value of z.
- Line 7: u is defined here using the value of v, which is derived from z.

Example 3:

Code:
1: m = 3
2: n = 4
3: p = m * n
4: o = 60  
5: q = p + 2
6: if q > 10:
7:     r = q - 1
8: s = r / 2
9: t = 500  
10: u = t + 5  
11: v = 700  
12: w = v - 3  

Slicing seed: p at line 3

Slice: [3, 5, 6, 7, 8]
Explanation: 
- Line 3: p is defined here.
- Line 5: q is defined here using the value of p.
- Line 6: The branch condition q > 10 uses the value of q, which is derived from p.
- Line 7: r is defined here using the value of q, which is derived from p.
- Line 8: s is defined here using the value of r, which is derived from p.

Given the following code snippet, please generate the forward slice for the variable {SEED_VAR} at Line {SEED_LINE}.
{CODE_WITH_LINE_NUMBERS}

Please think step by step and output your answer in the following format:

Slice: The above program has the slice as follows: [Line l1, Line l2, Line l3]
Explanation: [Your explanation here]
"""
    prompt = (
        inference_prompt.replace("{SEED_VAR}", var)
        .replace("{CODE_WITH_LINE_NUMBERS}", code)
        .replace("{SEED_LINE}", str(line_number))
    )
    return prompt


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
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
        help="Specify the sampling temperature for the LLM. Defaults to 0.0.",
    )
    parser.add_argument(
        "--source-file",
        type=str,
        default="",
        help="Run the slicing baseline on exactly one Java source file",
    )

    args = parser.parse_args()
    inference_model = args.inference_model
    run_all_data(
        inference_model,
        standard_keys[0],
        args.temperature,
        args.source_file,
    )
