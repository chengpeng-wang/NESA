package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.C$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class MapTypeAdapterFactory implements Factory {
    /* access modifiers changed from: private|final */
    public final boolean complexMapKeySerialization;
    private final ConstructorConstructor constructorConstructor;

    private final class Adapter<K, V> extends TypeAdapter<Map<K, V>> {
        private final ObjectConstructor<? extends Map<K, V>> constructor;
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;

        public Adapter(MiniGson context, Type keyType, TypeAdapter<K> keyTypeAdapter, Type valueType, TypeAdapter<V> valueTypeAdapter, ObjectConstructor<? extends Map<K, V>> constructor) {
            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper(context, keyTypeAdapter, keyType);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper(context, valueTypeAdapter, valueType);
            this.constructor = constructor;
        }

        public Map<K, V> read(JsonReader reader) throws IOException {
            JsonToken peek = reader.peek();
            if (peek == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            Map<K, V> map = (Map) this.constructor.construct();
            K key;
            if (peek == JsonToken.BEGIN_ARRAY) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginArray();
                    key = this.keyTypeAdapter.read(reader);
                    if (map.put(key, this.valueTypeAdapter.read(reader)) != null) {
                        throw new JsonSyntaxException("duplicate key: " + key);
                    }
                    reader.endArray();
                }
                reader.endArray();
                return map;
            }
            reader.beginObject();
            while (reader.hasNext()) {
                key = this.keyTypeAdapter.fromJsonElement(new JsonPrimitive(reader.nextName()));
                if (map.put(key, this.valueTypeAdapter.read(reader)) != null) {
                    throw new JsonSyntaxException("duplicate key: " + key);
                }
            }
            reader.endObject();
            return map;
        }

        public void write(JsonWriter writer, Map<K, V> map) throws IOException {
            if (map == null) {
                writer.nullValue();
            } else if (MapTypeAdapterFactory.this.complexMapKeySerialization) {
                boolean hasComplexKeys = false;
                List<JsonElement> keys = new ArrayList(map.size());
                List<V> values = new ArrayList(map.size());
                for (Entry<K, V> entry : map.entrySet()) {
                    JsonElement keyElement = this.keyTypeAdapter.toJsonElement(entry.getKey());
                    keys.add(keyElement);
                    values.add(entry.getValue());
                    int i = (keyElement.isJsonArray() || keyElement.isJsonObject()) ? 1 : 0;
                    hasComplexKeys |= i;
                }
                int i2;
                if (hasComplexKeys) {
                    writer.beginArray();
                    for (i2 = 0; i2 < keys.size(); i2++) {
                        writer.beginArray();
                        Streams.write((JsonElement) keys.get(i2), writer);
                        this.valueTypeAdapter.write(writer, values.get(i2));
                        writer.endArray();
                    }
                    writer.endArray();
                    return;
                }
                writer.beginObject();
                for (i2 = 0; i2 < keys.size(); i2++) {
                    writer.name(keyToString((JsonElement) keys.get(i2)));
                    this.valueTypeAdapter.write(writer, values.get(i2));
                }
                writer.endObject();
            } else {
                writer.beginObject();
                for (Entry<K, V> entry2 : map.entrySet()) {
                    writer.name(String.valueOf(entry2.getKey()));
                    this.valueTypeAdapter.write(writer, entry2.getValue());
                }
                writer.endObject();
            }
        }

        private String keyToString(JsonElement keyElement) {
            if (keyElement.isJsonPrimitive()) {
                JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    return String.valueOf(primitive.getAsNumber());
                }
                if (primitive.isBoolean()) {
                    return Boolean.toString(primitive.getAsBoolean());
                }
                if (primitive.isString()) {
                    return primitive.getAsString();
                }
                throw new AssertionError();
            } else if (keyElement.isJsonNull()) {
                return "null";
            } else {
                throw new AssertionError();
            }
        }
    }

    public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor, boolean complexMapKeySerialization) {
        this.constructorConstructor = constructorConstructor;
        this.complexMapKeySerialization = complexMapKeySerialization;
    }

    public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        if (!Map.class.isAssignableFrom(typeToken.getRawType())) {
            return null;
        }
        Type[] keyAndValueTypes = C$Gson$Types.getMapKeyAndValueTypes(type, C$Gson$Types.getRawType(type));
        return new Adapter(context, keyAndValueTypes[0], getKeyAdapter(context, keyAndValueTypes[0]), keyAndValueTypes[1], context.getAdapter(TypeToken.get(keyAndValueTypes[1])), this.constructorConstructor.getConstructor(typeToken));
    }

    private TypeAdapter<?> getKeyAdapter(MiniGson context, Type keyType) {
        return (keyType == Boolean.TYPE || keyType == Boolean.class) ? TypeAdapters.BOOLEAN_AS_STRING : context.getAdapter(TypeToken.get(keyType));
    }
}
