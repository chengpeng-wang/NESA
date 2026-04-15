package com.google.gson;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ParameterizedTypeHandlerMap;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.BigDecimalTypeAdapter;
import com.google.gson.internal.bind.BigIntegerTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.ExcludedTypeAdapterFactory;
import com.google.gson.internal.bind.JsonElementReader;
import com.google.gson.internal.bind.JsonElementWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.MiniGson;
import com.google.gson.internal.bind.MiniGson.Builder;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class Gson {
    static final AnonymousAndLocalClassExclusionStrategy DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY = new AnonymousAndLocalClassExclusionStrategy();
    private static final ExclusionStrategy DEFAULT_EXCLUSION_STRATEGY = createExclusionStrategy();
    static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    static final ModifierBasedExclusionStrategy DEFAULT_MODIFIER_BASED_EXCLUSION_STRATEGY = new ModifierBasedExclusionStrategy(128, 8);
    static final FieldNamingStrategy2 DEFAULT_NAMING_POLICY = new SerializedNameAnnotationInterceptingNamingPolicy(new JavaFieldNamingPolicy());
    static final SyntheticFieldExclusionStrategy DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY = new SyntheticFieldExclusionStrategy(true);
    static final ParameterizedTypeHandlerMap EMPTY_MAP = new ParameterizedTypeHandlerMap().makeUnmodifiable();
    private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    private final ConstructorConstructor constructorConstructor;
    /* access modifiers changed from: private|final */
    public final ExclusionStrategy deserializationExclusionStrategy;
    private final ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers;
    private final boolean generateNonExecutableJson;
    private final boolean htmlSafe;
    private final MiniGson miniGson;
    private final boolean prettyPrinting;
    /* access modifiers changed from: private|final */
    public final ExclusionStrategy serializationExclusionStrategy;
    private final boolean serializeNulls;
    private final ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers;

    public Gson() {
        this(DEFAULT_EXCLUSION_STRATEGY, DEFAULT_EXCLUSION_STRATEGY, DEFAULT_NAMING_POLICY, EMPTY_MAP, false, EMPTY_MAP, EMPTY_MAP, false, false, true, false, false, LongSerializationPolicy.DEFAULT, Collections.emptyList());
    }

    Gson(ExclusionStrategy deserializationExclusionStrategy, ExclusionStrategy serializationExclusionStrategy, final FieldNamingStrategy2 fieldNamingPolicy, ParameterizedTypeHandlerMap<InstanceCreator<?>> instanceCreators, boolean serializeNulls, ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, boolean complexMapKeySerialization, boolean generateNonExecutableGson, boolean htmlSafe, boolean prettyPrinting, boolean serializeSpecialFloatingPointValues, LongSerializationPolicy longSerializationPolicy, List<Factory> typeAdapterFactories) {
        this.deserializationExclusionStrategy = deserializationExclusionStrategy;
        this.serializationExclusionStrategy = serializationExclusionStrategy;
        this.constructorConstructor = new ConstructorConstructor(instanceCreators);
        this.serializeNulls = serializeNulls;
        this.serializers = serializers;
        this.deserializers = deserializers;
        this.generateNonExecutableJson = generateNonExecutableGson;
        this.htmlSafe = htmlSafe;
        this.prettyPrinting = prettyPrinting;
        Factory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(this.constructorConstructor) {
            public String getFieldName(Class<?> declaringClazz, Field f, Type declaredType) {
                return fieldNamingPolicy.translateName(new FieldAttributes(declaringClazz, f));
            }

            public boolean serializeField(Class<?> declaringClazz, Field f, Type declaredType) {
                ExclusionStrategy strategy = Gson.this.serializationExclusionStrategy;
                return (strategy.shouldSkipClass(f.getType()) || strategy.shouldSkipField(new FieldAttributes(declaringClazz, f))) ? false : true;
            }

            public boolean deserializeField(Class<?> declaringClazz, Field f, Type declaredType) {
                ExclusionStrategy strategy = Gson.this.deserializationExclusionStrategy;
                return (strategy.shouldSkipClass(f.getType()) || strategy.shouldSkipField(new FieldAttributes(declaringClazz, f))) ? false : true;
            }
        };
        Builder builder = new Builder().withoutDefaultFactories().factory(TypeAdapters.STRING_FACTORY).factory(TypeAdapters.INTEGER_FACTORY).factory(TypeAdapters.BOOLEAN_FACTORY).factory(TypeAdapters.BYTE_FACTORY).factory(TypeAdapters.SHORT_FACTORY).factory(TypeAdapters.newFactory(Long.TYPE, Long.class, longAdapter(longSerializationPolicy))).factory(TypeAdapters.newFactory(Double.TYPE, Double.class, doubleAdapter(serializeSpecialFloatingPointValues))).factory(TypeAdapters.newFactory(Float.TYPE, Float.class, floatAdapter(serializeSpecialFloatingPointValues))).factory(new ExcludedTypeAdapterFactory(serializationExclusionStrategy, deserializationExclusionStrategy)).factory(TypeAdapters.NUMBER_FACTORY).factory(TypeAdapters.CHARACTER_FACTORY).factory(TypeAdapters.STRING_BUILDER_FACTORY).factory(TypeAdapters.STRING_BUFFER_FACTORY).typeAdapter(BigDecimal.class, new BigDecimalTypeAdapter()).typeAdapter(BigInteger.class, new BigIntegerTypeAdapter()).factory(TypeAdapters.JSON_ELEMENT_FACTORY).factory(ObjectTypeAdapter.FACTORY);
        for (Factory factory : typeAdapterFactories) {
            builder.factory(factory);
        }
        builder.factory(new GsonToMiniGsonTypeAdapterFactory(this, serializers, deserializers)).factory(new CollectionTypeAdapterFactory(this.constructorConstructor)).factory(TypeAdapters.URL_FACTORY).factory(TypeAdapters.URI_FACTORY).factory(TypeAdapters.UUID_FACTORY).factory(TypeAdapters.LOCALE_FACTORY).factory(TypeAdapters.INET_ADDRESS_FACTORY).factory(TypeAdapters.BIT_SET_FACTORY).factory(DateTypeAdapter.FACTORY).factory(TypeAdapters.CALENDAR_FACTORY).factory(TimeTypeAdapter.FACTORY).factory(SqlDateTypeAdapter.FACTORY).factory(TypeAdapters.TIMESTAMP_FACTORY).factory(new MapTypeAdapterFactory(this.constructorConstructor, complexMapKeySerialization)).factory(ArrayTypeAdapter.FACTORY).factory(TypeAdapters.ENUM_FACTORY).factory(reflectiveTypeAdapterFactory);
        this.miniGson = builder.build();
    }

    private TypeAdapter<Number> doubleAdapter(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            public Double read(JsonReader reader) throws IOException {
                if (reader.peek() != JsonToken.NULL) {
                    return Double.valueOf(reader.nextDouble());
                }
                reader.nextNull();
                return null;
            }

            public void write(JsonWriter writer, Number value) throws IOException {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                Gson.this.checkValidFloatingPoint(value.doubleValue());
                writer.value(value);
            }
        };
    }

    private TypeAdapter<Number> floatAdapter(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            public Float read(JsonReader reader) throws IOException {
                if (reader.peek() != JsonToken.NULL) {
                    return Float.valueOf((float) reader.nextDouble());
                }
                reader.nextNull();
                return null;
            }

            public void write(JsonWriter writer, Number value) throws IOException {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                Gson.this.checkValidFloatingPoint((double) value.floatValue());
                writer.value(value);
            }
        };
    }

    /* access modifiers changed from: private */
    public void checkValidFloatingPoint(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(value + " is not a valid double value as per JSON specification. To override this" + " behavior, use GsonBuilder.serializeSpecialDoubleValues() method.");
        }
    }

    private TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {
            public Number read(JsonReader reader) throws IOException {
                if (reader.peek() != JsonToken.NULL) {
                    return Long.valueOf(reader.nextLong());
                }
                reader.nextNull();
                return null;
            }

            public void write(JsonWriter writer, Number value) throws IOException {
                if (value == null) {
                    writer.nullValue();
                } else {
                    writer.value(value.toString());
                }
            }
        };
    }

    private static ExclusionStrategy createExclusionStrategy() {
        List<ExclusionStrategy> strategies = new LinkedList();
        strategies.add(DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY);
        strategies.add(DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY);
        strategies.add(DEFAULT_MODIFIER_BASED_EXCLUSION_STRATEGY);
        return new DisjunctionExclusionStrategy(strategies);
    }

    public JsonElement toJsonTree(Object src) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        return toJsonTree(src, src.getClass());
    }

    public JsonElement toJsonTree(Object src, Type typeOfSrc) {
        JsonWriter writer = new JsonElementWriter();
        toJson(src, typeOfSrc, writer);
        return writer.get();
    }

    public String toJson(Object src) {
        if (src == null) {
            return toJson(JsonNull.INSTANCE);
        }
        return toJson(src, src.getClass());
    }

    public String toJson(Object src, Type typeOfSrc) {
        Appendable writer = new StringWriter();
        toJson(src, typeOfSrc, writer);
        return writer.toString();
    }

    public void toJson(Object src, Appendable writer) throws JsonIOException {
        if (src != null) {
            toJson(src, src.getClass(), writer);
        } else {
            toJson(JsonNull.INSTANCE, writer);
        }
    }

    public void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
        try {
            toJson(src, typeOfSrc, newJsonWriter(Streams.writerForAppendable(writer)));
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }

    public void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
        TypeAdapter<?> adapter = this.miniGson.getAdapter(TypeToken.get(typeOfSrc));
        boolean oldLenient = writer.isLenient();
        writer.setLenient(true);
        boolean oldHtmlSafe = writer.isHtmlSafe();
        writer.setHtmlSafe(this.htmlSafe);
        boolean oldSerializeNulls = writer.getSerializeNulls();
        writer.setSerializeNulls(this.serializeNulls);
        try {
            adapter.write(writer, src);
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            writer.setSerializeNulls(oldSerializeNulls);
        } catch (IOException e) {
            throw new JsonIOException(e);
        } catch (Throwable th) {
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            writer.setSerializeNulls(oldSerializeNulls);
        }
    }

    public String toJson(JsonElement jsonElement) {
        Appendable writer = new StringWriter();
        toJson(jsonElement, writer);
        return writer.toString();
    }

    public void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
        try {
            toJson(jsonElement, newJsonWriter(Streams.writerForAppendable(writer)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonWriter newJsonWriter(Writer writer) throws IOException {
        if (this.generateNonExecutableJson) {
            writer.write(JSON_NON_EXECUTABLE_PREFIX);
        }
        JsonWriter jsonWriter = new JsonWriter(writer);
        if (this.prettyPrinting) {
            jsonWriter.setIndent("  ");
        }
        jsonWriter.setSerializeNulls(this.serializeNulls);
        return jsonWriter;
    }

    public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
        boolean oldLenient = writer.isLenient();
        writer.setLenient(true);
        boolean oldHtmlSafe = writer.isHtmlSafe();
        writer.setHtmlSafe(this.htmlSafe);
        boolean oldSerializeNulls = writer.getSerializeNulls();
        writer.setSerializeNulls(this.serializeNulls);
        try {
            Streams.write(jsonElement, writer);
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            writer.setSerializeNulls(oldSerializeNulls);
        } catch (IOException e) {
            throw new JsonIOException(e);
        } catch (Throwable th) {
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            writer.setSerializeNulls(oldSerializeNulls);
        }
    }

    public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return Primitives.wrap(classOfT).cast(fromJson(json, (Type) classOfT));
    }

    public <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        return fromJson(new StringReader(json), typeOfT);
    }

    public <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        JsonReader jsonReader = new JsonReader(json);
        Object object = fromJson(jsonReader, (Type) classOfT);
        assertFullConsumption(object, jsonReader);
        return Primitives.wrap(classOfT).cast(object);
    }

    public <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        JsonReader jsonReader = new JsonReader(json);
        T object = fromJson(jsonReader, typeOfT);
        assertFullConsumption(object, jsonReader);
        return object;
    }

    private static void assertFullConsumption(Object obj, JsonReader reader) {
        if (obj != null) {
            try {
                if (reader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
            } catch (MalformedJsonException e) {
                throw new JsonSyntaxException(e);
            } catch (IOException e2) {
                throw new JsonIOException(e2);
            }
        }
    }

    public <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        boolean isEmpty = true;
        boolean oldLenient = reader.isLenient();
        reader.setLenient(true);
        try {
            reader.peek();
            isEmpty = false;
            Object read = this.miniGson.getAdapter(TypeToken.get(typeOfT)).read(reader);
            reader.setLenient(oldLenient);
            return read;
        } catch (EOFException e) {
            if (isEmpty) {
                reader.setLenient(oldLenient);
                return null;
            }
            throw new JsonSyntaxException(e);
        } catch (IllegalStateException e2) {
            throw new JsonSyntaxException(e2);
        } catch (IOException e22) {
            throw new JsonSyntaxException(e22);
        } catch (Throwable th) {
            reader.setLenient(oldLenient);
        }
    }

    public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        return Primitives.wrap(classOfT).cast(fromJson(json, (Type) classOfT));
    }

    public <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        return fromJson(new JsonElementReader(json), typeOfT);
    }

    public String toString() {
        return "{" + "serializeNulls:" + this.serializeNulls + ",serializers:" + this.serializers + ",deserializers:" + this.deserializers + ",instanceCreators:" + this.constructorConstructor + "}";
    }
}
