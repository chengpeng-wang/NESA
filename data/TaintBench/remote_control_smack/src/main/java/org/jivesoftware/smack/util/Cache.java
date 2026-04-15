package org.jivesoftware.smack.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jivesoftware.smack.util.collections.AbstractMapEntry;

public class Cache<K, V> implements Map<K, V> {
    protected LinkedList ageList;
    protected long cacheHits;
    protected long cacheMisses = 0;
    protected LinkedList lastAccessedList;
    protected Map<K, CacheObject<V>> map;
    protected int maxCacheSize;
    protected long maxLifetime;

    private static class CacheObject<V> {
        public LinkedListNode ageListNode;
        public LinkedListNode lastAccessedListNode;
        public V object;
        public int readCount = 0;

        public CacheObject(V object) {
            this.object = object;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CacheObject)) {
                return false;
            }
            return this.object.equals(((CacheObject) o).object);
        }

        public int hashCode() {
            return this.object.hashCode();
        }
    }

    private static class LinkedList {
        private LinkedListNode head = new LinkedListNode("head", null, null);

        public LinkedList() {
            LinkedListNode linkedListNode = this.head;
            LinkedListNode linkedListNode2 = this.head;
            LinkedListNode linkedListNode3 = this.head;
            linkedListNode2.previous = linkedListNode3;
            linkedListNode.next = linkedListNode3;
        }

        public LinkedListNode getFirst() {
            LinkedListNode node = this.head.next;
            if (node == this.head) {
                return null;
            }
            return node;
        }

        public LinkedListNode getLast() {
            LinkedListNode node = this.head.previous;
            if (node == this.head) {
                return null;
            }
            return node;
        }

        public LinkedListNode addFirst(LinkedListNode node) {
            node.next = this.head.next;
            node.previous = this.head;
            node.previous.next = node;
            node.next.previous = node;
            return node;
        }

        public LinkedListNode addFirst(Object object) {
            LinkedListNode node = new LinkedListNode(object, this.head.next, this.head);
            node.previous.next = node;
            node.next.previous = node;
            return node;
        }

        public LinkedListNode addLast(Object object) {
            LinkedListNode node = new LinkedListNode(object, this.head, this.head.previous);
            node.previous.next = node;
            node.next.previous = node;
            return node;
        }

        public void clear() {
            LinkedListNode node = getLast();
            while (node != null) {
                node.remove();
                node = getLast();
            }
            LinkedListNode linkedListNode = this.head;
            LinkedListNode linkedListNode2 = this.head;
            LinkedListNode linkedListNode3 = this.head;
            linkedListNode2.previous = linkedListNode3;
            linkedListNode.next = linkedListNode3;
        }

        public String toString() {
            StringBuilder buf = new StringBuilder();
            for (LinkedListNode node = this.head.next; node != this.head; node = node.next) {
                buf.append(node.toString()).append(", ");
            }
            return buf.toString();
        }
    }

    private static class LinkedListNode {
        public LinkedListNode next;
        public Object object;
        public LinkedListNode previous;
        public long timestamp;

        public LinkedListNode(Object object, LinkedListNode next, LinkedListNode previous) {
            this.object = object;
            this.next = next;
            this.previous = previous;
        }

        public void remove() {
            this.previous.next = this.next;
            this.next.previous = this.previous;
        }

        public String toString() {
            return this.object.toString();
        }
    }

    public Cache(int maxSize, long maxLifetime) {
        if (maxSize == 0) {
            throw new IllegalArgumentException("Max cache size cannot be 0.");
        }
        this.maxCacheSize = maxSize;
        this.maxLifetime = maxLifetime;
        this.map = new HashMap(103);
        this.lastAccessedList = new LinkedList();
        this.ageList = new LinkedList();
    }

    public synchronized V put(K key, V value) {
        V oldValue;
        oldValue = null;
        if (this.map.containsKey(key)) {
            oldValue = remove(key, true);
        }
        CacheObject<V> cacheObject = new CacheObject(value);
        this.map.put(key, cacheObject);
        cacheObject.lastAccessedListNode = this.lastAccessedList.addFirst((Object) key);
        LinkedListNode ageNode = this.ageList.addFirst((Object) key);
        ageNode.timestamp = System.currentTimeMillis();
        cacheObject.ageListNode = ageNode;
        cullCache();
        return oldValue;
    }

    public synchronized V get(Object key) {
        V v;
        deleteExpiredEntries();
        CacheObject<V> cacheObject = (CacheObject) this.map.get(key);
        if (cacheObject == null) {
            this.cacheMisses++;
            v = null;
        } else {
            cacheObject.lastAccessedListNode.remove();
            this.lastAccessedList.addFirst(cacheObject.lastAccessedListNode);
            this.cacheHits++;
            cacheObject.readCount++;
            v = cacheObject.object;
        }
        return v;
    }

    public synchronized V remove(Object key) {
        return remove(key, false);
    }

    public synchronized V remove(Object key, boolean internal) {
        V v = null;
        synchronized (this) {
            CacheObject<V> cacheObject = (CacheObject) this.map.remove(key);
            if (cacheObject != null) {
                cacheObject.lastAccessedListNode.remove();
                cacheObject.ageListNode.remove();
                cacheObject.ageListNode = null;
                cacheObject.lastAccessedListNode = null;
                v = cacheObject.object;
            }
        }
        return v;
    }

    public synchronized void clear() {
        for (Object key : this.map.keySet().toArray()) {
            remove(key);
        }
        this.map.clear();
        this.lastAccessedList.clear();
        this.ageList.clear();
        this.cacheHits = 0;
        this.cacheMisses = 0;
    }

    public synchronized int size() {
        deleteExpiredEntries();
        return this.map.size();
    }

    public synchronized boolean isEmpty() {
        deleteExpiredEntries();
        return this.map.isEmpty();
    }

    public synchronized Collection<V> values() {
        deleteExpiredEntries();
        return Collections.unmodifiableCollection(new AbstractCollection<V>() {
            Collection<CacheObject<V>> values = Cache.this.map.values();

            public Iterator<V> iterator() {
                return new Iterator<V>() {
                    Iterator<CacheObject<V>> it = AnonymousClass1.this.values.iterator();

                    public boolean hasNext() {
                        return this.it.hasNext();
                    }

                    public V next() {
                        return ((CacheObject) this.it.next()).object;
                    }

                    public void remove() {
                        this.it.remove();
                    }
                };
            }

            public int size() {
                return this.values.size();
            }
        });
    }

    public synchronized boolean containsKey(Object key) {
        deleteExpiredEntries();
        return this.map.containsKey(key);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            V value = entry.getValue();
            if (value instanceof CacheObject) {
                value = ((CacheObject) value).object;
            }
            put(entry.getKey(), value);
        }
    }

    public synchronized boolean containsValue(Object value) {
        deleteExpiredEntries();
        return this.map.containsValue(new CacheObject(value));
    }

    public synchronized Set<Entry<K, V>> entrySet() {
        deleteExpiredEntries();
        return new AbstractSet<Entry<K, V>>() {
            /* access modifiers changed from: private|final */
            public final Set<Entry<K, CacheObject<V>>> set = Cache.this.map.entrySet();

            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<Entry<K, V>>() {
                    private final Iterator<Entry<K, CacheObject<V>>> it = AnonymousClass2.this.set.iterator();

                    public boolean hasNext() {
                        return this.it.hasNext();
                    }

                    public Entry<K, V> next() {
                        Entry<K, CacheObject<V>> entry = (Entry) this.it.next();
                        return new AbstractMapEntry<K, V>(entry.getKey(), ((CacheObject) entry.getValue()).object) {
                            public V setValue(V v) {
                                throw new UnsupportedOperationException("Cannot set");
                            }
                        };
                    }

                    public void remove() {
                        this.it.remove();
                    }
                };
            }

            public int size() {
                return this.set.size();
            }
        };
    }

    public synchronized Set<K> keySet() {
        deleteExpiredEntries();
        return Collections.unmodifiableSet(this.map.keySet());
    }

    public long getCacheHits() {
        return this.cacheHits;
    }

    public long getCacheMisses() {
        return this.cacheMisses;
    }

    public int getMaxCacheSize() {
        return this.maxCacheSize;
    }

    public synchronized void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        cullCache();
    }

    public long getMaxLifetime() {
        return this.maxLifetime;
    }

    public void setMaxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void deleteExpiredEntries() {
        if (this.maxLifetime > 0) {
            LinkedListNode node = this.ageList.getLast();
            if (node != null) {
                long expireTime = System.currentTimeMillis() - this.maxLifetime;
                while (expireTime > node.timestamp) {
                    if (remove(node.object, true) == null) {
                        System.err.println("Error attempting to remove(" + node.object.toString() + ") - cacheObject not found in cache!");
                        node.remove();
                    }
                    node = this.ageList.getLast();
                    if (node == null) {
                        break;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void cullCache() {
        if (this.maxCacheSize >= 0) {
            if (this.map.size() > this.maxCacheSize) {
                deleteExpiredEntries();
                int desiredSize = (int) (((double) this.maxCacheSize) * 0.9d);
                for (int i = this.map.size(); i > desiredSize; i--) {
                    if (remove(this.lastAccessedList.getLast().object, true) == null) {
                        System.err.println("Error attempting to cullCache with remove(" + this.lastAccessedList.getLast().object.toString() + ") - " + "cacheObject not found in cache!");
                        this.lastAccessedList.getLast().remove();
                    }
                }
            }
        }
    }
}
