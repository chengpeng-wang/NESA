package com.mvlove.http;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListTypeAdapter<T> implements JsonDeserializer<List<T>> {
    private Class<T> clazz;
    private Gson gson = new Gson();

    public ListTypeAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> deserialize(JsonElement je, Type type, JsonDeserializationContext context) throws JsonParseException {
        List<T> list = new ArrayList();
        Iterator<JsonElement> iter = je.getAsJsonArray().iterator();
        while (iter.hasNext()) {
            list.add(this.gson.fromJson(((JsonObject) iter.next()).toString(), this.clazz));
        }
        return list;
    }
}
