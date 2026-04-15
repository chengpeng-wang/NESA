package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.lang.reflect.Type;

final class JsonDeserializerExceptionWrapper<T> implements JsonDeserializer<T> {
    private final JsonDeserializer<T> delegate;

    JsonDeserializerExceptionWrapper(JsonDeserializer<T> delegate) {
        this.delegate = (JsonDeserializer) C$Gson$Preconditions.checkNotNull(delegate);
    }

    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return this.delegate.deserialize(json, typeOfT, context);
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e2) {
            throw new JsonParseException("The JsonDeserializer " + this.delegate + " failed to deserialize json object " + json + " given the type " + typeOfT, e2);
        }
    }

    public String toString() {
        return this.delegate.toString();
    }
}
