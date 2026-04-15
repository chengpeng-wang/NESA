package com.esotericsoftware.jsonbeans;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class JsonWriter extends Writer {
    private JsonObject current;
    private boolean named;
    private OutputType outputType = OutputType.json;
    private final ArrayList<JsonObject> stack = new ArrayList();
    final Writer writer;

    private class JsonObject {
        final boolean array;
        boolean needsComma;

        JsonObject(boolean z) throws IOException {
            this.array = z;
            JsonWriter.this.writer.write(z ? 91 : 123);
        }

        /* access modifiers changed from: 0000 */
        public void close() throws IOException {
            JsonWriter.this.writer.write(this.array ? 93 : 125);
        }
    }

    public JsonWriter(Writer writer) {
        this.writer = writer;
    }

    public Writer getWriter() {
        return this.writer;
    }

    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }

    public JsonWriter name(String str) throws IOException {
        if (this.current == null || this.current.array) {
            throw new IllegalStateException("Current item must be an object.");
        }
        if (this.current.needsComma) {
            this.writer.write(44);
        } else {
            this.current.needsComma = true;
        }
        this.writer.write(this.outputType.quoteName(str));
        this.writer.write(58);
        this.named = true;
        return this;
    }

    public JsonWriter object() throws IOException {
        if (this.current != null) {
            if (this.current.array) {
                if (this.current.needsComma) {
                    this.writer.write(44);
                } else {
                    this.current.needsComma = true;
                }
            } else if (this.named || this.current.array) {
                this.named = false;
            } else {
                throw new IllegalStateException("Name must be set.");
            }
        }
        ArrayList arrayList = this.stack;
        JsonObject jsonObject = new JsonObject(false);
        this.current = jsonObject;
        arrayList.add(jsonObject);
        return this;
    }

    public JsonWriter array() throws IOException {
        if (this.current != null) {
            if (this.current.array) {
                if (this.current.needsComma) {
                    this.writer.write(44);
                } else {
                    this.current.needsComma = true;
                }
            } else if (this.named || this.current.array) {
                this.named = false;
            } else {
                throw new IllegalStateException("Name must be set.");
            }
        }
        ArrayList arrayList = this.stack;
        JsonObject jsonObject = new JsonObject(true);
        this.current = jsonObject;
        arrayList.add(jsonObject);
        return this;
    }

    public JsonWriter value(Object obj) throws IOException {
        if (obj instanceof Number) {
            Number number = (Number) obj;
            long longValue = number.longValue();
            if (number.doubleValue() == ((double) longValue)) {
                obj = Long.valueOf(longValue);
            }
        }
        if (this.current != null) {
            if (this.current.array) {
                if (this.current.needsComma) {
                    this.writer.write(44);
                } else {
                    this.current.needsComma = true;
                }
            } else if (this.named) {
                this.named = false;
            } else {
                throw new IllegalStateException("Name must be set.");
            }
        }
        this.writer.write(this.outputType.quoteValue(obj));
        return this;
    }

    public JsonWriter object(String str) throws IOException {
        return name(str).object();
    }

    public JsonWriter array(String str) throws IOException {
        return name(str).array();
    }

    public JsonWriter set(String str, Object obj) throws IOException {
        return name(str).value(obj);
    }

    public JsonWriter pop() throws IOException {
        if (this.named) {
            throw new IllegalStateException("Expected an object, array, or value since a name was set.");
        }
        ((JsonObject) this.stack.remove(this.stack.size() - 1)).close();
        this.current = this.stack.isEmpty() ? null : (JsonObject) this.stack.get(this.stack.size() - 1);
        return this;
    }

    public void write(char[] cArr, int i, int i2) throws IOException {
        this.writer.write(cArr, i, i2);
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public void close() throws IOException {
        while (this.stack.size() > 0) {
            pop();
        }
        this.writer.close();
    }
}
