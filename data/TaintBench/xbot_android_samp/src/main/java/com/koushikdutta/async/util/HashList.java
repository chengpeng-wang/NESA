package com.koushikdutta.async.util;

import java.util.ArrayList;
import java.util.Hashtable;

public class HashList<T> extends Hashtable<String, ArrayList<T>> {
    private static final long serialVersionUID = 1;

    public boolean contains(String key) {
        ArrayList<T> check = (ArrayList) get(key);
        return check != null && check.size() > 0;
    }

    public void add(String key, T value) {
        ArrayList<T> ret = (ArrayList) get(key);
        if (ret == null) {
            ret = new ArrayList();
            put(key, ret);
        }
        ret.add(value);
    }
}
