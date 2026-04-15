#!/bin/bash

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
parent_path="$(cd "$script_dir/../.." && pwd)"

parse_args() {
    python_bin_default="/Users/xiangqian/miniconda3/envs/nesa/bin/python3"
    global_id=""
    inference_model="gpt-4o-mini"
    temperature="0.0"
    force_rerun="false"
    language="java"
    benchmark_json="$parent_path/data/TaintBench/benchmark.json"
    python_bin="${NESA_PYTHON:-$python_bin_default}"

    while [[ "$#" -gt 0 ]]; do
        case $1 in
            --benchmark-json) benchmark_json="$2"; shift ;;
            --inference-model) inference_model="$2"; shift ;;
            --temperature) temperature="$2"; shift ;;
            --language) language="$2"; shift ;;
            --python-bin) python_bin="$2"; shift ;;
            --force) force_rerun="true" ;;
            -*)
                echo "Unknown parameter passed: $1"
                exit 1
                ;;
            *)
                if [[ -z $global_id ]]; then
                    global_id="$1"
                else
                    echo "Unexpected positional argument: $1"
                    exit 1
                fi
                ;;
        esac
        shift
    done
}

parse_args "$@"

if [[ -z $global_id ]]; then
    echo "global_id is required."
    exit 1
fi

if [[ ! -x $python_bin ]]; then
    echo "python-bin not found or not executable: $python_bin"
    exit 1
fi

if [[ ! -f $benchmark_json ]]; then
    echo "benchmark-json not found: $benchmark_json"
    exit 1
fi

run_analysis_python_file="$parent_path/src/nesa/run.py"
analyzer_file="$parent_path/src/nesa/analysis/taintdetect.dl"
source_function_path="$parent_path/src/nesa/config/symbolic/unary/source_expr.json"
sink_function_path="$parent_path/src/nesa/config/symbolic/unary/sink_expr.json"

analyzed_count=0
matched_case="false"

matched_tsv="$(jq -r --argjson target "$global_id" '
    to_entries[]
    | .key as $apk
    | .value
    | to_entries[]
    | .value[]
    | select(.global_id == $target)
    | [
        $apk,
        .source.targetName,
        .sink.targetName,
        .source.fileName
      ]
    | @tsv
' "$benchmark_json")"

if [[ -n $matched_tsv ]]; then
    matched_case="true"
fi

while IFS=$'\t' read -r apk_name source_function sink_function source_file_name; do
    [[ -z $apk_name ]] && continue

    source_file="$parent_path/data/TaintBench/$source_file_name"
    file_stem="$(basename "$source_file" .java)"
    log_dir="$parent_path/log/taintbench/$apk_name/$file_stem"
    log_file="$log_dir/$global_id.txt"

    if [[ ! -f $source_file ]]; then
        echo "source-file not found: $source_file"
        exit 1
    fi

    mkdir -p "$log_dir"

    if [[ -f $log_file && $force_rerun != "true" ]]; then
        continue
    fi

    jq -n --arg function "$source_function" '{function: $function}' > "$source_function_path"
    jq -n --arg function "$sink_function" '{function: $function}' > "$sink_function_path"

    echo "Running analysis on $source_file"
    echo "Dump the log to $log_file"

    cmd="time $python_bin $run_analysis_python_file --source-file $source_file --analyzer-file $analyzer_file --inference-model $inference_model --temperature $temperature --language $language --eval-rule-mode full-featured --parallel-rule-n 8 --parallel-primitive-n 8 -semi-naive-evaluation -measure-token-cost | tee $log_file"

    analyzed_count=$((analyzed_count + 1))
    eval "$cmd"

    sleep 10
done <<< "$matched_tsv"

if [[ $matched_case != "true" ]]; then
    echo "global_id $global_id was not found in benchmark-json: $benchmark_json"
fi

echo "#analyzed instances: $analyzed_count"
