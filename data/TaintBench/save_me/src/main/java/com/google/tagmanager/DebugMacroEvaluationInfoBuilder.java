package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.MacroEvaluationInfo;

class DebugMacroEvaluationInfoBuilder implements MacroEvaluationInfoBuilder {
    private MacroEvaluationInfo macroEvaluationInfo;

    public DebugMacroEvaluationInfoBuilder(MacroEvaluationInfo macroEvaluationInfo) {
        this.macroEvaluationInfo = macroEvaluationInfo;
    }

    public ResolvedFunctionCallBuilder createResult() {
        return new DebugResolvedFunctionCallBuilder(this.macroEvaluationInfo.getMutableResult());
    }

    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new DebugRuleEvaluationStepInfoBuilder(this.macroEvaluationInfo.getMutableRulesEvaluation());
    }
}
