package com.google.gson.internal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ParameterizedTypeHandlerMap<T> {
    private static final Logger logger = Logger.getLogger(ParameterizedTypeHandlerMap.class.getName());
    private boolean modifiable = true;
    private final Map<Type, T> systemMap = new HashMap();
    private final List<Pair<Class<?>, T>> systemTypeHierarchyList = new ArrayList();
    private final Map<Type, T> userMap = new HashMap();
    private final List<Pair<Class<?>, T>> userTypeHierarchyList = new ArrayList();

    public synchronized void registerForTypeHierarchy(Class<?> typeOfT, T value, boolean isSystem) {
        registerForTypeHierarchy(new Pair(typeOfT, value), isSystem);
    }

    public synchronized void registerForTypeHierarchy(Pair<Class<?>, T> pair, boolean isSystem) {
        if (this.modifiable) {
            List<Pair<Class<?>, T>> typeHierarchyList = isSystem ? this.systemTypeHierarchyList : this.userTypeHierarchyList;
            int index = getIndexOfSpecificHandlerForTypeHierarchy((Class) pair.first, typeHierarchyList);
            if (index >= 0) {
                logger.log(Level.WARNING, "Overriding the existing type handler for {0}", pair.first);
                typeHierarchyList.remove(index);
            }
            index = getIndexOfAnOverriddenHandler((Class) pair.first, typeHierarchyList);
            if (index >= 0) {
                throw new IllegalArgumentException("The specified type handler for type " + pair.first + " hides the previously registered type hierarchy handler for " + ((Pair) typeHierarchyList.get(index)).first + ". Gson does not allow this.");
            }
            typeHierarchyList.add(0, pair);
        } else {
            throw new IllegalStateException("Attempted to modify an unmodifiable map.");
        }
    }

    private static <T> int getIndexOfAnOverriddenHandler(Class<?> type, List<Pair<Class<?>, T>> typeHierarchyList) {
        for (int i = typeHierarchyList.size() - 1; i >= 0; i--) {
            if (type.isAssignableFrom((Class) ((Pair) typeHierarchyList.get(i)).first)) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void register(Type typeOfT, T value, boolean isSystem) {
        if (this.modifiable) {
            if (hasSpecificHandlerFor(typeOfT)) {
                logger.log(Level.WARNING, "Overriding the existing type handler for {0}", typeOfT);
            }
            (isSystem ? this.systemMap : this.userMap).put(typeOfT, value);
        } else {
            throw new IllegalStateException("Attempted to modify an unmodifiable map.");
        }
    }

    public synchronized void registerIfAbsent(ParameterizedTypeHandlerMap<T> other) {
        if (this.modifiable) {
            int i;
            Pair<Class<?>, T> entry;
            for (Entry<Type, T> entry2 : other.userMap.entrySet()) {
                if (!this.userMap.containsKey(entry2.getKey())) {
                    register((Type) entry2.getKey(), entry2.getValue(), false);
                }
            }
            for (Entry<Type, T> entry22 : other.systemMap.entrySet()) {
                if (!this.systemMap.containsKey(entry22.getKey())) {
                    register((Type) entry22.getKey(), entry22.getValue(), true);
                }
            }
            for (i = other.userTypeHierarchyList.size() - 1; i >= 0; i--) {
                entry = (Pair) other.userTypeHierarchyList.get(i);
                if (getIndexOfSpecificHandlerForTypeHierarchy((Class) entry.first, this.userTypeHierarchyList) < 0) {
                    registerForTypeHierarchy(entry, false);
                }
            }
            for (i = other.systemTypeHierarchyList.size() - 1; i >= 0; i--) {
                entry = (Pair) other.systemTypeHierarchyList.get(i);
                if (getIndexOfSpecificHandlerForTypeHierarchy((Class) entry.first, this.systemTypeHierarchyList) < 0) {
                    registerForTypeHierarchy(entry, true);
                }
            }
        } else {
            throw new IllegalStateException("Attempted to modify an unmodifiable map.");
        }
    }

    public synchronized ParameterizedTypeHandlerMap<T> makeUnmodifiable() {
        this.modifiable = false;
        return this;
    }

    public synchronized T getHandlerFor(Type type, boolean systemOnly) {
        T handler;
        T handler2;
        if (!systemOnly) {
            handler2 = this.userMap.get(type);
            if (handler2 != null) {
                handler = handler2;
            }
        }
        handler2 = this.systemMap.get(type);
        if (handler2 != null) {
            handler = handler2;
        } else {
            Type rawClass = C$Gson$Types.getRawType(type);
            if (rawClass != type) {
                handler2 = getHandlerFor(rawClass, systemOnly);
                if (handler2 != null) {
                    handler = handler2;
                }
            }
            handler = getHandlerForTypeHierarchy(rawClass, systemOnly);
        }
        return handler;
    }

    private T getHandlerForTypeHierarchy(Class<?> type, boolean systemOnly) {
        if (!systemOnly) {
            for (Pair<Class<?>, T> entry : this.userTypeHierarchyList) {
                if (((Class) entry.first).isAssignableFrom(type)) {
                    return entry.second;
                }
            }
        }
        for (Pair<Class<?>, T> entry2 : this.systemTypeHierarchyList) {
            if (((Class) entry2.first).isAssignableFrom(type)) {
                return entry2.second;
            }
        }
        return null;
    }

    public synchronized boolean hasSpecificHandlerFor(Type type) {
        boolean z;
        z = this.userMap.containsKey(type) || this.systemMap.containsKey(type);
        return z;
    }

    private static <T> int getIndexOfSpecificHandlerForTypeHierarchy(Class<?> type, List<Pair<Class<?>, T>> typeHierarchyList) {
        for (int i = typeHierarchyList.size() - 1; i >= 0; i--) {
            if (type.equals(((Pair) typeHierarchyList.get(i)).first)) {
                return i;
            }
        }
        return -1;
    }

    public synchronized ParameterizedTypeHandlerMap<T> copyOf() {
        ParameterizedTypeHandlerMap<T> copy;
        copy = new ParameterizedTypeHandlerMap();
        copy.systemMap.putAll(this.systemMap);
        copy.userMap.putAll(this.userMap);
        copy.systemTypeHierarchyList.addAll(this.systemTypeHierarchyList);
        copy.userTypeHierarchyList.addAll(this.userTypeHierarchyList);
        return copy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{userTypeHierarchyList:{");
        appendList(sb, this.userTypeHierarchyList);
        sb.append("},systemTypeHierarchyList:{");
        appendList(sb, this.systemTypeHierarchyList);
        sb.append("},userMap:{");
        appendMap(sb, this.userMap);
        sb.append("},systemMap:{");
        appendMap(sb, this.systemMap);
        sb.append("}");
        return sb.toString();
    }

    private void appendList(StringBuilder sb, List<Pair<Class<?>, T>> list) {
        boolean first = true;
        for (Pair<Class<?>, T> entry : list) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(typeToString((Type) entry.first)).append(':');
            sb.append(entry.second);
        }
    }

    private void appendMap(StringBuilder sb, Map<Type, T> map) {
        boolean first = true;
        for (Entry<Type, T> entry : map.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(typeToString((Type) entry.getKey())).append(':');
            sb.append(entry.getValue());
        }
    }

    private String typeToString(Type type) {
        return C$Gson$Types.getRawType(type).getSimpleName();
    }
}
