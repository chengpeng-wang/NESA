package com.moceanmobile.mast;

import android.util.Log;

public class AdTracking {
    /* access modifiers changed from: private|static|final */
    public static final String LOGTAG = AdTracking.class.getSimpleName();

    public static void invokeTrackingUrl(final int timeout, final String url, final String userAgent) {
        new Thread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:22:0x008c  */
            /* JADX WARNING: Removed duplicated region for block: B:31:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:19:0x0085  */
            public void run() {
                /*
                r6 = this;
                r1 = 0;
                r0 = new java.net.URL;	 Catch:{ Exception -> 0x0069, all -> 0x0089 }
                r2 = r3;	 Catch:{ Exception -> 0x0069, all -> 0x0089 }
                r0.<init>(r2);	 Catch:{ Exception -> 0x0069, all -> 0x0089 }
                r0 = r0.openConnection();	 Catch:{ Exception -> 0x0069, all -> 0x0089 }
                r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x0069, all -> 0x0089 }
                if (r0 == 0) goto L_0x0063;
            L_0x0010:
                r1 = "GET";
                r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x0095 }
                r1 = "Connection";
                r2 = "close";
                r0.setRequestProperty(r1, r2);	 Catch:{ Exception -> 0x0095 }
                r1 = "User-Agent";
                r2 = r4;	 Catch:{ Exception -> 0x0095 }
                r0.setRequestProperty(r1, r2);	 Catch:{ Exception -> 0x0095 }
                r1 = r2;	 Catch:{ Exception -> 0x0095 }
                r1 = r1 * 1000;
                r0.setConnectTimeout(r1);	 Catch:{ Exception -> 0x0095 }
                r1 = r0.getResponseCode();	 Catch:{ Exception -> 0x0095 }
                r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
                if (r1 == r2) goto L_0x005a;
            L_0x0032:
                r2 = com.moceanmobile.mast.AdTracking.LOGTAG;	 Catch:{ Exception -> 0x0095 }
                r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0095 }
                r4 = "Error while invoking tracking URL : ";
                r3.<init>(r4);	 Catch:{ Exception -> 0x0095 }
                r4 = r3;	 Catch:{ Exception -> 0x0095 }
                r3 = r3.append(r4);	 Catch:{ Exception -> 0x0095 }
                r4 = "HttpResponse:";
                r3 = r3.append(r4);	 Catch:{ Exception -> 0x0095 }
                r1 = r3.append(r1);	 Catch:{ Exception -> 0x0095 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x0095 }
                android.util.Log.w(r2, r1);	 Catch:{ Exception -> 0x0095 }
                if (r0 == 0) goto L_0x0059;
            L_0x0056:
                r0.disconnect();
            L_0x0059:
                return;
            L_0x005a:
                r1 = com.moceanmobile.mast.AdTracking.LOGTAG;	 Catch:{ Exception -> 0x0095 }
                r2 = "Ad Tracker fired successfully";
                android.util.Log.i(r1, r2);	 Catch:{ Exception -> 0x0095 }
            L_0x0063:
                if (r0 == 0) goto L_0x0059;
            L_0x0065:
                r0.disconnect();
                goto L_0x0059;
            L_0x0069:
                r0 = move-exception;
                r0 = r1;
            L_0x006b:
                r1 = com.moceanmobile.mast.AdTracking.LOGTAG;	 Catch:{ all -> 0x0090 }
                r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0090 }
                r3 = "Error while invoking tracking URL : ";
                r2.<init>(r3);	 Catch:{ all -> 0x0090 }
                r3 = r3;	 Catch:{ all -> 0x0090 }
                r2 = r2.append(r3);	 Catch:{ all -> 0x0090 }
                r2 = r2.toString();	 Catch:{ all -> 0x0090 }
                android.util.Log.w(r1, r2);	 Catch:{ all -> 0x0090 }
                if (r0 == 0) goto L_0x0059;
            L_0x0085:
                r0.disconnect();
                goto L_0x0059;
            L_0x0089:
                r0 = move-exception;
            L_0x008a:
                if (r1 == 0) goto L_0x008f;
            L_0x008c:
                r1.disconnect();
            L_0x008f:
                throw r0;
            L_0x0090:
                r1 = move-exception;
                r5 = r1;
                r1 = r0;
                r0 = r5;
                goto L_0x008a;
            L_0x0095:
                r1 = move-exception;
                goto L_0x006b;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.moceanmobile.mast.AdTracking$AnonymousClass1.run():void");
            }
        }).start();
    }

    public static void invokeTrackingUrl(int timeout, String[] urls, String userAgent) {
        if (urls != null) {
            for (String str : urls) {
                Log.d(LOGTAG, "Sending tracker : " + str);
                invokeTrackingUrl(timeout, str, userAgent);
            }
        }
    }
}
