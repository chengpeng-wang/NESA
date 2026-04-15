package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.util.Map;

abstract class StringPredicate extends Predicate {
    public abstract boolean evaluateString(String str, String str2, Map<String, Value> map);

    public StringPredicate(String functionId) {
        super(functionId);
    }

    /* access modifiers changed from: protected */
    public boolean evaluateNoDefaultValues(Value arg0, Value arg1, Map<String, Value> parameters) {
        String stringArg0 = Types.valueToString(arg0);
        String stringArg1 = Types.valueToString(arg1);
        return (stringArg0 == Types.getDefaultString() || stringArg1 == Types.getDefaultString()) ? false : evaluateString(stringArg0, stringArg1, parameters);
    }
}
