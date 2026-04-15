package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.ResolvedFunctionCall;
import com.google.analytics.containertag.proto.MutableDebug.ResolvedProperty;
import com.google.analytics.containertag.proto.MutableDebug.ResolvedRule;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.ResourceUtil.ExpandedFunctionCall;
import java.util.List;
import java.util.Map.Entry;

class DebugResolvedRuleBuilder implements ResolvedRuleBuilder {
    ResolvedFunctionCallTranslatorList addMacrosHolder = new DebugResolvedFunctionCallListTranslator(this.resolvedRule.getMutableAddMacrosList());
    ResolvedFunctionCallTranslatorList addTagsHolder = new DebugResolvedFunctionCallListTranslator(this.resolvedRule.getMutableAddTagsList());
    ResolvedFunctionCallTranslatorList removeMacrosHolder = new DebugResolvedFunctionCallListTranslator(this.resolvedRule.getMutableRemoveMacrosList());
    ResolvedFunctionCallTranslatorList removeTagsHolder = new DebugResolvedFunctionCallListTranslator(this.resolvedRule.getMutableRemoveTagsList());
    ResolvedRule resolvedRule;

    class DebugResolvedFunctionCallListTranslator implements ResolvedFunctionCallTranslatorList {
        @VisibleForTesting
        List<ResolvedFunctionCall> toBuild;

        DebugResolvedFunctionCallListTranslator(List<ResolvedFunctionCall> result) {
            this.toBuild = result;
        }

        public void translateAndAddAll(List<ExpandedFunctionCall> functions, List<String> ruleNames) {
            for (int i = 0; i < functions.size(); i++) {
                this.toBuild.add(DebugResolvedRuleBuilder.translateExpandedFunctionCall((ExpandedFunctionCall) functions.get(i)));
                if (i < ruleNames.size()) {
                    ((ResolvedFunctionCall) this.toBuild.get(i)).setAssociatedRuleName((String) ruleNames.get(i));
                } else {
                    ((ResolvedFunctionCall) this.toBuild.get(i)).setAssociatedRuleName("Unknown");
                }
            }
        }
    }

    public DebugResolvedRuleBuilder(ResolvedRule rule) {
        this.resolvedRule = rule;
    }

    public ResolvedFunctionCallBuilder createNegativePredicate() {
        return new DebugResolvedFunctionCallBuilder(this.resolvedRule.addNegativePredicates());
    }

    public ResolvedFunctionCallBuilder createPositivePredicate() {
        return new DebugResolvedFunctionCallBuilder(this.resolvedRule.addPositivePredicates());
    }

    public ResolvedFunctionCallTranslatorList getAddedMacroFunctions() {
        return this.addMacrosHolder;
    }

    public ResolvedFunctionCallTranslatorList getRemovedMacroFunctions() {
        return this.removeMacrosHolder;
    }

    public ResolvedFunctionCallTranslatorList getAddedTagFunctions() {
        return this.addTagsHolder;
    }

    public ResolvedFunctionCallTranslatorList getRemovedTagFunctions() {
        return this.removeTagsHolder;
    }

    public void setValue(Value result) {
        this.resolvedRule.setResult(DebugValueBuilder.copyImmutableValue(result));
    }

    public static ResolvedFunctionCall translateExpandedFunctionCall(ExpandedFunctionCall f) {
        ResolvedFunctionCall result = ResolvedFunctionCall.newMessage();
        for (Entry<String, Value> originalParam : f.getProperties().entrySet()) {
            ResolvedProperty prop = ResolvedProperty.newMessage();
            prop.setKey((String) originalParam.getKey());
            prop.setValue(DebugValueBuilder.copyImmutableValue((Value) originalParam.getValue()));
            result.addProperties(prop);
        }
        return result;
    }
}
