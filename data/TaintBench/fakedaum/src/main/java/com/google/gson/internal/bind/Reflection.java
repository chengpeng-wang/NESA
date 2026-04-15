package com.google.gson.internal.bind;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

final class Reflection {
    Reflection() {
    }

    public static Type getRuntimeTypeIfMoreSpecific(Type type, Object value) {
        if (value == null) {
            return type;
        }
        if (type == Object.class || (type instanceof TypeVariable) || (type instanceof Class)) {
            return value.getClass();
        }
        return type;
    }
}
