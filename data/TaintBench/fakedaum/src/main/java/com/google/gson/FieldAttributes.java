package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import com.google.gson.internal.Pair;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class FieldAttributes {
    private static final Cache<Pair<Class<?>, String>, Collection<Annotation>> ANNOTATION_CACHE = new LruCache(getMaxCacheSize());
    private static final String MAX_CACHE_PROPERTY_NAME = "com.google.gson.annotation_cache_size_hint";
    private Collection<Annotation> annotations;
    private final Class<?> declaredType;
    private final Class<?> declaringClazz;
    private final Field field;
    private Type genericType;
    private final boolean isSynthetic;
    private final int modifiers;
    private final String name;

    FieldAttributes(Class<?> declaringClazz, Field f) {
        this.declaringClazz = (Class) C$Gson$Preconditions.checkNotNull(declaringClazz);
        this.name = f.getName();
        this.declaredType = f.getType();
        this.isSynthetic = f.isSynthetic();
        this.modifiers = f.getModifiers();
        this.field = f;
    }

    private static int getMaxCacheSize() {
        int i = 2000;
        try {
            return Integer.parseInt(System.getProperty(MAX_CACHE_PROPERTY_NAME, String.valueOf(2000)));
        } catch (NumberFormatException e) {
            return i;
        }
    }

    public Class<?> getDeclaringClass() {
        return this.declaringClazz;
    }

    public String getName() {
        return this.name;
    }

    public Type getDeclaredType() {
        if (this.genericType == null) {
            this.genericType = this.field.getGenericType();
        }
        return this.genericType;
    }

    public Class<?> getDeclaredClass() {
        return this.declaredType;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return getAnnotationFromArray(getAnnotations(), annotation);
    }

    public Collection<Annotation> getAnnotations() {
        if (this.annotations == null) {
            Pair<Class<?>, String> key = new Pair(this.declaringClazz, this.name);
            Collection<Annotation> cachedValue = (Collection) ANNOTATION_CACHE.getElement(key);
            if (cachedValue == null) {
                cachedValue = Collections.unmodifiableCollection(Arrays.asList(this.field.getAnnotations()));
                ANNOTATION_CACHE.addElement(key, cachedValue);
            }
            this.annotations = cachedValue;
        }
        return this.annotations;
    }

    public boolean hasModifier(int modifier) {
        return (this.modifiers & modifier) != 0;
    }

    /* access modifiers changed from: 0000 */
    public Object get(Object instance) throws IllegalAccessException {
        return this.field.get(instance);
    }

    /* access modifiers changed from: 0000 */
    public boolean isSynthetic() {
        return this.isSynthetic;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public Field getFieldObject() {
        return this.field;
    }

    private static <T extends Annotation> T getAnnotationFromArray(Collection<Annotation> annotations, Class<T> annotation) {
        for (Annotation a : annotations) {
            if (a.annotationType() == annotation) {
                return a;
            }
        }
        return null;
    }
}
