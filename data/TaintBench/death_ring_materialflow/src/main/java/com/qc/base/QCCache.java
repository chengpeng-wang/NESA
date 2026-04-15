package com.qc.base;

import android.content.Context;
import com.qc.model.InstalledApkDBHelper;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class QCCache {
    private static final Object LOCK = new Object();
    private static QCCache cache = null;
    private HashMap<String, Object> cacheContent = new HashMap();
    private Queue<Object> queue = new LinkedList();

    private QCCache() {
    }

    public static synchronized QCCache getInstance() {
        QCCache qCCache;
        synchronized (QCCache.class) {
            if (cache == null) {
                cache = new QCCache();
            }
            qCCache = cache;
        }
        return qCCache;
    }

    public void init(Context context) {
        synchronized (LOCK) {
            getInstance().reSetValue("installedApks", new InstalledApkDBHelper(context).getAll());
        }
    }

    public void reSetValues(String[] keys, Object[] values) {
        synchronized (LOCK) {
            for (int i = 0; i < values.length; i++) {
                this.cacheContent.put(keys[i], values[i]);
            }
        }
    }

    public void reSetValue(String key, Object value) {
        synchronized (LOCK) {
            this.cacheContent.put(key, value);
        }
    }

    public void offer(Object obj) {
        synchronized (LOCK) {
            this.queue.offer(obj);
        }
    }

    public int queueCount() {
        int size;
        synchronized (LOCK) {
            size = this.queue.size();
        }
        return size;
    }

    public void clearQueue() {
        synchronized (LOCK) {
            this.queue.clear();
        }
    }

    public Object pull() {
        Object poll;
        synchronized (LOCK) {
            poll = this.queue.poll();
        }
        return poll;
    }

    public Object peek() {
        Object peek;
        synchronized (LOCK) {
            peek = this.queue.peek();
        }
        return peek;
    }

    public Object getValue(String key) {
        Object obj;
        synchronized (LOCK) {
            obj = this.cacheContent.get(key);
        }
        return obj;
    }

    public void deleteCache(String key) {
        synchronized (LOCK) {
            this.cacheContent.remove(key);
        }
    }

    public void removeCache(String[] keys) {
        synchronized (LOCK) {
            for (Object remove : keys) {
                this.cacheContent.remove(remove);
            }
        }
    }

    public void clearCache() {
        synchronized (LOCK) {
            this.cacheContent.clear();
        }
    }

    public int getCacheSize() {
        int size;
        synchronized (LOCK) {
            size = this.cacheContent.size();
        }
        return size;
    }
}
