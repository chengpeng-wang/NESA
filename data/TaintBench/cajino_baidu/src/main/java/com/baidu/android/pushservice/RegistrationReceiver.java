package com.baidu.android.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import com.baidu.android.common.logging.Log;

public class RegistrationReceiver extends BroadcastReceiver {
    private void a(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("r_sync_rdata");
        String stringExtra2 = intent.getStringExtra("r_sync_rdata_v2");
        String stringExtra3 = intent.getStringExtra("r_sync_from");
        if (!context.getPackageName().equals(stringExtra3)) {
            if (stringExtra != null) {
                if (b.a()) {
                    Log.i("RegistrationReceiver", "handleRegisterSync rdata: " + stringExtra + " from: " + stringExtra3);
                }
                Editor edit = context.getSharedPreferences(context.getPackageName() + ".push_sync", 1).edit();
                edit.putString("r", stringExtra);
                edit.commit();
                a.b(context);
            }
            if (stringExtra2 != null) {
                if (b.a()) {
                    Log.i("RegistrationReceiver", "handleRegisterSync rdata v2: " + stringExtra2 + " from: " + stringExtra3);
                }
                Editor edit2 = context.getSharedPreferences(context.getPackageName() + ".push_sync", 1).edit();
                edit2.putString("r_v2", stringExtra2);
                edit2.commit();
                a.b(context);
            }
        }
    }

    static void a(Context context, d dVar) {
        Intent intent = new Intent();
        intent.setAction(PushConstants.ACTION_METHOD);
        intent.putExtra("method", "com.baidu.android.pushservice.action.UNBINDAPP");
        intent.putExtra("package_name", dVar.a);
        intent.putExtra(PushConstants.EXTRA_APP_ID, dVar.b);
        intent.putExtra(PushConstants.EXTRA_USER_ID, dVar.c);
        b.a(context, intent);
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
            action = intent.getData().getSchemeSpecificPart();
            boolean booleanExtra = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
            Log.i("RegistrationReceiver", "start for ACTION_PACKAGE_REMOVED，replacing：" + booleanExtra + " ,packageName: " + action);
            if (!booleanExtra) {
                PushSettings.a(context, action);
            }
            d a = a.a(context).a(action);
            if (booleanExtra || a == null || context.getPackageName().equals(a.a)) {
                Log.i("RegistrationReceiver", "replacing or not registered push client : " + action);
                b.a(context, intent);
                return;
            }
            Log.i("RegistrationReceiver", "unregister registered push client : " + action);
            a(context, a);
            return;
        }
        if ("com.baidu.android.pushservice.action.BIND_SYNC".equals(action)) {
            a(context, intent);
        }
        b.a(context, intent);
    }
}
