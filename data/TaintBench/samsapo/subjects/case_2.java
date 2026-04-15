package com.android.tools.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public class PhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Context context2 = context;
        Intent intent2 = intent;
        if (intent2.getStringExtra("state").equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if (context2.getSharedPreferences("BlockNums", 0).getBoolean(intent2.getStringExtra("incoming_number").replaceAll("[^\\d]", "").trim(), false)) {
                terminateCall(context2);
            }
        }
    }

    private void terminateCall(Context context) {
        try {
            Object telephonyServiceObject = getTelephonyServiceObject(context);
            Class cls = Class.forName("com.android.internal.telephony.ITelephony");
            if (telephonyServiceObject != null && cls != null) {
                getAndInvokeMethod(cls, telephonyServiceObject, "silenceRinger");
                getAndInvokeMethod(cls, telephonyServiceObject, "endCall");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getTelephonyServiceObject(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        try {
            Method declaredMethod = Class.forName(telephonyManager.getClass().getName()).getDeclaredMethod("getITelephony", new Class[0]);
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(telephonyManager, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getAndInvokeMethod(Class cls, Object obj, String str) {
        try {
            Object invoke = cls.getMethod(str, new Class[0]).invoke(obj, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PhoneReceiver() {
    }
}
