package com.google.gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

abstract class RecursiveFieldNamingPolicy implements FieldNamingStrategy2 {
    public abstract String translateName(String str, Type type, Collection<Annotation> collection);

    RecursiveFieldNamingPolicy() {
    }

    public final String translateName(FieldAttributes f) {
        return translateName(f.getName(), f.getDeclaredType(), f.getAnnotations());
    }
}
