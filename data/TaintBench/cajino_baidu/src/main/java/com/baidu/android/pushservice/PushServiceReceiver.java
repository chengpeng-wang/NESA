package com.baidu.android.pushservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.message.PublicMsg;
import com.baidu.android.pushservice.richmedia.b;
import com.baidu.android.pushservice.richmedia.n;
import com.baidu.android.pushservice.richmedia.o;
import com.baidu.android.pushservice.richmedia.p;
import java.io.File;

public class PushServiceReceiver extends BroadcastReceiver {
    private void a(Context context, String str, String str2, PublicMsg publicMsg, String str3) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        Intent intent = new Intent("com.baidu.android.pushservice.action.media.CLICK");
        intent.setClassName(str, str2);
        intent.setData(Uri.parse("content://" + publicMsg.a));
        intent.putExtra("public_msg", publicMsg);
        intent.putExtra(PushConstants.EXTRA_APP_ID, str3);
        PendingIntent service = PendingIntent.getService(context, 0, intent, 0);
        if (b.a()) {
            Log.i("PushServiceReceiver", "Set click broadcast, pkgname: " + publicMsg.f + " action: " + "com.baidu.android.pushservice.action.media.CLICK");
        }
        Intent intent2 = new Intent();
        intent2.setClassName(str, str2);
        intent2.setAction("com.baidu.android.pushservice.action.media.DELETE");
        intent2.setData(Uri.parse("content://" + publicMsg.a));
        intent2.putExtra("public_msg", publicMsg);
        intent2.putExtra(PushConstants.EXTRA_APP_ID, str3);
        PendingIntent service2 = PendingIntent.getService(context, 0, intent2, 0);
        Notification a = c.a(context, 8888, publicMsg.c, "富媒体消息：点击后下载与查看");
        a.contentIntent = service;
        a.deleteIntent = service2;
        notificationManager.notify(publicMsg.a, 0, a);
    }

    private void a(Context context, String str, String str2, PublicMsg publicMsg, String str3, String str4) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        Intent intent = new Intent();
        intent.setClassName(str, str2);
        intent.setAction("com.baidu.android.pushservice.action.privatenotification.CLICK");
        intent.setData(Uri.parse("content://" + str3));
        intent.putExtra("public_msg", publicMsg);
        intent.putExtra(PushConstants.EXTRA_APP_ID, str4);
        intent.putExtra("msg_id", str3);
        PendingIntent service = PendingIntent.getService(context, 0, intent, 0);
        intent = new Intent();
        intent.setClassName(str, str2);
        intent.setAction("com.baidu.android.pushservice.action.privatenotification.DELETE");
        intent.setData(Uri.parse("content://" + str3));
        intent.putExtra("public_msg", publicMsg);
        intent.putExtra(PushConstants.EXTRA_APP_ID, str4);
        intent.putExtra("msg_id", str3);
        PendingIntent service2 = PendingIntent.getService(context, 0, intent, 0);
        Notification a = publicMsg.j == 0 ? c.a(context, publicMsg.j, publicMsg.m, publicMsg.c, publicMsg.d) : c.a(context, publicMsg.j, publicMsg.c, publicMsg.d);
        a.contentIntent = service;
        a.deleteIntent = service2;
        notificationManager.notify(str3, 0, a);
    }

    public void a(Context context, PublicMsg publicMsg) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            Uri parse = Uri.parse(publicMsg.e);
            String path = parse.getPath();
            if (!TextUtils.isEmpty(parse.getPath())) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "baidu/pushservice/files" + "/" + parse.getAuthority() + "/" + path.substring(0, path.lastIndexOf(47)));
                if (b.a()) {
                    Log.d("PushServiceReceiver", "<<< download url " + parse.toString());
                }
                n a = p.a(o.REQ_TYPE_GET_ZIP, parse.toString());
                a.a = publicMsg.f;
                a.b = file.getAbsolutePath();
                a.c = publicMsg.c;
                a.d = publicMsg.d;
                new b(context, new o(this, context, publicMsg)).execute(new n[]{a});
                return;
            }
            return;
        }
        Toast makeText = Toast.makeText(context, "请插入SD卡", 1);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.BOOT_COMPLETED".equals(action) || "android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            Log.i("PushServiceReceiver", "start PushSerevice for ACTION_BOOT_COMPLETED or CONNECTIVITY_ACTION");
            b.a(context, new Intent("com.baidu.pushservice.action.START"));
        } else if ("com.baidu.android.pushservice.action.notification.SHOW".equals(action)) {
            String stringExtra = intent.getStringExtra("pushService_package_name");
            String stringExtra2 = intent.getStringExtra("service_name");
            PublicMsg publicMsg = (PublicMsg) intent.getParcelableExtra("public_msg");
            action = intent.getStringExtra("notify_type");
            if ("private".equals(action)) {
                Log.i("PushServiceReceiver", "Show private notification");
                a(context, stringExtra, stringExtra2, publicMsg, intent.getStringExtra("message_id"), intent.getStringExtra(PushConstants.EXTRA_APP_ID));
            } else if ("rich_media".equals(action)) {
                Log.i("PushServiceReceiver", "Show rich media notification");
                a(context, stringExtra, stringExtra2, publicMsg, intent.getStringExtra(PushConstants.EXTRA_APP_ID));
            }
        } else if ("com.baidu.android.pushservice.action.media.CLICK".equals(action)) {
            if (b.a()) {
                Log.d("PushServiceReceiver", "Rich media notification clicked");
            }
            a(context, (PublicMsg) intent.getParcelableExtra("public_msg"));
        }
    }
}
