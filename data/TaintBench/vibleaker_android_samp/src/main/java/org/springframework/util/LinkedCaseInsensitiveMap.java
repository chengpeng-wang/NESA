package org.springframework.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class LinkedCaseInsensitiveMap<V> extends LinkedHashMap<String, V> {
    private final Map<String, String> caseInsensitiveKeys;
    private final Locale locale;

    public LinkedCaseInsensitiveMap() {
        this(null);
    }

    public LinkedCaseInsensitiveMap(Locale locale) {
        this.caseInsensitiveKeys = new HashMap();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        this.locale = locale;
    }

    public LinkedCaseInsensitiveMap(int initialCapacity) {
        this(initialCapacity, null);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale) {
        super(initialCapacity);
        this.caseInsensitiveKeys = new HashMap(initialCapacity);
        if (locale == null) {
            locale = Locale.getDefault();
        }
        this.locale = locale;
    }

    public V put(String key, V value) {
        String oldKey = (String) this.caseInsensitiveKeys.put(convertKey(key), key);
        if (!(oldKey == null || oldKey.equals(key))) {
            super.remove(oldKey);
        }
        return super.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends V> map) {
        if (!map.isEmpty()) {
            for (Entry<? extends String, ? extends V> entry : map.entrySet()) {
                put((String) entry.getKey(), entry.getValue());
            }
        }
    }

    public boolean containsKey(Object key) {
        return (key instanceof String) && this.caseInsensitiveKeys.containsKey(convertKey((String) key));
    }

    public V get(Object key) {
        if (key instanceof String) {
            return super.get(this.caseInsensitiveKeys.get(convertKey((String) key)));
        }
        return null;
    }

    public V remove(Object key) {
        if (key instanceof String) {
            return super.remove(this.caseInsensitiveKeys.remove(convertKey((String) key)));
        }
        return null;
    }

    public void clear() {
        this.caseInsensitiveKeys.clear();
        super.clear();
    }

    /* access modifiers changed from: protected */
    public String convertKey(String key) {
        return key.toLowerCase(this.locale);
    }
}
