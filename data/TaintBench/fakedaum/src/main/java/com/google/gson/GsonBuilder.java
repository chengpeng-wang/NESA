package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import com.google.gson.internal.ParameterizedTypeHandlerMap;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class GsonBuilder {
    private static final ExposeAnnotationDeserializationExclusionStrategy exposeAnnotationDeserializationExclusionStrategy = new ExposeAnnotationDeserializationExclusionStrategy();
    private static final ExposeAnnotationSerializationExclusionStrategy exposeAnnotationSerializationExclusionStrategy = new ExposeAnnotationSerializationExclusionStrategy();
    private static final InnerClassExclusionStrategy innerClassExclusionStrategy = new InnerClassExclusionStrategy();
    private boolean complexMapKeySerialization = false;
    private String datePattern;
    private int dateStyle;
    private final Set<ExclusionStrategy> deserializeExclusionStrategies = new HashSet();
    private final ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers;
    private boolean escapeHtmlChars;
    private boolean excludeFieldsWithoutExposeAnnotation;
    private FieldNamingStrategy2 fieldNamingPolicy;
    private boolean generateNonExecutableJson;
    private double ignoreVersionsAfter;
    private final ParameterizedTypeHandlerMap<InstanceCreator<?>> instanceCreators;
    private LongSerializationPolicy longSerializationPolicy;
    private ModifierBasedExclusionStrategy modifierBasedExclusionStrategy;
    private boolean prettyPrinting;
    private final Set<ExclusionStrategy> serializeExclusionStrategies = new HashSet();
    private boolean serializeInnerClasses;
    private boolean serializeNulls;
    private boolean serializeSpecialFloatingPointValues;
    private final ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers;
    private int timeStyle;
    private final List<Factory> typeAdapterFactories = new ArrayList();

    public GsonBuilder() {
        this.deserializeExclusionStrategies.add(Gson.DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY);
        this.deserializeExclusionStrategies.add(Gson.DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY);
        this.serializeExclusionStrategies.add(Gson.DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY);
        this.serializeExclusionStrategies.add(Gson.DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY);
        this.ignoreVersionsAfter = -1.0d;
        this.serializeInnerClasses = true;
        this.prettyPrinting = false;
        this.escapeHtmlChars = true;
        this.modifierBasedExclusionStrategy = Gson.DEFAULT_MODIFIER_BASED_EXCLUSION_STRATEGY;
        this.excludeFieldsWithoutExposeAnnotation = false;
        this.longSerializationPolicy = LongSerializationPolicy.DEFAULT;
        this.fieldNamingPolicy = Gson.DEFAULT_NAMING_POLICY;
        this.instanceCreators = new ParameterizedTypeHandlerMap();
        this.serializers = new ParameterizedTypeHandlerMap();
        this.deserializers = new ParameterizedTypeHandlerMap();
        this.serializeNulls = false;
        this.dateStyle = 2;
        this.timeStyle = 2;
        this.serializeSpecialFloatingPointValues = false;
        this.generateNonExecutableJson = false;
    }

    public GsonBuilder setVersion(double ignoreVersionsAfter) {
        this.ignoreVersionsAfter = ignoreVersionsAfter;
        return this;
    }

    public GsonBuilder excludeFieldsWithModifiers(int... modifiers) {
        this.modifierBasedExclusionStrategy = new ModifierBasedExclusionStrategy(modifiers);
        return this;
    }

    public GsonBuilder generateNonExecutableJson() {
        this.generateNonExecutableJson = true;
        return this;
    }

    public GsonBuilder excludeFieldsWithoutExposeAnnotation() {
        this.excludeFieldsWithoutExposeAnnotation = true;
        return this;
    }

    public GsonBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }

    public GsonBuilder enableComplexMapKeySerialization() {
        this.complexMapKeySerialization = true;
        return this;
    }

    public GsonBuilder disableInnerClassSerialization() {
        this.serializeInnerClasses = false;
        return this;
    }

    public GsonBuilder setLongSerializationPolicy(LongSerializationPolicy serializationPolicy) {
        this.longSerializationPolicy = serializationPolicy;
        return this;
    }

    public GsonBuilder setFieldNamingPolicy(FieldNamingPolicy namingConvention) {
        return setFieldNamingStrategy(namingConvention.getFieldNamingPolicy());
    }

    public GsonBuilder setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        return setFieldNamingStrategy(new FieldNamingStrategy2Adapter(fieldNamingStrategy));
    }

    /* access modifiers changed from: 0000 */
    public GsonBuilder setFieldNamingStrategy(FieldNamingStrategy2 fieldNamingStrategy) {
        this.fieldNamingPolicy = new SerializedNameAnnotationInterceptingNamingPolicy(fieldNamingStrategy);
        return this;
    }

    public GsonBuilder setExclusionStrategies(ExclusionStrategy... strategies) {
        List<ExclusionStrategy> strategyList = Arrays.asList(strategies);
        this.serializeExclusionStrategies.addAll(strategyList);
        this.deserializeExclusionStrategies.addAll(strategyList);
        return this;
    }

    public GsonBuilder addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        this.serializeExclusionStrategies.add(strategy);
        return this;
    }

    public GsonBuilder addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        this.deserializeExclusionStrategies.add(strategy);
        return this;
    }

    public GsonBuilder setPrettyPrinting() {
        this.prettyPrinting = true;
        return this;
    }

    public GsonBuilder disableHtmlEscaping() {
        this.escapeHtmlChars = false;
        return this;
    }

    public GsonBuilder setDateFormat(String pattern) {
        this.datePattern = pattern;
        return this;
    }

    public GsonBuilder setDateFormat(int style) {
        this.dateStyle = style;
        this.datePattern = null;
        return this;
    }

    public GsonBuilder setDateFormat(int dateStyle, int timeStyle) {
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        this.datePattern = null;
        return this;
    }

    public GsonBuilder registerTypeAdapter(Type type, Object typeAdapter) {
        return registerTypeAdapter(type, typeAdapter, false);
    }

    private GsonBuilder registerTypeAdapter(Type type, Object typeAdapter, boolean isSystem) {
        boolean z = (typeAdapter instanceof JsonSerializer) || (typeAdapter instanceof JsonDeserializer) || (typeAdapter instanceof InstanceCreator) || (typeAdapter instanceof Factory);
        C$Gson$Preconditions.checkArgument(z);
        if (Primitives.isPrimitive(type) || Primitives.isWrapperType(type)) {
            throw new IllegalArgumentException("Cannot register type adapters for " + type);
        }
        if (typeAdapter instanceof InstanceCreator) {
            registerInstanceCreator(type, (InstanceCreator) typeAdapter, isSystem);
        }
        if (typeAdapter instanceof JsonSerializer) {
            registerSerializer(type, (JsonSerializer) typeAdapter, isSystem);
        }
        if (typeAdapter instanceof JsonDeserializer) {
            registerDeserializer(type, (JsonDeserializer) typeAdapter, isSystem);
        }
        if (typeAdapter instanceof Factory) {
            this.typeAdapterFactories.add((Factory) typeAdapter);
        }
        return this;
    }

    private <T> GsonBuilder registerInstanceCreator(Type typeOfT, InstanceCreator<? extends T> instanceCreator, boolean isSystem) {
        this.instanceCreators.register(typeOfT, instanceCreator, isSystem);
        return this;
    }

    private <T> GsonBuilder registerSerializer(Type typeOfT, JsonSerializer<T> serializer, boolean isSystem) {
        this.serializers.register(typeOfT, serializer, isSystem);
        return this;
    }

    private <T> GsonBuilder registerDeserializer(Type typeOfT, JsonDeserializer<T> deserializer, boolean isSystem) {
        this.deserializers.register(typeOfT, new JsonDeserializerExceptionWrapper(deserializer), isSystem);
        return this;
    }

    public GsonBuilder registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter) {
        return registerTypeHierarchyAdapter(baseType, typeAdapter, false);
    }

    private GsonBuilder registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter, boolean isSystem) {
        boolean z = (typeAdapter instanceof JsonSerializer) || (typeAdapter instanceof JsonDeserializer) || (typeAdapter instanceof InstanceCreator);
        C$Gson$Preconditions.checkArgument(z);
        if (typeAdapter instanceof InstanceCreator) {
            registerInstanceCreatorForTypeHierarchy(baseType, (InstanceCreator) typeAdapter, isSystem);
        }
        if (typeAdapter instanceof JsonSerializer) {
            registerSerializerForTypeHierarchy(baseType, (JsonSerializer) typeAdapter, isSystem);
        }
        if (typeAdapter instanceof JsonDeserializer) {
            registerDeserializerForTypeHierarchy(baseType, (JsonDeserializer) typeAdapter, isSystem);
        }
        return this;
    }

    private <T> GsonBuilder registerInstanceCreatorForTypeHierarchy(Class<?> classOfT, InstanceCreator<? extends T> instanceCreator, boolean isSystem) {
        this.instanceCreators.registerForTypeHierarchy(classOfT, instanceCreator, isSystem);
        return this;
    }

    private <T> GsonBuilder registerSerializerForTypeHierarchy(Class<?> classOfT, JsonSerializer<T> serializer, boolean isSystem) {
        this.serializers.registerForTypeHierarchy(classOfT, serializer, isSystem);
        return this;
    }

    private <T> GsonBuilder registerDeserializerForTypeHierarchy(Class<?> classOfT, JsonDeserializer<T> deserializer, boolean isSystem) {
        this.deserializers.registerForTypeHierarchy(classOfT, new JsonDeserializerExceptionWrapper(deserializer), isSystem);
        return this;
    }

    public GsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }

    public Gson create() {
        List<ExclusionStrategy> linkedList = new LinkedList(this.deserializeExclusionStrategies);
        linkedList = new LinkedList(this.serializeExclusionStrategies);
        linkedList.add(this.modifierBasedExclusionStrategy);
        linkedList.add(this.modifierBasedExclusionStrategy);
        if (!this.serializeInnerClasses) {
            linkedList.add(innerClassExclusionStrategy);
            linkedList.add(innerClassExclusionStrategy);
        }
        if (this.ignoreVersionsAfter != -1.0d) {
            VersionExclusionStrategy versionExclusionStrategy = new VersionExclusionStrategy(this.ignoreVersionsAfter);
            linkedList.add(versionExclusionStrategy);
            linkedList.add(versionExclusionStrategy);
        }
        if (this.excludeFieldsWithoutExposeAnnotation) {
            linkedList.add(exposeAnnotationDeserializationExclusionStrategy);
            linkedList.add(exposeAnnotationSerializationExclusionStrategy);
        }
        addTypeAdaptersForDate(this.datePattern, this.dateStyle, this.timeStyle, this.serializers, this.deserializers);
        return new Gson(new DisjunctionExclusionStrategy(linkedList), new DisjunctionExclusionStrategy(linkedList), this.fieldNamingPolicy, this.instanceCreators.copyOf().makeUnmodifiable(), this.serializeNulls, this.serializers.copyOf().makeUnmodifiable(), this.deserializers.copyOf().makeUnmodifiable(), this.complexMapKeySerialization, this.generateNonExecutableJson, this.escapeHtmlChars, this.prettyPrinting, this.serializeSpecialFloatingPointValues, this.longSerializationPolicy, this.typeAdapterFactories);
    }

    private static void addTypeAdaptersForDate(String datePattern, int dateStyle, int timeStyle, ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers) {
        DefaultDateTypeAdapter dateTypeAdapter = null;
        if (datePattern != null && !"".equals(datePattern.trim())) {
            dateTypeAdapter = new DefaultDateTypeAdapter(datePattern);
        } else if (!(dateStyle == 2 || timeStyle == 2)) {
            dateTypeAdapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);
        }
        if (dateTypeAdapter != null) {
            registerIfAbsent(Date.class, serializers, dateTypeAdapter);
            registerIfAbsent(Date.class, deserializers, dateTypeAdapter);
            registerIfAbsent(Timestamp.class, serializers, dateTypeAdapter);
            registerIfAbsent(Timestamp.class, deserializers, dateTypeAdapter);
            registerIfAbsent(java.sql.Date.class, serializers, dateTypeAdapter);
            registerIfAbsent(java.sql.Date.class, deserializers, dateTypeAdapter);
        }
    }

    private static <T> void registerIfAbsent(Class<?> type, ParameterizedTypeHandlerMap<T> adapters, T adapter) {
        if (!adapters.hasSpecificHandlerFor(type)) {
            adapters.register(type, adapter, false);
        }
    }
}
