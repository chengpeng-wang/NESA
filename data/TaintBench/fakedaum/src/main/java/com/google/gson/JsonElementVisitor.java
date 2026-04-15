package com.google.gson;

import java.io.IOException;

interface JsonElementVisitor {
    void endArray(JsonArray jsonArray) throws IOException;

    void endObject(JsonObject jsonObject) throws IOException;

    void startArray(JsonArray jsonArray) throws IOException;

    void startObject(JsonObject jsonObject) throws IOException;

    void visitArrayMember(JsonArray jsonArray, JsonArray jsonArray2, boolean z) throws IOException;

    void visitArrayMember(JsonArray jsonArray, JsonObject jsonObject, boolean z) throws IOException;

    void visitArrayMember(JsonArray jsonArray, JsonPrimitive jsonPrimitive, boolean z) throws IOException;

    void visitNull() throws IOException;

    void visitNullArrayMember(JsonArray jsonArray, boolean z) throws IOException;

    void visitNullObjectMember(JsonObject jsonObject, String str, boolean z) throws IOException;

    void visitObjectMember(JsonObject jsonObject, String str, JsonArray jsonArray, boolean z) throws IOException;

    void visitObjectMember(JsonObject jsonObject, String str, JsonObject jsonObject2, boolean z) throws IOException;

    void visitObjectMember(JsonObject jsonObject, String str, JsonPrimitive jsonPrimitive, boolean z) throws IOException;

    void visitPrimitive(JsonPrimitive jsonPrimitive) throws IOException;
}
