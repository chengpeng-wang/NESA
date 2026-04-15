package com.baidu.android.pushservice.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.security.Base64;
import com.baidu.android.common.security.RSAUtil;
import com.baidu.android.common.util.DeviceId;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushSDK;
import com.baidu.android.pushservice.b;
import com.baidu.android.pushservice.util.m;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class y implements Runnable {
    private Context a;
    private int b = 5;
    private int c = 0;
    private boolean d = false;

    public y(Context context) {
        this.a = context.getApplicationContext();
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:38:0x017b=Splitter:B:38:0x017b, B:31:0x015e=Splitter:B:31:0x015e} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0164 A:{Catch:{ all -> 0x01a1 }} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0181 A:{Catch:{ all -> 0x01a1 }} */
    private boolean b() {
        /*
        r10 = this;
        r0 = 1;
        r1 = 0;
        r2 = com.baidu.android.pushservice.w.e;
        r3 = com.baidu.android.pushservice.b.a();
        if (r3 == 0) goto L_0x0022;
    L_0x000a:
        r3 = "TokenRequester";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = ">>> token request:";
        r4 = r4.append(r5);
        r4 = r4.append(r2);
        r4 = r4.toString();
        com.baidu.android.common.logging.Log.d(r3, r4);
    L_0x0022:
        r3 = new com.baidu.android.common.net.ProxyHttpClient;
        r4 = r10.a;
        r3.m2163init(r4);
        r4 = new org.apache.http.client.methods.HttpPost;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4.m2273init(r2);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r2 = "Content-Type";
        r5 = "application/x-www-form-urlencoded";
        r4.addHeader(r2, r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r2 = r10.c();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = new org.apache.http.client.entity.UrlEncodedFormEntity;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r6 = "UTF-8";
        r5.m2184init(r2, r6);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4.setEntity(r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r2 = r3.execute(r4);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4 = r2.getStatusLine();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4 = r4.getStatusCode();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r4 != r5) goto L_0x0117;
    L_0x0053:
        r2 = r2.getEntity();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r2 = org.apache.http.util.EntityUtils.toString(r2);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4 = com.baidu.android.pushservice.b.a();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        if (r4 == 0) goto L_0x0079;
    L_0x0061:
        r4 = "TokenRequester";
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5.<init>();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r6 = "<<< RequestToken return string :  ";
        r5 = r5.append(r6);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = r5.append(r2);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = r5.toString();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        com.baidu.android.common.logging.Log.i(r4, r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
    L_0x0079:
        r4 = new org.json.JSONObject;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4.<init>(r2);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = "response_params";
        r4 = r4.getJSONObject(r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        if (r4 == 0) goto L_0x00f7;
    L_0x0086:
        r2 = "channel_id";
        r2 = r4.getString(r2);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = "rsa_channel_token";
        r5 = r4.getString(r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r6 = "expires_time";
        r4 = r4.getString(r6);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r6 = com.baidu.android.pushservice.b.a();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        if (r6 == 0) goto L_0x00e6;
    L_0x009e:
        r6 = "TokenRequester";
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r7.<init>();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r8 = "RequestToken channelId :  ";
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r7 = r7.append(r2);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r7 = r7.toString();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        com.baidu.android.common.logging.Log.i(r6, r7);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r6 = "TokenRequester";
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r7.<init>();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r8 = "RequestToken rsaChannelToken :  ";
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r7 = r7.append(r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r7 = r7.toString();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        com.baidu.android.common.logging.Log.i(r6, r7);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r6 = "TokenRequester";
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r7.<init>();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r8 = "RequestToken expiresTime :  ";
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4 = r7.append(r4);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        com.baidu.android.common.logging.Log.i(r6, r4);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
    L_0x00e6:
        r4 = com.baidu.android.pushservice.y.a();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4.a(r2, r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
    L_0x00ed:
        r1 = 0;
        r10.c = r1;	 Catch:{ IOException -> 0x01a8, Exception -> 0x01a6 }
        r1 = 0;
        r10.d = r1;	 Catch:{ IOException -> 0x01a8, Exception -> 0x01a6 }
        r3.close();
    L_0x00f6:
        return r0;
    L_0x00f7:
        r0 = com.baidu.android.pushservice.b.a();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        if (r0 == 0) goto L_0x0115;
    L_0x00fd:
        r0 = "TokenRequester";
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4.<init>();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = "RequestToken failed :  ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r2 = r4.append(r2);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        com.baidu.android.common.logging.Log.i(r0, r2);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
    L_0x0115:
        r0 = r1;
        goto L_0x00ed;
    L_0x0117:
        r0 = com.baidu.android.pushservice.b.a();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        if (r0 == 0) goto L_0x0115;
    L_0x011d:
        r0 = "TokenRequester";
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4.<init>();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = "RequestToken request failed  ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = r2.getStatusLine();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        com.baidu.android.common.logging.Log.i(r0, r4);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r0 = r2.getEntity();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r0 = org.apache.http.util.EntityUtils.toString(r0);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r2 = "TokenRequester";
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r4.<init>();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r5 = "<<< RequestToken return string :  ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r0 = r4.append(r0);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        r0 = r0.toString();	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        com.baidu.android.common.logging.Log.i(r2, r0);	 Catch:{ IOException -> 0x015a, Exception -> 0x0177 }
        goto L_0x0115;
    L_0x015a:
        r0 = move-exception;
        r9 = r0;
        r0 = r1;
        r1 = r9;
    L_0x015e:
        r2 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x01a1 }
        if (r2 == 0) goto L_0x0170;
    L_0x0164:
        r2 = "TokenRequester";
        com.baidu.android.common.logging.Log.e(r2, r1);	 Catch:{ all -> 0x01a1 }
        r1 = "TokenRequester";
        r2 = "io exception, schedule retry";
        com.baidu.android.common.logging.Log.i(r1, r2);	 Catch:{ all -> 0x01a1 }
    L_0x0170:
        r1 = 1;
        r10.d = r1;	 Catch:{ all -> 0x01a1 }
        r3.close();
        goto L_0x00f6;
    L_0x0177:
        r0 = move-exception;
        r9 = r0;
        r0 = r1;
        r1 = r9;
    L_0x017b:
        r2 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x01a1 }
        if (r2 == 0) goto L_0x0199;
    L_0x0181:
        r2 = "TokenRequester";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01a1 }
        r4.<init>();	 Catch:{ all -> 0x01a1 }
        r5 = "Connect Exception:";
        r4 = r4.append(r5);	 Catch:{ all -> 0x01a1 }
        r1 = r4.append(r1);	 Catch:{ all -> 0x01a1 }
        r1 = r1.toString();	 Catch:{ all -> 0x01a1 }
        com.baidu.android.common.logging.Log.e(r2, r1);	 Catch:{ all -> 0x01a1 }
    L_0x0199:
        r1 = 0;
        r10.d = r1;	 Catch:{ all -> 0x01a1 }
        r3.close();
        goto L_0x00f6;
    L_0x01a1:
        r0 = move-exception;
        r3.close();
        throw r0;
    L_0x01a6:
        r1 = move-exception;
        goto L_0x017b;
    L_0x01a8:
        r1 = move-exception;
        goto L_0x015e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.a.y.b():boolean");
    }

    private List c() {
        int i = 1;
        List<NameValuePair> arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("method", "token"));
        b.a((List) arrayList);
        arrayList.add(new BasicNameValuePair("device_type", "3"));
        arrayList.add(new BasicNameValuePair("rsa_device_id", Base64.encode(RSAUtil.encryptByPublicKey(DeviceId.getDeviceID(this.a).getBytes(), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/7VlVn9LIrZ71PL2RZMbK/Yxc\r\ndb046w/cXVylxS7ouPY06namZUFVhdbUnNRJzmGUZlzs3jUbvMO3l+4c9cw/n9aQ\r\nrm/brgaRDeZbeSrQYRZv60xzJIimuFFxsRM+ku6/dAyYmXiQXlRbgvFQ0MsVng4j\r\nv+cXhtTis2Kbwb8mQwIDAQAB\r\n"), "utf-8")));
        arrayList.add(new BasicNameValuePair("device_name", Build.MODEL));
        SharedPreferences sharedPreferences = this.a.getSharedPreferences(this.a.getPackageName(), 1);
        int i2 = sharedPreferences.getInt("com.baidu.android.pushservice.PushManager.LOGIN_TYPE", -1);
        String string = sharedPreferences.getString("com.baidu.android.pushservice.PushManager.LONGIN_VALUE", "");
        if (i2 == 2) {
            arrayList.add(new BasicNameValuePair("rsa_bduss", PushConstants.rsaEncrypt(sharedPreferences.getString("com.baidu.android.pushservice.PushManager.BDUSS", ""))));
            arrayList.add(new BasicNameValuePair("appid", string));
        } else if (i2 == 0) {
            arrayList.add(new BasicNameValuePair("rsa_access_token", PushConstants.rsaEncrypt(string)));
        } else {
            arrayList.add(new BasicNameValuePair("apikey", string));
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("api_level", VERSION.SDK_INT);
        int[] b = m.b(this.a);
        jSONObject.put("screen_height", b[0]);
        jSONObject.put("screen_width", b[1]);
        jSONObject.put("model", Build.MODEL);
        jSONObject.put("isroot", m.a(this.a) ? 1 : 0);
        String str = "is_baidu_app";
        if (!m.e(this.a, this.a.getPackageName())) {
            i = 0;
        }
        jSONObject.put(str, i);
        jSONObject.put(PushConstants.EXTRA_PUSH_SDK_VERSION, 13);
        arrayList.add(new BasicNameValuePair("info", jSONObject.toString()));
        if (b.a()) {
            for (NameValuePair obj : arrayList) {
                Log.d("TokenRequester", "TOKEN param -- " + obj.toString());
            }
        }
        return arrayList;
    }

    private void d() {
        this.c++;
        if (this.c < this.b) {
            int i = ((1 << (this.c - 1)) * 5) * 1000;
            if (b.a()) {
                Log.i("TokenRequester", "schedule retry-- retry times: " + this.c + "time delay: " + i);
            }
            try {
                Thread.sleep((long) i);
                return;
            } catch (InterruptedException e) {
                if (b.a()) {
                    Log.e("TokenRequester", e);
                    return;
                }
                return;
            }
        }
        if (b.a()) {
            Log.i("TokenRequester", "hava reconnect " + this.b + " times, all failed.");
        }
        this.d = false;
    }

    /* access modifiers changed from: protected */
    public void a() {
        boolean b;
        do {
            b = b();
            if (this.d) {
                d();
            }
            if (this.b <= 0) {
                break;
            }
        } while (this.d);
        if (b.a()) {
            Log.i("TokenRequester", "RequestTokenThread connectResult: " + b);
        }
        if (b) {
            b.b(this.a);
            if (PushSDK.mPushConnection != null) {
                if (b.a()) {
                    Log.i("TokenRequester", "TokenRequester start PushService after Request finish.");
                }
                b.a(this.a);
                return;
            }
            return;
        }
        m.i(this.a);
    }

    public void a(int i) {
        this.b = i;
    }

    public void run() {
        a();
        synchronized (com.baidu.android.pushservice.y.a()) {
            com.baidu.android.pushservice.y.a().a(true);
            com.baidu.android.pushservice.y.a().notifyAll();
        }
    }
}
