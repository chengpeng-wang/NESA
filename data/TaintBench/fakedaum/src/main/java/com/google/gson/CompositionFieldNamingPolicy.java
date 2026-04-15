package com.google.gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

abstract class CompositionFieldNamingPolicy extends RecursiveFieldNamingPolicy {
    private final RecursiveFieldNamingPolicy[] fieldPolicies;

    public CompositionFieldNamingPolicy(RecursiveFieldNamingPolicy... fieldNamingPolicies) {
        if (fieldNamingPolicies == null) {
            throw new NullPointerException("naming policies can not be null.");
        }
        this.fieldPolicies = fieldNamingPolicies;
    }

    /* access modifiers changed from: protected */
    public String translateName(String target, Type fieldType, Collection<Annotation> annotations) {
        for (RecursiveFieldNamingPolicy policy : this.fieldPolicies) {
            target = policy.translateName(target, fieldType, annotations);
        }
        return target;
    }
}
