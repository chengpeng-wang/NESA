package com.baidu.android.pushservice.b;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushService;
import com.baidu.android.pushservice.PushSettings;
import com.baidu.android.pushservice.b;
import com.baidu.android.pushservice.util.PushDatabase;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.cookie.ClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class o {
    public static String a = "";
    private static volatile o c = null;
    /* access modifiers changed from: private */
    public Context b = null;
    private r d = null;
    private boolean e;

    public o(Context context) {
        this.b = context.getApplicationContext();
        this.d = r.a(context);
        this.e = false;
    }

    /* access modifiers changed from: private */
    public InputStream a(HttpEntity httpEntity) {
        Header contentEncoding = httpEntity.getContentEncoding();
        return (contentEncoding == null || contentEncoding.getValue().toLowerCase().indexOf("gzip") == -1) ? null : new GZIPInputStream(httpEntity.getContent());
    }

    /* access modifiers changed from: private */
    public String a(InputStream inputStream) {
        return m.a(inputStream);
    }

    /* access modifiers changed from: private */
    public String b(InputStream inputStream) {
        String a = m.a(inputStream);
        if (!TextUtils.isEmpty(a)) {
            try {
                JSONObject jSONObject = new JSONObject(a);
                int i = jSONObject.getInt("config_type");
                int i2 = jSONObject.getInt("interval");
                if (i == 0) {
                    if (i2 > 0) {
                        PushSettings.c((long) i2);
                    }
                } else if (i == 1) {
                    this.e = true;
                } else if (i == 2 && i2 > 0) {
                    PushSettings.b(1);
                    Intent intent = new Intent(PushConstants.ACTION_METHOD);
                    intent.putExtra("method", "com.baidu.android.pushservice.action.ENBALE_APPSTAT");
                    intent.setClass(this.b, PushService.class);
                    PendingIntent service = PendingIntent.getService(this.b.getApplicationContext(), 0, intent, 268435456);
                    long elapsedRealtime = SystemClock.elapsedRealtime() + ((long) i2);
                    AlarmManager alarmManager = (AlarmManager) this.b.getSystemService("alarm");
                    alarmManager.cancel(service);
                    alarmManager.set(1, elapsedRealtime, service);
                }
            } catch (JSONException e) {
                if (b.a(this.b)) {
                    Log.d("StatisticPoster", "parse 201 exception" + e);
                }
            }
        }
        return a;
    }

    /* access modifiers changed from: private */
    public String c(InputStream inputStream) {
        String a = m.a(inputStream);
        if (!TextUtils.isEmpty(a)) {
            try {
                JSONObject jSONObject = new JSONObject(a);
                int i = jSONObject.getInt("error_code");
                jSONObject.getString(PushConstants.EXTRA_ERROR_CODE);
                if (i == 50009) {
                    PushSettings.b(1);
                }
            } catch (JSONException e) {
            }
        }
        return a;
    }

    /* access modifiers changed from: private */
    public boolean d() {
        return (PushSettings.e() == 1 || this.e) ? false : System.currentTimeMillis() - PushSettings.c(this.b) <= 43200000 ? PushDatabase.getBehaviorInfoCounts(PushDatabase.getDb(this.b)) < 10 ? false : m.c(this.b) : true;
    }

    public String a() {
        String str = null;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ClientCookie.VERSION_ATTR, "1.0");
            String b = this.d.b();
            if (!TextUtils.isEmpty(b)) {
                jSONObject.put("common", new JSONObject(b));
            }
            b = this.d.a();
            if (TextUtils.isEmpty(b)) {
                return "";
            }
            byte[] a;
            jSONObject.put("application_info", new JSONArray(b));
            try {
                a = g.a(jSONObject.toString());
                a[0] = (byte) 117;
                a[1] = (byte) 123;
            } catch (IOException e) {
                a = str;
            }
            return a == null ? str : Base64.encodeToString(a, 0);
        } catch (JSONException e2) {
        }
    }

    public void b() {
        try {
            new Thread(new p(this), "PushCheckSendS").start();
        } catch (OutOfMemoryError e) {
            Log.e("StatisticPoster", "OutOfMemoryError when PushCheckSendS");
        }
    }

    public void c() {
        String a = a();
        String str = "http://statsonline.pushct.baidu.com/pushlog";
        try {
            if (!TextUtils.isEmpty(a)) {
                new q(this, str, a).start();
            }
        } catch (OutOfMemoryError e) {
            Log.e("StatisticPoster", "OutOfMemoryError when posting");
        }
    }
}
