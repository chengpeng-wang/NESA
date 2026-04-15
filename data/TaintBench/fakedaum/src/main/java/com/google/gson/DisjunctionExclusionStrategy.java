package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.util.Collection;

final class DisjunctionExclusionStrategy implements ExclusionStrategy {
    private final Collection<ExclusionStrategy> strategies;

    DisjunctionExclusionStrategy(Collection<ExclusionStrategy> strategies) {
        this.strategies = (Collection) C$Gson$Preconditions.checkNotNull(strategies);
    }

    public boolean shouldSkipField(FieldAttributes f) {
        for (ExclusionStrategy strategy : this.strategies) {
            if (strategy.shouldSkipField(f)) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        for (ExclusionStrategy strategy : this.strategies) {
            if (strategy.shouldSkipClass(clazz)) {
                return true;
            }
        }
        return false;
    }
}
