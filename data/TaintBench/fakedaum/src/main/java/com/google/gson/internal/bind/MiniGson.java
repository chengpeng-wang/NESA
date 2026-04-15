package com.google.gson.internal.bind;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MiniGson {
    private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls;
    private final List<Factory> factories;

    public static final class Builder {
        boolean addDefaultFactories = true;
        /* access modifiers changed from: private|final */
        public final List<Factory> factories = new ArrayList();

        public Builder factory(Factory factory) {
            this.factories.add(factory);
            return this;
        }

        public Builder withoutDefaultFactories() {
            this.addDefaultFactories = false;
            return this;
        }

        public <T> Builder typeAdapter(Class<T> type, TypeAdapter<T> typeAdapter) {
            this.factories.add(TypeAdapters.newFactory((Class) type, (TypeAdapter) typeAdapter));
            return this;
        }

        public <T> Builder typeAdapter(TypeToken<T> type, TypeAdapter<T> typeAdapter) {
            this.factories.add(TypeAdapters.newFactory((TypeToken) type, (TypeAdapter) typeAdapter));
            return this;
        }

        public <T> Builder typeHierarchyAdapter(Class<T> type, TypeAdapter<T> typeAdapter) {
            this.factories.add(TypeAdapters.newTypeHierarchyFactory(type, typeAdapter));
            return this;
        }

        public MiniGson build() {
            return new MiniGson(this, null);
        }
    }

    static class FutureTypeAdapter<T> extends TypeAdapter<T> {
        private TypeAdapter<T> delegate;

        FutureTypeAdapter() {
        }

        public void setDelegate(TypeAdapter<T> typeAdapter) {
            if (this.delegate != null) {
                throw new AssertionError();
            }
            this.delegate = typeAdapter;
        }

        public T read(JsonReader reader) throws IOException {
            if (this.delegate != null) {
                return this.delegate.read(reader);
            }
            throw new IllegalStateException();
        }

        public void write(JsonWriter writer, T value) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            this.delegate.write(writer, (Object) value);
        }
    }

    /* synthetic */ MiniGson(Builder x0, AnonymousClass1 x1) {
        this(x0);
    }

    private MiniGson(Builder builder) {
        this.calls = new ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>>() {
            /* access modifiers changed from: protected */
            public Map<TypeToken<?>, FutureTypeAdapter<?>> initialValue() {
                return new HashMap();
            }
        };
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        List<Factory> factories = new ArrayList();
        if (builder.addDefaultFactories) {
            factories.add(TypeAdapters.BOOLEAN_FACTORY);
            factories.add(TypeAdapters.INTEGER_FACTORY);
            factories.add(TypeAdapters.DOUBLE_FACTORY);
            factories.add(TypeAdapters.FLOAT_FACTORY);
            factories.add(TypeAdapters.LONG_FACTORY);
            factories.add(TypeAdapters.STRING_FACTORY);
        }
        factories.addAll(builder.factories);
        if (builder.addDefaultFactories) {
            factories.add(new CollectionTypeAdapterFactory(constructorConstructor));
            factories.add(new StringToValueMapTypeAdapterFactory(constructorConstructor));
            factories.add(ArrayTypeAdapter.FACTORY);
            factories.add(ObjectTypeAdapter.FACTORY);
            factories.add(new ReflectiveTypeAdapterFactory(constructorConstructor));
        }
        this.factories = Collections.unmodifiableList(factories);
    }

    public <T> TypeAdapter<T> getAdapter(TypeToken<T> type) {
        Map<TypeToken<?>, FutureTypeAdapter<?>> threadCalls = (Map) this.calls.get();
        FutureTypeAdapter<T> ongoingCall = (FutureTypeAdapter) threadCalls.get(type);
        if (ongoingCall != null) {
            return ongoingCall;
        }
        FutureTypeAdapter<T> call = new FutureTypeAdapter();
        threadCalls.put(type, call);
        try {
            for (Factory factory : this.factories) {
                TypeAdapter<T> candidate = factory.create(this, type);
                if (candidate != null) {
                    call.setDelegate(candidate);
                    return candidate;
                }
            }
            throw new IllegalArgumentException("This MiniGSON cannot handle " + type);
        } finally {
            threadCalls.remove(type);
        }
    }

    public <T> TypeAdapter<T> getNextAdapter(Factory skipPast, TypeToken<T> type) {
        boolean skipPastFound = false;
        for (Factory factory : this.factories) {
            if (skipPastFound) {
                TypeAdapter<T> candidate = factory.create(this, type);
                if (candidate != null) {
                    return candidate;
                }
            } else if (factory == skipPast) {
                skipPastFound = true;
            }
        }
        throw new IllegalArgumentException("This MiniGSON cannot serialize " + type);
    }

    public <T> TypeAdapter<T> getAdapter(Class<T> type) {
        return getAdapter(TypeToken.get((Class) type));
    }

    public List<Factory> getFactories() {
        return this.factories;
    }
}
