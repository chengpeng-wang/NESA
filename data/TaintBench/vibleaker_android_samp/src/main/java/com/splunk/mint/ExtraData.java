package com.splunk.mint;

import java.util.HashMap;
import java.util.Map.Entry;

public class ExtraData extends HashMap<String, Object> {
    private static final long serialVersionUID = -3516111185615801729L;

    public boolean addExtraData(String key, Object value) {
        if (key == null || value == null) {
            return false;
        }
        put(key, value);
        return true;
    }

    public HashMap<String, Object> getExtraData() {
        return this;
    }

    public boolean addExtraDataMap(HashMap<String, Object> extras) {
        if (extras == null) {
            return false;
        }
        for (Entry<String, Object> entryVar : extras.entrySet()) {
            put(entryVar.getKey(), entryVar.getValue());
        }
        return true;
    }

    public boolean addExtraData(ExtraData extras) {
        if (extras == null) {
            return false;
        }
        for (Entry<String, Object> entryVar : extras.entrySet()) {
            put(entryVar.getKey(), entryVar.getValue());
        }
        return true;
    }

    public boolean removeKey(String key) {
        if (key == null || !containsKey(key)) {
            return false;
        }
        remove(key);
        return true;
    }

    public void clearData() {
        clear();
    }
}
