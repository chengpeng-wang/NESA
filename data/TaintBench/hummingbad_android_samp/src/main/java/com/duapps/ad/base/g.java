package com.duapps.ad.base;

import android.content.Context;
import android.text.TextUtils;
import com.duapps.ad.stats.ToolStatsCore;
import com.duapps.ad.stats.h;
import com.duapps.ad.stats.n;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONStringer;

/* compiled from: ParseExecutor */
public class g {
    static final String a = g.class.getSimpleName();
    public static final String b;
    private static g c;
    private static final ThreadFactory d = new h();
    private final PriorityBlockingQueue<Runnable> e = new PriorityBlockingQueue(20);
    /* access modifiers changed from: private */
    public HashSet<String> f = new HashSet();
    /* access modifiers changed from: private */
    public Context g;
    private ThreadPoolExecutor h;

    /* compiled from: ParseExecutor */
    class a implements Comparable<a>, Runnable {
        private com.duapps.ad.entity.a a;
        private long b;

        public final /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return ((a) obj).a.q - this.a.q;
        }

        public a(com.duapps.ad.entity.a aVar) {
            this.a = aVar;
        }

        /* JADX WARNING: Removed duplicated region for block: B:49:0x0138  */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x00df A:{Catch:{ all -> 0x0135 }} */
        /* JADX WARNING: Removed duplicated region for block: B:41:0x0106  */
        public final void run() {
            /*
            r12 = this;
            r10 = 10;
            r4 = 3;
            r5 = 0;
            r3 = 0;
            android.os.Process.setThreadPriority(r10);
            r1 = r12.a;
            r9 = r1.g;
            r1 = com.duapps.ad.base.g.this;
            r1 = r1.f;
            r1.add(r9);
            r1 = com.duapps.ad.base.g.this;
            r1 = r1.g;
            r1 = com.duapps.ad.stats.n.a(r1);
            r1 = r1.a(r9);
            r2 = r1.c;
            if (r2 == 0) goto L_0x002b;
        L_0x0027:
            r2 = r1.c;
            if (r2 != r4) goto L_0x013c;
        L_0x002b:
            r2 = r12.a;
            r4 = r2.g;	 Catch:{ Exception -> 0x015d, all -> 0x015a }
            r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x015d, all -> 0x015a }
            r12.b = r6;	 Catch:{ Exception -> 0x015d, all -> 0x015a }
        L_0x0035:
            if (r4 == 0) goto L_0x00af;
        L_0x0037:
            if (r5 >= r10) goto L_0x00af;
        L_0x0039:
            r5 = r5 + 1;
            r1 = new java.net.URL;	 Catch:{ Exception -> 0x015d, all -> 0x015a }
            r1.<init>(r4);	 Catch:{ Exception -> 0x015d, all -> 0x015a }
            r1 = r1.openConnection();	 Catch:{ Exception -> 0x015d, all -> 0x015a }
            r0 = r1;
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x015d, all -> 0x015a }
            r8 = r0;
            r1 = 0;
            r8.setInstanceFollowRedirects(r1);	 Catch:{ Exception -> 0x00d8 }
            r1 = "User-Agent";
            r6 = com.duapps.ad.base.g.b;	 Catch:{ Exception -> 0x00d8 }
            r8.setRequestProperty(r1, r6);	 Catch:{ Exception -> 0x00d8 }
            r1 = "Pragma";
            r6 = "no-cache";
            r8.setRequestProperty(r1, r6);	 Catch:{ Exception -> 0x00d8 }
            r1 = "Accept-Encoding";
            r6 = "gzip,deflate";
            r8.setRequestProperty(r1, r6);	 Catch:{ Exception -> 0x00d8 }
            r1 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
            r8.setConnectTimeout(r1);	 Catch:{ Exception -> 0x00d8 }
            r1 = r8.getResponseCode();	 Catch:{ Exception -> 0x00d8 }
            r6 = 302; // 0x12e float:4.23E-43 double:1.49E-321;
            if (r1 == r6) goto L_0x007a;
        L_0x006e:
            r6 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
            if (r1 == r6) goto L_0x007a;
        L_0x0072:
            r6 = 307; // 0x133 float:4.3E-43 double:1.517E-321;
            if (r1 == r6) goto L_0x007a;
        L_0x0076:
            r6 = 303; // 0x12f float:4.25E-43 double:1.497E-321;
            if (r1 != r6) goto L_0x010a;
        L_0x007a:
            r1 = "Location";
            r4 = r8.getHeaderField(r1);	 Catch:{ Exception -> 0x00d8 }
            r1 = com.duapps.ad.stats.h.a(r4);	 Catch:{ Exception -> 0x00d8 }
            if (r1 == 0) goto L_0x00b9;
        L_0x0086:
            r1 = com.duapps.ad.base.f.a();	 Catch:{ Exception -> 0x00d8 }
            if (r1 == 0) goto L_0x00a0;
        L_0x008c:
            r1 = com.duapps.ad.base.g.a;	 Catch:{ Exception -> 0x00d8 }
            r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d8 }
            r6 = "DONE [TCTP] url = ";
            r3.<init>(r6);	 Catch:{ Exception -> 0x00d8 }
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x00d8 }
            r3 = r3.toString();	 Catch:{ Exception -> 0x00d8 }
            com.duapps.ad.base.f.c(r1, r3);	 Catch:{ Exception -> 0x00d8 }
        L_0x00a0:
            r3 = 1;
            r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x00d8 }
            r10 = r12.b;	 Catch:{ Exception -> 0x00d8 }
            r6 = r6 - r10;
            r1 = r12;
            r1.a(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x00d8 }
            r8.disconnect();	 Catch:{ Exception -> 0x00d8 }
        L_0x00af:
            r1 = com.duapps.ad.base.g.this;
            r1 = r1.f;
            r1.remove(r9);
            return;
        L_0x00b9:
            r1 = com.duapps.ad.base.f.a();	 Catch:{ Exception -> 0x00d8 }
            if (r1 == 0) goto L_0x00d3;
        L_0x00bf:
            r1 = com.duapps.ad.base.g.a;	 Catch:{ Exception -> 0x00d8 }
            r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d8 }
            r7 = "Middle LOC = ";
            r6.<init>(r7);	 Catch:{ Exception -> 0x00d8 }
            r6 = r6.append(r4);	 Catch:{ Exception -> 0x00d8 }
            r6 = r6.toString();	 Catch:{ Exception -> 0x00d8 }
            com.duapps.ad.base.f.c(r1, r6);	 Catch:{ Exception -> 0x00d8 }
        L_0x00d3:
            r8.disconnect();	 Catch:{ Exception -> 0x00d8 }
            goto L_0x0035;
        L_0x00d8:
            r1 = move-exception;
        L_0x00d9:
            r3 = com.duapps.ad.base.f.a();	 Catch:{ all -> 0x0135 }
            if (r3 == 0) goto L_0x00f7;
        L_0x00df:
            r3 = com.duapps.ad.base.g.a;	 Catch:{ all -> 0x0135 }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0135 }
            r6 = "DONE [TCTB] = EXCEPTION; ";
            r4.<init>(r6);	 Catch:{ all -> 0x0135 }
            r1 = r1.getMessage();	 Catch:{ all -> 0x0135 }
            r1 = r4.append(r1);	 Catch:{ all -> 0x0135 }
            r1 = r1.toString();	 Catch:{ all -> 0x0135 }
            com.duapps.ad.base.f.c(r3, r1);	 Catch:{ all -> 0x0135 }
        L_0x00f7:
            r3 = 3;
            r4 = 0;
            r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x0135 }
            r10 = r12.b;	 Catch:{ all -> 0x0135 }
            r6 = r6 - r10;
            r1 = r12;
            r1.a(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x0135 }
            if (r8 == 0) goto L_0x00af;
        L_0x0106:
            r8.disconnect();
            goto L_0x00af;
        L_0x010a:
            r1 = com.duapps.ad.base.f.a();	 Catch:{ Exception -> 0x00d8 }
            if (r1 == 0) goto L_0x0124;
        L_0x0110:
            r1 = com.duapps.ad.base.g.a;	 Catch:{ Exception -> 0x00d8 }
            r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d8 }
            r6 = "DONE [TCTB] = ";
            r3.<init>(r6);	 Catch:{ Exception -> 0x00d8 }
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x00d8 }
            r3 = r3.toString();	 Catch:{ Exception -> 0x00d8 }
            com.duapps.ad.base.f.c(r1, r3);	 Catch:{ Exception -> 0x00d8 }
        L_0x0124:
            r3 = 2;
            r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x00d8 }
            r10 = r12.b;	 Catch:{ Exception -> 0x00d8 }
            r6 = r6 - r10;
            r1 = r12;
            r1.a(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x00d8 }
            r8.disconnect();	 Catch:{ Exception -> 0x00d8 }
            goto L_0x00af;
        L_0x0135:
            r1 = move-exception;
        L_0x0136:
            if (r8 == 0) goto L_0x013b;
        L_0x0138:
            r8.disconnect();
        L_0x013b:
            throw r1;
        L_0x013c:
            r2 = com.duapps.ad.base.f.a();
            if (r2 == 0) goto L_0x00af;
        L_0x0142:
            r2 = com.duapps.ad.base.g.a;
            r3 = new java.lang.StringBuilder;
            r4 = "DONE [CACHED] type = ";
            r3.<init>(r4);
            r1 = r1.c;
            r1 = r3.append(r1);
            r1 = r1.toString();
            com.duapps.ad.base.f.c(r2, r1);
            goto L_0x00af;
        L_0x015a:
            r1 = move-exception;
            r8 = r3;
            goto L_0x0136;
        L_0x015d:
            r1 = move-exception;
            r8 = r3;
            goto L_0x00d9;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.duapps.ad.base.g$a.run():void");
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return this.a.equals(((a) obj).a);
        }

        private void a(com.duapps.ad.entity.a aVar, int i, String str, int i2, long j) {
            g.this.a(aVar, i, i2, j);
            d dVar = new d();
            dVar.a = aVar.g;
            dVar.d = str;
            dVar.b = aVar.c;
            dVar.c = i;
            dVar.e = System.currentTimeMillis();
            n.a(g.this.g).a(dVar);
        }
    }

    static {
        String property = System.getProperty("http.agent");
        if (TextUtils.isEmpty(property)) {
            b = "dianxinosdxbs/3.2 (Linux; Android; Tapas OTA) Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.18) Gecko/20110628 Ubuntu/10.04 (lucid) Firefox/3.6.18";
        } else {
            b = property;
        }
    }

    public static g a(Context context) {
        synchronized (g.class) {
            if (c == null) {
                c = new g(context.getApplicationContext());
            }
        }
        return c;
    }

    private g(Context context) {
        this.g = context;
        this.h = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, this.e, d);
    }

    public final d a(String str) {
        return n.a(this.g).a(str);
    }

    public final boolean a(List<com.duapps.ad.entity.a> list) {
        for (com.duapps.ad.entity.a aVar : list) {
            if (!d.a(this.g, aVar.c)) {
                Context context = this.g;
                if (com.duapps.ad.entity.a.a(aVar) && !h.a(aVar.g)) {
                    if (this.f.contains(aVar.g)) {
                        f.c(a, "Task already Running.");
                    } else {
                        a aVar2 = new a(aVar);
                        if (this.e.contains(aVar2)) {
                            f.c(a, "Task already in Queue");
                        } else {
                            this.h.execute(aVar2);
                        }
                    }
                }
            }
        }
        return true;
    }

    /* access modifiers changed from: final */
    public final void a(com.duapps.ad.entity.a aVar, int i, int i2, long j) {
        Context context = this.g;
        if (1 <= i.l(context)) {
            try {
                ToolStatsCore.getInstance(context).reportEvent(MASTNativeAdConstants.RESPONSE_NATIVE_STRING, new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value("pclick").key(MASTNativeAdConstants.ID_STRING).value(aVar.a).key("logid").value(aVar.m).key("sid").value((long) aVar.o).key("ptype").value((long) i).key("loop").value((long) i2).key("tsi").value(j).endObject().toString(), 1);
            } catch (JSONException e) {
            }
        }
    }

    public static <T extends com.duapps.ad.entity.a> List<T> a(Context context, List<T> list) {
        n a = n.a(context);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            com.duapps.ad.entity.a aVar = (com.duapps.ad.entity.a) it.next();
            if (com.duapps.ad.entity.a.a(aVar) && a.b(aVar.g) == 2) {
                it.remove();
            }
        }
        return list;
    }
}
