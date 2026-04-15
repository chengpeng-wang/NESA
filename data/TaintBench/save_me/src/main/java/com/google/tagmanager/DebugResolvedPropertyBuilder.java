package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.ResolvedProperty;
import com.google.analytics.midtier.proto.containertag.MutableTypeSystem;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;

class DebugResolvedPropertyBuilder implements ResolvedPropertyBuilder {
    private ResolvedProperty resolvedProperty;

    public DebugResolvedPropertyBuilder(ResolvedProperty resolvedProperty) {
        this.resolvedProperty = resolvedProperty;
    }

    public ValueBuilder createPropertyValueBuilder(Value propertyValue) {
        MutableTypeSystem.Value val = DebugValueBuilder.copyImmutableValue(propertyValue);
        this.resolvedProperty.setValue(val);
        return new DebugValueBuilder(val);
    }
}
