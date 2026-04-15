package com.mopub.volley.toolbox;

import android.os.SystemClock;
import com.duapps.ad.AdError;
import com.mopub.volley.Cache.Entry;
import com.mopub.volley.Network;
import com.mopub.volley.Request;
import com.mopub.volley.RetryPolicy;
import com.mopub.volley.ServerError;
import com.mopub.volley.VolleyError;
import com.mopub.volley.VolleyLog;
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

    /* JADX WARNING: Removed duplicated region for block: B:21:0x008f A:{ExcHandler: SocketTimeoutException (e java.net.SocketTimeoutException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00af A:{ExcHandler: ConnectTimeoutException (e org.apache.http.conn.ConnectTimeoutException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00be A:{ExcHandler: MalformedURLException (r1_6 'e' java.net.MalformedURLException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x011d A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00dc  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x008f A:{ExcHandler: SocketTimeoutException (e java.net.SocketTimeoutException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00af A:{ExcHandler: ConnectTimeoutException (e org.apache.http.conn.ConnectTimeoutException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00be A:{ExcHandler: MalformedURLException (r1_6 'e' java.net.MalformedURLException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00dc  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x011d A:{SYNTHETIC} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:22:0x0090, code skipped:
            attemptRetryOnException("socket", r17, new com.mopub.volley.TimeoutError());
     */
    /* JADX WARNING: Missing block: B:29:0x00b0, code skipped:
            attemptRetryOnException("connection", r17, new com.mopub.volley.TimeoutError());
     */
    /* JADX WARNING: Missing block: B:30:0x00be, code skipped:
            r1 = move-exception;
     */
    /* JADX WARNING: Missing block: B:32:0x00d7, code skipped:
            throw new java.lang.RuntimeException("Bad URL " + r17.getUrl(), r1);
     */
    /* JADX WARNING: Missing block: B:36:0x00dc, code skipped:
            r2 = r2.getStatusLine().getStatusCode();
            com.mopub.volley.VolleyLog.e("Unexpected response code %d for %s", java.lang.Integer.valueOf(r2), r17.getUrl());
     */
    /* JADX WARNING: Missing block: B:37:0x00fa, code skipped:
            if (r3 != null) goto L_0x00fc;
     */
    /* JADX WARNING: Missing block: B:38:0x00fc, code skipped:
            r1 = new com.mopub.volley.NetworkResponse(r2, r3, r4, false, android.os.SystemClock.elapsedRealtime() - r14);
     */
    /* JADX WARNING: Missing block: B:39:0x0109, code skipped:
            if (r2 == com.picksinit.ErrorInfo.ERROR_CODE_AD_DATA_IS_NULL) goto L_0x010f;
     */
    /* JADX WARNING: Missing block: B:42:0x010f, code skipped:
            attemptRetryOnException("auth", r17, new com.mopub.volley.AuthFailureError(r1));
     */
    /* JADX WARNING: Missing block: B:44:0x0122, code skipped:
            throw new com.mopub.volley.NoConnectionError(r1);
     */
    /* JADX WARNING: Missing block: B:46:0x0128, code skipped:
            throw new com.mopub.volley.ServerError(r1);
     */
    /* JADX WARNING: Missing block: B:48:0x012f, code skipped:
            throw new com.mopub.volley.NetworkError(null);
     */
    /* JADX WARNING: Missing block: B:49:0x0130, code skipped:
            r1 = e;
     */
    /* JADX WARNING: Missing block: B:50:0x0131, code skipped:
            r3 = null;
            r2 = r13;
     */
    /* JADX WARNING: Missing block: B:51:0x0134, code skipped:
            r1 = e;
     */
    /* JADX WARNING: Missing block: B:52:0x0135, code skipped:
            r3 = r9;
            r2 = r13;
     */
    public com.mopub.volley.NetworkResponse performRequest(com.mopub.volley.Request<?> r17) throws com.mopub.volley.VolleyError {
        /*
        r16 = this;
        r14 = android.os.SystemClock.elapsedRealtime();
    L_0x0004:
        r2 = 0;
        r12 = 0;
        r4 = java.util.Collections.emptyMap();
        r1 = new java.util.HashMap;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00d8 }
        r1.<init>();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00d8 }
        r3 = r17.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00d8 }
        r0 = r16;
        r0.addCacheHeaders(r1, r3);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00d8 }
        r0 = r16;
        r3 = r0.mHttpStack;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00d8 }
        r0 = r17;
        r13 = r3.performRequest(r0, r1);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00d8 }
        r10 = r13.getStatusLine();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r2 = r10.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r1 = r13.getAllHeaders();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r4 = convertHeaders(r1);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r1 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        if (r2 != r1) goto L_0x0064;
    L_0x0036:
        r1 = r17.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        if (r1 != 0) goto L_0x004b;
    L_0x003c:
        r1 = new com.mopub.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r2 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r3 = 0;
        r5 = 1;
        r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r6 = r6 - r14;
        r1.m2345init(r2, r3, r4, r5, r6);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
    L_0x004a:
        return r1;
    L_0x004b:
        r2 = r1.responseHeaders;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r2.putAll(r4);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r5 = new com.mopub.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r6 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r7 = r1.data;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r8 = r1.responseHeaders;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r9 = 1;
        r1 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r10 = r1 - r14;
        r5.m2345init(r6, r7, r8, r9, r10);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r1 = r5;
        goto L_0x004a;
    L_0x0064:
        r1 = r13.getEntity();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        if (r1 == 0) goto L_0x009e;
    L_0x006a:
        r1 = r13.getEntity();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        r0 = r16;
        r9 = r0.entityToBytes(r1);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
    L_0x0074:
        r5 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r6 = r5 - r14;
        r5 = r16;
        r8 = r17;
        r5.logSlowRequests(r6, r8, r9, r10);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 < r1) goto L_0x0089;
    L_0x0085:
        r1 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r2 <= r1) goto L_0x00a2;
    L_0x0089:
        r1 = new java.io.IOException;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r1.<init>();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        throw r1;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
    L_0x008f:
        r1 = move-exception;
        r1 = "socket";
        r2 = new com.mopub.volley.TimeoutError;
        r2.m3959init();
        r0 = r17;
        attemptRetryOnException(r1, r0, r2);
        goto L_0x0004;
    L_0x009e:
        r1 = 0;
        r9 = new byte[r1];	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0130 }
        goto L_0x0074;
    L_0x00a2:
        r1 = new com.mopub.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r5 = 0;
        r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r6 = r6 - r14;
        r3 = r9;
        r1.m2345init(r2, r3, r4, r5, r6);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        goto L_0x004a;
    L_0x00af:
        r1 = move-exception;
        r1 = "connection";
        r2 = new com.mopub.volley.TimeoutError;
        r2.m3959init();
        r0 = r17;
        attemptRetryOnException(r1, r0, r2);
        goto L_0x0004;
    L_0x00be:
        r1 = move-exception;
        r2 = new java.lang.RuntimeException;
        r3 = new java.lang.StringBuilder;
        r4 = "Bad URL ";
        r3.<init>(r4);
        r4 = r17.getUrl();
        r3 = r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3, r1);
        throw r2;
    L_0x00d8:
        r1 = move-exception;
        r3 = r12;
    L_0x00da:
        if (r2 == 0) goto L_0x011d;
    L_0x00dc:
        r1 = r2.getStatusLine();
        r2 = r1.getStatusCode();
        r1 = "Unexpected response code %d for %s";
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r7 = java.lang.Integer.valueOf(r2);
        r5[r6] = r7;
        r6 = 1;
        r7 = r17.getUrl();
        r5[r6] = r7;
        com.mopub.volley.VolleyLog.e(r1, r5);
        if (r3 == 0) goto L_0x0129;
    L_0x00fc:
        r1 = new com.mopub.volley.NetworkResponse;
        r5 = 0;
        r6 = android.os.SystemClock.elapsedRealtime();
        r6 = r6 - r14;
        r1.m2345init(r2, r3, r4, r5, r6);
        r3 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        if (r2 == r3) goto L_0x010f;
    L_0x010b:
        r3 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        if (r2 != r3) goto L_0x0123;
    L_0x010f:
        r2 = "auth";
        r3 = new com.mopub.volley.AuthFailureError;
        r3.m3943init(r1);
        r0 = r17;
        attemptRetryOnException(r2, r0, r3);
        goto L_0x0004;
    L_0x011d:
        r2 = new com.mopub.volley.NoConnectionError;
        r2.m4695init(r1);
        throw r2;
    L_0x0123:
        r2 = new com.mopub.volley.ServerError;
        r2.m3958init(r1);
        throw r2;
    L_0x0129:
        r1 = new com.mopub.volley.NetworkError;
        r2 = 0;
        r1.m3951init(r2);
        throw r1;
    L_0x0130:
        r1 = move-exception;
        r3 = r12;
        r2 = r13;
        goto L_0x00da;
    L_0x0134:
        r1 = move-exception;
        r3 = r9;
        r2 = r13;
        goto L_0x00da;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.volley.toolbox.BasicNetwork.performRequest(com.mopub.volley.Request):com.mopub.volley.NetworkResponse");
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
        int timeoutMs = request.getTimeoutMs();
        try {
            retryPolicy.retry(exception);
            request.addMarker(String.format("%s-retry [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(timeoutMs)}));
        } catch (VolleyError e) {
            request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(timeoutMs)}));
            throw e;
        }
    }

    private void addCacheHeaders(Map<String, String> headers, Entry entry) {
        if (entry != null) {
            if (entry.etag != null) {
                headers.put("If-None-Match", entry.etag);
            }
            if (entry.serverDate > 0) {
                headers.put("If-Modified-Since", DateUtils.formatDate(new Date(entry.serverDate)));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void logError(String what, String url, long start) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", what, Long.valueOf(elapsedRealtime - start), url);
    }

    private byte[] entityToBytes(HttpEntity entity) throws IOException, ServerError {
        PoolingByteArrayOutputStream poolingByteArrayOutputStream = new PoolingByteArrayOutputStream(this.mPool, (int) entity.getContentLength());
        byte[] bArr = null;
        try {
            InputStream content = entity.getContent();
            if (content == null) {
                throw new ServerError();
            }
            bArr = this.mPool.getBuf(1024);
            while (true) {
                int read = content.read(bArr);
                if (read == -1) {
                    break;
                }
                poolingByteArrayOutputStream.write(bArr, 0, read);
            }
            byte[] toByteArray = poolingByteArrayOutputStream.toByteArray();
            return toByteArray;
        } finally {
            try {
                entity.consumeContent();
            } catch (IOException e) {
                VolleyLog.v("Error occured when calling consumingContent", new Object[0]);
            }
            this.mPool.returnBuf(bArr);
            poolingByteArrayOutputStream.close();
        }
    }

    protected static Map<String, String> convertHeaders(Header[] headers) {
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headers.length; i++) {
            treeMap.put(headers[i].getName(), headers[i].getValue());
        }
        return treeMap;
    }
}
