package com.esotericsoftware.kryo.util;

import com.esotericsoftware.kryo.ClassResolver;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class DefaultClassResolver implements ClassResolver {
    public static final byte NAME = (byte) -1;
    protected IdentityObjectIntMap<Class> classToNameId;
    protected final ObjectMap<Class, Registration> classToRegistration = new ObjectMap();
    protected final IntMap<Registration> idToRegistration = new IntMap();
    protected Kryo kryo;
    private int memoizedClassId = -1;
    private Registration memoizedClassIdValue;
    protected IntMap<Class> nameIdToClass;
    protected ObjectMap<String, Class> nameToClass;
    protected int nextNameId;

    public void setKryo(Kryo kryo) {
        this.kryo = kryo;
    }

    public Registration register(Registration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("registration cannot be null.");
        }
        this.classToRegistration.put(registration.getType(), registration);
        this.idToRegistration.put(registration.getId(), registration);
        if (registration.getType().isPrimitive()) {
            this.classToRegistration.put(Util.getWrapperClass(registration.getType()), registration);
        }
        return registration;
    }

    public Registration registerImplicit(Class cls) {
        return register(new Registration(cls, this.kryo.getDefaultSerializer(cls), -1));
    }

    public Registration getRegistration(Class cls) {
        return (Registration) this.classToRegistration.get(cls);
    }

    public Registration getRegistration(int i) {
        return (Registration) this.idToRegistration.get(i);
    }

    public Registration writeClass(Output output, Class cls) {
        if (cls == null) {
            output.writeByte((byte) 0);
            return null;
        }
        Registration registration = this.kryo.getRegistration(cls);
        if (registration.getId() == -1) {
            writeName(output, cls, registration);
            return registration;
        }
        output.writeInt(registration.getId() + 2, true);
        return registration;
    }

    /* access modifiers changed from: protected */
    public void writeName(Output output, Class cls, Registration registration) {
        int i;
        output.writeByte(1);
        if (this.classToNameId != null) {
            i = this.classToNameId.get(cls, -1);
            if (i != -1) {
                output.writeInt(i, true);
                return;
            }
        }
        i = this.nextNameId;
        this.nextNameId = i + 1;
        if (this.classToNameId == null) {
            this.classToNameId = new IdentityObjectIntMap();
        }
        this.classToNameId.put(cls, i);
        output.write(i);
        output.writeString(cls.getName());
    }

    public Registration readClass(Input input) {
        int readInt = input.readInt(true);
        switch (readInt) {
            case 0:
                return null;
            case 1:
                return readName(input);
            default:
                if (readInt == this.memoizedClassId) {
                    return this.memoizedClassIdValue;
                }
                Registration registration = (Registration) this.idToRegistration.get(readInt - 2);
                if (registration == null) {
                    throw new KryoException("Encountered unregistered class ID: " + (readInt - 2));
                }
                this.memoizedClassId = readInt;
                this.memoizedClassIdValue = registration;
                return registration;
        }
    }

    /* access modifiers changed from: protected */
    public Registration readName(Input input) {
        int readInt = input.readInt(true);
        if (this.nameIdToClass == null) {
            this.nameIdToClass = new IntMap();
        }
        Class cls = (Class) this.nameIdToClass.get(readInt);
        if (cls == null) {
            String readString = input.readString();
            if (this.nameToClass != null) {
                cls = (Class) this.nameToClass.get(readString);
            }
            if (cls == null) {
                try {
                    cls = Class.forName(readString, false, this.kryo.getClassLoader());
                    if (this.nameToClass == null) {
                        this.nameToClass = new ObjectMap();
                    }
                    this.nameToClass.put(readString, cls);
                } catch (ClassNotFoundException e) {
                    throw new KryoException("Unable to find class: " + readString, e);
                }
            }
            this.nameIdToClass.put(readInt, cls);
        }
        return this.kryo.getRegistration(cls);
    }

    public void reset() {
        if (!this.kryo.isRegistrationRequired()) {
            if (this.classToNameId != null) {
                this.classToNameId.clear();
            }
            if (this.nameIdToClass != null) {
                this.nameIdToClass.clear();
            }
            this.nextNameId = 0;
        }
    }
}
