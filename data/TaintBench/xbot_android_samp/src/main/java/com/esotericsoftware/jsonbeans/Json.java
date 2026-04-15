package com.esotericsoftware.jsonbeans;

import com.esotericsoftware.jsonbeans.ObjectMap.Entry;
import com.esotericsoftware.jsonbeans.ObjectMap.Values;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Json {
    private static final boolean debug = false;
    private final ObjectMap<Class, Object[]> classToDefaultValues;
    private final ObjectMap<Class, Serializer> classToSerializer;
    private final ObjectMap<Class, String> classToTag;
    private boolean ignoreUnknownFields;
    private OutputType outputType;
    private final ObjectMap<String, Class> tagToClass;
    private String typeName;
    private final ObjectMap<Class, ObjectMap<String, FieldMetadata>> typeToFields;
    private boolean usePrototypes;
    private JsonWriter writer;

    private static class FieldMetadata {
        Class elementType;
        Field field;

        public FieldMetadata(Field field) {
            this.field = field;
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
                if (actualTypeArguments.length == 1) {
                    genericType = actualTypeArguments[0];
                    if (genericType instanceof Class) {
                        this.elementType = (Class) genericType;
                    } else if (genericType instanceof ParameterizedType) {
                        this.elementType = (Class) ((ParameterizedType) genericType).getRawType();
                    }
                }
            }
        }
    }

    public interface Serializable {
        void read(Json json, JsonValue jsonValue);

        void write(Json json);
    }

    public interface Serializer<T> {
        T read(Json json, JsonValue jsonValue, Class cls);

        void write(Json json, T t, Class cls);
    }

    public static abstract class ReadOnlySerializer<T> implements Serializer<T> {
        public abstract T read(Json json, JsonValue jsonValue, Class cls);

        public void write(Json json, T t, Class cls) {
        }
    }

    public Json() {
        this.typeName = "class";
        this.usePrototypes = true;
        this.typeToFields = new ObjectMap();
        this.tagToClass = new ObjectMap();
        this.classToTag = new ObjectMap();
        this.classToSerializer = new ObjectMap();
        this.classToDefaultValues = new ObjectMap();
        this.outputType = OutputType.minimal;
    }

    public Json(OutputType outputType) {
        this.typeName = "class";
        this.usePrototypes = true;
        this.typeToFields = new ObjectMap();
        this.tagToClass = new ObjectMap();
        this.classToTag = new ObjectMap();
        this.classToSerializer = new ObjectMap();
        this.classToDefaultValues = new ObjectMap();
        this.outputType = outputType;
    }

    public void setIgnoreUnknownFields(boolean z) {
        this.ignoreUnknownFields = z;
    }

    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }

    public void addClassTag(String str, Class cls) {
        this.tagToClass.put(str, cls);
        this.classToTag.put(cls, str);
    }

    public Class getClass(String str) {
        Class cls = (Class) this.tagToClass.get(str);
        if (cls != null) {
            return cls;
        }
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new JsonException(e);
        }
    }

    public String getTag(Class cls) {
        String str = (String) this.classToTag.get(cls);
        return str != null ? str : cls.getName();
    }

    public void setTypeName(String str) {
        this.typeName = str;
    }

    public <T> void setSerializer(Class<T> cls, Serializer<T> serializer) {
        this.classToSerializer.put(cls, serializer);
    }

    public <T> Serializer<T> getSerializer(Class<T> cls) {
        return (Serializer) this.classToSerializer.get(cls);
    }

    public void setUsePrototypes(boolean z) {
        this.usePrototypes = z;
    }

    public void setElementType(Class cls, String str, Class cls2) {
        ObjectMap objectMap = (ObjectMap) this.typeToFields.get(cls);
        if (objectMap == null) {
            objectMap = cacheFields(cls);
        }
        FieldMetadata fieldMetadata = (FieldMetadata) objectMap.get(str);
        if (fieldMetadata == null) {
            throw new JsonException("Field not found: " + str + " (" + cls.getName() + ")");
        }
        fieldMetadata.elementType = cls2;
    }

    private ObjectMap<String, FieldMetadata> cacheFields(Class cls) {
        ArrayList arrayList = new ArrayList();
        for (Class cls2 = cls; cls2 != Object.class; cls2 = cls2.getSuperclass()) {
            Collections.addAll(arrayList, cls2.getDeclaredFields());
        }
        ObjectMap objectMap = new ObjectMap();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Field field = (Field) arrayList.get(i);
            int modifiers = field.getModifiers();
            if (!(Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers) || field.isSynthetic())) {
                if (!field.isAccessible()) {
                    try {
                        field.setAccessible(true);
                    } catch (AccessControlException e) {
                    }
                }
                objectMap.put(field.getName(), new FieldMetadata(field));
            }
        }
        this.typeToFields.put(cls, objectMap);
        return objectMap;
    }

    public String toJson(Object obj) {
        return toJson(obj, obj == null ? null : obj.getClass(), (Class) null);
    }

    public String toJson(Object obj, Class cls) {
        return toJson(obj, cls, (Class) null);
    }

    public String toJson(Object obj, Class cls, Class cls2) {
        Writer stringWriter = new StringWriter();
        toJson(obj, cls, cls2, stringWriter);
        return stringWriter.toString();
    }

    public void toJson(Object obj, File file) {
        toJson(obj, obj == null ? null : obj.getClass(), null, file);
    }

    public void toJson(Object obj, Class cls, File file) {
        toJson(obj, cls, null, file);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002d A:{SYNTHETIC, Splitter:B:15:0x002d} */
    public void toJson(java.lang.Object r6, java.lang.Class r7, java.lang.Class r8, java.io.File r9) {
        /*
        r5 = this;
        r2 = 0;
        r1 = new java.io.FileWriter;	 Catch:{ Exception -> 0x000f, all -> 0x0035 }
        r1.<init>(r9);	 Catch:{ Exception -> 0x000f, all -> 0x0035 }
        r5.toJson(r6, r7, r8, r1);	 Catch:{ Exception -> 0x0038 }
        if (r1 == 0) goto L_0x000e;
    L_0x000b:
        r1.close();	 Catch:{ IOException -> 0x0031 }
    L_0x000e:
        return;
    L_0x000f:
        r0 = move-exception;
        r1 = r2;
    L_0x0011:
        r2 = new com.esotericsoftware.jsonbeans.JsonException;	 Catch:{ all -> 0x002a }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x002a }
        r3.<init>();	 Catch:{ all -> 0x002a }
        r4 = "Error writing file: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x002a }
        r3 = r3.append(r9);	 Catch:{ all -> 0x002a }
        r3 = r3.toString();	 Catch:{ all -> 0x002a }
        r2.m63init(r3, r0);	 Catch:{ all -> 0x002a }
        throw r2;	 Catch:{ all -> 0x002a }
    L_0x002a:
        r0 = move-exception;
    L_0x002b:
        if (r1 == 0) goto L_0x0030;
    L_0x002d:
        r1.close();	 Catch:{ IOException -> 0x0033 }
    L_0x0030:
        throw r0;
    L_0x0031:
        r0 = move-exception;
        goto L_0x000e;
    L_0x0033:
        r1 = move-exception;
        goto L_0x0030;
    L_0x0035:
        r0 = move-exception;
        r1 = r2;
        goto L_0x002b;
    L_0x0038:
        r0 = move-exception;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.jsonbeans.Json.toJson(java.lang.Object, java.lang.Class, java.lang.Class, java.io.File):void");
    }

    public void toJson(Object obj, Writer writer) {
        toJson(obj, obj == null ? null : obj.getClass(), null, writer);
    }

    public void toJson(Object obj, Class cls, Writer writer) {
        toJson(obj, cls, null, writer);
    }

    public void toJson(Object obj, Class cls, Class cls2, Writer writer) {
        setWriter(writer);
        try {
            writeValue(obj, cls, cls2);
        } finally {
            try {
                this.writer.close();
            } catch (Exception e) {
            }
            this.writer = null;
        }
    }

    public void setWriter(Writer writer) {
        JsonWriter jsonWriter;
        if (writer instanceof JsonWriter) {
            Writer jsonWriter2 = writer;
        } else {
            jsonWriter2 = new JsonWriter(writer);
        }
        this.writer = jsonWriter2;
        this.writer.setOutputType(this.outputType);
    }

    public JsonWriter getWriter() {
        return this.writer;
    }

    public void writeFields(Object obj) {
        Class cls = obj.getClass();
        Object[] defaultValues = getDefaultValues(cls);
        ObjectMap objectMap = (ObjectMap) this.typeToFields.get(cls);
        if (objectMap == null) {
            objectMap = cacheFields(cls);
        }
        int i = 0;
        Iterator it = new Values(objectMap).iterator();
        while (it.hasNext()) {
            FieldMetadata fieldMetadata = (FieldMetadata) it.next();
            Field field = fieldMetadata.field;
            try {
                Object obj2 = field.get(obj);
                if (defaultValues != null) {
                    int i2 = i + 1;
                    Object obj3 = defaultValues[i];
                    i = (obj2 == null && obj3 == null) ? i2 : (obj2 == null || obj3 == null || !obj2.equals(obj3)) ? i2 : i2;
                }
                this.writer.name(field.getName());
                writeValue(obj2, field.getType(), fieldMetadata.elementType);
            } catch (IllegalAccessException e) {
                throw new JsonException("Error accessing field: " + field.getName() + " (" + cls.getName() + ")", e);
            } catch (JsonException e2) {
                e2.addTrace(field + " (" + cls.getName() + ")");
                throw e2;
            } catch (Exception e3) {
                JsonException jsonException = new JsonException(e3);
                jsonException.addTrace(field + " (" + cls.getName() + ")");
                throw jsonException;
            }
        }
    }

    private Object[] getDefaultValues(Class cls) {
        if (!this.usePrototypes) {
            return null;
        }
        if (this.classToDefaultValues.containsKey(cls)) {
            return (Object[]) this.classToDefaultValues.get(cls);
        }
        try {
            Object newInstance = newInstance(cls);
            ObjectMap objectMap = (ObjectMap) this.typeToFields.get(cls);
            if (objectMap == null) {
                objectMap = cacheFields(cls);
            }
            Object[] objArr = new Object[objectMap.size];
            this.classToDefaultValues.put(cls, objArr);
            int i = 0;
            Iterator it = objectMap.values().iterator();
            while (it.hasNext()) {
                Field field = ((FieldMetadata) it.next()).field;
                int i2 = i + 1;
                try {
                    objArr[i] = field.get(newInstance);
                    i = i2;
                } catch (IllegalAccessException e) {
                    throw new JsonException("Error accessing field: " + field.getName() + " (" + cls.getName() + ")", e);
                } catch (JsonException e2) {
                    e2.addTrace(field + " (" + cls.getName() + ")");
                    throw e2;
                } catch (RuntimeException e3) {
                    JsonException jsonException = new JsonException(e3);
                    jsonException.addTrace(field + " (" + cls.getName() + ")");
                    throw jsonException;
                }
            }
            return objArr;
        } catch (Exception e4) {
            this.classToDefaultValues.put(cls, null);
            return null;
        }
    }

    public void writeField(Object obj, String str) {
        writeField(obj, str, str, null);
    }

    public void writeField(Object obj, String str, Class cls) {
        writeField(obj, str, str, cls);
    }

    public void writeField(Object obj, String str, String str2) {
        writeField(obj, str, str2, null);
    }

    public void writeField(Object obj, String str, String str2, Class cls) {
        Class cls2 = obj.getClass();
        ObjectMap objectMap = (ObjectMap) this.typeToFields.get(cls2);
        if (objectMap == null) {
            objectMap = cacheFields(cls2);
        }
        FieldMetadata fieldMetadata = (FieldMetadata) objectMap.get(str);
        if (fieldMetadata == null) {
            throw new JsonException("Field not found: " + str + " (" + cls2.getName() + ")");
        }
        Field field = fieldMetadata.field;
        if (cls == null) {
            cls = fieldMetadata.elementType;
        }
        try {
            this.writer.name(str2);
            writeValue(field.get(obj), field.getType(), cls);
        } catch (IllegalAccessException e) {
            throw new JsonException("Error accessing field: " + field.getName() + " (" + cls2.getName() + ")", e);
        } catch (JsonException e2) {
            e2.addTrace(field + " (" + cls2.getName() + ")");
            throw e2;
        } catch (Exception e3) {
            JsonException jsonException = new JsonException(e3);
            jsonException.addTrace(field + " (" + cls2.getName() + ")");
            throw jsonException;
        }
    }

    public void writeValue(String str, Object obj) {
        try {
            this.writer.name(str);
            if (obj == null) {
                writeValue(obj, null, null);
            } else {
                writeValue(obj, obj.getClass(), null);
            }
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeValue(String str, Object obj, Class cls) {
        try {
            this.writer.name(str);
            writeValue(obj, cls, null);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeValue(String str, Object obj, Class cls, Class cls2) {
        try {
            this.writer.name(str);
            writeValue(obj, cls, cls2);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeValue(Object obj) {
        if (obj == null) {
            writeValue(obj, null, null);
        } else {
            writeValue(obj, obj.getClass(), null);
        }
    }

    public void writeValue(Object obj, Class cls) {
        writeValue(obj, cls, null);
    }

    public void writeValue(Object obj, Class cls, Class cls2) {
        if (obj == null) {
            try {
                this.writer.value(null);
            } catch (IOException e) {
                throw new JsonException(e);
            }
        } else if ((cls != null && cls.isPrimitive()) || cls == String.class || cls == Integer.class || cls == Boolean.class || cls == Float.class || cls == Long.class || cls == Double.class || cls == Short.class || cls == Byte.class || cls == Character.class) {
            this.writer.value(obj);
        } else {
            Class cls3 = obj.getClass();
            if (cls3.isPrimitive() || cls3 == String.class || cls3 == Integer.class || cls3 == Boolean.class || cls3 == Float.class || cls3 == Long.class || cls3 == Double.class || cls3 == Short.class || cls3 == Byte.class || cls3 == Character.class) {
                writeObjectStart(cls3, null);
                writeValue("value", obj);
                writeObjectEnd();
            } else if (obj instanceof Serializable) {
                writeObjectStart(cls3, cls);
                ((Serializable) obj).write(this);
                writeObjectEnd();
            } else {
                Serializer serializer = (Serializer) this.classToSerializer.get(cls3);
                Iterator it;
                if (serializer != null) {
                    serializer.write(this, obj, cls);
                } else if (obj instanceof Collection) {
                    if (cls == null || cls3 == cls || cls3 == ArrayList.class) {
                        writeArrayStart();
                        for (Object writeValue : (Collection) obj) {
                            writeValue(writeValue, cls2, null);
                        }
                        writeArrayEnd();
                        return;
                    }
                    throw new JsonException("Serialization of a Collection other than the known type is not supported.\nKnown type: " + cls + "\nActual type: " + cls3);
                } else if (cls3.isArray()) {
                    if (cls2 == null) {
                        cls2 = cls3.getComponentType();
                    }
                    int length = Array.getLength(obj);
                    writeArrayStart();
                    for (int i = 0; i < length; i++) {
                        writeValue(Array.get(obj, i), cls2, null);
                    }
                    writeArrayEnd();
                } else if (obj instanceof ObjectMap) {
                    if (cls == null) {
                        cls = ObjectMap.class;
                    }
                    writeObjectStart(cls3, cls);
                    it = ((ObjectMap) obj).entries().iterator();
                    while (it.hasNext()) {
                        Entry entry = (Entry) it.next();
                        this.writer.name(convertToString(entry.key));
                        writeValue(entry.value, cls2, null);
                    }
                    writeObjectEnd();
                } else if (obj instanceof Map) {
                    if (cls == null) {
                        cls = HashMap.class;
                    }
                    writeObjectStart(cls3, cls);
                    for (Map.Entry entry2 : ((Map) obj).entrySet()) {
                        this.writer.name(convertToString(entry2.getKey()));
                        writeValue(entry2.getValue(), cls2, null);
                    }
                    writeObjectEnd();
                } else if (Enum.class.isAssignableFrom(cls3)) {
                    this.writer.value(obj);
                } else {
                    writeObjectStart(cls3, cls);
                    writeFields(obj);
                    writeObjectEnd();
                }
            }
        }
    }

    public void writeObjectStart(String str) {
        try {
            this.writer.name(str);
            writeObjectStart();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeObjectStart(String str, Class cls, Class cls2) {
        try {
            this.writer.name(str);
            writeObjectStart(cls, cls2);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeObjectStart() {
        try {
            this.writer.object();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeObjectStart(Class cls, Class cls2) {
        try {
            this.writer.object();
            if (cls2 == null || cls2 != cls) {
                writeType(cls);
            }
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeObjectEnd() {
        try {
            this.writer.pop();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeArrayStart(String str) {
        try {
            this.writer.name(str);
            this.writer.array();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeArrayStart() {
        try {
            this.writer.array();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeArrayEnd() {
        try {
            this.writer.pop();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public void writeType(Class cls) {
        if (this.typeName != null) {
            Object obj = (String) this.classToTag.get(cls);
            if (obj == null) {
                obj = cls.getName();
            }
            try {
                this.writer.set(this.typeName, obj);
            } catch (IOException e) {
                throw new JsonException(e);
            }
        }
    }

    public <T> T fromJson(Class<T> cls, Reader reader) {
        return readValue((Class) cls, null, new JsonReader().parse(reader));
    }

    public <T> T fromJson(Class<T> cls, Class cls2, Reader reader) {
        return readValue((Class) cls, cls2, new JsonReader().parse(reader));
    }

    public <T> T fromJson(Class<T> cls, InputStream inputStream) {
        return readValue((Class) cls, null, new JsonReader().parse(inputStream));
    }

    public <T> T fromJson(Class<T> cls, Class cls2, InputStream inputStream) {
        return readValue((Class) cls, cls2, new JsonReader().parse(inputStream));
    }

    public <T> T fromJson(Class<T> cls, File file) {
        try {
            return readValue((Class) cls, null, new JsonReader().parse(file));
        } catch (Exception e) {
            throw new JsonException("Error reading file: " + file, e);
        }
    }

    public <T> T fromJson(Class<T> cls, Class cls2, File file) {
        try {
            return readValue((Class) cls, cls2, new JsonReader().parse(file));
        } catch (Exception e) {
            throw new JsonException("Error reading file: " + file, e);
        }
    }

    public <T> T fromJson(Class<T> cls, char[] cArr, int i, int i2) {
        return readValue((Class) cls, null, new JsonReader().parse(cArr, i, i2));
    }

    public <T> T fromJson(Class<T> cls, Class cls2, char[] cArr, int i, int i2) {
        return readValue((Class) cls, cls2, new JsonReader().parse(cArr, i, i2));
    }

    public <T> T fromJson(Class<T> cls, String str) {
        return readValue((Class) cls, null, new JsonReader().parse(str));
    }

    public <T> T fromJson(Class<T> cls, Class cls2, String str) {
        return readValue((Class) cls, cls2, new JsonReader().parse(str));
    }

    public void readField(Object obj, String str, JsonValue jsonValue) {
        readField(obj, str, str, null, jsonValue);
    }

    public void readField(Object obj, String str, Class cls, JsonValue jsonValue) {
        readField(obj, str, str, cls, jsonValue);
    }

    public void readField(Object obj, String str, String str2, JsonValue jsonValue) {
        readField(obj, str, str2, null, jsonValue);
    }

    public void readField(Object obj, String str, String str2, Class cls, JsonValue jsonValue) {
        Class cls2 = obj.getClass();
        ObjectMap objectMap = (ObjectMap) this.typeToFields.get(cls2);
        if (objectMap == null) {
            objectMap = cacheFields(cls2);
        }
        FieldMetadata fieldMetadata = (FieldMetadata) objectMap.get(str);
        if (fieldMetadata == null) {
            throw new JsonException("Field not found: " + str + " (" + cls2.getName() + ")");
        }
        Field field = fieldMetadata.field;
        JsonValue jsonValue2 = jsonValue.get(str2);
        if (jsonValue2 != null) {
            if (cls == null) {
                cls = fieldMetadata.elementType;
            }
            try {
                field.set(obj, readValue(field.getType(), cls, jsonValue2));
            } catch (IllegalAccessException e) {
                throw new JsonException("Error accessing field: " + field.getName() + " (" + cls2.getName() + ")", e);
            } catch (JsonException e2) {
                e2.addTrace(field.getName() + " (" + cls2.getName() + ")");
                throw e2;
            } catch (RuntimeException e3) {
                JsonException jsonException = new JsonException(e3);
                jsonException.addTrace(field.getName() + " (" + cls2.getName() + ")");
                throw jsonException;
            }
        }
    }

    public void readFields(Object obj, JsonValue jsonValue) {
        ObjectMap cacheFields;
        Class cls = obj.getClass();
        ObjectMap objectMap = (ObjectMap) this.typeToFields.get(cls);
        if (objectMap == null) {
            cacheFields = cacheFields(cls);
        } else {
            cacheFields = objectMap;
        }
        for (JsonValue child = jsonValue.child(); child != null; child = child.next()) {
            FieldMetadata fieldMetadata = (FieldMetadata) cacheFields.get(child.name());
            if (fieldMetadata != null) {
                Field field = fieldMetadata.field;
                try {
                    field.set(obj, readValue(field.getType(), fieldMetadata.elementType, child));
                } catch (IllegalAccessException e) {
                    throw new JsonException("Error accessing field: " + field.getName() + " (" + cls.getName() + ")", e);
                } catch (JsonException e2) {
                    e2.addTrace(field.getName() + " (" + cls.getName() + ")");
                    throw e2;
                } catch (RuntimeException e3) {
                    JsonException jsonException = new JsonException(e3);
                    jsonException.addTrace(field.getName() + " (" + cls.getName() + ")");
                    throw jsonException;
                }
            } else if (!this.ignoreUnknownFields) {
                throw new JsonException("Field not found: " + child.name() + " (" + cls.getName() + ")");
            }
        }
    }

    public <T> T readValue(String str, Class<T> cls, JsonValue jsonValue) {
        return readValue((Class) cls, null, jsonValue.get(str));
    }

    public <T> T readValue(String str, Class<T> cls, T t, JsonValue jsonValue) {
        JsonValue jsonValue2 = jsonValue.get(str);
        return jsonValue2 == null ? t : readValue((Class) cls, null, jsonValue2);
    }

    public <T> T readValue(String str, Class<T> cls, Class cls2, JsonValue jsonValue) {
        return readValue((Class) cls, cls2, jsonValue.get(str));
    }

    public <T> T readValue(String str, Class<T> cls, Class cls2, T t, JsonValue jsonValue) {
        JsonValue jsonValue2 = jsonValue.get(str);
        return jsonValue2 == null ? t : readValue((Class) cls, cls2, jsonValue2);
    }

    public <T> T readValue(Class<T> cls, Class cls2, T t, JsonValue jsonValue) {
        return readValue((Class) cls, cls2, jsonValue);
    }

    public <T> T readValue(Class<T> cls, JsonValue jsonValue) {
        return readValue((Class) cls, null, jsonValue);
    }

    /* JADX WARNING: Removed duplicated region for block: B:192:0x02e8  */
    /* JADX WARNING: Removed duplicated region for block: B:134:0x0215  */
    /* JADX WARNING: Missing block: B:128:0x01f8, code skipped:
            if (r7 != java.lang.Boolean.class) goto L_0x0205;
     */
    public <T> T readValue(java.lang.Class<T> r7, java.lang.Class r8, com.esotericsoftware.jsonbeans.JsonValue r9) {
        /*
        r6 = this;
        r3 = 0;
        r2 = 0;
        if (r9 != 0) goto L_0x0006;
    L_0x0004:
        r1 = r2;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r9.isObject();
        if (r0 == 0) goto L_0x00c0;
    L_0x000c:
        r0 = r6.typeName;
        if (r0 != 0) goto L_0x0048;
    L_0x0010:
        r0 = r2;
    L_0x0011:
        if (r0 == 0) goto L_0x001d;
    L_0x0013:
        r1 = r6.typeName;
        r9.remove(r1);
        r0 = java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x004f }
        r7 = r0;
    L_0x001d:
        r0 = java.lang.String.class;
        if (r7 == r0) goto L_0x0041;
    L_0x0021:
        r0 = java.lang.Integer.class;
        if (r7 == r0) goto L_0x0041;
    L_0x0025:
        r0 = java.lang.Boolean.class;
        if (r7 == r0) goto L_0x0041;
    L_0x0029:
        r0 = java.lang.Float.class;
        if (r7 == r0) goto L_0x0041;
    L_0x002d:
        r0 = java.lang.Long.class;
        if (r7 == r0) goto L_0x0041;
    L_0x0031:
        r0 = java.lang.Double.class;
        if (r7 == r0) goto L_0x0041;
    L_0x0035:
        r0 = java.lang.Short.class;
        if (r7 == r0) goto L_0x0041;
    L_0x0039:
        r0 = java.lang.Byte.class;
        if (r7 == r0) goto L_0x0041;
    L_0x003d:
        r0 = java.lang.Character.class;
        if (r7 != r0) goto L_0x0060;
    L_0x0041:
        r0 = "value";
        r1 = r6.readValue(r0, r7, r9);
        goto L_0x0005;
    L_0x0048:
        r0 = r6.typeName;
        r0 = r9.getString(r0, r2);
        goto L_0x0011;
    L_0x004f:
        r1 = move-exception;
        r3 = r6.tagToClass;
        r0 = r3.get(r0);
        r0 = (java.lang.Class) r0;
        if (r0 != 0) goto L_0x02eb;
    L_0x005a:
        r0 = new com.esotericsoftware.jsonbeans.JsonException;
        r0.m64init(r1);
        throw r0;
    L_0x0060:
        if (r7 == 0) goto L_0x009c;
    L_0x0062:
        r0 = r6.classToSerializer;
        r0 = r0.get(r7);
        r0 = (com.esotericsoftware.jsonbeans.Json.Serializer) r0;
        if (r0 == 0) goto L_0x0071;
    L_0x006c:
        r1 = r0.read(r6, r9, r7);
        goto L_0x0005;
    L_0x0071:
        r1 = r6.newInstance(r7);
        r0 = r1 instanceof com.esotericsoftware.jsonbeans.Json.Serializable;
        if (r0 == 0) goto L_0x0080;
    L_0x0079:
        r0 = r1;
        r0 = (com.esotericsoftware.jsonbeans.Json.Serializable) r0;
        r0.read(r6, r9);
        goto L_0x0005;
    L_0x0080:
        r0 = r1 instanceof java.util.HashMap;
        if (r0 == 0) goto L_0x009f;
    L_0x0084:
        r1 = (java.util.HashMap) r1;
        r0 = r9.child();
    L_0x008a:
        if (r0 == 0) goto L_0x0005;
    L_0x008c:
        r3 = r0.name();
        r4 = r6.readValue(r8, r2, r0);
        r1.put(r3, r4);
        r0 = r0.next();
        goto L_0x008a;
    L_0x009c:
        r1 = r9;
        goto L_0x0005;
    L_0x009f:
        r0 = r1 instanceof com.esotericsoftware.jsonbeans.ObjectMap;
        if (r0 == 0) goto L_0x00bb;
    L_0x00a3:
        r1 = (com.esotericsoftware.jsonbeans.ObjectMap) r1;
        r0 = r9.child();
    L_0x00a9:
        if (r0 == 0) goto L_0x0005;
    L_0x00ab:
        r3 = r0.name();
        r4 = r6.readValue(r8, r2, r0);
        r1.put(r3, r4);
        r0 = r0.next();
        goto L_0x00a9;
    L_0x00bb:
        r6.readFields(r1, r9);
        goto L_0x0005;
    L_0x00c0:
        if (r7 == 0) goto L_0x00d2;
    L_0x00c2:
        r0 = r6.classToSerializer;
        r0 = r0.get(r7);
        r0 = (com.esotericsoftware.jsonbeans.Json.Serializer) r0;
        if (r0 == 0) goto L_0x00d2;
    L_0x00cc:
        r1 = r0.read(r6, r9, r7);
        goto L_0x0005;
    L_0x00d2:
        r0 = r9.isArray();
        if (r0 == 0) goto L_0x015a;
    L_0x00d8:
        r0 = java.util.List.class;
        r0 = r0.isAssignableFrom(r7);
        if (r0 == 0) goto L_0x0103;
    L_0x00e0:
        if (r7 != 0) goto L_0x00f9;
    L_0x00e2:
        r0 = new java.util.ArrayList;
        r0.<init>();
    L_0x00e7:
        r1 = r9.child();
    L_0x00eb:
        if (r1 == 0) goto L_0x0100;
    L_0x00ed:
        r3 = r6.readValue(r8, r2, r1);
        r0.add(r3);
        r1 = r1.next();
        goto L_0x00eb;
    L_0x00f9:
        r0 = r6.newInstance(r7);
        r0 = (java.util.List) r0;
        goto L_0x00e7;
    L_0x0100:
        r1 = r0;
        goto L_0x0005;
    L_0x0103:
        r0 = r7.isArray();
        if (r0 == 0) goto L_0x012d;
    L_0x0109:
        r0 = r7.getComponentType();
        if (r8 != 0) goto L_0x0110;
    L_0x010f:
        r8 = r0;
    L_0x0110:
        r1 = r9.size();
        r1 = java.lang.reflect.Array.newInstance(r0, r1);
        r0 = r9.child();
    L_0x011c:
        if (r0 == 0) goto L_0x0005;
    L_0x011e:
        r4 = r3 + 1;
        r5 = r6.readValue(r8, r2, r0);
        java.lang.reflect.Array.set(r1, r3, r5);
        r0 = r0.next();
        r3 = r4;
        goto L_0x011c;
    L_0x012d:
        r0 = new com.esotericsoftware.jsonbeans.JsonException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to convert value to required type: ";
        r1 = r1.append(r2);
        r1 = r1.append(r9);
        r2 = " (";
        r1 = r1.append(r2);
        r2 = r7.getName();
        r1 = r1.append(r2);
        r2 = ")";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.m62init(r1);
        throw r0;
    L_0x015a:
        r0 = r9.isNumber();
        if (r0 == 0) goto L_0x01ea;
    L_0x0160:
        if (r7 == 0) goto L_0x016a;
    L_0x0162:
        r0 = java.lang.Float.TYPE;	 Catch:{ NumberFormatException -> 0x01df }
        if (r7 == r0) goto L_0x016a;
    L_0x0166:
        r0 = java.lang.Float.class;
        if (r7 != r0) goto L_0x0174;
    L_0x016a:
        r0 = r9.asFloat();	 Catch:{ NumberFormatException -> 0x01df }
        r1 = java.lang.Float.valueOf(r0);	 Catch:{ NumberFormatException -> 0x01df }
        goto L_0x0005;
    L_0x0174:
        r0 = java.lang.Integer.TYPE;	 Catch:{ NumberFormatException -> 0x01df }
        if (r7 == r0) goto L_0x017c;
    L_0x0178:
        r0 = java.lang.Integer.class;
        if (r7 != r0) goto L_0x0186;
    L_0x017c:
        r0 = r9.asInt();	 Catch:{ NumberFormatException -> 0x01df }
        r1 = java.lang.Integer.valueOf(r0);	 Catch:{ NumberFormatException -> 0x01df }
        goto L_0x0005;
    L_0x0186:
        r0 = java.lang.Long.TYPE;	 Catch:{ NumberFormatException -> 0x01df }
        if (r7 == r0) goto L_0x018e;
    L_0x018a:
        r0 = java.lang.Long.class;
        if (r7 != r0) goto L_0x0198;
    L_0x018e:
        r0 = r9.asLong();	 Catch:{ NumberFormatException -> 0x01df }
        r1 = java.lang.Long.valueOf(r0);	 Catch:{ NumberFormatException -> 0x01df }
        goto L_0x0005;
    L_0x0198:
        r0 = java.lang.Double.TYPE;	 Catch:{ NumberFormatException -> 0x01df }
        if (r7 == r0) goto L_0x01a0;
    L_0x019c:
        r0 = java.lang.Double.class;
        if (r7 != r0) goto L_0x01ab;
    L_0x01a0:
        r0 = r9.asFloat();	 Catch:{ NumberFormatException -> 0x01df }
        r0 = (double) r0;	 Catch:{ NumberFormatException -> 0x01df }
        r1 = java.lang.Double.valueOf(r0);	 Catch:{ NumberFormatException -> 0x01df }
        goto L_0x0005;
    L_0x01ab:
        r0 = java.lang.String.class;
        if (r7 != r0) goto L_0x01b9;
    L_0x01af:
        r0 = r9.asFloat();	 Catch:{ NumberFormatException -> 0x01df }
        r1 = java.lang.Float.toString(r0);	 Catch:{ NumberFormatException -> 0x01df }
        goto L_0x0005;
    L_0x01b9:
        r0 = java.lang.Short.TYPE;	 Catch:{ NumberFormatException -> 0x01df }
        if (r7 == r0) goto L_0x01c1;
    L_0x01bd:
        r0 = java.lang.Short.class;
        if (r7 != r0) goto L_0x01cc;
    L_0x01c1:
        r0 = r9.asInt();	 Catch:{ NumberFormatException -> 0x01df }
        r0 = (short) r0;	 Catch:{ NumberFormatException -> 0x01df }
        r1 = java.lang.Short.valueOf(r0);	 Catch:{ NumberFormatException -> 0x01df }
        goto L_0x0005;
    L_0x01cc:
        r0 = java.lang.Byte.TYPE;	 Catch:{ NumberFormatException -> 0x01df }
        if (r7 == r0) goto L_0x01d4;
    L_0x01d0:
        r0 = java.lang.Byte.class;
        if (r7 != r0) goto L_0x01e0;
    L_0x01d4:
        r0 = r9.asInt();	 Catch:{ NumberFormatException -> 0x01df }
        r0 = (byte) r0;	 Catch:{ NumberFormatException -> 0x01df }
        r1 = java.lang.Byte.valueOf(r0);	 Catch:{ NumberFormatException -> 0x01df }
        goto L_0x0005;
    L_0x01df:
        r0 = move-exception;
    L_0x01e0:
        r0 = new com.esotericsoftware.jsonbeans.JsonValue;
        r1 = r9.asString();
        r0.m73init(r1);
        r9 = r0;
    L_0x01ea:
        r0 = r9.isBoolean();
        if (r0 == 0) goto L_0x020f;
    L_0x01f0:
        if (r7 == 0) goto L_0x01fa;
    L_0x01f2:
        r0 = java.lang.Boolean.TYPE;	 Catch:{ NumberFormatException -> 0x0204 }
        if (r7 == r0) goto L_0x01fa;
    L_0x01f6:
        r0 = java.lang.Boolean.class;
        if (r7 != r0) goto L_0x0205;
    L_0x01fa:
        r0 = r9.asBoolean();	 Catch:{ NumberFormatException -> 0x0204 }
        r1 = java.lang.Boolean.valueOf(r0);	 Catch:{ NumberFormatException -> 0x0204 }
        goto L_0x0005;
    L_0x0204:
        r0 = move-exception;
    L_0x0205:
        r0 = new com.esotericsoftware.jsonbeans.JsonValue;
        r1 = r9.asString();
        r0.m73init(r1);
        r9 = r0;
    L_0x020f:
        r0 = r9.isString();
        if (r0 == 0) goto L_0x02e8;
    L_0x0215:
        r1 = r9.asString();
        if (r7 == 0) goto L_0x0005;
    L_0x021b:
        r0 = java.lang.String.class;
        if (r7 == r0) goto L_0x0005;
    L_0x021f:
        r0 = java.lang.Integer.TYPE;	 Catch:{ NumberFormatException -> 0x0273 }
        if (r7 == r0) goto L_0x0227;
    L_0x0223:
        r0 = java.lang.Integer.class;
        if (r7 != r0) goto L_0x022d;
    L_0x0227:
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ NumberFormatException -> 0x0273 }
        goto L_0x0005;
    L_0x022d:
        r0 = java.lang.Float.TYPE;	 Catch:{ NumberFormatException -> 0x0273 }
        if (r7 == r0) goto L_0x0235;
    L_0x0231:
        r0 = java.lang.Float.class;
        if (r7 != r0) goto L_0x023b;
    L_0x0235:
        r1 = java.lang.Float.valueOf(r1);	 Catch:{ NumberFormatException -> 0x0273 }
        goto L_0x0005;
    L_0x023b:
        r0 = java.lang.Long.TYPE;	 Catch:{ NumberFormatException -> 0x0273 }
        if (r7 == r0) goto L_0x0243;
    L_0x023f:
        r0 = java.lang.Long.class;
        if (r7 != r0) goto L_0x0249;
    L_0x0243:
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ NumberFormatException -> 0x0273 }
        goto L_0x0005;
    L_0x0249:
        r0 = java.lang.Double.TYPE;	 Catch:{ NumberFormatException -> 0x0273 }
        if (r7 == r0) goto L_0x0251;
    L_0x024d:
        r0 = java.lang.Double.class;
        if (r7 != r0) goto L_0x0257;
    L_0x0251:
        r1 = java.lang.Double.valueOf(r1);	 Catch:{ NumberFormatException -> 0x0273 }
        goto L_0x0005;
    L_0x0257:
        r0 = java.lang.Short.TYPE;	 Catch:{ NumberFormatException -> 0x0273 }
        if (r7 == r0) goto L_0x025f;
    L_0x025b:
        r0 = java.lang.Short.class;
        if (r7 != r0) goto L_0x0265;
    L_0x025f:
        r1 = java.lang.Short.valueOf(r1);	 Catch:{ NumberFormatException -> 0x0273 }
        goto L_0x0005;
    L_0x0265:
        r0 = java.lang.Byte.TYPE;	 Catch:{ NumberFormatException -> 0x0273 }
        if (r7 == r0) goto L_0x026d;
    L_0x0269:
        r0 = java.lang.Byte.class;
        if (r7 != r0) goto L_0x0274;
    L_0x026d:
        r1 = java.lang.Byte.valueOf(r1);	 Catch:{ NumberFormatException -> 0x0273 }
        goto L_0x0005;
    L_0x0273:
        r0 = move-exception;
    L_0x0274:
        r0 = java.lang.Boolean.TYPE;
        if (r7 == r0) goto L_0x027c;
    L_0x0278:
        r0 = java.lang.Boolean.class;
        if (r7 != r0) goto L_0x0282;
    L_0x027c:
        r1 = java.lang.Boolean.valueOf(r1);
        goto L_0x0005;
    L_0x0282:
        r0 = java.lang.Character.TYPE;
        if (r7 == r0) goto L_0x028a;
    L_0x0286:
        r0 = java.lang.Character.class;
        if (r7 != r0) goto L_0x0294;
    L_0x028a:
        r0 = r1.charAt(r3);
        r1 = java.lang.Character.valueOf(r0);
        goto L_0x0005;
    L_0x0294:
        r0 = java.lang.Enum.class;
        r0 = r0.isAssignableFrom(r7);
        if (r0 == 0) goto L_0x02b7;
    L_0x029c:
        r2 = r7.getEnumConstants();
        r4 = r2.length;
        r0 = r3;
    L_0x02a2:
        if (r0 >= r4) goto L_0x02b7;
    L_0x02a4:
        r3 = r2[r0];
        r3 = r3.toString();
        r3 = r1.equals(r3);
        if (r3 == 0) goto L_0x02b4;
    L_0x02b0:
        r1 = r2[r0];
        goto L_0x0005;
    L_0x02b4:
        r0 = r0 + 1;
        goto L_0x02a2;
    L_0x02b7:
        r0 = java.lang.CharSequence.class;
        if (r7 == r0) goto L_0x0005;
    L_0x02bb:
        r0 = new com.esotericsoftware.jsonbeans.JsonException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to convert value to required type: ";
        r1 = r1.append(r2);
        r1 = r1.append(r9);
        r2 = " (";
        r1 = r1.append(r2);
        r2 = r7.getName();
        r1 = r1.append(r2);
        r2 = ")";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.m62init(r1);
        throw r0;
    L_0x02e8:
        r1 = r2;
        goto L_0x0005;
    L_0x02eb:
        r7 = r0;
        goto L_0x001d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.jsonbeans.Json.readValue(java.lang.Class, java.lang.Class, com.esotericsoftware.jsonbeans.JsonValue):java.lang.Object");
    }

    private String convertToString(Object obj) {
        if (obj instanceof Class) {
            return ((Class) obj).getName();
        }
        return String.valueOf(obj);
    }

    private Object newInstance(Class cls) {
        Throwable e;
        try {
            return cls.newInstance();
        } catch (Exception e2) {
            e = e2;
            try {
                Constructor declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
                declaredConstructor.setAccessible(true);
                return declaredConstructor.newInstance(new Object[0]);
            } catch (SecurityException e3) {
            } catch (NoSuchMethodException e4) {
                if (cls.isArray()) {
                    throw new JsonException("Encountered JSON object when expected array of type: " + cls.getName(), e);
                } else if (!cls.isMemberClass() || Modifier.isStatic(cls.getModifiers())) {
                    throw new JsonException("Class cannot be created (missing no-arg constructor): " + cls.getName(), e);
                } else {
                    throw new JsonException("Class cannot be created (non-static member class): " + cls.getName(), e);
                }
            } catch (Exception e5) {
                e = e5;
            }
        }
        throw new JsonException("Error constructing instance of class: " + cls.getName(), e);
    }

    public String prettyPrint(Object obj) {
        return prettyPrint(obj, 0);
    }

    public String prettyPrint(String str) {
        return prettyPrint(str, 0);
    }

    public String prettyPrint(Object obj, int i) {
        return prettyPrint(toJson(obj), i);
    }

    public String prettyPrint(String str, int i) {
        return new JsonReader().parse(str).prettyPrint(this.outputType, i);
    }
}
