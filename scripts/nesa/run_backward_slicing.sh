#!/bin/bash

# Obtain the absolute path of the parent directory of the script (project root)
script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
parent_path="$(cd "$script_dir/../.." && pwd)"

# read the list from backward_benchmark.json and strore it in backward_slicing_case
backward_slicing_case=($(jq -r '.[]' "$parent_path/data/backwardslice/backward_benchmark.json"))

# ehco backward_slicing_case
# echo "backward_slicing_case: ${backward_slicing_case[@]}"


# Function to parse command-line arguments
parse_args() {
    python_bin_default="/Users/xiangqian/miniconda3/envs/nesa/bin/python3"
    eval_rule_mode=""
    semi_naive_evaluation=""
    parallel_rule_n="8"
    parallel_primitive_n="8"
    measure_token_cost=""
    source_file=""
    seed_file=""
    inference_model="gpt-4o-mini"
    temperature="0.0"
    language="java"
    python_bin="${NESA_PYTHON:-$python_bin_default}"

    while [[ "$#" -gt 0 ]]; do
        case $1 in
            --eval-rule-mode) eval_rule_mode="$2"; shift ;;
            -semi-naive-evaluation) semi_naive_evaluation="semi-naive-evaluation" ;;
            --parallel-rule-n) parallel_rule_n="$2"; shift ;;
            --parallel-primitive-n) parallel_primitive_n="$2"; shift ;;
            -measure-token-cost) measure_token_cost="measure-token-cost" ;;
            --source-file) source_file="$2"; shift ;;
            --seed-file) seed_file="$2"; shift ;;
            --inference-model) inference_model="$2"; shift ;;
            --temperature) temperature="$2"; shift ;;
            --language) language="$2"; shift ;;
            --python-bin) python_bin="$2"; shift ;;
            *) echo "Unknown parameter passed: $1"; exit 1 ;;
        esac
        shift
    done
}

# Print "is ready"
echo "is ready"

# Parse the command-line arguments
parse_args "$@"

if [[ ! -x $python_bin ]]; then
    echo "python-bin not found or not executable: $python_bin"
    exit 1
fi

if [[ -n $source_file && -z $seed_file ]]; then
    echo "seed-file is required when source-file is provided."
    exit 1
fi

# Construct the log path based on the options
log_path="$parent_path/log/backslice"
[[ -n $eval_rule_mode ]] && log_path="${log_path}_${eval_rule_mode}"
[[ -n $semi_naive_evaluation ]] && log_path="${log_path}_semi_naive"
log_path="${log_path}_parallel_rule_${parallel_rule_n}_parallel_primitive_${parallel_primitive_n}"

# Create the log directory if it does not exist
mkdir -p $log_path

if [[ -n $source_file ]]; then
    if [[ ! -f $source_file ]]; then
        echo "source-file not found: $source_file"
        exit 1
    fi
    java_files=("$source_file")
else
    # Collect all the java files in the parent_path/data/xssdata directory and save their absolute paths in a list
    mapfile -t java_files < <(find "$parent_path/data/backwardslice/source_files" -type f -name "*.java")
fi

# run_analysis_python_file is parent_path/src/nesa/run.py
run_analysis_python_file="$parent_path/src/nesa/run.py"

counter=0

# Iterate over the list of java files and run the run.py script on each file
for java_file in "${java_files[@]}"
do
    # extract the substring between the last / and .java to get the name of the file
    file_name="${java_file##*/}"
    file_name="${file_name%.*}"
    log_file="$log_path/$file_name.txt"
    current_seed_file="$parent_path/data/backwardslice/seed_files/$file_name.json"

    if [[ -n $source_file ]]; then
        current_seed_file="$seed_file"
        if [[ ! -f $current_seed_file ]]; then
            echo "seed-file not found: $current_seed_file"
            exit 1
        fi
    else
        # Check if file_name is in backward_slicing_case
        is_in_backward_slicing_case=false
        for case in "${backward_slicing_case[@]}"; do
            if [[ "$case" == "$file_name" ]]; then
                is_in_backward_slicing_case=true
                break
            fi
        done

        # Skip if file_name is not in backward_slicing_case
        if [[ "$is_in_backward_slicing_case" == false ]]; then
            continue
        fi
    fi


    # Increment the counter
    ((counter++))

    # # if log file already exists, delete it
    # if [ -f $log_file ]; then
    #     rm $log_file
    # fi

    # if log file already exists, skip the analysis
    if [ -f $log_file ]; then
        continue
    fi

    echo "Running analysis on $java_file"   
    echo "Dump the log to $log_file"

    cmd="time $python_bin $run_analysis_python_file --analyzer-file $parent_path/src/nesa/analysis/backslice.dl --seed-file $current_seed_file --source-file $java_file --inference-model $inference_model --temperature $temperature --language $language"
    [[ -n $eval_rule_mode ]] && cmd="$cmd --eval-rule-mode $eval_rule_mode"
    [[ -n $semi_naive_evaluation ]] && cmd="$cmd -semi-naive-evaluation"
    cmd="$cmd --parallel-rule-n $parallel_rule_n --parallel-primitive-n $parallel_primitive_n"
    [[ -n $measure_token_cost ]] && cmd="$cmd -measure-token-cost"
    cmd="$cmd | tee $log_file"
    
    eval $cmd
    
done
