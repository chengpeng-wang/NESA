package com.google.gson.internal.bind;

import com.google.gson.ExclusionStrategy;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public final class ExcludedTypeAdapterFactory implements Factory {
    private final ExclusionStrategy deserializationExclusionStrategy;
    private final ExclusionStrategy serializationExclusionStrategy;

    public ExcludedTypeAdapterFactory(ExclusionStrategy serializationExclusionStrategy, ExclusionStrategy deserializationExclusionStrategy) {
        this.serializationExclusionStrategy = serializationExclusionStrategy;
        this.deserializationExclusionStrategy = deserializationExclusionStrategy;
    }

    public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> type) {
        Class<?> rawType = type.getRawType();
        final boolean skipSerialize = this.serializationExclusionStrategy.shouldSkipClass(rawType);
        final boolean skipDeserialize = this.deserializationExclusionStrategy.shouldSkipClass(rawType);
        if (!skipSerialize && !skipDeserialize) {
            return null;
        }
        final MiniGson miniGson = context;
        final TypeToken<T> typeToken = type;
        return new TypeAdapter<T>() {
            private TypeAdapter<T> delegate;

            public T read(JsonReader reader) throws IOException {
                if (!skipDeserialize) {
                    return delegate().read(reader);
                }
                reader.skipValue();
                return null;
            }

            public void write(JsonWriter writer, T value) throws IOException {
                if (skipSerialize) {
                    writer.nullValue();
                } else {
                    delegate().write(writer, (Object) value);
                }
            }

            private TypeAdapter<T> delegate() {
                TypeAdapter<T> d = this.delegate;
                if (d != null) {
                    return d;
                }
                d = miniGson.getNextAdapter(ExcludedTypeAdapterFactory.this, typeToken);
                this.delegate = d;
                return d;
            }
        };
    }
}
