package com.google.gson.internal.bind;

import com.google.gson.internal.C$Gson$Types;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class ArrayTypeAdapter<E> extends TypeAdapter<Object> {
    public static final Factory FACTORY = new Factory() {
        public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
            Type type = typeToken.getType();
            if (!(type instanceof GenericArrayType) && (!(type instanceof Class) || !((Class) type).isArray())) {
                return null;
            }
            Type componentType = C$Gson$Types.getArrayComponentType(type);
            return new ArrayTypeAdapter(context, context.getAdapter(TypeToken.get(componentType)), C$Gson$Types.getRawType(componentType));
        }
    };
    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;

    public ArrayTypeAdapter(MiniGson context, TypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper(context, componentTypeAdapter, componentType);
        this.componentType = componentType;
    }

    public Object read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        List<E> list = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            list.add(this.componentTypeAdapter.read(reader));
        }
        reader.endArray();
        Object array = Array.newInstance(this.componentType, list.size());
        for (int i = 0; i < list.size(); i++) {
            Array.set(array, i, list.get(i));
        }
        return array;
    }

    public void write(JsonWriter writer, Object array) throws IOException {
        if (array == null) {
            writer.nullValue();
            return;
        }
        writer.beginArray();
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            this.componentTypeAdapter.write(writer, Array.get(array, i));
        }
        writer.endArray();
    }
}
