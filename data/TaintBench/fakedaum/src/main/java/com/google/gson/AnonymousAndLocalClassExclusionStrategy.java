package com.google.gson;

final class AnonymousAndLocalClassExclusionStrategy implements ExclusionStrategy {
    AnonymousAndLocalClassExclusionStrategy() {
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return isAnonymousOrLocal(f.getDeclaredClass());
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return isAnonymousOrLocal(clazz);
    }

    private boolean isAnonymousOrLocal(Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }
}
