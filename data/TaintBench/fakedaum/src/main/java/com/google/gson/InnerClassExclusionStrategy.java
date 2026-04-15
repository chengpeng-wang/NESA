package com.google.gson;

final class InnerClassExclusionStrategy implements ExclusionStrategy {
    InnerClassExclusionStrategy() {
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return isInnerClass(f.getDeclaredClass());
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return isInnerClass(clazz);
    }

    private boolean isInnerClass(Class<?> clazz) {
        return clazz.isMemberClass() && !isStatic(clazz);
    }

    private boolean isStatic(Class<?> clazz) {
        return (clazz.getModifiers() & 8) != 0;
    }
}
