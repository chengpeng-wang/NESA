package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.ResolvedFunctionCall;
import com.google.analytics.containertag.proto.MutableDebug.ResolvedProperty;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;

class DebugResolvedFunctionCallBuilder implements ResolvedFunctionCallBuilder {
    private ResolvedFunctionCall resolvedFunctionCall;

    public DebugResolvedFunctionCallBuilder(ResolvedFunctionCall functionCall) {
        this.resolvedFunctionCall = functionCall;
    }

    public ResolvedPropertyBuilder createResolvedPropertyBuilder(String key) {
        ResolvedProperty newProperty = this.resolvedFunctionCall.addProperties();
        newProperty.setKey(key);
        return new DebugResolvedPropertyBuilder(newProperty);
    }

    public void setFunctionResult(Value functionResult) {
        this.resolvedFunctionCall.setResult(DebugValueBuilder.copyImmutableValue(functionResult));
    }
}
