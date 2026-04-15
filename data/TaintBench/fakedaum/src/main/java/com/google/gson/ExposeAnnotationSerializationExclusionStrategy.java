package com.google.gson;

import com.google.gson.annotations.Expose;

final class ExposeAnnotationSerializationExclusionStrategy implements ExclusionStrategy {
    ExposeAnnotationSerializationExclusionStrategy() {
    }

    public boolean shouldSkipClass(Class<?> cls) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        Expose annotation = (Expose) f.getAnnotation(Expose.class);
        if (annotation != null && annotation.serialize()) {
            return false;
        }
        return true;
    }
}
