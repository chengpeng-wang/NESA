package com.baidu.inf.iis.bcs.model;

import java.util.ArrayList;
import java.util.List;

public class Pair<T> {
    private T first;
    private T second;

    public Pair(T t, T t2) {
        this.first = t;
        this.second = t2;
    }

    public T getFirst() {
        return this.first;
    }

    public T getSecond() {
        return this.second;
    }

    public void setFirst(T t) {
        this.first = t;
    }

    public void setSecond(T t) {
        this.second = t;
    }

    public List<T> toArrayList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.first);
        arrayList.add(this.second);
        return arrayList;
    }
}
