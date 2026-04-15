#!/bin/bash

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
parent_path="$(cd "$script_dir/../.." && pwd)"

parse_args() {
    bug_type=""
    source_file=""
    code_directory=""
    inference_model="gpt-4o-mini"
    temperature="0.0"

    while [[ "$#" -gt 0 ]]; do
        case $1 in
            --bug-type) bug_type="$2"; shift ;;
            --source-file) source_file="$2"; shift ;;
            --code-directory) code_directory="$2"; shift ;;
            --inference-model) inference_model="$2"; shift ;;
            --temperature) temperature="$2"; shift ;;
            *) echo "Unknown parameter passed: $1"; exit 1 ;;
        esac
        shift
    done
}

parse_args "$@"

if [[ -z $bug_type ]]; then
    echo "bug-type is required."
    exit 1
fi

cmd="python3 $parent_path/src/baseline/run_fscot_detect.py --bug-type $bug_type --inference-model $inference_model --temperature $temperature"

if [[ -n $source_file ]]; then
    if [[ ! -f $source_file ]]; then
        echo "source-file not found: $source_file"
        exit 1
    fi
    cmd="$cmd --source-file $source_file"
elif [[ -n $code_directory ]]; then
    if [[ ! -d $code_directory ]]; then
        echo "code-directory not found: $code_directory"
        exit 1
    fi
    cmd="$cmd --code-directory $code_directory"
fi

eval "$cmd"
