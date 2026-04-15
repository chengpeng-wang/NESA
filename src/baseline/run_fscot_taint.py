import os
import json
from pathlib import Path
import argparse
from typing import Optional
from llm import LLM
from utils import *
import time


def construct_cot_taint_prompt(code: str, src_function: str, sink_function: str) -> str:
    inference_prompt = """We will provide a source file with line numbers. Please detect the sensitive data flow from any sensitive output value of the function call of {SRC_FUNCTION} to the argument of the function call of {SINK_FUNCTION}.

You can follow the following steps to detect the desired data flow:
Step 1: First, identify the source function call and sink function call.
Step 2: Second, check whether the argument of sink function call can be changed by the output value of the source function call.

Your output should be in the following format:
-------------BEGIN REPORT----------------
There is {BUG_NUM} bug(s) in the program:
- Bug {i}, [Explanation: {BUG_EXPLANATION}], [Buggy function: {FUNCTION_NAME}]
---------------END REPORT----------------
Here is the description of the above format:
- The first line shows the number of the Absolute Path Traversal bugs in the program, i.e., {BUG_NUM}.
- Starting from the second line, you should list each bug line by line.
   - {i} shows the i-th bug report.
   - {BUG_EXPLANATION} is the natural language explanation of the bug
   - {FUNCTION_NAME} is the name of the function where the bug occurs.
Here are several examples:
Example 1:
User:
File name: Example1.java
```
1.  public String fun1(HttpServletRequest request) {
2.      InputStreamReader readerInputStream = new InputStreamReader(System.in, "UTF-8");
3.      BufferedReader readerBuffered = new BufferedReader(readerInputStream);
4.      String token = {SRC_FUNCTION}();
5.      return token;
6.  }
7.  public void fun2(HttpServletRequest request, HttpServletResponse response) {
8.      String t = fun1(request);
9.      {SINK_FUNCTION}(t + "user");
10.  }
```
Please detect the bugs in the program step by step.
System:
-------------BEGIN REPORT----------------
There is 1 bug in the program:
- Bug 1: [Explanation: In the file Example1.java, the value of token at the line 4 is produced by the return value of {SRC_FUNCTION}. It is returned by the return statement at line 5 and propagated back to the caller function named fun2 at line 8. The assignment at line 8 make t sensitive. The expression t + "user" is also sensitive. Hence, the argument of {SINK_FUNCTION} at line 9 is sensitive. Therefore, there is a bug at the line 9.], [Buggy function: fun2]
---------------END REPORT----------------

Now I will give you the program as follows: 

```
{CODE_WITH_LINE_NUMBERS}
```

Please report the bugs with the explanations and buggy functions in the above formats. The first line of your report should be "-------------BEGIN REPORT----------------" and the last line should "---------------END REPORT----------------". Between the two lines, you should report the bug number at the beginning with a single line in the format "There is {BUG_NUM} bug(s) in the program:". Then you should report each bug in the format "- Bug {i}, [Explanation, {BUG_EXPLANATION}], [Buggy function: {FUNCTION_NAME}]".
Please analyze the code right now. It is very urgent! Don't output the sentence like "I will analyze the provided source files and detect the bugs according to the given instructions. I will then report the bugs with explanations and buggy functions in the specified format. Let me start the analysis.". Just give me the report in this round. Also, please try your best to identify the bugs AS MANY AS POSSIBLE.
"""
    prompt = (
        inference_prompt.replace("{CODE_WITH_LINE_NUMBERS}", code)
        .replace("{SRC_FUNCTION}", src_function)
        .replace("{SINK_FUNCTION}", sink_function)
    )
    return prompt


def run_all(
    model_name: str, temperature: float, global_id: Optional[int] = None
) -> None:
    file_path = os.path.join(
        os.path.dirname(__file__), "../../data/TaintBench/benchmark.json"
    )
    taint_dir = os.path.join(os.path.dirname(__file__), "../../data/TaintBench")
    log_dir = os.path.join(
        os.path.dirname(__file__), "../../log/taintlog_fscot_" + model_name
    )

    if not os.path.exists(log_dir):
        os.makedirs(log_dir)

    with open(file_path, "r") as f:
        data = json.load(f)
    for apk_name in data:
        for k in data[apk_name]:
            global_ids = sorted([item["global_id"] for item in data[apk_name][k]])
            if global_id is not None and global_id not in global_ids:
                continue

            ks = k.split(" | ")
            single_file_path = ks[0]
            source_function = ks[1]
            sink_function = ks[2]

            if global_id is not None:
                name = str(global_id)
            else:
                name = (
                    k.replace(" ", "_")
                    .replace("|", "_")
                    .replace("/", "_")
                    .replace(".", "_")
                )
            log_file = os.path.join(log_dir, f"{name}.txt")

            # IF the log file already exists, delete it
            if os.path.exists(log_file):
                continue

            # read the content from single_file_path
            with open(os.path.join(taint_dir, single_file_path), "r") as read_file:
                code = read_file.read()

            lined_code = "\n".join(
                [f"{idx + 1} {line}" for idx, line in enumerate(code.split("\n"))]
            )
            prompt = construct_cot_taint_prompt(
                lined_code, source_function, sink_function
            )

            model = LLM(model_name, standard_keys[0], temperature, True)

            time1 = time.time()
            response, input_token_cost, output_token_cost = model.infer(prompt)
            time2 = time.time()

            total_time = time2 - time1

            with open(log_file, "w") as write_file:
                write_file.write("=====================================\n")
                write_file.write("Prompt: \n")
                write_file.write(prompt + "\n")
                write_file.write("=====================================\n")
                write_file.write("Repsonse: \n")
                write_file.write(response + "\n")
                write_file.write("=====================================\n")
                write_file.write(str(Path(taint_dir) / single_file_path) + "\n")
                write_file.write(source_function + "\n")
                write_file.write(sink_function + "\n")
                write_file.write("=====================================\n")
                write_file.write("Global id: " + str(global_ids) + "\n")
                write_file.write(f"Time: {total_time}\n")
                write_file.write(f"Input token cost: {input_token_cost}\n")
                write_file.write(f"Output token cost: {output_token_cost}\n")
                write_file.write("=====================================\n")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
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
        "--global-id",
        type=int,
        default=None,
        help="Run the taint baseline for exactly one TaintBench global id",
    )

    args = parser.parse_args()
    run_all(args.inference_model, args.temperature, args.global_id)
