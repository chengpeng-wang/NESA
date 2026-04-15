package com.google.gson;

import com.google.gson.annotations.Expose;

final class ExposeAnnotationDeserializationExclusionStrategy implements ExclusionStrategy {
    ExposeAnnotationDeserializationExclusionStrategy() {
    }

    public boolean shouldSkipClass(Class<?> cls) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        Expose annotation = (Expose) f.getAnnotation(Expose.class);
        if (annotation != null && annotation.deserialize()) {
            return false;
        }
        return true;
    }
}
