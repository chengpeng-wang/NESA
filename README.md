# NESA: Relational Neuro-Symbolic Static Program Analysis

## Table of Contents

- [Overview](README.md#overview)
- [Prerequisite](README.md#prerequisite)
- [Core NESA Client](README.md#core-nesa-client)
- [How to Run NESA](README.md#how-to-run-nesa)
- [How to Run Baselines](README.md#how-to-run-baselines)
- [Troubleshooting](README.md#troubleshooting)
- [Paper](README.md#paper)
- [Contact](README.md#contact)
- [License](README.md#license)



## Overview

NESA is a static analysis framework for Java and C/C++ code. It combines lightweight symbolic analysis with LLM-backed neural primitives for slicing and bug detection tasks.

This repository supports:

- Direct runs through `src/nesa/run.py`
- Shell wrappers under `scripts/nesa/` for benchmark and project analysis
- Baseline runners under `scripts/baseline/`

All model-backed entrypoints use the same core contract:

```bash
--inference-model MODEL_ID   # optional, defaults to gpt-4o-mini
--temperature FLOAT          # optional, defaults to 0.0
```



## Prerequisite

Set up the environment:

```bash
conda create -n nesa python=3.9
conda activate nesa
pip install -r requirements.txt
python lib/build.py
chmod +x scripts/nesa/*.sh scripts/baseline/*.sh
```

Runtime notes:

- NESA requires `lib/build/my-languages.so`; `python lib/build.py` generates it.
- Install `jq` if you want to use the shell wrappers in `scripts/`.
- The wrappers under `scripts/nesa/` default to a Python binary inside a local `nesa` conda environment.
- If your environment lives elsewhere, set `NESA_PYTHON="$(which python)"` after activation, or pass `--python-bin /path/to/python3`.

Credentials:

- OpenAI models use `OPENAI_API_KEY`
- Claude models are invoked through AWS Bedrock and require working AWS credentials plus a Bedrock model ID

Example:

```bash
export OPENAI_API_KEY=your_openai_api_key_here
```



## Core NESA Client

Use `src/nesa/run.py` when you want to run the engine directly.

Single-file example:

```bash
python src/nesa/run.py \
  --source-file data/juliet/xssdata/CWE80_XSS__Servlet_getCookies_Servlet_07.java \
  --analyzer-file xssdetect.dl \
  --eval-rule-mode full-featured \
  --parallel-rule-n 8 \
  --parallel-primitive-n 8 \
  -semi-naive-evaluation \
  -measure-token-cost
```

Project example:

```bash
python src/nesa/run.py \
  --src-project data/h3 \
  --analyzer-file intraml.dl \
  --language cpp
```

Important options:

- Use either `--source-file` or `--src-project`
- `--analyzer-file` is required
- `--language` defaults to `java`; use `cpp` for C/C++
- `--seed-file` is only needed for slicing workloads
- `--parallel-rule-n` and `--parallel-primitive-n` default to `8`



## How to Run NESA

NESA wrappers live under `scripts/nesa/`.

Available detector wrappers:

- `run_xss_detector.sh`
- `run_apt_detector.sh`
- `run_dbz_detector.sh`
- `run_npd_detector.sh`
- `run_re_detector.sh`
- `run_uowh_detector.sh`
- `run_taintbench_detector.sh`
- `run_intraml_detector.sh`
- `run_backward_slicing.sh`

Common NESA workflows:

Backward slicing:

```bash
bash scripts/nesa/run_backward_slicing.sh \
  --source-file data/backwardslice/source_files/p00007_s586604643_14_n.java \
  --seed-file data/backwardslice/seed_files/p00007_s586604643_14_n.json
```

Juliet detectors:

```bash
bash scripts/nesa/run_apt_detector.sh \
  --source-file data/juliet/aptdata/CWE36_Absolute_Path_Traversal__console_readLine_45.java
```

TaintBench:

```bash
bash scripts/nesa/run_taintbench_detector.sh 112 \
  --benchmark-json data/TaintBench/benchmark.json
```

Memory leak detection on a C/C++ project:

```bash
bash scripts/nesa/run_intraml_detector.sh \
  --src-project data/h3
```

Notes:

- Juliet detector wrappers support `--benchmark-file` for batch mode and `--source-file` for single-file mode
- `run_intraml_detector.sh` accepts `--src-project` only
- Logs are written under `log/`



## How to Run Baselines

Baseline wrappers live under `scripts/baseline/`.

Available baseline runners:

- `run_fscot_slicing.sh`
- `run_fscot_detect.sh`
- `run_fscot_taint.sh`

Examples:

Backward slicing baseline:

```bash
bash scripts/baseline/run_fscot_slicing.sh \
  --source-file data/backwardslice/source_files/p00007_s586604643_14_n.java
```

Juliet bug detection baseline:

```bash
bash scripts/baseline/run_fscot_detect.sh \
  --bug-type apt \
  --source-file data/juliet/aptdata/CWE36_Absolute_Path_Traversal__console_readLine_01.java
```

TaintBench baseline:

```bash
bash scripts/baseline/run_fscot_taint.sh \
  --global-id 112
```

Notes:

- Supported Juliet `--bug-type` values are `apt`, `dbz`, `npd`, `re`, `uowh`, and `xss`
- Baseline logs are also written under `log/`



## Troubleshooting

- Model selection
  If you omit `--inference-model`, NESA and the baseline wrappers default to `gpt-4o-mini`.
- API key or provider errors
Make sure `OPENAI_API_KEY` or AWS Bedrock credentials are configured before running.
- Missing `jq`
Install `jq` before using the shell wrappers in `scripts/`.
- Missing tree-sitter library
Re-run `python lib/build.py` if `lib/build/my-languages.so` is missing.
- Existing logs cause a run to be skipped
Some runners skip files whose log already exists; delete the log or use the runner's force option if available.


## Paper 

If you find our research or tools helpful, please cite the corresponding papers from this project.

```
@article{10.1145/3808161,
author = {Wang, Chengpeng and Gao, Yifei and Zhang, Wuqi and Liu, Xuwei and Guo, Jinyao and Zheng, Mingwei and Shi, Qingkai and Zhang, Xiangyu},
title = {NESA: Relational Neuro-Symbolic Static Program Analysis},
year = {2026},
issue_date = {July 2026},
publisher = {Association for Computing Machinery},
address = {New York, NY, USA},
volume = {3},
number = {FSE},
journal = {Proc. ACM Softw. Eng.},
month = jul,
articleno = {FSE154},
doi = {10.1145/3808161}
}
```



## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).


## Contact

For any questions or suggestions, please submit issues or pull requests on GitHub. You can also reach out to the maintainer:

- Chengpeng Wang (Purdue University) - [wang6590@purdue.edu](mailto:wang6590@purdue.edu), [stephenw.wangcp@gmail.com](mailto:stephenw.wangcp@gmail.com)
