package com.esotericsoftware.jsonbeans;

public interface JsonSerializable {
    void read(Json json, JsonValue jsonValue);

    void write(Json json);
}
