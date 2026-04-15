package com.google.tagmanager;

import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.util.Map;

abstract class Predicate extends FunctionCallImplementation {
    private static final String ARG0 = Key.ARG0.toString();
    private static final String ARG1 = Key.ARG1.toString();

    public abstract boolean evaluateNoDefaultValues(Value value, Value value2, Map<String, Value> map);

    public static String getArg0Key() {
        return ARG0;
    }

    public static String getArg1Key() {
        return ARG1;
    }

    public Predicate(String functionId) {
        super(functionId, ARG0, ARG1);
    }

    public Value evaluate(Map<String, Value> parameters) {
        boolean result = false;
        for (Value v : parameters.values()) {
            if (v == Types.getDefaultValue()) {
                return Types.objectToValue(Boolean.valueOf(false));
            }
        }
        Value arg0 = (Value) parameters.get(ARG0);
        Value arg1 = (Value) parameters.get(ARG1);
        if (!(arg0 == null || arg1 == null)) {
            result = evaluateNoDefaultValues(arg0, arg1, parameters);
        }
        return Types.objectToValue(Boolean.valueOf(result));
    }

    public boolean isCacheable() {
        return true;
    }
}
