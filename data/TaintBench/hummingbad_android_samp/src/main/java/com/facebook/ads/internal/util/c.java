package com.facebook.ads.internal.util;

import java.util.ArrayList;
import java.util.List;

public class c {
    private static final List<b> a = new ArrayList();

    /* JADX WARNING: Missing block: B:9:0x001c, code skipped:
            r1 = new org.json.JSONArray();
            r2 = r0.iterator();
     */
    /* JADX WARNING: Missing block: B:11:0x0029, code skipped:
            if (r2.hasNext() == false) goto L_0x003c;
     */
    /* JADX WARNING: Missing block: B:12:0x002b, code skipped:
            r1.put(((com.facebook.ads.internal.util.b) r2.next()).a());
     */
    /* JADX WARNING: Missing block: B:19:?, code skipped:
            return r1.toString();
     */
    public static java.lang.String a() {
        /*
        r1 = a;
        monitor-enter(r1);
        r0 = a;	 Catch:{ all -> 0x0039 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0039 }
        if (r0 == 0) goto L_0x000f;
    L_0x000b:
        r0 = "";
        monitor-exit(r1);	 Catch:{ all -> 0x0039 }
    L_0x000e:
        return r0;
    L_0x000f:
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x0039 }
        r2 = a;	 Catch:{ all -> 0x0039 }
        r0.<init>(r2);	 Catch:{ all -> 0x0039 }
        r2 = a;	 Catch:{ all -> 0x0039 }
        r2.clear();	 Catch:{ all -> 0x0039 }
        monitor-exit(r1);	 Catch:{ all -> 0x0039 }
        r1 = new org.json.JSONArray;
        r1.<init>();
        r2 = r0.iterator();
    L_0x0025:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x003c;
    L_0x002b:
        r0 = r2.next();
        r0 = (com.facebook.ads.internal.util.b) r0;
        r0 = r0.a();
        r1.put(r0);
        goto L_0x0025;
    L_0x0039:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
    L_0x003c:
        r0 = r1.toString();
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.ads.internal.util.c.a():java.lang.String");
    }

    public static void a(b bVar) {
        synchronized (a) {
            a.add(bVar);
        }
    }
}
