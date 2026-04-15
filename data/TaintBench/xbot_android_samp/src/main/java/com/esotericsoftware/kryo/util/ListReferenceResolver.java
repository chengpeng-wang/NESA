package com.esotericsoftware.kryo.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ReferenceResolver;
import java.util.ArrayList;

public class ListReferenceResolver implements ReferenceResolver {
    protected Kryo kryo;
    protected final ArrayList seenObjects = new ArrayList();

    public void setKryo(Kryo kryo) {
        this.kryo = kryo;
    }

    public int addWrittenObject(Object obj) {
        int size = this.seenObjects.size();
        this.seenObjects.add(obj);
        return size;
    }

    public int getWrittenId(Object obj) {
        int size = this.seenObjects.size();
        for (int i = 0; i < size; i++) {
            if (this.seenObjects.get(i) == obj) {
                return i;
            }
        }
        return -1;
    }

    public int nextReadId(Class cls) {
        return this.seenObjects.size();
    }

    public void addReadObject(int i, Object obj) {
        if (i == this.seenObjects.size()) {
            this.seenObjects.add(obj);
            return;
        }
        while (i >= this.seenObjects.size()) {
            this.seenObjects.add(null);
        }
        this.seenObjects.set(i, obj);
    }

    public Object getReadObject(Class cls, int i) {
        if (i < this.seenObjects.size()) {
            return this.seenObjects.get(i);
        }
        return null;
    }

    public void reset() {
        this.seenObjects.clear();
    }

    public boolean useReferences(Class cls) {
        return !Util.isWrapperClass(cls);
    }
}
