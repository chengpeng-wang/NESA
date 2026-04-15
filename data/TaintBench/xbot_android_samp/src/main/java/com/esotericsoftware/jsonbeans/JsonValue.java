package com.esotericsoftware.jsonbeans;

import org.objectweb.asm.Opcodes;

public class JsonValue {
    private Boolean booleanValue;
    private JsonValue child;
    private Double doubleValue;
    private long longValue;
    private String name;
    private JsonValue next;
    private JsonValue prev;
    private String stringValue;
    private ValueType type;

    public enum ValueType {
        object,
        array,
        stringValue,
        doubleValue,
        longValue,
        booleanValue,
        nullValue
    }

    public JsonValue(ValueType valueType) {
        this.type = valueType;
    }

    public JsonValue(String str) {
        set(str);
    }

    public JsonValue(double d) {
        set(d);
    }

    public JsonValue(long j) {
        set(j);
    }

    public JsonValue(boolean z) {
        set(z);
    }

    public JsonValue get(int i) {
        JsonValue jsonValue = this.child;
        while (jsonValue != null && i > 0) {
            i--;
            jsonValue = jsonValue.next;
        }
        return jsonValue;
    }

    public JsonValue get(String str) {
        JsonValue jsonValue = this.child;
        while (jsonValue != null && !jsonValue.name.equalsIgnoreCase(str)) {
            jsonValue = jsonValue.next;
        }
        return jsonValue;
    }

    public JsonValue require(int i) {
        JsonValue jsonValue = this.child;
        while (jsonValue != null && i > 0) {
            i--;
            jsonValue = jsonValue.next;
        }
        if (jsonValue != null) {
            return jsonValue;
        }
        throw new IllegalArgumentException("Child not found with index: " + i);
    }

    public JsonValue require(String str) {
        JsonValue jsonValue = this.child;
        while (jsonValue != null && !jsonValue.name.equalsIgnoreCase(str)) {
            jsonValue = jsonValue.next;
        }
        if (jsonValue != null) {
            return jsonValue;
        }
        throw new IllegalArgumentException("Child not found with name: " + str);
    }

    public JsonValue remove(int i) {
        JsonValue jsonValue = get(i);
        if (jsonValue == null) {
            return null;
        }
        if (jsonValue.prev == null) {
            this.child = jsonValue.next;
            if (this.child != null) {
                this.child.prev = null;
            }
        } else {
            jsonValue.prev.next = jsonValue.next;
            if (jsonValue.next != null) {
                jsonValue.next.prev = jsonValue.prev;
            }
        }
        return jsonValue;
    }

    public JsonValue remove(String str) {
        JsonValue jsonValue = get(str);
        if (jsonValue == null) {
            return null;
        }
        if (jsonValue.prev == null) {
            this.child = jsonValue.next;
            if (this.child != null) {
                this.child.prev = null;
            }
        } else {
            jsonValue.prev.next = jsonValue.next;
            if (jsonValue.next != null) {
                jsonValue.next.prev = jsonValue.prev;
            }
        }
        return jsonValue;
    }

    public int size() {
        int i = 0;
        for (JsonValue jsonValue = this.child; jsonValue != null; jsonValue = jsonValue.next) {
            i++;
        }
        return i;
    }

    public String asString() {
        if (this.stringValue != null) {
            return this.stringValue;
        }
        if (this.doubleValue != null) {
            if (this.doubleValue.doubleValue() == ((double) this.longValue)) {
                return Long.toString(this.longValue);
            }
            return Double.toString(this.doubleValue.doubleValue());
        } else if (this.booleanValue != null) {
            return Boolean.toString(this.booleanValue.booleanValue());
        } else {
            if (this.type == ValueType.nullValue) {
                return null;
            }
            throw new IllegalStateException("Value cannot be converted to string: " + this.type);
        }
    }

    public float asFloat() {
        if (this.doubleValue != null) {
            return this.doubleValue.floatValue();
        }
        if (this.stringValue != null) {
            try {
                return Float.parseFloat(this.stringValue);
            } catch (NumberFormatException e) {
            }
        }
        if (this.booleanValue != null) {
            return this.booleanValue.booleanValue() ? 1.0f : 0.0f;
        } else {
            throw new IllegalStateException("Value cannot be converted to float: " + this.type);
        }
    }

    public double asDouble() {
        if (this.doubleValue != null) {
            return this.doubleValue.doubleValue();
        }
        if (this.stringValue != null) {
            try {
                return Double.parseDouble(this.stringValue);
            } catch (NumberFormatException e) {
            }
        }
        if (this.booleanValue != null) {
            return this.booleanValue.booleanValue() ? 1.0d : 0.0d;
        } else {
            throw new IllegalStateException("Value cannot be converted to double: " + this.type);
        }
    }

    public long asLong() {
        if (this.doubleValue != null) {
            return this.longValue;
        }
        if (this.stringValue != null) {
            try {
                return Long.parseLong(this.stringValue);
            } catch (NumberFormatException e) {
            }
        }
        if (this.booleanValue != null) {
            return this.booleanValue.booleanValue() ? 1 : 0;
        } else {
            throw new IllegalStateException("Value cannot be converted to long: " + this.type);
        }
    }

    public int asInt() {
        if (this.doubleValue != null) {
            return (int) this.longValue;
        }
        if (this.stringValue != null) {
            try {
                return Integer.parseInt(this.stringValue);
            } catch (NumberFormatException e) {
            }
        }
        if (this.booleanValue != null) {
            return this.booleanValue.booleanValue() ? 1 : 0;
        } else {
            throw new IllegalStateException("Value cannot be converted to int: " + this.type);
        }
    }

    public boolean asBoolean() {
        if (this.booleanValue != null) {
            return this.booleanValue.booleanValue();
        }
        if (this.doubleValue != null) {
            return this.longValue == 0;
        } else {
            if (this.stringValue != null) {
                return this.stringValue.equalsIgnoreCase("true");
            }
            throw new IllegalStateException("Value cannot be converted to boolean: " + this.type);
        }
    }

    public JsonValue getChild(String str) {
        JsonValue jsonValue = get(str);
        return jsonValue == null ? null : jsonValue.child;
    }

    public String getString(String str, String str2) {
        JsonValue jsonValue = get(str);
        return (jsonValue == null || !jsonValue.isValue() || jsonValue.isNull()) ? str2 : jsonValue.asString();
    }

    public float getFloat(String str, float f) {
        JsonValue jsonValue = get(str);
        return (jsonValue == null || !jsonValue.isValue()) ? f : jsonValue.asFloat();
    }

    public double getDouble(String str, double d) {
        JsonValue jsonValue = get(str);
        return (jsonValue == null || !jsonValue.isValue()) ? d : jsonValue.asDouble();
    }

    public long getLong(String str, long j) {
        JsonValue jsonValue = get(str);
        return (jsonValue == null || !jsonValue.isValue()) ? j : jsonValue.asLong();
    }

    public int getInt(String str, int i) {
        JsonValue jsonValue = get(str);
        return (jsonValue == null || !jsonValue.isValue()) ? i : jsonValue.asInt();
    }

    public boolean getBoolean(String str, boolean z) {
        JsonValue jsonValue = get(str);
        return (jsonValue == null || !jsonValue.isValue()) ? z : jsonValue.asBoolean();
    }

    public String getString(String str) {
        JsonValue jsonValue = get(str);
        if (jsonValue != null) {
            return jsonValue.asString();
        }
        throw new IllegalArgumentException("Named value not found: " + str);
    }

    public float getFloat(String str) {
        JsonValue jsonValue = get(str);
        if (jsonValue != null) {
            return jsonValue.asFloat();
        }
        throw new IllegalArgumentException("Named value not found: " + str);
    }

    public double getDouble(String str) {
        JsonValue jsonValue = get(str);
        if (jsonValue != null) {
            return jsonValue.asDouble();
        }
        throw new IllegalArgumentException("Named value not found: " + str);
    }

    public long getLong(String str) {
        JsonValue jsonValue = get(str);
        if (jsonValue != null) {
            return jsonValue.asLong();
        }
        throw new IllegalArgumentException("Named value not found: " + str);
    }

    public int getInt(String str) {
        JsonValue jsonValue = get(str);
        if (jsonValue != null) {
            return jsonValue.asInt();
        }
        throw new IllegalArgumentException("Named value not found: " + str);
    }

    public boolean getBoolean(String str) {
        JsonValue jsonValue = get(str);
        if (jsonValue != null) {
            return jsonValue.asBoolean();
        }
        throw new IllegalArgumentException("Named value not found: " + str);
    }

    public String getString(int i) {
        JsonValue jsonValue = get(i);
        if (jsonValue != null) {
            return jsonValue.asString();
        }
        throw new IllegalArgumentException("Indexed value not found: " + this.name);
    }

    public float getFloat(int i) {
        JsonValue jsonValue = get(i);
        if (jsonValue != null) {
            return jsonValue.asFloat();
        }
        throw new IllegalArgumentException("Indexed value not found: " + this.name);
    }

    public double getDouble(int i) {
        JsonValue jsonValue = get(i);
        if (jsonValue != null) {
            return jsonValue.asDouble();
        }
        throw new IllegalArgumentException("Indexed value not found: " + this.name);
    }

    public long getLong(int i) {
        JsonValue jsonValue = get(i);
        if (jsonValue != null) {
            return jsonValue.asLong();
        }
        throw new IllegalArgumentException("Indexed value not found: " + this.name);
    }

    public int getInt(int i) {
        JsonValue jsonValue = get(i);
        if (jsonValue != null) {
            return jsonValue.asInt();
        }
        throw new IllegalArgumentException("Indexed value not found: " + this.name);
    }

    public boolean getBoolean(int i) {
        JsonValue jsonValue = get(i);
        if (jsonValue != null) {
            return jsonValue.asBoolean();
        }
        throw new IllegalArgumentException("Indexed value not found: " + this.name);
    }

    public ValueType type() {
        return this.type;
    }

    public void setType(ValueType valueType) {
        if (valueType == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }
        this.type = valueType;
    }

    public boolean isArray() {
        return this.type == ValueType.array;
    }

    public boolean isObject() {
        return this.type == ValueType.object;
    }

    public boolean isString() {
        return this.type == ValueType.stringValue;
    }

    public boolean isNumber() {
        return this.type == ValueType.doubleValue || this.type == ValueType.longValue;
    }

    public boolean isDouble() {
        return this.type == ValueType.doubleValue;
    }

    public boolean isLong() {
        return this.type == ValueType.longValue;
    }

    public boolean isBoolean() {
        return this.type == ValueType.booleanValue;
    }

    public boolean isNull() {
        return this.type == ValueType.nullValue;
    }

    public boolean isValue() {
        switch (this.type) {
            case stringValue:
            case doubleValue:
            case longValue:
            case booleanValue:
            case nullValue:
                return true;
            default:
                return false;
        }
    }

    public String name() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public JsonValue child() {
        return this.child;
    }

    public void addChild(JsonValue jsonValue) {
        JsonValue jsonValue2 = this.child;
        if (jsonValue2 == null) {
            this.child = jsonValue;
            return;
        }
        while (jsonValue2.next != null) {
            jsonValue2 = jsonValue2.next;
        }
        jsonValue2.next = jsonValue;
        jsonValue.prev = jsonValue2;
    }

    public JsonValue next() {
        return this.next;
    }

    public void setNext(JsonValue jsonValue) {
        this.next = jsonValue;
    }

    public JsonValue prev() {
        return this.prev;
    }

    public void setPrev(JsonValue jsonValue) {
        this.prev = jsonValue;
    }

    public void set(String str) {
        this.stringValue = str;
        this.type = str == null ? ValueType.nullValue : ValueType.stringValue;
    }

    public void set(double d) {
        this.doubleValue = Double.valueOf(d);
        this.longValue = (long) d;
        this.type = ValueType.doubleValue;
    }

    public void set(long j) {
        this.longValue = j;
        this.doubleValue = Double.valueOf((double) j);
        this.type = ValueType.longValue;
    }

    public void set(boolean z) {
        this.booleanValue = Boolean.valueOf(z);
        this.type = ValueType.booleanValue;
    }

    public String toString() {
        return prettyPrint(OutputType.minimal, 0);
    }

    public String prettyPrint(OutputType outputType, int i) {
        StringBuilder stringBuilder = new StringBuilder(Opcodes.ACC_INTERFACE);
        prettyPrint(this, stringBuilder, outputType, 0, i);
        return stringBuilder.toString();
    }

    private void prettyPrint(JsonValue jsonValue, StringBuilder stringBuilder, OutputType outputType, int i, int i2) {
        Object obj;
        int length;
        Object obj2;
        JsonValue child;
        int obj22;
        if (jsonValue.isObject()) {
            if (jsonValue.child() == null) {
                stringBuilder.append("{}");
                return;
            }
            obj = !isFlat(jsonValue) ? 1 : null;
            length = stringBuilder.length();
            obj22 = obj;
            loop0:
            while (true) {
                stringBuilder.append(obj22 != null ? "{\n" : "{ ");
                child = jsonValue.child();
                while (child != null) {
                    if (obj22 != null) {
                        indent(i, stringBuilder);
                    }
                    stringBuilder.append(outputType.quoteName(child.name()));
                    stringBuilder.append(": ");
                    prettyPrint(child, stringBuilder, outputType, i + 1, i2);
                    if (child.next() != null) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(obj22 != null ? 10 : ' ');
                    if (obj22 != null || stringBuilder.length() - length <= i2) {
                        child = child.next();
                    } else {
                        stringBuilder.setLength(length);
                        obj22 = 1;
                    }
                }
                break loop0;
            }
            if (obj22 != null) {
                indent(i - 1, stringBuilder);
            }
            stringBuilder.append('}');
        } else if (jsonValue.isArray()) {
            if (jsonValue.child() == null) {
                stringBuilder.append("[]");
                return;
            }
            obj = !isFlat(jsonValue) ? 1 : null;
            length = stringBuilder.length();
            obj22 = obj;
            loop2:
            while (true) {
                stringBuilder.append(obj22 != null ? "[\n" : "[ ");
                child = jsonValue.child();
                while (child != null) {
                    if (obj22 != null) {
                        indent(i, stringBuilder);
                    }
                    prettyPrint(child, stringBuilder, outputType, i + 1, i2);
                    if (child.next() != null) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(obj22 != null ? 10 : ' ');
                    if (obj22 != null || stringBuilder.length() - length <= i2) {
                        child = child.next();
                    } else {
                        stringBuilder.setLength(length);
                        obj22 = 1;
                    }
                }
                break loop2;
            }
            if (obj22 != null) {
                indent(i - 1, stringBuilder);
            }
            stringBuilder.append(']');
        } else if (jsonValue.isString()) {
            stringBuilder.append(outputType.quoteValue(jsonValue.asString()));
        } else if (jsonValue.isDouble()) {
            double asDouble = jsonValue.asDouble();
            long asLong = jsonValue.asLong();
            if (asDouble == ((double) asLong)) {
                asDouble = (double) asLong;
            }
            stringBuilder.append(asDouble);
        } else if (jsonValue.isLong()) {
            stringBuilder.append(jsonValue.asLong());
        } else if (jsonValue.isBoolean()) {
            stringBuilder.append(jsonValue.asBoolean());
        } else if (jsonValue.isNull()) {
            stringBuilder.append("null");
        } else {
            throw new JsonException("Unknown object type: " + jsonValue);
        }
    }

    private static boolean isFlat(JsonValue jsonValue) {
        JsonValue child = jsonValue.child();
        while (child != null) {
            if (child.isObject() || child.isArray()) {
                return false;
            }
            child = child.next();
        }
        return true;
    }

    private static void indent(int i, StringBuilder stringBuilder) {
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append(9);
        }
    }
}
