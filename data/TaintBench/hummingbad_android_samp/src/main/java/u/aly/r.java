package u.aly;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import com.moceanmobile.mast.Defaults;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.a;
import java.net.URLEncoder;

/* compiled from: NetworkHelper */
public class r {
    public static final int a = 1;
    public static final int b = 2;
    public static final int c = 3;
    private final int d = 1;
    private String e;
    private String f = "10.0.0.172";
    private int g = 80;
    private Context h;
    private w i;
    private f j;

    public r(Context context) {
        this.h = context;
        this.j = h.b(context);
        this.e = a(context);
    }

    public void a(w wVar) {
        this.i = wVar;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0009  */
    public int a(byte[] r4) {
        /*
        r3 = this;
        r1 = 0;
        r0 = 0;
    L_0x0002:
        r2 = com.umeng.analytics.a.f;
        r2 = r2.length;
        if (r0 < r2) goto L_0x000b;
    L_0x0007:
        if (r1 != 0) goto L_0x002b;
    L_0x0009:
        r0 = 1;
    L_0x000a:
        return r0;
    L_0x000b:
        r1 = com.umeng.analytics.a.f;
        r1 = r1[r0];
        r1 = r3.a(r4, r1);
        if (r1 == 0) goto L_0x001f;
    L_0x0015:
        r0 = r3.i;
        if (r0 == 0) goto L_0x0007;
    L_0x0019:
        r0 = r3.i;
        r0.c();
        goto L_0x0007;
    L_0x001f:
        r2 = r3.i;
        if (r2 == 0) goto L_0x0028;
    L_0x0023:
        r2 = r3.i;
        r2.d();
    L_0x0028:
        r0 = r0 + 1;
        goto L_0x0002;
    L_0x002b:
        r0 = r3.b(r1);
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: u.aly.r.a(byte[]):int");
    }

    private boolean a() {
        if (this.h.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", this.h.getPackageName()) != 0) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.h.getSystemService("connectivity")).getActiveNetworkInfo();
            if (!(activeNetworkInfo == null || activeNetworkInfo.getType() == 1)) {
                String extraInfo = activeNetworkInfo.getExtraInfo();
                if (extraInfo != null && (extraInfo.equals("cmwap") || extraInfo.equals("3gwap") || extraInfo.equals("uniwap"))) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    private byte[] a(byte[] r8, java.lang.String r9) {
        /*
        r7 = this;
        r1 = 0;
        r0 = new org.apache.http.client.methods.HttpPost;
        r0.<init>(r9);
        r2 = new org.apache.http.params.BasicHttpParams;
        r2.<init>();
        r3 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        org.apache.http.params.HttpConnectionParams.setConnectionTimeout(r2, r3);
        r3 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        org.apache.http.params.HttpConnectionParams.setSoTimeout(r2, r3);
        r3 = new org.apache.http.impl.client.DefaultHttpClient;
        r3.<init>(r2);
        r2 = "X-Umeng-Sdk";
        r4 = r7.e;
        r0.addHeader(r2, r4);
        r2 = "Msg-Type";
        r4 = "envelope";
        r0.addHeader(r2, r4);
        r2 = r7.a();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        if (r2 == 0) goto L_0x0040;
    L_0x002e:
        r2 = new org.apache.http.HttpHost;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r4 = r7.f;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r5 = r7.g;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2.<init>(r4, r5);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r4 = r3.getParams();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r5 = "http.route.default-proxy";
        r4.setParameter(r5, r2);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
    L_0x0040:
        r2 = new org.apache.http.entity.InputStreamEntity;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r4 = new java.io.ByteArrayInputStream;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r4.<init>(r8);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r5 = r8.length;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r5 = (long) r5;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2.<init>(r4, r5);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r0.setEntity(r2);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2 = r7.i;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        if (r2 == 0) goto L_0x0058;
    L_0x0053:
        r2 = r7.i;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2.e();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
    L_0x0058:
        r0 = r3.execute(r0);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2 = r7.i;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        if (r2 == 0) goto L_0x0065;
    L_0x0060:
        r2 = r7.i;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2.f();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
    L_0x0065:
        r2 = r0.getStatusLine();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2 = r2.getStatusCode();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r3 = "MobclickAgent";
        r4 = new java.lang.StringBuilder;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r5 = "status code : ";
        r4.<init>(r5);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2 = r4.append(r2);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2 = r2.toString();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        u.aly.aj.a(r3, r2);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2 = r0.getStatusLine();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r2 = r2.getStatusCode();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 != r3) goto L_0x00c4;
    L_0x008d:
        r2 = "MobclickAgent";
        r3 = new java.lang.StringBuilder;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r4 = "Sent message to ";
        r3.<init>(r4);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r3 = r3.append(r9);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r3 = r3.toString();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        u.aly.aj.a(r2, r3);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r0 = r0.getEntity();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        if (r0 == 0) goto L_0x00c2;
    L_0x00a7:
        r2 = r0.getContent();	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        r0 = u.aly.av.b(r2);	 Catch:{ all -> 0x00b3 }
        u.aly.av.c(r2);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
    L_0x00b2:
        return r0;
    L_0x00b3:
        r0 = move-exception;
        u.aly.av.c(r2);	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
        throw r0;	 Catch:{ ClientProtocolException -> 0x00b8, IOException -> 0x00c6 }
    L_0x00b8:
        r0 = move-exception;
        r2 = "MobclickAgent";
        r3 = "ClientProtocolException,Failed to send message.";
        u.aly.aj.b(r2, r3, r0);
        r0 = r1;
        goto L_0x00b2;
    L_0x00c2:
        r0 = r1;
        goto L_0x00b2;
    L_0x00c4:
        r0 = r1;
        goto L_0x00b2;
    L_0x00c6:
        r0 = move-exception;
        r2 = "MobclickAgent";
        r3 = "IOException,Failed to send message.";
        u.aly.aj.b(r2, r3, r0);
        r0 = r1;
        goto L_0x00b2;
        */
        throw new UnsupportedOperationException("Method not decompiled: u.aly.r.a(byte[], java.lang.String):byte[]");
    }

    private String a(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Android");
        stringBuffer.append("/");
        stringBuffer.append(a.c);
        stringBuffer.append(" ");
        try {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(ai.v(context));
            stringBuffer2.append("/");
            stringBuffer2.append(ai.d(context));
            stringBuffer2.append(" ");
            stringBuffer2.append(Build.MODEL);
            stringBuffer2.append("/");
            stringBuffer2.append(VERSION.RELEASE);
            stringBuffer2.append(" ");
            stringBuffer2.append(av.a(AnalyticsConfig.getAppkey(context)));
            stringBuffer.append(URLEncoder.encode(stringBuffer2.toString(), Defaults.ENCODING_UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    private int b(byte[] bArr) {
        az abVar = new ab();
        try {
            new aC(new aR.a()).a(abVar, bArr);
            if (abVar.a == 1) {
                this.j.b(abVar.j());
                this.j.c();
            }
            aj.a(a.e, "send log:" + abVar.f());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (abVar.a == 1) {
            return 2;
        }
        return 3;
    }
}
