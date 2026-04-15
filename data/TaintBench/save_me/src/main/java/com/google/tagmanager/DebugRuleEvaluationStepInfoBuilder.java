package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.RuleEvaluationStepInfo;
import com.google.tagmanager.ResourceUtil.ExpandedFunctionCall;
import java.util.Set;

class DebugRuleEvaluationStepInfoBuilder implements RuleEvaluationStepInfoBuilder {
    private RuleEvaluationStepInfo ruleEvaluationStepInfo;

    public DebugRuleEvaluationStepInfoBuilder(RuleEvaluationStepInfo ruleEvaluationStepInfo) {
        this.ruleEvaluationStepInfo = ruleEvaluationStepInfo;
    }

    public void setEnabledFunctions(Set<ExpandedFunctionCall> enabledFunctions) {
        for (ExpandedFunctionCall enabledFunction : enabledFunctions) {
            this.ruleEvaluationStepInfo.addEnabledFunctions(DebugResolvedRuleBuilder.translateExpandedFunctionCall(enabledFunction));
        }
    }

    public ResolvedRuleBuilder createResolvedRuleBuilder() {
        return new DebugResolvedRuleBuilder(this.ruleEvaluationStepInfo.addRules());
    }
}
