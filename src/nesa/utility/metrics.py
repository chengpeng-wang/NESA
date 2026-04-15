import os
import json
from typing import Any, Dict


def print_precision_recall(
    ground_truth_slices: Dict[str, Any], predict_slices: Dict[str, Any]
) -> Dict[str, Any]:
    precision = 0
    recall = 0
    count = 0
    statistics = {}
    for slice_key in ground_truth_slices:
        slice_item = ground_truth_slices[slice_key]
        truth = slice_item[slice_type]
        if 1 in truth:
            truth.remove(1)
        if slice_key not in predict_slices:
            continue
        count += 1
        pred = predict_slices[slice_key][slice_type]

        print("Truth: ", truth)
        print("Pred: ", pred)
        single_precision = (
            len(set(truth).intersection(set(pred))) / len(pred) if len(pred) > 0 else 0
        )
        single_recall = (
            len(set(truth).intersection(set(pred))) / len(truth)
            if len(truth) > 0
            else 0
        )
        statistics[slice_key] = {"precision": single_precision, "recall": single_recall}
        precision += single_precision
        recall += single_recall

    precision /= count
    recall /= count
    print("precision: ", precision)
    print("recall: ", recall)
    return statistics
