#!/bin/bash

set -euo pipefail

# Obtain the absolute path of the parent directory of the script (project root)
script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
parent_path="$(cd "$script_dir/../.." && pwd)"

parse_args() {
    python_bin_default="/Users/xiangqian/miniconda3/envs/nesa/bin/python3"
    eval_rule_mode="full-featured"
    semi_naive_evaluation="semi-naive-evaluation"
    parallel_rule_n="8"
    parallel_primitive_n="8"
    measure_token_cost=""
    src_project=""
    inference_model="gpt-4o-mini"
    temperature="0.0"
    language="cpp"
    python_bin="${NESA_PYTHON:-$python_bin_default}"

    while [[ "$#" -gt 0 ]]; do
        case $1 in
            --src-project) src_project="$2"; shift ;;
            --eval-rule-mode) eval_rule_mode="$2"; shift ;;
            --parallel-rule-n) parallel_rule_n="$2"; shift ;;
            --parallel-primitive-n) parallel_primitive_n="$2"; shift ;;
            -measure-token-cost) measure_token_cost="measure-token-cost" ;;
            --inference-model) inference_model="$2"; shift ;;
            --temperature) temperature="$2"; shift ;;
            --language) language="$2"; shift ;;
            --python-bin) python_bin="$2"; shift ;;
            *) echo "Unknown parameter passed: $1"; exit 1 ;;
        esac
        shift
    done
}

parse_args "$@"

if [[ -z $src_project ]]; then
    echo "src-project is required."
    exit 1
fi

if [[ ! -d $src_project ]]; then
    echo "src-project not found or not a directory: $src_project"
    exit 1
fi

if [[ ! -x $python_bin ]]; then
    echo "python-bin not found or not executable: $python_bin"
    exit 1
fi

log_path="$parent_path/log/intraml_detector"
[[ -n $eval_rule_mode ]] && log_path="${log_path}_${eval_rule_mode}"
[[ -n $semi_naive_evaluation ]] && log_path="${log_path}_semi_naive"
log_path="${log_path}_parallel_rule_${parallel_rule_n}_parallel_primitive_${parallel_primitive_n}"

mkdir -p "$log_path"

project_name="$(basename "${src_project%/}")"
log_file="$log_path/$project_name.txt"

run_analysis_python_file="$parent_path/src/nesa/run.py"

echo "Running intra memory leak analysis on $src_project"
echo "Dump the log to $log_file"

cmd=(
    "$python_bin"
    "$run_analysis_python_file"
    --analyzer-file "$parent_path/src/nesa/analysis/intraml.dl"
    --src-project "$src_project"
    --inference-model "$inference_model"
    --temperature "$temperature"
    --language "$language"
)

[[ -n $eval_rule_mode ]] && cmd+=(--eval-rule-mode "$eval_rule_mode")
[[ -n $semi_naive_evaluation ]] && cmd+=(-semi-naive-evaluation)
cmd+=(--parallel-rule-n "$parallel_rule_n" --parallel-primitive-n "$parallel_primitive_n")
[[ -n $measure_token_cost ]] && cmd+=(-measure-token-cost)

time "${cmd[@]}" | tee "$log_file"
echo "$log_file"
