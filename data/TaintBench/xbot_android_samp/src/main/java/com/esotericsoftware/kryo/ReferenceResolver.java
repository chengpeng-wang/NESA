package com.esotericsoftware.kryo;

public interface ReferenceResolver {
    void addReadObject(int i, Object obj);

    int addWrittenObject(Object obj);

    Object getReadObject(Class cls, int i);

    int getWrittenId(Object obj);

    int nextReadId(Class cls);

    void reset();

    void setKryo(Kryo kryo);

    boolean useReferences(Class cls);
}
