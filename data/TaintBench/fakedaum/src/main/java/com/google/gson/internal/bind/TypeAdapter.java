package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public abstract class TypeAdapter<T> {

    public interface Factory {
        <T> TypeAdapter<T> create(MiniGson miniGson, TypeToken<T> typeToken);
    }

    public abstract T read(JsonReader jsonReader) throws IOException;

    public abstract void write(JsonWriter jsonWriter, T t) throws IOException;

    public final String toJson(T value) throws IOException {
        Writer stringWriter = new StringWriter();
        write(stringWriter, (Object) value);
        return stringWriter.toString();
    }

    public final void write(Writer out, T value) throws IOException {
        write(new JsonWriter(out), (Object) value);
    }

    public final T fromJson(String json) throws IOException {
        return read(new StringReader(json));
    }

    public final T read(Reader in) throws IOException {
        JsonReader reader = new JsonReader(in);
        reader.setLenient(true);
        return read(reader);
    }

    public JsonElement toJsonElement(T src) {
        try {
            JsonWriter jsonWriter = new JsonElementWriter();
            jsonWriter.setLenient(true);
            write(jsonWriter, (Object) src);
            return jsonWriter.get();
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }

    public T fromJsonElement(JsonElement json) {
        try {
            JsonReader jsonReader = new JsonElementReader(json);
            jsonReader.setLenient(true);
            return read(jsonReader);
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }
}
