package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.util.Map;

abstract class NumberPredicate extends Predicate {
    public abstract boolean evaluateNumber(TypedNumber typedNumber, TypedNumber typedNumber2, Map<String, Value> map);

    public NumberPredicate(String functionId) {
        super(functionId);
    }

    /* access modifiers changed from: protected */
    public boolean evaluateNoDefaultValues(Value arg0, Value arg1, Map<String, Value> parameters) {
        TypedNumber numberArg0 = Types.valueToNumber(arg0);
        TypedNumber numberArg1 = Types.valueToNumber(arg1);
        return (numberArg0 == Types.getDefaultNumber() || numberArg1 == Types.getDefaultNumber()) ? false : evaluateNumber(numberArg0, numberArg1, parameters);
    }
}
