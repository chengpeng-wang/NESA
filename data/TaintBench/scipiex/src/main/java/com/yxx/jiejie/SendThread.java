package com.yxx.jiejie;

import android.content.Context;
import android.util.Log;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

public class SendThread implements Runnable {
    private String content;
    private Context context;
    String which;

    public SendThread(Context context, String content, String which) {
        this.content = content;
        this.context = context;
        this.which = which;
    }

    public void run() {
        HashMap hm = new HashMap();
        hm.put("p", this.content);
        try {
            String rsp = sendPostRequest(getFromAsset("url.txt") + this.which, hm, HTTP.UTF_8);
        } catch (Exception e) {
            Log.i("zhou", "rsp question");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x003a A:{SYNTHETIC, Splitter:B:21:0x003a} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003f A:{Catch:{ IOException -> 0x0043 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004b A:{SYNTHETIC, Splitter:B:29:0x004b} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0050 A:{Catch:{ IOException -> 0x0054 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004b A:{SYNTHETIC, Splitter:B:29:0x004b} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0050 A:{Catch:{ IOException -> 0x0054 }} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003a A:{SYNTHETIC, Splitter:B:21:0x003a} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003f A:{Catch:{ IOException -> 0x0043 }} */
    public java.lang.String getFromAsset(java.lang.String r10) {
        /*
        r9 = this;
        r3 = 0;
        r5 = 0;
        r0 = 0;
        r7 = "";
        r8 = r9.context;	 Catch:{ IOException -> 0x0034 }
        r8 = r8.getResources();	 Catch:{ IOException -> 0x0034 }
        r8 = r8.getAssets();	 Catch:{ IOException -> 0x0034 }
        r3 = r8.open(r10);	 Catch:{ IOException -> 0x0034 }
        r6 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x0034 }
        r6.<init>(r3);	 Catch:{ IOException -> 0x0034 }
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0067, all -> 0x0060 }
        r1.<init>(r6);	 Catch:{ IOException -> 0x0067, all -> 0x0060 }
        r4 = "";
    L_0x001f:
        r4 = r1.readLine();	 Catch:{ IOException -> 0x006a, all -> 0x0063 }
        if (r4 != 0) goto L_0x0032;
    L_0x0025:
        if (r1 == 0) goto L_0x002a;
    L_0x0027:
        r1.close();	 Catch:{ IOException -> 0x0059 }
    L_0x002a:
        if (r6 == 0) goto L_0x005d;
    L_0x002c:
        r6.close();	 Catch:{ IOException -> 0x0059 }
        r0 = r1;
        r5 = r6;
    L_0x0031:
        return r7;
    L_0x0032:
        r7 = r4;
        goto L_0x001f;
    L_0x0034:
        r2 = move-exception;
    L_0x0035:
        r2.printStackTrace();	 Catch:{ all -> 0x0048 }
        if (r0 == 0) goto L_0x003d;
    L_0x003a:
        r0.close();	 Catch:{ IOException -> 0x0043 }
    L_0x003d:
        if (r5 == 0) goto L_0x0031;
    L_0x003f:
        r5.close();	 Catch:{ IOException -> 0x0043 }
        goto L_0x0031;
    L_0x0043:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0031;
    L_0x0048:
        r8 = move-exception;
    L_0x0049:
        if (r0 == 0) goto L_0x004e;
    L_0x004b:
        r0.close();	 Catch:{ IOException -> 0x0054 }
    L_0x004e:
        if (r5 == 0) goto L_0x0053;
    L_0x0050:
        r5.close();	 Catch:{ IOException -> 0x0054 }
    L_0x0053:
        throw r8;
    L_0x0054:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0053;
    L_0x0059:
        r2 = move-exception;
        r2.printStackTrace();
    L_0x005d:
        r0 = r1;
        r5 = r6;
        goto L_0x0031;
    L_0x0060:
        r8 = move-exception;
        r5 = r6;
        goto L_0x0049;
    L_0x0063:
        r8 = move-exception;
        r0 = r1;
        r5 = r6;
        goto L_0x0049;
    L_0x0067:
        r2 = move-exception;
        r5 = r6;
        goto L_0x0035;
    L_0x006a:
        r2 = move-exception;
        r0 = r1;
        r5 = r6;
        goto L_0x0035;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yxx.jiejie.SendThread.getFromAsset(java.lang.String):java.lang.String");
    }

    private String sendPostRequest(String path, Map<String, String> params, String encoding) throws Exception {
        StringBuilder sb = new StringBuilder("");
        if (!(params == null || params.isEmpty())) {
            for (Entry<String, String> entry : params.entrySet()) {
                sb.append((String) entry.getKey()).append('=').append((String) entry.getValue()).append('&');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        Log.i("zhou", new StringBuilder(String.valueOf(path)).append("   ").append(sb.toString()).append("shit").toString());
        byte[] data = sb.toString().getBytes();
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setRequestMethod(HttpPost.METHOD_NAME);
        conn.setConnectTimeout(1000);
        conn.setDoOutput(true);
        conn.setRequestProperty(HTTP.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);
        conn.setRequestProperty(HTTP.CONTENT_LEN, String.valueOf(data.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        conn.getResponseCode();
        return null;
    }
}
