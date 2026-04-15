package com.google.gson;

interface Cache<K, V> {
    void addElement(K k, V v);

    V getElement(K k);
}
