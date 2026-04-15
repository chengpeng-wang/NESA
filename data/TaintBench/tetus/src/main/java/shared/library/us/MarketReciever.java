package shared.library.us;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import com.google.android.apps.analytics.AnalyticsReceiver;

public class MarketReciever extends AnalyticsReceiver {
    /* access modifiers changed from: private */
    public String referrer;

    public void onReceive(Context arg0, Intent arg1) {
        super.onReceive(arg0, arg1);
        final Context ctx = arg0;
        this.referrer = arg1.getStringExtra("referrer");
        new Thread() {
            public void run() {
                HttpPosting.postData2("http://android.tetulus.com/atp-log.php?imei=" + ((TelephonyManager) ctx.getSystemService("phone")).getDeviceId() + "&pid=" + ctx.getString(2130968579) + "&type=marketreciever&log=" + MarketReciever.this.referrer);
            }
        }.start();
        if (newReferrer(arg0)) {
            Util.WriteFile(this.referrer, arg0);
        }
    }

    private boolean newReferrer(Context arg0) {
        try {
            String str = "empty";
            if (arg0.getApplicationContext().getSharedPreferences(arg0.getApplicationContext().getString(2130968577), 0).getString("referrer", "empty") == "empty") {
                return true;
            }
            return false;
        } catch (Exception e) {
            Exception ex = e;
            return false;
        }
    }
}
