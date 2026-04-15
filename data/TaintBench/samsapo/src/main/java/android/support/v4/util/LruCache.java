package android.support.v4.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
        jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 2, insn: 0x00a0: MOVE  (r9 ?[OBJECT, ARRAY]) = (r2 ?[OBJECT, ARRAY]), block:B:27:0x009e
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVarsInBlock(SSATransform.java:157)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:129)
        	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:51)
        	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:41)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1257)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:61)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public void trimToSize(int r14) {
        /*
        r13 = this;
        r0 = r13;
        r1 = r14;
        r7 = r0;
        r12 = r7;
        r7 = r12;
        r8 = r12;
        r4 = r8;
        monitor-enter(r7);
        r7 = r0;
        r7 = r7.size;	 Catch:{ all -> 0x0043 }
        if (r7 < 0) goto L_0x001b;	 Catch:{ all -> 0x0043 }
        r7 = r0;	 Catch:{ all -> 0x0043 }
        r7 = r7.map;	 Catch:{ all -> 0x0043 }
        r7 = r7.isEmpty();	 Catch:{ all -> 0x0043 }
        if (r7 == 0) goto L_0x0049;	 Catch:{ all -> 0x0043 }
        r7 = r0;	 Catch:{ all -> 0x0043 }
        r7 = r7.size;	 Catch:{ all -> 0x0043 }
        if (r7 == 0) goto L_0x0049;	 Catch:{ all -> 0x0043 }
        r7 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0043 }
        r12 = r7;	 Catch:{ all -> 0x0043 }
        r7 = r12;	 Catch:{ all -> 0x0043 }
        r8 = r12;	 Catch:{ all -> 0x0043 }
        r9 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0043 }
        r12 = r9;	 Catch:{ all -> 0x0043 }
        r9 = r12;	 Catch:{ all -> 0x0043 }
        r10 = r12;	 Catch:{ all -> 0x0043 }
        r10.<init>();	 Catch:{ all -> 0x0043 }
        r10 = r0;	 Catch:{ all -> 0x0043 }
        r10 = r10.getClass();	 Catch:{ all -> 0x0043 }
        r10 = r10.getName();	 Catch:{ all -> 0x0043 }
        r9 = r9.append(r10);	 Catch:{ all -> 0x0043 }
        r10 = ".sizeOf() is reporting inconsistent results!";	 Catch:{ all -> 0x0043 }
        r9 = r9.append(r10);	 Catch:{ all -> 0x0043 }
        r9 = r9.toString();	 Catch:{ all -> 0x0043 }
        r8.<init>(r9);	 Catch:{ all -> 0x0043 }
        throw r7;	 Catch:{ all -> 0x0043 }
        r7 = move-exception;	 Catch:{ all -> 0x0043 }
        r6 = r7;	 Catch:{ all -> 0x0043 }
        r7 = r4;	 Catch:{ all -> 0x0043 }
        monitor-exit(r7);	 Catch:{ all -> 0x0043 }
        r7 = r6;
        throw r7;
        r7 = r0;
        r7 = r7.size;	 Catch:{ all -> 0x0043 }
        r8 = r1;	 Catch:{ all -> 0x0043 }
        if (r7 <= r8) goto L_0x0058;	 Catch:{ all -> 0x0043 }
        r7 = r0;	 Catch:{ all -> 0x0043 }
        r7 = r7.map;	 Catch:{ all -> 0x0043 }
        r7 = r7.isEmpty();	 Catch:{ all -> 0x0043 }
        if (r7 == 0) goto L_0x005b;	 Catch:{ all -> 0x0043 }
        r7 = r4;	 Catch:{ all -> 0x0043 }
        monitor-exit(r7);	 Catch:{ all -> 0x0043 }
        return;	 Catch:{ all -> 0x0043 }
        r7 = r0;	 Catch:{ all -> 0x0043 }
        r7 = r7.map;	 Catch:{ all -> 0x0043 }
        r7 = r7.entrySet();	 Catch:{ all -> 0x0043 }
        r7 = r7.iterator();	 Catch:{ all -> 0x0043 }
        r7 = r7.next();	 Catch:{ all -> 0x0043 }
        r7 = (java.util.Map.Entry) r7;	 Catch:{ all -> 0x0043 }
        r5 = r7;	 Catch:{ all -> 0x0043 }
        r7 = r5;	 Catch:{ all -> 0x0043 }
        r7 = r7.getKey();	 Catch:{ all -> 0x0043 }
        r2 = r7;	 Catch:{ all -> 0x0043 }
        r7 = r5;	 Catch:{ all -> 0x0043 }
        r7 = r7.getValue();	 Catch:{ all -> 0x0043 }
        r3 = r7;	 Catch:{ all -> 0x0043 }
        r7 = r0;	 Catch:{ all -> 0x0043 }
        r7 = r7.map;	 Catch:{ all -> 0x0043 }
        r8 = r2;	 Catch:{ all -> 0x0043 }
        r7 = r7.remove(r8);	 Catch:{ all -> 0x0043 }
        r7 = r0;	 Catch:{ all -> 0x0043 }
        r12 = r7;	 Catch:{ all -> 0x0043 }
        r7 = r12;	 Catch:{ all -> 0x0043 }
        r8 = r12;	 Catch:{ all -> 0x0043 }
        r8 = r8.size;	 Catch:{ all -> 0x0043 }
        r9 = r0;	 Catch:{ all -> 0x0043 }
        r10 = r2;	 Catch:{ all -> 0x0043 }
        r11 = r3;	 Catch:{ all -> 0x0043 }
        r9 = r9.safeSizeOf(r10, r11);	 Catch:{ all -> 0x0043 }
        r8 = r8 - r9;	 Catch:{ all -> 0x0043 }
        r7.size = r8;	 Catch:{ all -> 0x0043 }
        r7 = r0;	 Catch:{ all -> 0x0043 }
        r12 = r7;	 Catch:{ all -> 0x0043 }
        r7 = r12;	 Catch:{ all -> 0x0043 }
        r8 = r12;	 Catch:{ all -> 0x0043 }
        r8 = r8.evictionCount;	 Catch:{ all -> 0x0043 }
        r9 = 1;	 Catch:{ all -> 0x0043 }
        r8 = r8 + 1;	 Catch:{ all -> 0x0043 }
        r7.evictionCount = r8;	 Catch:{ all -> 0x0043 }
        r7 = r4;	 Catch:{ all -> 0x0043 }
        monitor-exit(r7);	 Catch:{ all -> 0x0043 }
        r7 = r0;
        r8 = 1;
        r9 = r2;
        r10 = r3;
        r11 = 0;
        r7.entryRemoved(r8, r9, r10, r11);
        goto L_0x0002;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.LruCache.trimToSize(int):void");
    }

    public LruCache(int i) {
        int i2 = i;
        if (i2 <= 0) {
            IllegalArgumentException illegalArgumentException = r8;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("maxSize <= 0");
            throw illegalArgumentException;
        }
        this.maxSize = i2;
        LinkedHashMap linkedHashMap = r8;
        LinkedHashMap linkedHashMap2 = new LinkedHashMap(0, 0.75f, true);
        this.map = linkedHashMap;
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x007e  */
    /* JADX WARNING: Missing block: B:40:0x009e, code skipped:
            r6 = r5;
     */
    public final V get(K r13) {
        /*
        r12 = this;
        r0 = r12;
        r1 = r13;
        r6 = r1;
        if (r6 != 0) goto L_0x0010;
    L_0x0005:
        r6 = new java.lang.NullPointerException;
        r11 = r6;
        r6 = r11;
        r7 = r11;
        r8 = "key == null";
        r7.<init>(r8);
        throw r6;
    L_0x0010:
        r6 = r0;
        r11 = r6;
        r6 = r11;
        r7 = r11;
        r3 = r7;
        monitor-enter(r6);
        r6 = r0;
        r6 = r6.map;	 Catch:{ all -> 0x004c }
        r7 = r1;
        r6 = r6.get(r7);	 Catch:{ all -> 0x004c }
        r2 = r6;
        r6 = r2;
        if (r6 == 0) goto L_0x0032;
    L_0x0022:
        r6 = r0;
        r11 = r6;
        r6 = r11;
        r7 = r11;
        r7 = r7.hitCount;	 Catch:{ all -> 0x004c }
        r8 = 1;
        r7 = r7 + 1;
        r6.hitCount = r7;	 Catch:{ all -> 0x004c }
        r6 = r2;
        r7 = r3;
        monitor-exit(r7);	 Catch:{ all -> 0x004c }
        r0 = r6;
    L_0x0031:
        return r0;
    L_0x0032:
        r6 = r0;
        r11 = r6;
        r6 = r11;
        r7 = r11;
        r7 = r7.missCount;	 Catch:{ all -> 0x004c }
        r8 = 1;
        r7 = r7 + 1;
        r6.missCount = r7;	 Catch:{ all -> 0x004c }
        r6 = r3;
        monitor-exit(r6);	 Catch:{ all -> 0x004c }
        r6 = r0;
        r7 = r1;
        r6 = r6.create(r7);
        r3 = r6;
        r6 = r3;
        if (r6 != 0) goto L_0x0052;
    L_0x0049:
        r6 = 0;
        r0 = r6;
        goto L_0x0031;
    L_0x004c:
        r6 = move-exception;
        r4 = r6;
        r6 = r3;
        monitor-exit(r6);	 Catch:{ all -> 0x004c }
        r6 = r4;
        throw r6;
    L_0x0052:
        r6 = r0;
        r11 = r6;
        r6 = r11;
        r7 = r11;
        r4 = r7;
        monitor-enter(r6);
        r6 = r0;
        r11 = r6;
        r6 = r11;
        r7 = r11;
        r7 = r7.createCount;	 Catch:{ all -> 0x009a }
        r8 = 1;
        r7 = r7 + 1;
        r6.createCount = r7;	 Catch:{ all -> 0x009a }
        r6 = r0;
        r6 = r6.map;	 Catch:{ all -> 0x009a }
        r7 = r1;
        r8 = r3;
        r6 = r6.put(r7, r8);	 Catch:{ all -> 0x009a }
        r2 = r6;
        r6 = r2;
        if (r6 == 0) goto L_0x0089;
    L_0x0070:
        r6 = r0;
        r6 = r6.map;	 Catch:{ all -> 0x009a }
        r7 = r1;
        r8 = r2;
        r6 = r6.put(r7, r8);	 Catch:{ all -> 0x009a }
    L_0x0079:
        r6 = r4;
        monitor-exit(r6);	 Catch:{ all -> 0x009a }
        r6 = r2;
        if (r6 == 0) goto L_0x00a0;
    L_0x007e:
        r6 = r0;
        r7 = 0;
        r8 = r1;
        r9 = r3;
        r10 = r2;
        r6.entryRemoved(r7, r8, r9, r10);
        r6 = r2;
        r0 = r6;
        goto L_0x0031;
    L_0x0089:
        r6 = r0;
        r11 = r6;
        r6 = r11;
        r7 = r11;
        r7 = r7.size;	 Catch:{ all -> 0x009a }
        r8 = r0;
        r9 = r1;
        r10 = r3;
        r8 = r8.safeSizeOf(r9, r10);	 Catch:{ all -> 0x009a }
        r7 = r7 + r8;
        r6.size = r7;	 Catch:{ all -> 0x009a }
        goto L_0x0079;
    L_0x009a:
        r6 = move-exception;
        r5 = r6;
        r6 = r4;
        monitor-exit(r6);	 Catch:{ all -> 0x009a }
        r6 = r5;
        throw r6;
    L_0x00a0:
        r6 = r0;
        r7 = r0;
        r7 = r7.maxSize;
        r6.trimToSize(r7);
        r6 = r3;
        r0 = r6;
        goto L_0x0031;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.LruCache.get(java.lang.Object):java.lang.Object");
    }

    public final V put(K k, V v) {
        K k2 = k;
        V v2 = v;
        if (k2 == null || v2 == null) {
            NullPointerException nullPointerException = r11;
            NullPointerException nullPointerException2 = new NullPointerException("key == null || value == null");
            throw nullPointerException;
        }
        synchronized (this) {
            LruCache thisR;
            V put;
            LruCache lruCache = this;
            try {
                lruCache.putCount++;
                lruCache = this;
                lruCache.size += safeSizeOf(k2, v2);
                put = this.map.put(k2, v2);
                if (put != null) {
                    r11.size -= safeSizeOf(k2, put);
                }
            } finally {
                LruCache lruCache2 = thisR;
                thisR = lruCache2;
            }
            if (put != null) {
                entryRemoved(false, k2, put, v2);
            }
            trimToSize(this.maxSize);
            return put;
        }
    }

    public final V remove(K k) {
        K k2 = k;
        if (k2 == null) {
            NullPointerException nullPointerException = r10;
            NullPointerException nullPointerException2 = new NullPointerException("key == null");
            throw nullPointerException;
        }
        synchronized (this) {
            V remove;
            LruCache thisR;
            try {
                remove = this.map.remove(k2);
                if (remove != null) {
                    this.size -= safeSizeOf(k2, remove);
                }
            } finally {
                LruCache lruCache = thisR;
                thisR = lruCache;
            }
            if (remove != null) {
                entryRemoved(false, k2, remove, null);
            }
            return remove;
        }
    }

    /* access modifiers changed from: protected */
    public void entryRemoved(boolean z, K k, V v, V v2) {
    }

    /* access modifiers changed from: protected */
    public V create(K k) {
        K k2 = k;
        return null;
    }

    private int safeSizeOf(K k, V v) {
        K k2 = k;
        V v2 = v;
        int sizeOf = sizeOf(k2, v2);
        if (sizeOf >= 0) {
            return sizeOf;
        }
        IllegalStateException illegalStateException = r8;
        StringBuilder stringBuilder = r8;
        StringBuilder stringBuilder2 = new StringBuilder();
        IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append("Negative size: ").append(k2).append("=").append(v2).toString());
        throw illegalStateException;
    }

    /* access modifiers changed from: protected */
    public int sizeOf(K k, V v) {
        K k2 = k;
        V v2 = v;
        return 1;
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final synchronized int size() {
        int i;
        synchronized (this) {
            i = this.size;
        }
        return i;
    }

    public final synchronized int maxSize() {
        int i;
        synchronized (this) {
            i = this.maxSize;
        }
        return i;
    }

    public final synchronized int hitCount() {
        int i;
        synchronized (this) {
            i = this.hitCount;
        }
        return i;
    }

    public final synchronized int missCount() {
        int i;
        synchronized (this) {
            i = this.missCount;
        }
        return i;
    }

    public final synchronized int createCount() {
        int i;
        synchronized (this) {
            i = this.createCount;
        }
        return i;
    }

    public final synchronized int putCount() {
        int i;
        synchronized (this) {
            i = this.putCount;
        }
        return i;
    }

    public final synchronized int evictionCount() {
        int i;
        synchronized (this) {
            i = this.evictionCount;
        }
        return i;
    }

    public final synchronized Map<K, V> snapshot() {
        LinkedHashMap linkedHashMap;
        synchronized (this) {
            LinkedHashMap linkedHashMap2 = r5;
            LinkedHashMap linkedHashMap3 = new LinkedHashMap(this.map);
            linkedHashMap = linkedHashMap2;
        }
        return linkedHashMap;
    }

    public final synchronized String toString() {
        String format;
        synchronized (this) {
            int i = this.hitCount + this.missCount;
            int i2 = i != 0 ? (100 * this.hitCount) / i : 0;
            Integer[] numArr = new Object[4];
            Integer[] numArr2 = numArr;
            numArr[0] = Integer.valueOf(this.maxSize);
            numArr = numArr2;
            numArr2 = numArr;
            numArr[1] = Integer.valueOf(this.hitCount);
            numArr = numArr2;
            numArr2 = numArr;
            numArr[2] = Integer.valueOf(this.missCount);
            numArr = numArr2;
            numArr2 = numArr;
            numArr[3] = Integer.valueOf(i2);
            format = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", numArr2);
        }
        return format;
    }
}
