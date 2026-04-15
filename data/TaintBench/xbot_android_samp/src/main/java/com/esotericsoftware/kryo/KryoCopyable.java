package com.esotericsoftware.kryo;

public interface KryoCopyable<T> {
    T copy(Kryo kryo);
}
