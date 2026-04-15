package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract class FunctionCallImplementation {
    private static final String FUNCTION_KEY = "function";
    private final String mFunctionId;
    private final Set<String> mRequiredKeys;

    public abstract Value evaluate(Map<String, Value> map);

    public abstract boolean isCacheable();

    public static String getFunctionKey() {
        return FUNCTION_KEY;
    }

    public String getInstanceFunctionId() {
        return this.mFunctionId;
    }

    public FunctionCallImplementation(String functionId, String... requiredKeys) {
        this.mFunctionId = functionId;
        this.mRequiredKeys = new HashSet(requiredKeys.length);
        for (String requiredKey : requiredKeys) {
            this.mRequiredKeys.add(requiredKey);
        }
    }

    public Set<String> getRequiredKeys() {
        return this.mRequiredKeys;
    }

    /* access modifiers changed from: 0000 */
    public boolean hasRequiredKeys(Set<String> keys) {
        return keys.containsAll(this.mRequiredKeys);
    }
}
