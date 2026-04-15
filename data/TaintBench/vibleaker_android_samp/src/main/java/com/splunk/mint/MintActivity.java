package com.splunk.mint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import java.util.Calendar;
import java.util.TimeZone;

public class MintActivity extends Activity {
    private static final String LASTMINTTIMESTAMP = "LASTMINTTIMESTAMP";
    /* access modifiers changed from: private|static */
    public static Editor editor;
    private static SharedPreferences preferences;

    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            public void run() {
                Properties.BATTERY_LEVEL = Utils.getBatteryLevel(MintActivity.this);
                Mint.logView(MintActivity.this.getClass().getName(), null);
                MintActivity.this.checkIFNeedToSendGnip(MintActivity.this);
            }
        }).start();
    }

    public void onStop() {
        super.onStop();
        setLastStop(this);
    }

    /* access modifiers changed from: private */
    public void checkIFNeedToSendGnip(final Context ctx) {
        new Thread(new Runnable() {
            public void run() {
                long currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000;
                long lastPingTime = 0;
                if (ctx != null) {
                    SharedPreferences preferences = ctx.getSharedPreferences("Mint", 0);
                    if (preferences != null) {
                        lastPingTime = preferences.getLong("LASTPINGTIME", 0) / 1000;
                    }
                }
                if (lastPingTime > 0 && currentTime - lastPingTime > 300) {
                    ActionEvent.createGnip(MintActivity.getLastStop(ctx)).save(new DataSaver());
                    ActionEvent.createPing().send(new NetSender(), true);
                }
            }
        }).start();
    }

    public static synchronized void setLastStop(final Context ctx) {
        synchronized (MintActivity.class) {
            new Thread(new Runnable() {
                public void run() {
                    MintActivity.initPreferences(ctx);
                    MintActivity.editor.putLong(MintActivity.LASTMINTTIMESTAMP, Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000).commit();
                }
            }).start();
        }
    }

    public static synchronized Long getLastStop(Context ctx) {
        Long valueOf;
        synchronized (MintActivity.class) {
            initPreferences(ctx);
            valueOf = Long.valueOf(preferences.getLong(LASTMINTTIMESTAMP, 0));
        }
        return valueOf;
    }

    @SuppressLint({"CommitPrefEdits"})
    public static synchronized void initPreferences(Context ctx) {
        synchronized (MintActivity.class) {
            if (preferences == null) {
                preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
            }
            if (editor == null) {
                editor = preferences.edit();
            }
        }
    }
}
