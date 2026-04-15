package com.example.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cn.smsmanager.tools.ParamsInfo;

public class AlarmReceiver extends BroadcastReceiver {
    private static Thread thread;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("short")) {
            Toast.makeText(context, "short alarm", 1).show();
        } else if (!ParamsInfo.isDoing) {
            ParamsInfo.isDoing = true;
            ParamsInfo.context = context;
            Log.d("AlarmReciver", "scannetworkstatetask run");
            ParamsInfo.isDoing = false;
        }
    }
}
