package com.google.gson.internal.bind;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

final class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T> {
    private final MiniGson context;
    private final TypeAdapter<T> delegate;
    private final Type type;

    TypeAdapterRuntimeTypeWrapper(MiniGson context, TypeAdapter<T> delegate, Type type) {
        this.context = context;
        this.delegate = delegate;
        this.type = type;
    }

    public T read(JsonReader reader) throws IOException {
        return this.delegate.read(reader);
    }

    public void write(JsonWriter writer, T value) throws IOException {
        TypeAdapter chosen = this.delegate;
        Type runtimeType = Reflection.getRuntimeTypeIfMoreSpecific(this.type, value);
        if (runtimeType != this.type) {
            TypeAdapter runtimeTypeAdapter = this.context.getAdapter(TypeToken.get(runtimeType));
            if (!(runtimeTypeAdapter instanceof Adapter)) {
                chosen = runtimeTypeAdapter;
            } else if (this.delegate instanceof Adapter) {
                chosen = runtimeTypeAdapter;
            } else {
                chosen = this.delegate;
            }
        }
        chosen.write(writer, (Object) value);
    }
}
