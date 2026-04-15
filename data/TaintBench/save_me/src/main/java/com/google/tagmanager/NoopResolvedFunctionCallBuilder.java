package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;

class NoopResolvedFunctionCallBuilder implements ResolvedFunctionCallBuilder {
    NoopResolvedFunctionCallBuilder() {
    }

    public ResolvedPropertyBuilder createResolvedPropertyBuilder(String key) {
        return new NoopResolvedPropertyBuilder();
    }

    public void setFunctionResult(Value functionResult) {
    }
}
