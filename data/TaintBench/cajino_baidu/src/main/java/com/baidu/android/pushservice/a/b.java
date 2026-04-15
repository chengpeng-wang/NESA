package com.baidu.android.pushservice.a;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.baidu.android.common.util.Util;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushService;
import com.baidu.android.pushservice.a;
import com.baidu.android.pushservice.d;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.message.BasicNameValuePair;

public final class b {
    public static void a(Context context) {
        Intent intent = new Intent("com.baidu.pushservice.action.START");
        intent.setClass(context, PushService.class);
        context.startService(intent);
    }

    private static void a(Context context, ArrayList arrayList) {
        if (arrayList != null) {
            PackageManager packageManager = context.getPackageManager();
            synchronized (arrayList) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    d dVar = (d) it.next();
                    PackageInfo packageInfo = null;
                    try {
                        packageInfo = packageManager.getPackageInfo(dVar.a, 0);
                    } catch (NameNotFoundException e) {
                        Log.w("ApiUtils", Log.getStackTraceString(e));
                    }
                    if (packageInfo == null) {
                        Intent intent = new Intent(PushConstants.ACTION_METHOD);
                        intent.putExtra("method", "com.baidu.android.pushservice.action.UNBINDAPP");
                        intent.putExtra("package_name", dVar.a);
                        com.baidu.android.pushservice.b.a(context, intent);
                    }
                }
            }
        }
    }

    public static void a(List list) {
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        list.add(new BasicNameValuePair("timestamp", currentTimeMillis + ""));
        list.add(new BasicNameValuePair(ClientCookie.EXPIRES_ATTR, (86400 + currentTimeMillis) + ""));
        list.add(new BasicNameValuePair("v", "1"));
        try {
            list.add(new BasicNameValuePair("vcode", Util.toMd5(URLEncoder.encode(currentTimeMillis + "bccs", "UTF-8").getBytes(), false)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void b(Context context) {
        a(context, a.a(context).a);
        a(context, a.a(context).b);
    }
}
