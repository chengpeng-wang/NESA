#!/bin/bash

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
parent_path="$(cd "$script_dir/../.." && pwd)"

parse_args() {
    source_file=""
    inference_model="gpt-4o-mini"
    temperature="0.0"

    while [[ "$#" -gt 0 ]]; do
        case $1 in
            --source-file) source_file="$2"; shift ;;
            --inference-model) inference_model="$2"; shift ;;
            --temperature) temperature="$2"; shift ;;
            *) echo "Unknown parameter passed: $1"; exit 1 ;;
        esac
        shift
    done
}

parse_args "$@"

cmd="python3 $parent_path/src/baseline/run_fscot_slicing.py --inference-model $inference_model --temperature $temperature"

if [[ -n $source_file ]]; then
    if [[ ! -f $source_file ]]; then
        echo "source-file not found: $source_file"
        exit 1
    fi
    cmd="$cmd --source-file $source_file"
fi

eval "$cmd"
