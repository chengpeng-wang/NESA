package com.google.gson.internal.bind;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.C$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactory implements Factory {
    private final ConstructorConstructor constructorConstructor;

    static abstract class BoundField {
        final boolean deserialized;
        final String name;
        final boolean serialized;

        public abstract void read(JsonReader jsonReader, Object obj) throws IOException, IllegalAccessException;

        public abstract void write(JsonWriter jsonWriter, Object obj) throws IOException, IllegalAccessException;

        protected BoundField(String name, boolean serialized, boolean deserialized) {
            this.name = name;
            this.serialized = serialized;
            this.deserialized = deserialized;
        }
    }

    public final class Adapter<T> extends TypeAdapter<T> {
        private final Map<String, BoundField> boundFields;
        private final ObjectConstructor<T> constructor;

        /* synthetic */ Adapter(ReflectiveTypeAdapterFactory x0, ObjectConstructor x1, Map x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        private Adapter(ObjectConstructor<T> constructor, Map<String, BoundField> boundFields) {
            this.constructor = constructor;
            this.boundFields = boundFields;
        }

        public T read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            T instance = this.constructor.construct();
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    BoundField field = (BoundField) this.boundFields.get(reader.nextName());
                    if (field == null || !field.deserialized) {
                        reader.skipValue();
                    } else {
                        field.read(reader, instance);
                    }
                }
                reader.endObject();
                return instance;
            } catch (IllegalStateException e) {
                throw new JsonSyntaxException(e);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            }
        }

        public void write(JsonWriter writer, T value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    if (boundField.serialized) {
                        writer.name(boundField.name);
                        boundField.write(writer, value);
                    }
                }
                writer.endObject();
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            }
        }
    }

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    /* access modifiers changed from: protected */
    public boolean serializeField(Class<?> cls, Field f, Type declaredType) {
        return !f.isSynthetic();
    }

    /* access modifiers changed from: protected */
    public boolean deserializeField(Class<?> cls, Field f, Type declaredType) {
        return !f.isSynthetic();
    }

    /* access modifiers changed from: protected */
    public String getFieldName(Class<?> cls, Field f, Type declaredType) {
        return f.getName();
    }

    public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        if (Object.class.isAssignableFrom(raw)) {
            return new Adapter(this, this.constructorConstructor.getConstructor(type), getBoundFields(context, type, raw), null);
        }
        return null;
    }

    private BoundField createBoundField(MiniGson context, Field field, String name, TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
        final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
        final MiniGson miniGson = context;
        final TypeToken<?> typeToken = fieldType;
        final Field field2 = field;
        return new BoundField(name, serialize, deserialize) {
            final TypeAdapter<?> typeAdapter = miniGson.getAdapter(typeToken);

            /* access modifiers changed from: 0000 */
            public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                new TypeAdapterRuntimeTypeWrapper(miniGson, this.typeAdapter, typeToken.getType()).write(writer, field2.get(value));
            }

            /* access modifiers changed from: 0000 */
            public void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
                Object fieldValue = this.typeAdapter.read(reader);
                if (fieldValue != null || !isPrimitive) {
                    field2.set(value, fieldValue);
                }
            }
        };
    }

    private Map<String, BoundField> getBoundFields(MiniGson context, TypeToken<?> type, Class<?> raw) {
        Map<String, BoundField> result = new LinkedHashMap();
        if (!raw.isInterface()) {
            Type declaredType = type.getType();
            while (raw != Object.class) {
                Field[] fields = raw.getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (Field field : fields) {
                    boolean serialize = serializeField(raw, field, declaredType);
                    boolean deserialize = deserializeField(raw, field, declaredType);
                    if (serialize || deserialize) {
                        BoundField boundField = createBoundField(context, field, getFieldName(raw, field, declaredType), TypeToken.get(C$Gson$Types.resolve(type.getType(), raw, field.getGenericType())), serialize, deserialize);
                        BoundField previous = (BoundField) result.put(boundField.name, boundField);
                        if (previous != null) {
                            throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named " + previous.name);
                        }
                    }
                }
                type = TypeToken.get(C$Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
                raw = type.getRawType();
            }
        }
        return result;
    }
}
