package com.android.volley.toolbox;

import android.os.SystemClock;
import com.android.volley.Cache.Entry;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.duapps.ad.AdError;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.impl.cookie.DateUtils;

public class BasicNetwork implements Network {
    protected static final boolean DEBUG = VolleyLog.DEBUG;
    private static int DEFAULT_POOL_SIZE = 4096;
    private static int SLOW_REQUEST_THRESHOLD_MS = AdError.TIME_OUT_CODE;
    protected final HttpStack mHttpStack;
    protected final ByteArrayPool mPool;

    public BasicNetwork(HttpStack httpStack) {
        this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
    }

    public BasicNetwork(HttpStack httpStack, ByteArrayPool pool) {
        this.mHttpStack = httpStack;
        this.mPool = pool;
    }

    /* JADX WARNING: Removed duplicated region for block: B:63:0x0143 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00fe  */
    public com.android.volley.NetworkResponse performRequest(com.android.volley.Request<?> r27) throws com.android.volley.VolleyError {
        /*
        r26 = this;
        r23 = android.os.SystemClock.elapsedRealtime();
    L_0x0004:
        r22 = 0;
        r25 = 0;
        r5 = java.util.Collections.emptyMap();
        r21 = new java.util.HashMap;	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r21.<init>();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r2 = r27.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r0 = r26;
        r1 = r21;
        r0.addCacheHeaders(r1, r2);	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r0 = r26;
        r2 = r0.mHttpStack;	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r0 = r27;
        r1 = r21;
        r22 = r2.performRequest(r0, r1);	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r11 = r22.getStatusLine();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r13 = r11.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r2 = r22.getAllHeaders();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r5 = convertHeaders(r2);	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r2 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        if (r13 != r2) goto L_0x0075;
    L_0x003c:
        r20 = r27.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        if (r20 != 0) goto L_0x0054;
    L_0x0042:
        r2 = new com.android.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r3 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r4 = 0;
        r6 = 1;
        r14 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r7 = r14 - r23;
        r2.m422init(r3, r4, r5, r6, r7);	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r10 = r25;
    L_0x0053:
        return r2;
    L_0x0054:
        r0 = r20;
        r2 = r0.responseHeaders;	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r2.putAll(r5);	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r6 = new com.android.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r7 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r0 = r20;
        r8 = r0.data;	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r0 = r20;
        r9 = r0.responseHeaders;	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r10 = 1;
        r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r11 = r2 - r23;
        r6.m422init(r7, r8, r9, r10, r11);	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r10 = r25;
        r2 = r6;
        goto L_0x0053;
    L_0x0075:
        r2 = r22.getEntity();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        if (r2 == 0) goto L_0x00af;
    L_0x007b:
        r2 = r22.getEntity();	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        r0 = r26;
        r10 = r0.entityToBytes(r2);	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
    L_0x0085:
        r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x00a0, ConnectTimeoutException -> 0x015c, MalformedURLException -> 0x0159, IOException -> 0x0157 }
        r7 = r2 - r23;
        r6 = r26;
        r9 = r27;
        r6.logSlowRequests(r7, r9, r10, r11);	 Catch:{ SocketTimeoutException -> 0x00a0, ConnectTimeoutException -> 0x015c, MalformedURLException -> 0x0159, IOException -> 0x0157 }
        r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r13 < r2) goto L_0x009a;
    L_0x0096:
        r2 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r13 <= r2) goto L_0x00b3;
    L_0x009a:
        r2 = new java.io.IOException;	 Catch:{ SocketTimeoutException -> 0x00a0, ConnectTimeoutException -> 0x015c, MalformedURLException -> 0x0159, IOException -> 0x0157 }
        r2.<init>();	 Catch:{ SocketTimeoutException -> 0x00a0, ConnectTimeoutException -> 0x015c, MalformedURLException -> 0x0159, IOException -> 0x0157 }
        throw r2;	 Catch:{ SocketTimeoutException -> 0x00a0, ConnectTimeoutException -> 0x015c, MalformedURLException -> 0x0159, IOException -> 0x0157 }
    L_0x00a0:
        r19 = move-exception;
    L_0x00a1:
        r2 = "socket";
        r3 = new com.android.volley.TimeoutError;
        r3.m3120init();
        r0 = r27;
        attemptRetryOnException(r2, r0, r3);
        goto L_0x0004;
    L_0x00af:
        r2 = 0;
        r10 = new byte[r2];	 Catch:{ SocketTimeoutException -> 0x015f, ConnectTimeoutException -> 0x00c4, MalformedURLException -> 0x00d5, IOException -> 0x00f7 }
        goto L_0x0085;
    L_0x00b3:
        r12 = new com.android.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x00a0, ConnectTimeoutException -> 0x015c, MalformedURLException -> 0x0159, IOException -> 0x0157 }
        r16 = 0;
        r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x00a0, ConnectTimeoutException -> 0x015c, MalformedURLException -> 0x0159, IOException -> 0x0157 }
        r17 = r2 - r23;
        r14 = r10;
        r15 = r5;
        r12.m422init(r13, r14, r15, r16, r17);	 Catch:{ SocketTimeoutException -> 0x00a0, ConnectTimeoutException -> 0x015c, MalformedURLException -> 0x0159, IOException -> 0x0157 }
        r2 = r12;
        goto L_0x0053;
    L_0x00c4:
        r19 = move-exception;
        r10 = r25;
    L_0x00c7:
        r2 = "connection";
        r3 = new com.android.volley.TimeoutError;
        r3.m3120init();
        r0 = r27;
        attemptRetryOnException(r2, r0, r3);
        goto L_0x0004;
    L_0x00d5:
        r19 = move-exception;
        r10 = r25;
    L_0x00d8:
        r2 = new java.lang.RuntimeException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Bad URL ";
        r3 = r3.append(r4);
        r4 = r27.getUrl();
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r19;
        r2.<init>(r3, r0);
        throw r2;
    L_0x00f7:
        r19 = move-exception;
        r10 = r25;
    L_0x00fa:
        r13 = 0;
        r12 = 0;
        if (r22 == 0) goto L_0x0143;
    L_0x00fe:
        r2 = r22.getStatusLine();
        r13 = r2.getStatusCode();
        r2 = "Unexpected response code %d for %s";
        r3 = 2;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r6 = java.lang.Integer.valueOf(r13);
        r3[r4] = r6;
        r4 = 1;
        r6 = r27.getUrl();
        r3[r4] = r6;
        com.android.volley.VolleyLog.e(r2, r3);
        if (r10 == 0) goto L_0x0151;
    L_0x011e:
        r12 = new com.android.volley.NetworkResponse;
        r16 = 0;
        r2 = android.os.SystemClock.elapsedRealtime();
        r17 = r2 - r23;
        r14 = r10;
        r15 = r5;
        r12.m422init(r13, r14, r15, r16, r17);
        r2 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        if (r13 == r2) goto L_0x0135;
    L_0x0131:
        r2 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        if (r13 != r2) goto L_0x014b;
    L_0x0135:
        r2 = "auth";
        r3 = new com.android.volley.AuthFailureError;
        r3.m3104init(r12);
        r0 = r27;
        attemptRetryOnException(r2, r0, r3);
        goto L_0x0004;
    L_0x0143:
        r2 = new com.android.volley.NoConnectionError;
        r0 = r19;
        r2.m4542init(r0);
        throw r2;
    L_0x014b:
        r2 = new com.android.volley.ServerError;
        r2.m3119init(r12);
        throw r2;
    L_0x0151:
        r2 = new com.android.volley.NetworkError;
        r2.m3112init(r12);
        throw r2;
    L_0x0157:
        r19 = move-exception;
        goto L_0x00fa;
    L_0x0159:
        r19 = move-exception;
        goto L_0x00d8;
    L_0x015c:
        r19 = move-exception;
        goto L_0x00c7;
    L_0x015f:
        r19 = move-exception;
        r10 = r25;
        goto L_0x00a1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.BasicNetwork.performRequest(com.android.volley.Request):com.android.volley.NetworkResponse");
    }

    private void logSlowRequests(long requestLifetime, Request<?> request, byte[] responseContents, StatusLine statusLine) {
        if (DEBUG || requestLifetime > ((long) SLOW_REQUEST_THRESHOLD_MS)) {
            String str = "HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]";
            Object[] objArr = new Object[5];
            objArr[0] = request;
            objArr[1] = Long.valueOf(requestLifetime);
            objArr[2] = responseContents != null ? Integer.valueOf(responseContents.length) : "null";
            objArr[3] = Integer.valueOf(statusLine.getStatusCode());
            objArr[4] = Integer.valueOf(request.getRetryPolicy().getCurrentRetryCount());
            VolleyLog.d(str, objArr);
        }
    }

    private static void attemptRetryOnException(String logPrefix, Request<?> request, VolleyError exception) throws VolleyError {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int oldTimeout = request.getTimeoutMs();
        try {
            retryPolicy.retry(exception);
            request.addMarker(String.format("%s-retry [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(oldTimeout)}));
        } catch (VolleyError e) {
            request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(oldTimeout)}));
            throw e;
        }
    }

    private void addCacheHeaders(Map<String, String> headers, Entry entry) {
        if (entry != null) {
            if (entry.etag != null) {
                headers.put("If-None-Match", entry.etag);
            }
            if (entry.lastModified > 0) {
                headers.put("If-Modified-Since", DateUtils.formatDate(new Date(entry.lastModified)));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void logError(String what, String url, long start) {
        long now = SystemClock.elapsedRealtime();
        VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", what, Long.valueOf(now - start), url);
    }

    private byte[] entityToBytes(HttpEntity entity) throws IOException, ServerError {
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(this.mPool, (int) entity.getContentLength());
        byte[] buffer = null;
        try {
            InputStream in = entity.getContent();
            if (in == null) {
                throw new ServerError();
            }
            buffer = this.mPool.getBuf(1024);
            while (true) {
                int count = in.read(buffer);
                if (count == -1) {
                    break;
                }
                bytes.write(buffer, 0, count);
            }
            byte[] toByteArray = bytes.toByteArray();
            return toByteArray;
        } finally {
            try {
                entity.consumeContent();
            } catch (IOException e) {
                VolleyLog.v("Error occured when calling consumingContent", new Object[0]);
            }
            this.mPool.returnBuf(buffer);
            bytes.close();
        }
    }

    protected static Map<String, String> convertHeaders(Header[] headers) {
        Map<String, String> result = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headers.length; i++) {
            result.put(headers[i].getName(), headers[i].getValue());
        }
        return result;
    }
}
