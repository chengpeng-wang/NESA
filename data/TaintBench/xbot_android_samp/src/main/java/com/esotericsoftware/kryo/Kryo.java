package com.esotericsoftware.kryo;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.BooleanArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.ByteArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.CharArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.DoubleArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.FloatArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.IntArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.LongArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.ObjectArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.ShortArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.StringArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigDecimalSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigIntegerSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BooleanSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.ByteSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CalendarSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CharSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.ClassSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsEmptyListSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsEmptyMapSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsEmptySetSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsSingletonListSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsSingletonMapSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsSingletonSetSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CurrencySerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.DateSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.DoubleSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.EnumSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.EnumSetSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.FloatSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.IntSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.KryoSerializableSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.LongSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.ShortSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.StringBufferSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.StringBuilderSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.StringSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.TimeZoneSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.TreeMapSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.esotericsoftware.kryo.util.DefaultClassResolver;
import com.esotericsoftware.kryo.util.IdentityMap;
import com.esotericsoftware.kryo.util.IntArray;
import com.esotericsoftware.kryo.util.MapReferenceResolver;
import com.esotericsoftware.kryo.util.ObjectMap;
import com.esotericsoftware.kryo.util.Util;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

public class Kryo {
    public static final byte NOT_NULL = (byte) 1;
    private static final int NO_REF = -2;
    public static final byte NULL = (byte) 0;
    private static final int REF = -1;
    private boolean autoReset;
    private ClassLoader classLoader;
    private final ClassResolver classResolver;
    private ObjectMap context;
    private int copyDepth;
    private boolean copyShallow;
    private Class<? extends Serializer> defaultSerializer;
    private final ArrayList<DefaultSerializerEntry> defaultSerializers;
    private int depth;
    private ObjectMap graphContext;
    private final int lowPriorityDefaultSerializerCount;
    private int maxDepth;
    private Class memoizedClass;
    private Registration memoizedClassValue;
    private Object needsCopyReference;
    private int nextRegisterID;
    private IdentityMap originalToCopy;
    private Object readObject;
    private final IntArray readReferenceIds;
    private ReferenceResolver referenceResolver;
    private boolean references;
    private boolean registrationRequired;
    private InstantiatorStrategy strategy;
    private volatile Thread thread;

    static final class DefaultSerializerEntry {
        Serializer serializer;
        Class<? extends Serializer> serializerClass;
        Class type;

        DefaultSerializerEntry() {
        }
    }

    public Kryo() {
        this(new DefaultClassResolver(), new MapReferenceResolver());
    }

    public Kryo(ReferenceResolver referenceResolver) {
        this(new DefaultClassResolver(), referenceResolver);
    }

    public Kryo(ClassResolver classResolver, ReferenceResolver referenceResolver) {
        this.defaultSerializer = FieldSerializer.class;
        this.defaultSerializers = new ArrayList(32);
        this.classLoader = getClass().getClassLoader();
        this.maxDepth = Integer.MAX_VALUE;
        this.autoReset = true;
        this.readReferenceIds = new IntArray(0);
        if (classResolver == null) {
            throw new IllegalArgumentException("classResolver cannot be null.");
        }
        this.classResolver = classResolver;
        classResolver.setKryo(this);
        this.referenceResolver = referenceResolver;
        if (referenceResolver != null) {
            referenceResolver.setKryo(this);
            this.references = true;
        }
        addDefaultSerializer(byte[].class, ByteArraySerializer.class);
        addDefaultSerializer(char[].class, CharArraySerializer.class);
        addDefaultSerializer(short[].class, ShortArraySerializer.class);
        addDefaultSerializer(int[].class, IntArraySerializer.class);
        addDefaultSerializer(long[].class, LongArraySerializer.class);
        addDefaultSerializer(float[].class, FloatArraySerializer.class);
        addDefaultSerializer(double[].class, DoubleArraySerializer.class);
        addDefaultSerializer(boolean[].class, BooleanArraySerializer.class);
        addDefaultSerializer(String[].class, StringArraySerializer.class);
        addDefaultSerializer(Object[].class, ObjectArraySerializer.class);
        addDefaultSerializer(BigInteger.class, BigIntegerSerializer.class);
        addDefaultSerializer(BigDecimal.class, BigDecimalSerializer.class);
        addDefaultSerializer(Class.class, ClassSerializer.class);
        addDefaultSerializer(Date.class, DateSerializer.class);
        addDefaultSerializer(Enum.class, EnumSerializer.class);
        addDefaultSerializer(EnumSet.class, EnumSetSerializer.class);
        addDefaultSerializer(Currency.class, CurrencySerializer.class);
        addDefaultSerializer(StringBuffer.class, StringBufferSerializer.class);
        addDefaultSerializer(StringBuilder.class, StringBuilderSerializer.class);
        addDefaultSerializer(Collections.EMPTY_LIST.getClass(), CollectionsEmptyListSerializer.class);
        addDefaultSerializer(Collections.EMPTY_MAP.getClass(), CollectionsEmptyMapSerializer.class);
        addDefaultSerializer(Collections.EMPTY_SET.getClass(), CollectionsEmptySetSerializer.class);
        addDefaultSerializer(Collections.singletonList(null).getClass(), CollectionsSingletonListSerializer.class);
        addDefaultSerializer(Collections.singletonMap(null, null).getClass(), CollectionsSingletonMapSerializer.class);
        addDefaultSerializer(Collections.singleton(null).getClass(), CollectionsSingletonSetSerializer.class);
        addDefaultSerializer(Collection.class, CollectionSerializer.class);
        addDefaultSerializer(TreeMap.class, TreeMapSerializer.class);
        addDefaultSerializer(Map.class, MapSerializer.class);
        addDefaultSerializer(KryoSerializable.class, KryoSerializableSerializer.class);
        addDefaultSerializer(TimeZone.class, TimeZoneSerializer.class);
        addDefaultSerializer(Calendar.class, CalendarSerializer.class);
        this.lowPriorityDefaultSerializerCount = this.defaultSerializers.size();
        register(Integer.TYPE, new IntSerializer());
        register(String.class, new StringSerializer());
        register(Float.TYPE, new FloatSerializer());
        register(Boolean.TYPE, new BooleanSerializer());
        register(Byte.TYPE, new ByteSerializer());
        register(Character.TYPE, new CharSerializer());
        register(Short.TYPE, new ShortSerializer());
        register(Long.TYPE, new LongSerializer());
        register(Double.TYPE, new DoubleSerializer());
    }

    public void setDefaultSerializer(Class<? extends Serializer> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        }
        this.defaultSerializer = cls;
    }

    public void addDefaultSerializer(Class cls, Serializer serializer) {
        if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            DefaultSerializerEntry defaultSerializerEntry = new DefaultSerializerEntry();
            defaultSerializerEntry.type = cls;
            defaultSerializerEntry.serializer = serializer;
            this.defaultSerializers.add(this.defaultSerializers.size() - this.lowPriorityDefaultSerializerCount, defaultSerializerEntry);
        }
    }

    public void addDefaultSerializer(Class cls, Class<? extends Serializer> cls2) {
        if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (cls2 == null) {
            throw new IllegalArgumentException("serializerClass cannot be null.");
        } else {
            DefaultSerializerEntry defaultSerializerEntry = new DefaultSerializerEntry();
            defaultSerializerEntry.type = cls;
            defaultSerializerEntry.serializerClass = cls2;
            this.defaultSerializers.add(this.defaultSerializers.size() - this.lowPriorityDefaultSerializerCount, defaultSerializerEntry);
        }
    }

    public Serializer getDefaultSerializer(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (cls.isAnnotationPresent(DefaultSerializer.class)) {
            return newSerializer(((DefaultSerializer) cls.getAnnotation(DefaultSerializer.class)).value(), cls);
        } else {
            int size = this.defaultSerializers.size();
            int i = 0;
            while (i < size) {
                DefaultSerializerEntry defaultSerializerEntry = (DefaultSerializerEntry) this.defaultSerializers.get(i);
                if (!defaultSerializerEntry.type.isAssignableFrom(cls)) {
                    i++;
                } else if (defaultSerializerEntry.serializer != null) {
                    return defaultSerializerEntry.serializer;
                } else {
                    return newSerializer(defaultSerializerEntry.serializerClass, cls);
                }
            }
            return newDefaultSerializer(cls);
        }
    }

    /* access modifiers changed from: protected */
    public Serializer newDefaultSerializer(Class cls) {
        return newSerializer(this.defaultSerializer, cls);
    }

    public Serializer newSerializer(Class<? extends Serializer> cls, Class cls2) {
        try {
            return (Serializer) cls.getConstructor(new Class[]{Kryo.class, Class.class}).newInstance(new Object[]{this, cls2});
        } catch (NoSuchMethodException e) {
            try {
                return (Serializer) cls.getConstructor(new Class[]{Kryo.class}).newInstance(new Object[]{this});
            } catch (NoSuchMethodException e2) {
                try {
                    return (Serializer) cls.getConstructor(new Class[]{Class.class}).newInstance(new Object[]{cls2});
                } catch (NoSuchMethodException e3) {
                    try {
                        return (Serializer) cls.newInstance();
                    } catch (Exception e4) {
                        throw new IllegalArgumentException("Unable to create serializer \"" + cls.getName() + "\" for class: " + Util.className(cls2), e4);
                    }
                }
            }
        }
    }

    public Registration register(Class cls) {
        Registration registration = this.classResolver.getRegistration(cls);
        return registration != null ? registration : register(cls, getDefaultSerializer(cls));
    }

    public Registration register(Class cls, int i) {
        Registration registration = this.classResolver.getRegistration(cls);
        return registration != null ? registration : register(cls, getDefaultSerializer(cls), i);
    }

    public Registration register(Class cls, Serializer serializer) {
        Registration registration = this.classResolver.getRegistration(cls);
        if (registration == null) {
            return this.classResolver.register(new Registration(cls, serializer, getNextRegistrationId()));
        }
        registration.setSerializer(serializer);
        return registration;
    }

    public Registration register(Class cls, Serializer serializer, int i) {
        if (i >= 0) {
            return register(new Registration(cls, serializer, i));
        }
        throw new IllegalArgumentException("id must be >= 0: " + i);
    }

    public Registration register(Registration registration) {
        int id = registration.getId();
        if (id < 0) {
            throw new IllegalArgumentException("id must be > 0: " + id);
        }
        Registration registration2 = getRegistration(registration.getId());
        if (registration2 == null || registration2.getType() == registration.getType()) {
            return this.classResolver.register(registration);
        }
        throw new KryoException("An existing registration with a different type already uses ID: " + registration.getId() + "\nExisting registration: " + registration2 + "\nUnable to set registration: " + registration);
    }

    public int getNextRegistrationId() {
        int i = this.nextRegisterID;
        while (this.classResolver.getRegistration(i) != null) {
            i++;
        }
        return i;
    }

    public Registration getRegistration(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (cls == this.memoizedClass) {
            return this.memoizedClassValue;
        } else {
            Registration registration = this.classResolver.getRegistration(cls);
            if (registration == null) {
                if (Proxy.isProxyClass(cls)) {
                    registration = getRegistration(InvocationHandler.class);
                } else if (!cls.isEnum() && Enum.class.isAssignableFrom(cls)) {
                    registration = getRegistration(cls.getEnclosingClass());
                } else if (EnumSet.class.isAssignableFrom(cls)) {
                    registration = this.classResolver.getRegistration(EnumSet.class);
                }
                if (registration == null) {
                    if (this.registrationRequired) {
                        throw new IllegalArgumentException("Class is not registered: " + Util.className(cls) + "\nNote: To register this class use: kryo.register(" + Util.className(cls) + ".class);");
                    }
                    registration = this.classResolver.registerImplicit(cls);
                }
            }
            this.memoizedClass = cls;
            this.memoizedClassValue = registration;
            return registration;
        }
    }

    public Registration getRegistration(int i) {
        return this.classResolver.getRegistration(i);
    }

    public Serializer getSerializer(Class cls) {
        return getRegistration(cls).getSerializer();
    }

    public Registration writeClass(Output output, Class cls) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        }
        try {
            Registration writeClass = this.classResolver.writeClass(output, cls);
            return writeClass;
        } finally {
            if (this.depth == 0 && this.autoReset) {
                reset();
            }
        }
    }

    public void writeObject(Output output, Object obj) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        } else if (obj == null) {
            throw new IllegalArgumentException("object cannot be null.");
        } else {
            beginObject();
            try {
                if (!this.references || !writeReferenceOrNull(output, obj, false)) {
                    getRegistration(obj.getClass()).getSerializer().write(this, output, obj);
                    int i = this.depth - 1;
                    this.depth = i;
                    if (i == 0 && this.autoReset) {
                        reset();
                    }
                }
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public void writeObject(Output output, Object obj, Serializer serializer) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        } else if (obj == null) {
            throw new IllegalArgumentException("object cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            beginObject();
            try {
                if (!this.references || !writeReferenceOrNull(output, obj, false)) {
                    serializer.write(this, output, obj);
                    int i = this.depth - 1;
                    this.depth = i;
                    if (i == 0 && this.autoReset) {
                        reset();
                    }
                }
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public void writeObjectOrNull(Output output, Object obj, Class cls) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        }
        beginObject();
        try {
            int i;
            Serializer serializer = getRegistration(cls).getSerializer();
            if (this.references) {
                if (writeReferenceOrNull(output, obj, true)) {
                    return;
                }
            } else if (!serializer.getAcceptsNull()) {
                if (obj == null) {
                    output.writeByte((byte) 0);
                    i = this.depth - 1;
                    this.depth = i;
                    if (i == 0 && this.autoReset) {
                        reset();
                        return;
                    }
                    return;
                }
                output.writeByte((byte) 1);
            }
            serializer.write(this, output, obj);
            i = this.depth - 1;
            this.depth = i;
            if (i == 0 && this.autoReset) {
                reset();
            }
        } finally {
            int i2 = this.depth - 1;
            this.depth = i2;
            if (i2 == 0 && this.autoReset) {
                reset();
            }
        }
    }

    public void writeObjectOrNull(Output output, Object obj, Serializer serializer) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            beginObject();
            try {
                int i;
                if (this.references) {
                    if (writeReferenceOrNull(output, obj, true)) {
                        return;
                    }
                } else if (!serializer.getAcceptsNull()) {
                    if (obj == null) {
                        output.writeByte((byte) 0);
                        i = this.depth - 1;
                        this.depth = i;
                        if (i == 0 && this.autoReset) {
                            reset();
                            return;
                        }
                        return;
                    }
                    output.writeByte((byte) 1);
                }
                serializer.write(this, output, obj);
                i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public void writeClassAndObject(Output output, Object obj) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        }
        beginObject();
        if (obj == null) {
            try {
                writeClass(output, null);
            } finally {
                int i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
            }
        } else {
            Registration writeClass = writeClass(output, obj.getClass());
            int i2;
            if (this.references && writeReferenceOrNull(output, obj, false)) {
                i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                    return;
                }
                return;
            }
            writeClass.getSerializer().write(this, output, obj);
            i2 = this.depth - 1;
            this.depth = i2;
            if (i2 == 0 && this.autoReset) {
                reset();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean writeReferenceOrNull(Output output, Object obj, boolean z) {
        if (obj == null) {
            output.writeByte((byte) 0);
            return true;
        } else if (this.referenceResolver.useReferences(obj.getClass())) {
            int writtenId = this.referenceResolver.getWrittenId(obj);
            if (writtenId != -1) {
                output.writeInt(writtenId + 2, true);
                return true;
            }
            this.referenceResolver.addWrittenObject(obj);
            output.writeByte((byte) 1);
            return false;
        } else {
            if (z) {
                output.writeByte((byte) 1);
            }
            return false;
        }
    }

    public Registration readClass(Input input) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        }
        try {
            Registration readClass = this.classResolver.readClass(input);
            return readClass;
        } finally {
            if (this.depth == 0 && this.autoReset) {
                reset();
            }
        }
    }

    public <T> T readObject(Input input, Class<T> cls) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        } else if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else {
            beginObject();
            int readReferenceOrNull;
            try {
                T t;
                if (this.references) {
                    readReferenceOrNull = readReferenceOrNull(input, cls, false);
                    if (readReferenceOrNull == -1) {
                        t = this.readObject;
                        return t;
                    }
                    t = getRegistration((Class) cls).getSerializer().read(this, input, cls);
                    if (readReferenceOrNull == this.readReferenceIds.size) {
                        reference(t);
                    }
                } else {
                    t = getRegistration((Class) cls).getSerializer().read(this, input, cls);
                }
                readReferenceOrNull = this.depth - 1;
                this.depth = readReferenceOrNull;
                if (readReferenceOrNull == 0 && this.autoReset) {
                    reset();
                }
                return t;
            } finally {
                readReferenceOrNull = this.depth - 1;
                this.depth = readReferenceOrNull;
                if (readReferenceOrNull == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public <T> T readObject(Input input, Class<T> cls, Serializer serializer) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        } else if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            beginObject();
            int readReferenceOrNull;
            try {
                T t;
                if (this.references) {
                    readReferenceOrNull = readReferenceOrNull(input, cls, false);
                    if (readReferenceOrNull == -1) {
                        t = this.readObject;
                        return t;
                    }
                    t = serializer.read(this, input, cls);
                    if (readReferenceOrNull == this.readReferenceIds.size) {
                        reference(t);
                    }
                } else {
                    t = serializer.read(this, input, cls);
                }
                readReferenceOrNull = this.depth - 1;
                this.depth = readReferenceOrNull;
                if (readReferenceOrNull == 0 && this.autoReset) {
                    reset();
                }
                return t;
            } finally {
                readReferenceOrNull = this.depth - 1;
                this.depth = readReferenceOrNull;
                if (readReferenceOrNull == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public <T> T readObjectOrNull(Input input, Class<T> cls) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        } else if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else {
            beginObject();
            int readReferenceOrNull;
            try {
                T t;
                if (this.references) {
                    readReferenceOrNull = readReferenceOrNull(input, cls, true);
                    if (readReferenceOrNull == -1) {
                        t = this.readObject;
                        return t;
                    }
                    t = getRegistration((Class) cls).getSerializer().read(this, input, cls);
                    if (readReferenceOrNull == this.readReferenceIds.size) {
                        reference(t);
                    }
                } else {
                    Serializer serializer = getRegistration((Class) cls).getSerializer();
                    if (serializer.getAcceptsNull() || input.readByte() != (byte) 0) {
                        t = serializer.read(this, input, cls);
                    } else {
                        t = null;
                        readReferenceOrNull = this.depth - 1;
                        this.depth = readReferenceOrNull;
                        if (readReferenceOrNull == 0 && this.autoReset) {
                            reset();
                        }
                        return t;
                    }
                }
                readReferenceOrNull = this.depth - 1;
                this.depth = readReferenceOrNull;
                if (readReferenceOrNull == 0 && this.autoReset) {
                    reset();
                }
                return t;
            } finally {
                readReferenceOrNull = this.depth - 1;
                this.depth = readReferenceOrNull;
                if (readReferenceOrNull == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public <T> T readObjectOrNull(Input input, Class<T> cls, Serializer serializer) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        } else if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            beginObject();
            int readReferenceOrNull;
            try {
                T t;
                if (this.references) {
                    readReferenceOrNull = readReferenceOrNull(input, cls, true);
                    if (readReferenceOrNull == -1) {
                        t = this.readObject;
                        return t;
                    }
                    t = serializer.read(this, input, cls);
                    if (readReferenceOrNull == this.readReferenceIds.size) {
                        reference(t);
                    }
                } else if (serializer.getAcceptsNull() || input.readByte() != (byte) 0) {
                    t = serializer.read(this, input, cls);
                } else {
                    t = null;
                    readReferenceOrNull = this.depth - 1;
                    this.depth = readReferenceOrNull;
                    if (readReferenceOrNull == 0 && this.autoReset) {
                        reset();
                    }
                    return t;
                }
                readReferenceOrNull = this.depth - 1;
                this.depth = readReferenceOrNull;
                if (readReferenceOrNull == 0 && this.autoReset) {
                    reset();
                }
                return t;
            } finally {
                readReferenceOrNull = this.depth - 1;
                this.depth = readReferenceOrNull;
                if (readReferenceOrNull == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public Object readClassAndObject(Input input) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        }
        beginObject();
        int i;
        try {
            Object obj;
            Registration readClass = readClass(input);
            if (readClass == null) {
                obj = null;
            } else {
                Class type = readClass.getType();
                if (this.references) {
                    int readReferenceOrNull = readReferenceOrNull(input, type, false);
                    if (readReferenceOrNull == -1) {
                        obj = this.readObject;
                        i = this.depth - 1;
                        this.depth = i;
                        if (i == 0 && this.autoReset) {
                            reset();
                        }
                    } else {
                        obj = readClass.getSerializer().read(this, input, type);
                        if (readReferenceOrNull == this.readReferenceIds.size) {
                            reference(obj);
                        }
                    }
                } else {
                    obj = readClass.getSerializer().read(this, input, type);
                }
                i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
            }
            return obj;
        } finally {
            i = this.depth - 1;
            this.depth = i;
            if (i == 0 && this.autoReset) {
                reset();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public int readReferenceOrNull(Input input, Class cls, boolean z) {
        int readInt;
        if (cls.isPrimitive()) {
            cls = Util.getWrapperClass(cls);
        }
        boolean useReferences = this.referenceResolver.useReferences(cls);
        if (z) {
            readInt = input.readInt(true);
            if (readInt == 0) {
                this.readObject = null;
                return -1;
            } else if (!useReferences) {
                this.readReferenceIds.add(-2);
                return this.readReferenceIds.size;
            }
        } else if (useReferences) {
            readInt = input.readInt(true);
        } else {
            this.readReferenceIds.add(-2);
            return this.readReferenceIds.size;
        }
        if (readInt == 1) {
            this.readReferenceIds.add(this.referenceResolver.nextReadId(cls));
            return this.readReferenceIds.size;
        }
        this.readObject = this.referenceResolver.getReadObject(cls, readInt - 2);
        return -1;
    }

    public void reference(Object obj) {
        if (this.copyDepth > 0) {
            if (this.needsCopyReference == null) {
                return;
            }
            if (obj == null) {
                throw new IllegalArgumentException("object cannot be null.");
            }
            this.originalToCopy.put(this.needsCopyReference, obj);
            this.needsCopyReference = null;
        } else if (this.references && obj != null) {
            int pop = this.readReferenceIds.pop();
            if (pop != -2) {
                this.referenceResolver.addReadObject(pop, obj);
            }
        }
    }

    public void reset() {
        this.depth = 0;
        if (this.graphContext != null) {
            this.graphContext.clear();
        }
        this.classResolver.reset();
        if (this.references) {
            this.referenceResolver.reset();
            this.readObject = null;
        }
        this.copyDepth = 0;
        if (this.originalToCopy != null) {
            this.originalToCopy.clear();
        }
    }

    public <T> T copy(T t) {
        if (t == null) {
            return null;
        }
        if (this.copyShallow) {
            return t;
        }
        this.copyDepth++;
        try {
            if (this.originalToCopy == null) {
                this.originalToCopy = new IdentityMap();
            }
            T t2 = this.originalToCopy.get(t);
            if (t2 != null) {
                return t2;
            }
            this.needsCopyReference = t;
            if (t instanceof KryoCopyable) {
                t = ((KryoCopyable) t).copy(this);
            } else {
                t = getSerializer(t.getClass()).copy(this, t);
            }
            if (this.needsCopyReference != null) {
                reference(t);
            }
            int i = this.copyDepth - 1;
            this.copyDepth = i;
            if (i != 0) {
                return t;
            }
            reset();
            return t;
        } finally {
            int i2 = this.copyDepth - 1;
            this.copyDepth = i2;
            if (i2 == 0) {
                reset();
            }
        }
    }

    public <T> T copy(T t, Serializer serializer) {
        if (t == null) {
            return null;
        }
        if (this.copyShallow) {
            return t;
        }
        this.copyDepth++;
        try {
            if (this.originalToCopy == null) {
                this.originalToCopy = new IdentityMap();
            }
            T t2 = this.originalToCopy.get(t);
            if (t2 != null) {
                return t2;
            }
            this.needsCopyReference = t;
            if (t instanceof KryoCopyable) {
                t = ((KryoCopyable) t).copy(this);
            } else {
                t = serializer.copy(this, t);
            }
            if (this.needsCopyReference != null) {
                reference(t);
            }
            int i = this.copyDepth - 1;
            this.copyDepth = i;
            if (i != 0) {
                return t;
            }
            reset();
            return t;
        } finally {
            int i2 = this.copyDepth - 1;
            this.copyDepth = i2;
            if (i2 == 0) {
                reset();
            }
        }
    }

    /* JADX WARNING: Failed to extract finally block: empty outs */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            return r0;
     */
    public <T> T copyShallow(T r4) {
        /*
        r3 = this;
        r2 = 0;
        if (r4 != 0) goto L_0x0005;
    L_0x0003:
        r0 = 0;
    L_0x0004:
        return r0;
    L_0x0005:
        r0 = r3.copyDepth;
        r0 = r0 + 1;
        r3.copyDepth = r0;
        r0 = 1;
        r3.copyShallow = r0;
        r0 = r3.originalToCopy;	 Catch:{ all -> 0x005d }
        if (r0 != 0) goto L_0x0019;
    L_0x0012:
        r0 = new com.esotericsoftware.kryo.util.IdentityMap;	 Catch:{ all -> 0x005d }
        r0.m128init();	 Catch:{ all -> 0x005d }
        r3.originalToCopy = r0;	 Catch:{ all -> 0x005d }
    L_0x0019:
        r0 = r3.originalToCopy;	 Catch:{ all -> 0x005d }
        r0 = r0.get(r4);	 Catch:{ all -> 0x005d }
        if (r0 == 0) goto L_0x002f;
    L_0x0021:
        r3.copyShallow = r2;
        r1 = r3.copyDepth;
        r1 = r1 + -1;
        r3.copyDepth = r1;
        if (r1 != 0) goto L_0x0004;
    L_0x002b:
        r3.reset();
        goto L_0x0004;
    L_0x002f:
        r3.needsCopyReference = r4;	 Catch:{ all -> 0x005d }
        r0 = r4 instanceof com.esotericsoftware.kryo.KryoCopyable;	 Catch:{ all -> 0x005d }
        if (r0 == 0) goto L_0x0050;
    L_0x0035:
        r4 = (com.esotericsoftware.kryo.KryoCopyable) r4;	 Catch:{ all -> 0x005d }
        r0 = r4.copy(r3);	 Catch:{ all -> 0x005d }
    L_0x003b:
        r1 = r3.needsCopyReference;	 Catch:{ all -> 0x005d }
        if (r1 == 0) goto L_0x0042;
    L_0x003f:
        r3.reference(r0);	 Catch:{ all -> 0x005d }
    L_0x0042:
        r3.copyShallow = r2;
        r1 = r3.copyDepth;
        r1 = r1 + -1;
        r3.copyDepth = r1;
        if (r1 != 0) goto L_0x0004;
    L_0x004c:
        r3.reset();
        goto L_0x0004;
    L_0x0050:
        r0 = r4.getClass();	 Catch:{ all -> 0x005d }
        r0 = r3.getSerializer(r0);	 Catch:{ all -> 0x005d }
        r0 = r0.copy(r3, r4);	 Catch:{ all -> 0x005d }
        goto L_0x003b;
    L_0x005d:
        r0 = move-exception;
        r3.copyShallow = r2;
        r1 = r3.copyDepth;
        r1 = r1 + -1;
        r3.copyDepth = r1;
        if (r1 != 0) goto L_0x006b;
    L_0x0068:
        r3.reset();
    L_0x006b:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryo.Kryo.copyShallow(java.lang.Object):java.lang.Object");
    }

    /* JADX WARNING: Failed to extract finally block: empty outs */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            return r0;
     */
    public <T> T copyShallow(T r4, com.esotericsoftware.kryo.Serializer r5) {
        /*
        r3 = this;
        r2 = 0;
        if (r4 != 0) goto L_0x0005;
    L_0x0003:
        r0 = 0;
    L_0x0004:
        return r0;
    L_0x0005:
        r0 = r3.copyDepth;
        r0 = r0 + 1;
        r3.copyDepth = r0;
        r0 = 1;
        r3.copyShallow = r0;
        r0 = r3.originalToCopy;	 Catch:{ all -> 0x0055 }
        if (r0 != 0) goto L_0x0019;
    L_0x0012:
        r0 = new com.esotericsoftware.kryo.util.IdentityMap;	 Catch:{ all -> 0x0055 }
        r0.m128init();	 Catch:{ all -> 0x0055 }
        r3.originalToCopy = r0;	 Catch:{ all -> 0x0055 }
    L_0x0019:
        r0 = r3.originalToCopy;	 Catch:{ all -> 0x0055 }
        r0 = r0.get(r4);	 Catch:{ all -> 0x0055 }
        if (r0 == 0) goto L_0x002f;
    L_0x0021:
        r3.copyShallow = r2;
        r1 = r3.copyDepth;
        r1 = r1 + -1;
        r3.copyDepth = r1;
        if (r1 != 0) goto L_0x0004;
    L_0x002b:
        r3.reset();
        goto L_0x0004;
    L_0x002f:
        r3.needsCopyReference = r4;	 Catch:{ all -> 0x0055 }
        r0 = r4 instanceof com.esotericsoftware.kryo.KryoCopyable;	 Catch:{ all -> 0x0055 }
        if (r0 == 0) goto L_0x0050;
    L_0x0035:
        r4 = (com.esotericsoftware.kryo.KryoCopyable) r4;	 Catch:{ all -> 0x0055 }
        r0 = r4.copy(r3);	 Catch:{ all -> 0x0055 }
    L_0x003b:
        r1 = r3.needsCopyReference;	 Catch:{ all -> 0x0055 }
        if (r1 == 0) goto L_0x0042;
    L_0x003f:
        r3.reference(r0);	 Catch:{ all -> 0x0055 }
    L_0x0042:
        r3.copyShallow = r2;
        r1 = r3.copyDepth;
        r1 = r1 + -1;
        r3.copyDepth = r1;
        if (r1 != 0) goto L_0x0004;
    L_0x004c:
        r3.reset();
        goto L_0x0004;
    L_0x0050:
        r0 = r5.copy(r3, r4);	 Catch:{ all -> 0x0055 }
        goto L_0x003b;
    L_0x0055:
        r0 = move-exception;
        r3.copyShallow = r2;
        r1 = r3.copyDepth;
        r1 = r1 + -1;
        r3.copyDepth = r1;
        if (r1 != 0) goto L_0x0063;
    L_0x0060:
        r3.reset();
    L_0x0063:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryo.Kryo.copyShallow(java.lang.Object, com.esotericsoftware.kryo.Serializer):java.lang.Object");
    }

    private void beginObject() {
        if (this.depth == this.maxDepth) {
            throw new KryoException("Max depth exceeded: " + this.depth);
        }
        this.depth++;
    }

    public ClassResolver getClassResolver() {
        return this.classResolver;
    }

    public ReferenceResolver getReferenceResolver() {
        return this.referenceResolver;
    }

    public void setClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader cannot be null.");
        }
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public void setRegistrationRequired(boolean z) {
        this.registrationRequired = z;
    }

    public boolean isRegistrationRequired() {
        return this.registrationRequired;
    }

    public boolean setReferences(boolean z) {
        if (z == this.references) {
            return z;
        }
        this.references = z;
        if (z && this.referenceResolver == null) {
            this.referenceResolver = new MapReferenceResolver();
        }
        return !z;
    }

    public void setReferenceResolver(ReferenceResolver referenceResolver) {
        if (referenceResolver == null) {
            throw new IllegalArgumentException("referenceResolver cannot be null.");
        }
        this.references = true;
        this.referenceResolver = referenceResolver;
    }

    public boolean getReferences() {
        return this.references;
    }

    public void setInstantiatorStrategy(InstantiatorStrategy instantiatorStrategy) {
        this.strategy = instantiatorStrategy;
    }

    /* access modifiers changed from: protected */
    public ObjectInstantiator newInstantiator(final Class cls) {
        Constructor constructor;
        if (!Util.isAndroid) {
            try {
                final ConstructorAccess constructorAccess = ConstructorAccess.get(cls);
                return new ObjectInstantiator() {
                    public Object newInstance() {
                        try {
                            return constructorAccess.newInstance();
                        } catch (Exception e) {
                            throw new KryoException("Error constructing instance of class: " + Util.className(cls), e);
                        }
                    }
                };
            } catch (Exception e) {
            }
        }
        try {
            constructor = cls.getConstructor((Class[]) null);
        } catch (Exception e2) {
            Constructor declaredConstructor = cls.getDeclaredConstructor((Class[]) null);
            declaredConstructor.setAccessible(true);
            constructor = declaredConstructor;
        }
        try {
            return new ObjectInstantiator() {
                public Object newInstance() {
                    try {
                        return constructor.newInstance(new Object[0]);
                    } catch (Exception e) {
                        throw new KryoException("Error constructing instance of class: " + Util.className(cls), e);
                    }
                }
            };
        } catch (Exception e3) {
            if (this.strategy != null) {
                return this.strategy.newInstantiatorOf(cls);
            }
            if (!cls.isMemberClass() || Modifier.isStatic(cls.getModifiers())) {
                throw new KryoException("Class cannot be created (missing no-arg constructor): " + Util.className(cls));
            }
            throw new KryoException("Class cannot be created (non-static member class): " + Util.className(cls));
        }
    }

    public <T> T newInstance(Class<T> cls) {
        Registration registration = getRegistration((Class) cls);
        ObjectInstantiator instantiator = registration.getInstantiator();
        if (instantiator == null) {
            instantiator = newInstantiator(cls);
            registration.setInstantiator(instantiator);
        }
        return instantiator.newInstance();
    }

    public ObjectMap getContext() {
        if (this.context == null) {
            this.context = new ObjectMap();
        }
        return this.context;
    }

    public ObjectMap getGraphContext() {
        if (this.graphContext == null) {
            this.graphContext = new ObjectMap();
        }
        return this.graphContext;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setAutoReset(boolean z) {
        this.autoReset = z;
    }

    public void setMaxDepth(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxDepth must be > 0.");
        }
        this.maxDepth = i;
    }

    public boolean isFinal(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (cls.isArray()) {
            return Modifier.isFinal(Util.getElementClass(cls).getModifiers());
        } else {
            return Modifier.isFinal(cls.getModifiers());
        }
    }

    public static Class[] getGenerics(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        Class[] clsArr = new Class[actualTypeArguments.length];
        int length = actualTypeArguments.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3;
            Type type2 = actualTypeArguments[i];
            if (type2 instanceof Class) {
                clsArr[i] = (Class) type2;
            } else if (type2 instanceof ParameterizedType) {
                clsArr[i] = (Class) ((ParameterizedType) type2).getRawType();
            } else {
                i3 = i2;
                i++;
                i2 = i3;
            }
            i3 = i2 + 1;
            i++;
            i2 = i3;
        }
        return i2 == 0 ? null : clsArr;
    }
}
