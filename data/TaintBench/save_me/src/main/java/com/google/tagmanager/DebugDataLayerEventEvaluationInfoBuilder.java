package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.DataLayerEventEvaluationInfo;

class DebugDataLayerEventEvaluationInfoBuilder implements DataLayerEventEvaluationInfoBuilder {
    private DataLayerEventEvaluationInfo dataLayerEvent;

    public DebugDataLayerEventEvaluationInfoBuilder(DataLayerEventEvaluationInfo dataLayerEvent) {
        this.dataLayerEvent = dataLayerEvent;
    }

    public ResolvedFunctionCallBuilder createAndAddResult() {
        return new DebugResolvedFunctionCallBuilder(this.dataLayerEvent.addResults());
    }

    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new DebugRuleEvaluationStepInfoBuilder(this.dataLayerEvent.getMutableRulesEvaluation());
    }
}
