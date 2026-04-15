package com.google.i18n.phonenumbers;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class RegexCache {
    private LRUCache<String, Pattern> cache;

    private static class LRUCache<K, V> {
        private LinkedHashMap<K, V> map;
        /* access modifiers changed from: private */
        public int size;

        public LRUCache(int size) {
            this.size = size;
            this.map = new LinkedHashMap<K, V>(((size * 4) / 3) + 1, 0.75f, true) {
                /* access modifiers changed from: protected */
                public boolean removeEldestEntry(Entry<K, V> entry) {
                    return size() > LRUCache.this.size;
                }
            };
        }

        public synchronized V get(K key) {
            return this.map.get(key);
        }

        public synchronized void put(K key, V value) {
            this.map.put(key, value);
        }

        public synchronized boolean containsKey(K key) {
            return this.map.containsKey(key);
        }
    }

    public RegexCache(int size) {
        this.cache = new LRUCache(size);
    }

    public Pattern getPatternForRegex(String regex) {
        Pattern pattern = (Pattern) this.cache.get(regex);
        if (pattern != null) {
            return pattern;
        }
        pattern = Pattern.compile(regex);
        this.cache.put(regex, pattern);
        return pattern;
    }

    /* access modifiers changed from: 0000 */
    public boolean containsRegex(String regex) {
        return this.cache.containsKey(regex);
    }
}
