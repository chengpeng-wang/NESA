package com.google.gson.internal.bind;

import com.google.gson.internal.C$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

public final class StringToValueMapTypeAdapterFactory implements Factory {
    private final ConstructorConstructor constructorConstructor;

    private final class Adapter<V> extends TypeAdapter<Map<String, V>> {
        private final ObjectConstructor<? extends Map<String, V>> constructor;
        private final TypeAdapter<V> valueTypeAdapter;

        public Adapter(TypeAdapter<V> valueTypeAdapter, ObjectConstructor<? extends Map<String, V>> constructor) {
            this.valueTypeAdapter = valueTypeAdapter;
            this.constructor = constructor;
        }

        public Map<String, V> read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            Map<String, V> map = (Map) this.constructor.construct();
            reader.beginObject();
            while (reader.hasNext()) {
                map.put(reader.nextName(), this.valueTypeAdapter.read(reader));
            }
            reader.endObject();
            return map;
        }

        public void write(JsonWriter writer, Map<String, V> map) throws IOException {
            if (map == null) {
                writer.nullValue();
                return;
            }
            writer.beginObject();
            for (Entry<String, V> entry : map.entrySet()) {
                writer.name((String) entry.getKey());
                this.valueTypeAdapter.write(writer, entry.getValue());
            }
            writer.endObject();
        }
    }

    public StringToValueMapTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        Class<? super T> rawType = typeToken.getRawType();
        if (!Map.class.isAssignableFrom(rawType)) {
            return null;
        }
        Type[] keyAndValueTypes = C$Gson$Types.getMapKeyAndValueTypes(type, rawType);
        if (keyAndValueTypes[0] == String.class) {
            return new Adapter(context.getAdapter(TypeToken.get(keyAndValueTypes[1])), this.constructorConstructor.getConstructor(typeToken));
        }
        return null;
    }
}
