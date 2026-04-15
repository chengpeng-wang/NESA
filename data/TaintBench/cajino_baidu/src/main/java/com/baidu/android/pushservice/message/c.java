package com.baidu.android.pushservice.message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.security.Base64;
import com.baidu.android.common.security.RSAUtil;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushSDK;
import com.baidu.android.pushservice.PushService;
import com.baidu.android.pushservice.PushSettings;
import com.baidu.android.pushservice.b;
import com.baidu.android.pushservice.b.j;
import com.baidu.android.pushservice.b.s;
import com.baidu.android.pushservice.d;
import com.baidu.android.pushservice.e;
import com.baidu.android.pushservice.util.a;
import com.baidu.android.pushservice.util.m;
import com.baidu.android.pushservice.y;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class c extends a {
    private a e;

    public c(Context context, e eVar) {
        super(context, eVar);
    }

    public c(Context context, e eVar, InputStream inputStream, OutputStream outputStream) {
        super(context, eVar, inputStream, outputStream);
        this.e = new a(inputStream);
    }

    private int a(String str, String str2, byte[] bArr) {
        PublicMsg a = i.a(str2, str, bArr);
        if (a != null && !TextUtils.isEmpty(a.e)) {
            d b = com.baidu.android.pushservice.a.a(this.c).b(str);
            if (!(b == null || b.a == null)) {
                a.f = b.a;
            }
            a(str, a, str2);
            if (b.a(this.c)) {
                Log.d("MessageHandler", ">>> Show rich media Notification!");
                m.a(">>> Show rich media Notification!");
            }
        } else if (b.a(this.c)) {
            Log.d("MessageHandler", ">>> Don't Show rich media Notification! url is null");
            m.a(">>> Don't Show rich media Notification! url is null");
        }
        return 0;
    }

    private int a(String str, byte[] bArr) {
        int i = 0;
        d b = com.baidu.android.pushservice.a.a(this.c).b(str);
        String str2;
        if (b != null) {
            PackageInfo packageInfo;
            try {
                packageInfo = this.c.getPackageManager().getPackageInfo(b.a, 0);
            } catch (NameNotFoundException e) {
                if (b.a()) {
                    Log.e("MessageHandler", Log.getStackTraceString(e));
                }
                packageInfo = null;
            }
            if (packageInfo == null) {
                com.baidu.android.pushservice.a.b.b(this.c);
                str2 = ">>> NOT deliver to app: " + b.a + ", package has been uninstalled.";
                a(str);
                if (b.a()) {
                    Log.i("MessageHandler", str2);
                    m.a(str2);
                }
            } else {
                Intent intent = new Intent(PushConstants.ACTION_MESSAGE);
                intent.setPackage(b.a);
                intent.putExtra("message", bArr);
                intent.putExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING, new String(bArr, "UTF-8"));
                intent.setFlags(32);
                this.c.sendBroadcast(intent);
                str2 = ">>> Deliver message to client: " + b.a;
                if (b.a()) {
                    Log.d("MessageHandler", str2);
                    m.a(str2);
                }
            }
        } else {
            i = 2;
            str2 = ">>> Not deliver message to client: client NOT found";
            if (b.a()) {
                Log.d("MessageHandler", str2);
                m.a(str2);
            }
        }
        return i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0090  */
    private int a(byte[] r8) {
        /*
        r7 = this;
        r2 = 0;
        r3 = 0;
        r0 = 1;
        r4 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0054 }
        r1 = new java.lang.String;	 Catch:{ JSONException -> 0x0054 }
        r1.<init>(r8);	 Catch:{ JSONException -> 0x0054 }
        r4.<init>(r1);	 Catch:{ JSONException -> 0x0054 }
        r1 = "action";
        r1 = r4.getString(r1);	 Catch:{ JSONException -> 0x0054 }
        r5 = "message";
        r2 = r4.getString(r5);	 Catch:{ JSONException -> 0x0096 }
    L_0x0019:
        if (r0 == 0) goto L_0x007c;
    L_0x001b:
        r0 = android.text.TextUtils.isEmpty(r1);
        if (r0 != 0) goto L_0x007c;
    L_0x0021:
        r0 = new android.content.Intent;
        r0.<init>(r1);
        r4 = "message";
        r0.putExtra(r4, r2);
        r2 = r7.c;
        r2 = com.baidu.android.pushservice.b.a(r2);
        if (r2 == 0) goto L_0x0049;
    L_0x0033:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = ">>> Deliver baidu supper msg with s action: ";
        r2 = r2.append(r4);
        r1 = r2.append(r1);
        r1 = r1.toString();
        com.baidu.android.pushservice.util.m.a(r1);
    L_0x0049:
        r1 = 32;
        r0.setFlags(r1);
        r1 = r7.c;
        r1.sendBroadcast(r0);
        return r3;
    L_0x0054:
        r0 = move-exception;
        r1 = r2;
    L_0x0056:
        r4 = r7.c;
        r4 = com.baidu.android.pushservice.b.a(r4);
        if (r4 == 0) goto L_0x007a;
    L_0x005e:
        r4 = "MessageHandler";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Supper message parsing action Fail:\r\n";
        r5 = r5.append(r6);
        r0 = r0.getMessage();
        r0 = r5.append(r0);
        r0 = r0.toString();
        com.baidu.android.common.logging.Log.d(r4, r0);
    L_0x007a:
        r0 = r3;
        goto L_0x0019;
    L_0x007c:
        r0 = new android.content.Intent;
        r1 = "com.baidu.pushservice.action.supper.MESSAGE";
        r0.<init>(r1);
        r1 = "message";
        r0.putExtra(r1, r8);
        r1 = r7.c;
        r1 = com.baidu.android.pushservice.b.a(r1);
        if (r1 == 0) goto L_0x0049;
    L_0x0090:
        r1 = ">>> Deliver baidu supper msg with g action: com.baidu.pushservice.action.supper.MESSAGE";
        com.baidu.android.pushservice.util.m.a(r1);
        goto L_0x0049;
    L_0x0096:
        r0 = move-exception;
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.message.c.a(byte[]):int");
    }

    private void a(d dVar, j jVar, com.baidu.android.pushservice.b.b bVar) {
        if (dVar != null) {
            bVar.d(dVar.a);
            bVar = m.a(bVar, this.c, dVar.a);
        }
        s.a(this.c, jVar);
        s.a(this.c, bVar);
    }

    private void a(PublicMsg publicMsg, String str) {
        NotificationManager notificationManager = (NotificationManager) this.c.getSystemService("notification");
        Intent intent = new Intent(this.c, PushService.class);
        intent.setAction("com.baidu.pushservice.action.publicmsg.CLICK_V2");
        intent.setData(Uri.parse("content://" + str));
        intent.putExtra("public_msg", publicMsg);
        Intent intent2 = new Intent(this.c, PushService.class);
        intent2.setAction("com.baidu.pushservice.action.publicmsg.DELETE_V2");
        intent2.setData(Uri.parse("content://" + str));
        intent2.putExtra("public_msg", publicMsg);
        intent.setClass(this.c, PushService.class);
        intent2.setClass(this.c, PushService.class);
        PendingIntent service = PendingIntent.getService(this.c, 0, intent, 0);
        PendingIntent service2 = PendingIntent.getService(this.c, 0, intent2, 0);
        Notification notification = new Notification();
        notification.icon = 17301569;
        notification.tickerText = publicMsg.c;
        notification.setLatestEventInfo(this.c, publicMsg.c, publicMsg.d, service);
        notification.sound = RingtoneManager.getDefaultUri(2);
        notification.deleteIntent = service2;
        notification.flags |= 16;
        notificationManager.notify((int) Long.parseLong(str), notification);
    }

    private void a(PublicMsg publicMsg, String str, String str2) {
        Intent intent = new Intent("com.baidu.android.pushservice.action.notification.SHOW");
        intent.setPackage(publicMsg.f);
        intent.putExtra("public_msg", publicMsg);
        intent.putExtra("notify_type", "private");
        intent.putExtra("pushService_package_name", this.c.getPackageName());
        intent.putExtra("message_id", str);
        intent.putExtra(PushConstants.EXTRA_APP_ID, str2);
        intent.putExtra("service_name", "com.baidu.android.pushservice.PushService");
        this.c.sendBroadcast(intent);
    }

    private void a(String str) {
        try {
            String b = PushSettings.b(str);
            if (!TextUtils.isEmpty(b)) {
                int intValue = new Integer(b.substring(0, 1)).intValue();
                if (b.length() > 1) {
                    b = b.substring(1);
                    if (PushSDK.getInstantce(this.c).getRegistrationService() != null) {
                        PushSDK.getInstantce(this.c).getRegistrationService().a(str, intValue, b);
                        PushSettings.a(str, 9, "");
                    }
                }
            }
        } catch (Exception e) {
            Log.i("MessageHandler", "unbind exception" + Log.getStackTraceString(e));
        }
    }

    private void a(String str, PublicMsg publicMsg, String str2) {
        Intent intent = new Intent("com.baidu.android.pushservice.action.notification.SHOW");
        intent.setPackage(publicMsg.f);
        intent.putExtra("public_msg", publicMsg);
        intent.putExtra("notify_type", "rich_media");
        intent.putExtra(PushConstants.EXTRA_APP_ID, str);
        intent.putExtra("message_id", str2);
        intent.putExtra("pushService_package_name", this.c.getPackageName());
        intent.putExtra("service_name", "com.baidu.android.pushservice.PushService");
        this.c.sendBroadcast(intent);
    }

    private void a(String str, String str2, int i, byte[] bArr, int i2) {
        j jVar = new j();
        jVar.c("010101");
        jVar.a(str2);
        jVar.a(System.currentTimeMillis());
        jVar.d(com.baidu.android.pushservice.b.m.d(this.c));
        jVar.b(new String(bArr).length());
        jVar.a(i2);
        jVar.c(i);
        jVar.e(str);
        com.baidu.android.pushservice.b.b bVar = new com.baidu.android.pushservice.b.b(str);
        d b = com.baidu.android.pushservice.a.a(this.c).b(str);
        if (b != null) {
            bVar.c(m.b(b.c));
            bVar.b(b.c);
            bVar.d(b.a);
        } else {
            bVar.c("0");
            bVar.b("0");
            bVar.d("NP");
        }
        a(b, jVar, bVar);
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
    private boolean a(com.baidu.android.pushservice.message.PublicMsg r7) {
        /*
        r6 = this;
        r1 = 1;
        r2 = 0;
        r0 = r7.i;
        if (r0 != r1) goto L_0x0054;
    L_0x0006:
        r0 = r6.c;
        r0 = r0.getApplicationContext();
        r3 = "connectivity";
        r0 = r0.getSystemService(r3);
        r0 = (android.net.ConnectivityManager) r0;
        r0 = r0.getActiveNetworkInfo();
        if (r0 == 0) goto L_0x00bb;
    L_0x001a:
        r3 = com.baidu.android.pushservice.b.a();
        if (r3 == 0) goto L_0x0040;
    L_0x0020:
        r3 = "MessageHandler";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "network type : ";
        r4 = r4.append(r5);
        r5 = r0.getTypeName();
        r5 = r5.toLowerCase();
        r4 = r4.append(r5);
        r4 = r4.toString();
        com.baidu.android.common.logging.Log.d(r3, r4);
    L_0x0040:
        r3 = "wifi";
        r0 = r0.getTypeName();
        r0 = r0.toLowerCase();
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x00bb;
    L_0x0050:
        r0 = r1;
    L_0x0051:
        if (r0 != 0) goto L_0x0054;
    L_0x0053:
        return r2;
    L_0x0054:
        r0 = r7.o;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x006b;
    L_0x005c:
        r0 = com.baidu.android.pushservice.b.a();
        if (r0 == 0) goto L_0x0069;
    L_0x0062:
        r0 = "MessageHandler";
        r2 = ">>> isNeedShowNotification supportapp = null";
        com.baidu.android.common.logging.Log.d(r0, r2);
    L_0x0069:
        r2 = r1;
        goto L_0x0053;
    L_0x006b:
        r0 = r6.c;
        r0 = r0.getPackageManager();
        r3 = r7.o;	 Catch:{ NameNotFoundException -> 0x00a9 }
        r4 = 0;
        r0 = r0.getPackageInfo(r3, r4);	 Catch:{ NameNotFoundException -> 0x00a9 }
        if (r0 == 0) goto L_0x00b9;
    L_0x007a:
        r0 = com.baidu.android.pushservice.b.a();	 Catch:{ NameNotFoundException -> 0x00a9 }
        if (r0 == 0) goto L_0x009a;
    L_0x0080:
        r0 = "MessageHandler";
        r3 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x00a9 }
        r3.<init>();	 Catch:{ NameNotFoundException -> 0x00a9 }
        r4 = ">>> isNeedShowNotification supportapp found \r\n pckname = ";
        r3 = r3.append(r4);	 Catch:{ NameNotFoundException -> 0x00a9 }
        r4 = r7.o;	 Catch:{ NameNotFoundException -> 0x00a9 }
        r3 = r3.append(r4);	 Catch:{ NameNotFoundException -> 0x00a9 }
        r3 = r3.toString();	 Catch:{ NameNotFoundException -> 0x00a9 }
        com.baidu.android.common.logging.Log.d(r0, r3);	 Catch:{ NameNotFoundException -> 0x00a9 }
    L_0x009a:
        r0 = r1;
    L_0x009b:
        r3 = r7.p;
        if (r3 == 0) goto L_0x00a1;
    L_0x009f:
        if (r0 != 0) goto L_0x00a7;
    L_0x00a1:
        r3 = r7.p;
        if (r3 != 0) goto L_0x0053;
    L_0x00a5:
        if (r0 != 0) goto L_0x0053;
    L_0x00a7:
        r2 = r1;
        goto L_0x0053;
    L_0x00a9:
        r0 = move-exception;
        r3 = com.baidu.android.pushservice.b.a();
        if (r3 == 0) goto L_0x00b9;
    L_0x00b0:
        r3 = "MessageHandler";
        r0 = r0.getMessage();
        com.baidu.android.common.logging.Log.d(r3, r0);
    L_0x00b9:
        r0 = r2;
        goto L_0x009b;
    L_0x00bb:
        r0 = r2;
        goto L_0x0051;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.message.c.a(com.baidu.android.pushservice.message.PublicMsg):boolean");
    }

    private byte[] a(long j, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        com.baidu.android.pushservice.util.c cVar = new com.baidu.android.pushservice.util.c(byteArrayOutputStream);
        try {
            cVar.a(j);
            cVar.b(i);
            cVar.b(0);
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
                cVar.a();
                return toByteArray;
            } catch (IOException e) {
                e.printStackTrace();
                return toByteArray;
            }
        } catch (Exception e2) {
            Log.e("MessageHandler", e2);
            try {
                byteArrayOutputStream.close();
                cVar.a();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return null;
        } catch (Throwable e22) {
            try {
                byteArrayOutputStream.close();
                cVar.a();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw e22;
        }
    }

    private byte[] a(String str, int i) {
        byte[] bArr = new byte[i];
        if (str != null) {
            byte[] bytes = str.getBytes();
            System.arraycopy(bytes, 0, bArr, 0, Math.min(bArr.length, bytes.length));
        }
        return bArr;
    }

    private byte[] a(short s, byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        com.baidu.android.pushservice.util.c cVar = new com.baidu.android.pushservice.util.c(byteArrayOutputStream);
        int length = bArr != null ? bArr.length : 0;
        try {
            cVar.a((int) s);
            if (!(s == (short) 5 || s == (short) 6)) {
                cVar.a(13);
                cVar.b(0);
                cVar.a(a(m.e(this.c, this.c.getPackageName()) ? "BaiduApp" : "DevApp", 16));
                cVar.b(-76508268);
                cVar.b(1);
                cVar.b(length);
                if (bArr != null) {
                    cVar.a(bArr);
                }
            }
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
                cVar.a();
                return toByteArray;
            } catch (IOException e) {
                e.printStackTrace();
                return toByteArray;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            try {
                byteArrayOutputStream.close();
                cVar.a();
            } catch (IOException e22) {
                e22.printStackTrace();
            }
            return null;
        } catch (Throwable th) {
            try {
                byteArrayOutputStream.close();
                cVar.a();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            throw th;
        }
    }

    private int b(String str, String str2, byte[] bArr) {
        PublicMsg a = i.a(str2, str, bArr);
        if (a == null || TextUtils.isEmpty(a.d)) {
            if (b.a()) {
                Log.e("MessageHandler", ">>> pMsg JSON parsing error!");
                m.a(">>> pMsg JSON parsing error!");
            }
            return 2;
        }
        d b = com.baidu.android.pushservice.a.a(this.c).b(str);
        if (b == null || b.a == null) {
            if (b.a(this.c)) {
                Log.d("MessageHandler", ">>> Don't Show pMsg private Notification! package name is null");
            }
            a(str);
            m.a(">>> Don't Show pMsg private Notification! package name is null");
            return 0;
        }
        a.f = b.a;
        if (TextUtils.isEmpty(a.c)) {
            PackageManager packageManager = this.c.getPackageManager();
            a.c = packageManager.getApplicationLabel(packageManager.getApplicationInfo(a.f, 128)).toString();
        }
        a(a, str2, str);
        if (!b.a(this.c)) {
            return 1;
        }
        Log.d("MessageHandler", ">>> Show pMsg private Notification!");
        m.a(">>> Show pMsg private Notification!");
        return 1;
    }

    private String b(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        int i = 0;
        while (i < bArr.length) {
            if (bArr[i] == (byte) 0) {
                break;
            }
            i++;
        }
        i = 0;
        return new String(bArr, 0, i);
    }

    private int c(String str, String str2, byte[] bArr) {
        PublicMsg a = i.a(str2, str, bArr);
        if (a == null || TextUtils.isEmpty(a.c) || TextUtils.isEmpty(a.d) || TextUtils.isEmpty(a.e)) {
            Log.e("MessageHandler", ">>> pMsg JSON parsing error!");
            if (b.a()) {
                m.a(">>> pMsg JSON parsing error!");
            }
            return 2;
        } else if (a(a) && m.e(this.c, this.c.getPackageName())) {
            if (b.a()) {
                Log.d("MessageHandler", ">>> Show pMsg Notification!");
                m.a(">>> Show pMsg Notification!");
            }
            a(a, str2);
            return 1;
        } else {
            String str3 = ">>> Don't Show pMsg Notification! --- IsBaiduApp = " + m.e(this.c, this.c.getPackageName());
            if (b.a()) {
                Log.d("MessageHandler", str3);
            }
            m.a(str3);
            return 0;
        }
    }

    private int d(String str, String str2, byte[] bArr) {
        d b = com.baidu.android.pushservice.a.a(this.c).b(str);
        String str3;
        if (b != null) {
            PublicMsg a = i.a(str2, str, bArr);
            if (a != null) {
                Intent intent = new Intent(PushConstants.ACTION_MESSAGE);
                Log.e("Chirs", "client = " + b);
                intent.setPackage(b.a);
                intent.putExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING, a.d);
                intent.setFlags(32);
                if (!TextUtils.isEmpty(a.n)) {
                    try {
                        JSONObject jSONObject = new JSONObject(a.n);
                        Iterator keys = jSONObject.keys();
                        while (keys.hasNext()) {
                            str3 = (String) keys.next();
                            intent.putExtra(str3, jSONObject.getString(str3));
                        }
                        intent.putExtra(PushConstants.EXTRA_EXTRA, a.n);
                    } catch (JSONException e) {
                        Log.w("MessageHandler", "Custom content to JSONObject exception::" + e.getMessage());
                    }
                }
                this.c.sendBroadcast(intent);
                str3 = ">>> Deliver message to client: " + b.a;
                if (b.a()) {
                    Log.i("MessageHandler", str3);
                    m.a(str3);
                }
            }
            return 0;
        }
        str3 = ">>> NOT delivere message to app: " + (b == null ? "client not found." : " client_userId-" + b.c + " in " + b.a);
        a(str);
        Log.i("MessageHandler", str3);
        if (b.a()) {
            m.a(str3);
        }
        return 2;
    }

    private void d(b bVar) {
        String str = new String(bVar.c);
        if (b.a()) {
            Log.i("MessageHandler", "handleMessage MSG_ID_HANDSHAKE : " + str);
        }
        int i = new JSONObject(str).getInt("ret");
        if (b.a()) {
            Log.i("MessageHandler", "handleMessage MSG_ID_HANDSHAKE : result = " + i);
        }
        if (i == 0) {
            com.baidu.android.pushservice.a.b.b(this.c);
        } else if (i == 5003) {
            com.baidu.android.pushservice.a.b.b(this.c);
        } else if (i == 2002) {
            y.a().a(null, null);
            m.i(this.c);
        } else {
            throw new d("MessageHandler handle handshake msg failed. ret = " + i);
        }
    }

    private void e(b bVar) {
        int i = 2;
        byte[] bArr = bVar.c;
        if (bArr != null) {
            a aVar = new a(new ByteArrayInputStream(bArr));
            byte[] bArr2 = new byte[128];
            aVar.a(bArr2);
            String b = b(bArr2);
            com.baidu.android.pushservice.util.b c = aVar.c();
            String str = c.a;
            long j = c.b;
            int a = aVar.a();
            int length = bArr.length - 140;
            if (length <= 0) {
                length = 0;
            }
            byte[] bArr3 = new byte[length];
            System.arraycopy(bArr, 140, bArr3, 0, length);
            if (b.a()) {
                String str2 = "type:" + a + " appid:" + b + " msgId:" + str;
                Log.i("MessageHandler", "New MSG: " + str2);
                Log.i("MessageHandler", "msgBody :" + new String(bArr3));
                m.a("New MSG: " + str2 + " msgBody :" + new String(bArr3));
            }
            b bVar2 = new b();
            if (this.d.a(str)) {
                if (b.a()) {
                    Log.d("MessageHandler", "Message ID(" + str + ") received duplicated, ack success to server directly.");
                }
                Log.i("MessageHandler", ">>> MSG ID duplicated, not deliver to app.");
                a(b, str, a, bArr3, 4);
                bVar2.c = a((short) 3, a(j, 0));
                a(bVar2);
                return;
            }
            if (a == 0 || a == 1) {
                i = a(b, bArr3);
            } else if (a == 6) {
                i = d(b, str, bArr3);
            } else if (a == 2 || a == 3) {
                i = c(b, str, bArr3);
            } else if (a == 5) {
                i = b(b, str, bArr3);
            } else if (a == 7) {
                i = a(b, str, bArr3);
            } else if (a == 10) {
                i = a(bArr3);
            } else if (b.a()) {
                Log.e("MessageHandler", ">>> Unknown msg_type : " + a);
                m.a(">>> Unknown msg_type : " + a);
            }
            a(b, str, a, bArr3, i);
            bVar2.c = a((short) 3, a(j, i));
            a(bVar2);
        }
    }

    public b a(byte[] bArr, int i) {
        int i2 = 20480;
        this.e = new a(new ByteArrayInputStream(bArr));
        short b = this.e.b();
        b bVar = new b();
        bVar.a = b;
        if (b == (short) 6 || b == (short) 5) {
            if (b.a()) {
                Log.i("MessageHandler", "readMessage tiny heart beat from server, msgId:" + b);
            }
            return bVar;
        }
        byte[] bArr2;
        short b2 = this.e.b();
        int a = this.e.a();
        this.e.a(new byte[16]);
        int a2 = this.e.a();
        int a3 = this.e.a();
        int a4 = this.e.a();
        if (b.a()) {
            Log.i("MessageHandler", "readMessage nshead, msgId:" + b + " magicNum:" + Integer.toHexString(a2) + " length:" + a4 + " version =" + b2 + " logId =" + a + " reserved = " + a3);
        }
        if (a4 > 0) {
            if (a4 <= 20480) {
                i2 = a4;
            }
            bArr2 = new byte[i2];
            this.e.a(bArr2);
        } else {
            bArr2 = null;
        }
        bVar.c = bArr2;
        return bVar;
    }

    public void a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("channel_token", new String(RSAUtil.decryptByPublicKey(Base64.decode(y.a().d().getBytes()), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/7VlVn9LIrZ71PL2RZMbK/Yxc\r\ndb046w/cXVylxS7ouPY06namZUFVhdbUnNRJzmGUZlzs3jUbvMO3l+4c9cw/n9aQ\r\nrm/brgaRDeZbeSrQYRZv60xzJIimuFFxsRM+ku6/dAyYmXiQXlRbgvFQ0MsVng4j\r\nv+cXhtTis2Kbwb8mQwIDAQAB\r\n")));
            jSONObject.put("channel_id", y.a().c());
            jSONObject.put("period", 1800);
            jSONObject.put("channel_type", 3);
            jSONObject.put("tinyheart", 1);
            jSONObject.put("connect_version", 2);
            jSONObject.put("tiny_msghead", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jSONObject2 = jSONObject.toString();
        if (b.a()) {
            Log.i("MessageHandler", "onSessionOpened, send handshake msg :" + jSONObject2);
        }
        byte[] a = a((short) 1, jSONObject2.getBytes());
        b bVar = new b();
        bVar.c = a;
        bVar.d = true;
        bVar.a(false);
        a(bVar);
    }

    public void a(int i) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("channel_token", new String(RSAUtil.decryptByPublicKey(Base64.decode(y.a().d().getBytes()), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/7VlVn9LIrZ71PL2RZMbK/Yxc\r\ndb046w/cXVylxS7ouPY06namZUFVhdbUnNRJzmGUZlzs3jUbvMO3l+4c9cw/n9aQ\r\nrm/brgaRDeZbeSrQYRZv60xzJIimuFFxsRM+ku6/dAyYmXiQXlRbgvFQ0MsVng4j\r\nv+cXhtTis2Kbwb8mQwIDAQAB\r\n")));
            jSONObject.put("channel_id", y.a().c());
            jSONObject.put("period", 1800);
            jSONObject.put("channel_type", 3);
            jSONObject.put("tinyheart", 1);
            jSONObject.put("connect_version", 2);
            jSONObject.put("tiny_msghead", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jSONObject2 = jSONObject.toString();
        if (b.a()) {
            Log.i("MessageHandler", "onSessionOpened, send handshake msg :" + jSONObject2);
        }
        byte[] a = a((short) 1, jSONObject2.getBytes());
        b bVar = new b();
        bVar.c = a;
        bVar.d = true;
        bVar.a(false);
        a(bVar);
    }

    public b b() {
        int i = 20480;
        short b = this.e.b();
        b bVar = new b();
        bVar.a = b;
        if (b == (short) 6 || b == (short) 5) {
            if (b.a()) {
                Log.i("MessageHandler", "readMessage tiny heart beat from server, msgId:" + b);
            }
            return bVar;
        }
        byte[] bArr;
        short b2 = this.e.b();
        int a = this.e.a();
        this.e.a(new byte[16]);
        int a2 = this.e.a();
        int a3 = this.e.a();
        int a4 = this.e.a();
        if (b.a()) {
            Log.i("MessageHandler", "readMessage nshead, msgId:" + b + " magicNum:" + Integer.toHexString(a2) + " length:" + a4 + " version =" + b2 + " logId =" + a + " reserved = " + a3);
        }
        if (a4 > 0) {
            if (a4 <= 20480) {
                i = a4;
            }
            bArr = new byte[i];
            this.e.a(bArr);
        } else {
            bArr = null;
        }
        bVar.c = bArr;
        return bVar;
    }

    public void b(b bVar) {
        if (bVar != null) {
            int i = bVar.a;
            if (i == 1) {
                d(bVar);
            } else if (i == 2 || i == 6) {
                c(bVar);
            } else if (i == 4) {
                if (b.a()) {
                    Log.i("MessageHandler", "handleMessage MSG_ID_HEARTBEAT_CLIENT");
                }
            } else if (i == 5) {
                if (b.a()) {
                    Log.i("MessageHandler", "handleMessage MSG_ID_TIMY_HEARTBEAT_CLIENT");
                }
            } else if (i == 3) {
                e(bVar);
            }
        }
    }

    public void c() {
    }

    public void c(b bVar) {
        if (b.a()) {
            Log.i("MessageHandler", "handleMessage: server heart beat id - " + bVar.a);
        }
        if (b.a()) {
            Log.i("MessageHandler", "handleServerHeartbeatMsg, send handshake return msg ");
        }
        byte[] a = a((short) bVar.a, null);
        b bVar2 = new b();
        bVar2.c = a;
        a(bVar2);
    }

    public void d() {
        if (b.a()) {
            Log.i("MessageHandler", "sendHeartbeatMessage ");
        }
        byte[] a = a((short) 5, null);
        b bVar = new b();
        bVar.c = a;
        bVar.d = true;
        bVar.a(true);
        a(bVar);
    }
}
