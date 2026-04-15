package com.baidu.android.pushservice.a;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.net.ConnectManager;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b;
import com.baidu.android.pushservice.b.m;
import com.baidu.android.pushservice.b.s;
import com.baidu.android.pushservice.d;
import com.baidu.android.pushservice.w;
import com.baidu.android.pushservice.y;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class a implements Runnable {
    protected Context a;
    protected l b;
    protected String c;
    protected boolean d = true;

    public a(l lVar, Context context) {
        this.b = lVar;
        this.a = context.getApplicationContext();
        this.c = w.f;
    }

    private void b(int i, byte[] bArr) {
        Intent intent = new Intent("com.baidu.android.pushservice.action.internal.RECEIVE");
        intent.putExtra("method", this.b.a);
        intent.putExtra(PushConstants.EXTRA_ERROR_CODE, i);
        intent.putExtra("content", bArr);
        intent.putExtra("appid", this.b.f);
        intent.setFlags(32);
        a(intent);
        if (b.a()) {
            Log.d("BaseBaseApiProcessor", "> sendInternalMethodResult  ,method:" + this.b.a + " ,errorCode : " + i + " ,content : " + new String(bArr));
        }
        this.a.sendBroadcast(intent);
    }

    /* access modifiers changed from: protected */
    public void a() {
        if (this.b != null && !TextUtils.isEmpty(this.b.a)) {
            if (!this.b.a.equals("com.baidu.android.pushservice.action.UNBIND") && TextUtils.isEmpty(this.b.e)) {
                return;
            }
            if (ConnectManager.isNetworkConnected(this.a)) {
                y a = y.a();
                synchronized (a) {
                    if (this.d && !a.e()) {
                        a.a(this.a, false);
                        this.d = false;
                        while (!a.b()) {
                            try {
                                a.wait();
                            } catch (InterruptedException e) {
                                if (b.a()) {
                                    Log.e("BaseBaseApiProcessor", e.getMessage());
                                }
                            }
                        }
                    }
                }
                if (y.a().e()) {
                    boolean b = b();
                    if (b.a()) {
                        Log.i("BaseBaseApiProcessor", "netWorkConnect connectResult: " + b);
                        return;
                    }
                    return;
                }
                a((int) PushConstants.ERROR_SERVICE_NOT_AVAILABLE);
                return;
            }
            if (b.a()) {
                Log.e("BaseBaseApiProcessor", "Network is not useful!");
            }
            a((int) PushConstants.ERROR_NETWORK_ERROR);
            b.a(this.a);
            if (b.a()) {
                Log.i("BaseBaseApiProcessor", "startPushService BaseApiProcess");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void a(int i) {
        a(i, PushConstants.getErrorMsg(i).getBytes());
    }

    /* access modifiers changed from: protected */
    public void a(int i, byte[] bArr) {
        if (TextUtils.isEmpty(this.b.b) || !this.b.b.equals("internal")) {
            Intent intent = new Intent(PushConstants.ACTION_RECEIVE);
            intent.putExtra("method", this.b.a);
            intent.putExtra(PushConstants.EXTRA_ERROR_CODE, i);
            intent.putExtra("content", bArr);
            intent.setFlags(32);
            a(intent);
            if (this.b.a.equals(PushConstants.METHOD_BIND)) {
                com.baidu.android.pushservice.b.a aVar = new com.baidu.android.pushservice.b.a();
                aVar.c("020101");
                aVar.a(System.currentTimeMillis());
                aVar.d(m.d(this.a));
                com.baidu.android.pushservice.b.b bVar = new com.baidu.android.pushservice.b.b();
                try {
                    JSONObject jSONObject = new JSONObject(new String(bArr));
                    aVar.b(jSONObject.getString("request_id"));
                    if (i != 0) {
                        aVar.a(jSONObject.getString(PushConstants.EXTRA_ERROR_CODE));
                    }
                    String string = jSONObject.getJSONObject("response_params").getString("appid");
                    d b = com.baidu.android.pushservice.a.a(this.a).b(string);
                    aVar.e(string);
                    String string2 = jSONObject.getJSONObject("response_params").getString(PushConstants.EXTRA_USER_ID);
                    aVar.e(string);
                    bVar.a(string);
                    bVar.c(com.baidu.android.pushservice.util.m.b(string2));
                    bVar.b(string2);
                    if (b != null) {
                        bVar.d(b.a);
                        bVar = com.baidu.android.pushservice.util.m.a(bVar, this.a, b.a);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aVar.a(i);
                s.a(this.a, aVar);
                s.a(this.a, bVar);
            }
            if (!TextUtils.isEmpty(this.b.e)) {
                intent.setPackage(this.b.e);
                if (b.a()) {
                    Log.d("BaseBaseApiProcessor", "> sendResult to " + this.b.e + " ,method:" + this.b.a + " ,errorCode : " + i + " ,content : " + new String(bArr));
                }
                this.a.sendBroadcast(intent);
                return;
            }
            return;
        }
        b(i, bArr);
    }

    /* access modifiers changed from: protected */
    public void a(Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void a(String str) {
        if (str != null) {
            if (!str.startsWith("{\"")) {
                str = str.substring(str.indexOf("{\""));
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                int i = jSONObject.getInt("error_code");
                String string = jSONObject.getString(PushConstants.EXTRA_ERROR_CODE);
                String string2 = jSONObject.getString("request_id");
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(PushConstants.EXTRA_ERROR_CODE, string);
                jSONObject2.put("requestId", string2);
                a(i, jSONObject2.toString().getBytes());
            } catch (JSONException e) {
                Log.e("BaseBaseApiProcessor", e.getMessage());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        b.a(list);
        if (!TextUtils.isEmpty(this.b.h)) {
            list.add(new BasicNameValuePair("rsa_bduss", this.b.h));
            list.add(new BasicNameValuePair("appid", this.b.f));
        } else if (TextUtils.isEmpty(this.b.d)) {
            list.add(new BasicNameValuePair("apikey", this.b.i));
        } else {
            list.add(new BasicNameValuePair("rsa_access_token", this.b.d));
        }
    }

    /* access modifiers changed from: protected */
    public String b(String str) {
        return str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0102 A:{Splitter:B:9:0x0046, ExcHandler: IOException (r0_7 'e' java.io.IOException)} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x012b A:{Catch:{ IOException -> 0x0102, Exception -> 0x014b, all -> 0x0146 }} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0140  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0136 A:{Catch:{ IOException -> 0x0102, Exception -> 0x014b, all -> 0x0146 }} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:28:0x0102, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:31:0x0107, code skipped:
            if (com.baidu.android.pushservice.b.a() != false) goto L_0x0109;
     */
    /* JADX WARNING: Missing block: B:32:0x0109, code skipped:
            com.baidu.android.common.logging.Log.e("BaseBaseApiProcessor", r0.getMessage());
            com.baidu.android.common.logging.Log.i("BaseBaseApiProcessor", "io exception do something ? ");
     */
    /* JADX WARNING: Missing block: B:33:0x0119, code skipped:
            a((int) com.baidu.android.pushservice.PushConstants.ERROR_SERVICE_NOT_AVAILABLE);
     */
    /* JADX WARNING: Missing block: B:34:0x011e, code skipped:
            r3.close();
     */
    /* JADX WARNING: Missing block: B:40:0x012b, code skipped:
            com.baidu.android.common.logging.Log.e("BaseBaseApiProcessor", r0.getMessage());
     */
    /* JADX WARNING: Missing block: B:42:0x0136, code skipped:
            a((int) com.baidu.android.pushservice.PushConstants.ERROR_SERVICE_NOT_AVAILABLE_TEMP);
     */
    /* JADX WARNING: Missing block: B:46:?, code skipped:
            a((int) com.baidu.android.pushservice.PushConstants.ERROR_UNKNOWN);
     */
    /* JADX WARNING: Missing block: B:48:0x0147, code skipped:
            r3.close();
     */
    /* JADX WARNING: Missing block: B:50:0x014b, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:55:?, code skipped:
            return false;
     */
    public boolean b() {
        /*
        r7 = this;
        r0 = 1;
        r1 = 0;
        r2 = "BaseBaseApiProcessor";
        r3 = "networkConnect";
        com.baidu.android.common.logging.Log.i(r2, r3);
        r2 = r7.c;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 == 0) goto L_0x001f;
    L_0x0011:
        r0 = com.baidu.android.pushservice.b.a();
        if (r0 == 0) goto L_0x001e;
    L_0x0017:
        r0 = "BaseBaseApiProcessor";
        r2 = "mUrl is null";
        com.baidu.android.common.logging.Log.e(r0, r2);
    L_0x001e:
        return r1;
    L_0x001f:
        r2 = com.baidu.android.pushservice.b.a();
        if (r2 == 0) goto L_0x003f;
    L_0x0025:
        r2 = "BaseBaseApiProcessor";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Request Url = ";
        r3 = r3.append(r4);
        r4 = r7.c;
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.baidu.android.common.logging.Log.d(r2, r3);
    L_0x003f:
        r3 = new com.baidu.android.common.net.ProxyHttpClient;
        r2 = r7.a;
        r3.m2163init(r2);
        r2 = new org.apache.http.client.methods.HttpPost;	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r4 = r7.c;	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r2.m2273init(r4);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r4 = "Content-Type";
        r5 = "application/x-www-form-urlencoded";
        r2.addHeader(r4, r5);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r4 = new java.util.ArrayList;	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r4.<init>();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r7.a(r4);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r5 = new org.apache.http.client.entity.UrlEncodedFormEntity;	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r6 = "UTF-8";
        r5.m2184init(r4, r6);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r2.setEntity(r5);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r4 = r3.execute(r2);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r2 = r4.getStatusLine();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r2 = r2.getStatusCode();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r5 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 != r5) goto L_0x00ae;
    L_0x0076:
        r2 = r4.getEntity();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r2 = org.apache.http.util.EntityUtils.toString(r2);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r4 = com.baidu.android.pushservice.b.a();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        if (r4 == 0) goto L_0x009c;
    L_0x0084:
        r4 = "BaseBaseApiProcessor";
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r5.<init>();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r6 = "<<< networkRegister return string :  ";
        r5 = r5.append(r6);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r5 = r5.append(r2);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r5 = r5.toString();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        com.baidu.android.common.logging.Log.i(r4, r5);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
    L_0x009c:
        r2 = r7.b(r2);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r4 = 0;
        r2 = r2.getBytes();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r7.a(r4, r2);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
    L_0x00a8:
        r3.close();
        r1 = r0;
        goto L_0x001e;
    L_0x00ae:
        r2 = "BaseBaseApiProcessor";
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r5.<init>();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r6 = "networkRegister request failed  ";
        r5 = r5.append(r6);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r6 = r4.getStatusLine();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r5 = r5.append(r6);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r5 = r5.toString();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        com.baidu.android.common.logging.Log.i(r2, r5);	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r2 = r4.getStatusLine();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r2 = r2.getStatusCode();	 Catch:{ IOException -> 0x0102, Exception -> 0x0123 }
        r5 = 503; // 0x1f7 float:7.05E-43 double:2.485E-321;
        if (r2 != r5) goto L_0x014d;
    L_0x00d6:
        r2 = r0;
    L_0x00d7:
        r0 = r4.getEntity();	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
        r0 = org.apache.http.util.EntityUtils.toString(r0);	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
        r4 = com.baidu.android.pushservice.b.a();	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
        if (r4 == 0) goto L_0x00fd;
    L_0x00e5:
        r4 = "BaseBaseApiProcessor";
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
        r5.<init>();	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
        r6 = "<<< networkRegister return string :  ";
        r5 = r5.append(r6);	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
        r5 = r5.append(r0);	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
        r5 = r5.toString();	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
        com.baidu.android.common.logging.Log.i(r4, r5);	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
    L_0x00fd:
        r7.a(r0);	 Catch:{ IOException -> 0x0102, Exception -> 0x014b }
        r0 = r1;
        goto L_0x00a8;
    L_0x0102:
        r0 = move-exception;
        r2 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x0146 }
        if (r2 == 0) goto L_0x0119;
    L_0x0109:
        r2 = "BaseBaseApiProcessor";
        r0 = r0.getMessage();	 Catch:{ all -> 0x0146 }
        com.baidu.android.common.logging.Log.e(r2, r0);	 Catch:{ all -> 0x0146 }
        r0 = "BaseBaseApiProcessor";
        r2 = "io exception do something ? ";
        com.baidu.android.common.logging.Log.i(r0, r2);	 Catch:{ all -> 0x0146 }
    L_0x0119:
        r0 = 10002; // 0x2712 float:1.4016E-41 double:4.9416E-320;
        r7.a(r0);	 Catch:{ all -> 0x0146 }
        r3.close();
        goto L_0x001e;
    L_0x0123:
        r0 = move-exception;
        r2 = r1;
    L_0x0125:
        r4 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x0146 }
        if (r4 == 0) goto L_0x0134;
    L_0x012b:
        r4 = "BaseBaseApiProcessor";
        r0 = r0.getMessage();	 Catch:{ all -> 0x0146 }
        com.baidu.android.common.logging.Log.e(r4, r0);	 Catch:{ all -> 0x0146 }
    L_0x0134:
        if (r2 == 0) goto L_0x0140;
    L_0x0136:
        r0 = 10003; // 0x2713 float:1.4017E-41 double:4.942E-320;
        r7.a(r0);	 Catch:{ all -> 0x0146 }
    L_0x013b:
        r3.close();
        goto L_0x001e;
    L_0x0140:
        r0 = 20001; // 0x4e21 float:2.8027E-41 double:9.882E-320;
        r7.a(r0);	 Catch:{ all -> 0x0146 }
        goto L_0x013b;
    L_0x0146:
        r0 = move-exception;
        r3.close();
        throw r0;
    L_0x014b:
        r0 = move-exception;
        goto L_0x0125;
    L_0x014d:
        r2 = r1;
        goto L_0x00d7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.a.a.b():boolean");
    }

    public void run() {
        a();
    }
}
