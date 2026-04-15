#!/bin/bash

# Obtain the absolute path of the parent directory of the script (project root)
script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
parent_path="$(cd "$script_dir/../.." && pwd)"

# Function to parse command-line arguments
parse_args() {
    python_bin_default="/Users/xiangqian/miniconda3/envs/nesa/bin/python3"
    eval_rule_mode=""
    semi_naive_evaluation=""
    parallel_rule_n="8"
    parallel_primitive_n="8"
    measure_token_cost=""
    benchmark_file="$parent_path/data/juliet/aptdata/benchmark.json"  # Default value
    source_file=""
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
            --benchmark-file) benchmark_file="$2"; shift ;;
            --source-file) source_file="$2"; shift ;;
            --inference-model) inference_model="$2"; shift ;;
            --temperature) temperature="$2"; shift ;;
            --language) language="$2"; shift ;;
            --python-bin) python_bin="$2"; shift ;;
            *) echo "Unknown parameter passed: $1"; exit 1 ;;
        esac
        shift
    done
}

# Parse the command-line arguments
parse_args "$@"

# Check if benchmark file is provided
if [[ -z $benchmark_file ]]; then
    echo "Benchmark file not provided. Use --benchmark-file to specify the JSON file."
    exit 1
fi

if [[ ! -x $python_bin ]]; then
    echo "python-bin not found or not executable: $python_bin"
    exit 1
fi

if [[ -n $source_file ]]; then
    echo "source-file mode: ignoring benchmark-file ($benchmark_file)"
fi

# Construct the log path based on the options
log_path="$parent_path/log/juliet/apt_detector"
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
    # Read the benchmark file and store the file names in an array
    benchmark_cases=($(jq -r '.[]' "$benchmark_file"))

    # Collect all the java files in the parent_path/data/aptdata directory and save their absolute paths in a list
    mapfile -t java_files < <(find "$parent_path/data/juliet/aptdata" -type f -name "*.java")
fi

# run_analysis_python_file is parent_path/src/nesa/run.py
run_analysis_python_file="$parent_path/src/nesa/run.py"

counter=0

# Iterate over the list of java files and run the run.py script on each file
for java_file in "${java_files[@]}"
do

    # Increment the counter
    ((counter++))

    # extract the substring between the last / and .java to get the name of the file
    file_name="${java_file##*/}"
    file_name="${file_name%.*}"
    log_file="$log_path/$file_name.txt"

    # Check if file_name is in benchmark_cases
    if [[ -z $source_file && ! " ${benchmark_cases[@]} " =~ " ${file_name} " ]]; then
        continue
    fi

    # if log file already exists, skip the analysis
    if [ -f $log_file ]; then
        continue
    fi

    # # if log file already exists, delete it
    # if [ -f $log_file ]; then
    #     rm $log_file
    # fi

    echo "Running analysis on $java_file"   
    echo "Dump the log to $log_file"

    cmd="time $python_bin $run_analysis_python_file --analyzer-file $parent_path/src/nesa/analysis/aptdetect.dl --source-file $java_file --inference-model $inference_model --temperature $temperature --language $language"
    [[ -n $eval_rule_mode ]] && cmd="$cmd --eval-rule-mode $eval_rule_mode"
    [[ -n $semi_naive_evaluation ]] && cmd="$cmd -semi-naive-evaluation"
    cmd="$cmd --parallel-rule-n $parallel_rule_n --parallel-primitive-n $parallel_primitive_n"
    [[ -n $measure_token_cost ]] && cmd="$cmd -measure-token-cost"
    cmd="$cmd | tee $log_file"
    
    eval $cmd

    sleep 3
    
done
