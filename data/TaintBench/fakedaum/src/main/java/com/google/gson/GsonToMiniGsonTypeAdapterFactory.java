package com.google.gson;

import com.google.gson.internal.ParameterizedTypeHandlerMap;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.MiniGson;
import com.google.gson.internal.bind.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

final class GsonToMiniGsonTypeAdapterFactory implements Factory {
    /* access modifiers changed from: private|final */
    public final JsonDeserializationContext deserializationContext;
    private final ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers;
    /* access modifiers changed from: private|final */
    public final JsonSerializationContext serializationContext;
    private final ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers;

    public GsonToMiniGsonTypeAdapterFactory(final Gson gson, ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers) {
        this.serializers = serializers;
        this.deserializers = deserializers;
        this.deserializationContext = new JsonDeserializationContext() {
            public <T> T deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
                return gson.fromJson(json, typeOfT);
            }
        };
        this.serializationContext = new JsonSerializationContext() {
            public JsonElement serialize(Object src) {
                return gson.toJsonTree(src);
            }

            public JsonElement serialize(Object src, Type typeOfSrc) {
                return gson.toJsonTree(src, typeOfSrc);
            }
        };
    }

    public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final JsonSerializer<T> serializer = (JsonSerializer) this.serializers.getHandlerFor(type, false);
        final JsonDeserializer<T> deserializer = (JsonDeserializer) this.deserializers.getHandlerFor(type, false);
        if (serializer == null && deserializer == null) {
            return null;
        }
        final MiniGson miniGson = context;
        final TypeToken<T> typeToken2 = typeToken;
        return new TypeAdapter<T>() {
            private TypeAdapter<T> delegate;

            public T read(JsonReader reader) throws IOException {
                if (deserializer == null) {
                    return delegate().read(reader);
                }
                JsonElement value = Streams.parse(reader);
                if (value.isJsonNull()) {
                    return null;
                }
                return deserializer.deserialize(value, type, GsonToMiniGsonTypeAdapterFactory.this.deserializationContext);
            }

            public void write(JsonWriter writer, T value) throws IOException {
                if (serializer == null) {
                    delegate().write(writer, (Object) value);
                } else if (value == null) {
                    writer.nullValue();
                } else {
                    Streams.write(serializer.serialize(value, type, GsonToMiniGsonTypeAdapterFactory.this.serializationContext), writer);
                }
            }

            private TypeAdapter<T> delegate() {
                TypeAdapter<T> d = this.delegate;
                if (d != null) {
                    return d;
                }
                d = miniGson.getNextAdapter(GsonToMiniGsonTypeAdapterFactory.this, typeToken2);
                this.delegate = d;
                return d;
            }
        };
    }
}
