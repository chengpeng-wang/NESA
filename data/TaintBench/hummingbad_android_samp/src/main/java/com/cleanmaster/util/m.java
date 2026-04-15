package com.cleanmaster.util;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.LinkedHashMap;

/* compiled from: LruCache */
public class m {
    private final LinkedHashMap a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;

    public m(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.c = i;
        this.a = new LinkedHashMap(0, 0.75f, true);
    }

    /* JADX WARNING: Missing block: B:11:0x0022, code skipped:
            r1 = b(r5);
     */
    /* JADX WARNING: Missing block: B:12:0x0026, code skipped:
            if (r1 != null) goto L_0x002d;
     */
    /* JADX WARNING: Missing block: B:18:0x002d, code skipped:
            monitor-enter(r4);
     */
    /* JADX WARNING: Missing block: B:20:?, code skipped:
            r4.e++;
            r0 = r4.a.put(r5, r1);
     */
    /* JADX WARNING: Missing block: B:21:0x003a, code skipped:
            if (r0 == null) goto L_0x0049;
     */
    /* JADX WARNING: Missing block: B:22:0x003c, code skipped:
            r4.a.put(r5, r0);
     */
    /* JADX WARNING: Missing block: B:23:0x0041, code skipped:
            monitor-exit(r4);
     */
    /* JADX WARNING: Missing block: B:24:0x0042, code skipped:
            if (r0 == null) goto L_0x0056;
     */
    /* JADX WARNING: Missing block: B:25:0x0044, code skipped:
            a(false, r5, r1, r0);
     */
    /* JADX WARNING: Missing block: B:27:?, code skipped:
            r4.b += c(r5, r1);
     */
    /* JADX WARNING: Missing block: B:31:0x0056, code skipped:
            a(r4.c);
     */
    /* JADX WARNING: Missing block: B:36:?, code skipped:
            return null;
     */
    /* JADX WARNING: Missing block: B:37:?, code skipped:
            return r0;
     */
    /* JADX WARNING: Missing block: B:38:?, code skipped:
            return r1;
     */
    public final java.lang.Object a(java.lang.Object r5) {
        /*
        r4 = this;
        if (r5 != 0) goto L_0x000a;
    L_0x0002:
        r0 = new java.lang.NullPointerException;
        r1 = "key == null";
        r0.<init>(r1);
        throw r0;
    L_0x000a:
        monitor-enter(r4);
        r0 = r4.a;	 Catch:{ all -> 0x002a }
        r0 = r0.get(r5);	 Catch:{ all -> 0x002a }
        if (r0 == 0) goto L_0x001b;
    L_0x0013:
        r1 = r4.g;	 Catch:{ all -> 0x002a }
        r1 = r1 + 1;
        r4.g = r1;	 Catch:{ all -> 0x002a }
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
    L_0x001a:
        return r0;
    L_0x001b:
        r0 = r4.h;	 Catch:{ all -> 0x002a }
        r0 = r0 + 1;
        r4.h = r0;	 Catch:{ all -> 0x002a }
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
        r1 = r4.b(r5);
        if (r1 != 0) goto L_0x002d;
    L_0x0028:
        r0 = 0;
        goto L_0x001a;
    L_0x002a:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
        throw r0;
    L_0x002d:
        monitor-enter(r4);
        r0 = r4.e;	 Catch:{ all -> 0x0053 }
        r0 = r0 + 1;
        r4.e = r0;	 Catch:{ all -> 0x0053 }
        r0 = r4.a;	 Catch:{ all -> 0x0053 }
        r0 = r0.put(r5, r1);	 Catch:{ all -> 0x0053 }
        if (r0 == 0) goto L_0x0049;
    L_0x003c:
        r2 = r4.a;	 Catch:{ all -> 0x0053 }
        r2.put(r5, r0);	 Catch:{ all -> 0x0053 }
    L_0x0041:
        monitor-exit(r4);	 Catch:{ all -> 0x0053 }
        if (r0 == 0) goto L_0x0056;
    L_0x0044:
        r2 = 0;
        r4.a(r2, r5, r1, r0);
        goto L_0x001a;
    L_0x0049:
        r2 = r4.b;	 Catch:{ all -> 0x0053 }
        r3 = r4.c(r5, r1);	 Catch:{ all -> 0x0053 }
        r2 = r2 + r3;
        r4.b = r2;	 Catch:{ all -> 0x0053 }
        goto L_0x0041;
    L_0x0053:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0053 }
        throw r0;
    L_0x0056:
        r0 = r4.c;
        r4.a(r0);
        r0 = r1;
        goto L_0x001a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cleanmaster.util.m.a(java.lang.Object):java.lang.Object");
    }

    public final Object b(Object obj, Object obj2) {
        if (obj == null || obj2 == null) {
            throw new NullPointerException("key == null || value == null");
        }
        Object put;
        synchronized (this) {
            this.d++;
            this.b += c(obj, obj2);
            put = this.a.put(obj, obj2);
            if (put != null) {
                this.b -= c(obj, put);
            }
        }
        if (put != null) {
            a(false, obj, put, obj2);
        }
        a(this.c);
        return put;
    }

    /* JADX WARNING: Missing block: B:26:?, code skipped:
            return;
     */
    private void a(int r5) {
        /*
        r4 = this;
    L_0x0000:
        monitor-enter(r4);
        r0 = r4.b;	 Catch:{ all -> 0x0021 }
        if (r0 < 0) goto L_0x0011;
    L_0x0005:
        r0 = r4.a;	 Catch:{ all -> 0x0021 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0021 }
        if (r0 == 0) goto L_0x0013;
    L_0x000d:
        r0 = r4.b;	 Catch:{ all -> 0x0021 }
        if (r0 == 0) goto L_0x0013;
    L_0x0011:
        monitor-exit(r4);	 Catch:{ all -> 0x0021 }
    L_0x0012:
        return;
    L_0x0013:
        r0 = r4.b;	 Catch:{ all -> 0x0021 }
        if (r0 <= r5) goto L_0x001f;
    L_0x0017:
        r0 = r4.a;	 Catch:{ all -> 0x0021 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0021 }
        if (r0 == 0) goto L_0x0024;
    L_0x001f:
        monitor-exit(r4);	 Catch:{ all -> 0x0021 }
        goto L_0x0012;
    L_0x0021:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0021 }
        throw r0;
    L_0x0024:
        r0 = r4.a;	 Catch:{ all -> 0x0021 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x0021 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0021 }
        r0 = r0.next();	 Catch:{ all -> 0x0021 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x0021 }
        r1 = r0.getKey();	 Catch:{ all -> 0x0021 }
        r0 = r0.getValue();	 Catch:{ all -> 0x0021 }
        r2 = r4.a;	 Catch:{ all -> 0x0021 }
        r2.remove(r1);	 Catch:{ all -> 0x0021 }
        r2 = r4.b;	 Catch:{ all -> 0x0021 }
        r3 = r4.c(r1, r0);	 Catch:{ all -> 0x0021 }
        r2 = r2 - r3;
        r4.b = r2;	 Catch:{ all -> 0x0021 }
        r2 = r4.f;	 Catch:{ all -> 0x0021 }
        r2 = r2 + 1;
        r4.f = r2;	 Catch:{ all -> 0x0021 }
        monitor-exit(r4);	 Catch:{ all -> 0x0021 }
        r2 = 1;
        r3 = 0;
        r4.a(r2, r1, r0, r3);
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cleanmaster.util.m.a(int):void");
    }

    /* access modifiers changed from: protected */
    public void a(boolean z, Object obj, Object obj2, Object obj3) {
    }

    /* access modifiers changed from: protected */
    public Object b(Object obj) {
        return null;
    }

    private int c(Object obj, Object obj2) {
        int a = a(obj, obj2);
        if (a >= 0) {
            return a;
        }
        throw new IllegalStateException("Negative size: " + obj + MASTNativeAdConstants.EQUAL + obj2);
    }

    /* access modifiers changed from: protected */
    public int a(Object obj, Object obj2) {
        return 1;
    }

    public final synchronized String toString() {
        String format;
        int i = 0;
        synchronized (this) {
            int i2 = this.g + this.h;
            if (i2 != 0) {
                i = (this.g * 100) / i2;
            }
            format = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.c), Integer.valueOf(this.g), Integer.valueOf(this.h), Integer.valueOf(i)});
        }
        return format;
    }
}
