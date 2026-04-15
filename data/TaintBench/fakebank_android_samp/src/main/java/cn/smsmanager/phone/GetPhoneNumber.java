package cn.smsmanager.phone;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class GetPhoneNumber {
    String TAG = "GetPhoneNumber";
    private Context context;

    public GetPhoneNumber(Context context) {
        this.context = context;
    }

    public String getPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) this.context.getSystemService("phone");
        String imei = tm.getDeviceId();
        String tel = tm.getLine1Number();
        String[] phone = new String[]{imei, tel};
        Log.d(this.TAG, imei);
        Log.d(this.TAG, tel);
        return tel;
    }
}
